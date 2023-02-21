package com.data.udh.controller;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.alibaba.fastjson.JSONObject;
import com.data.udh.controller.request.InitServiceRequest;
import com.data.udh.controller.response.ServiceInstanceVO;
import com.data.udh.dao.*;
import com.data.udh.dto.NodeInfo;
import com.data.udh.dto.ServiceTaskGroupType;
import com.data.udh.dto.TaskModel;
import com.data.udh.entity.*;
import com.data.udh.actor.CommandExecuteActor;
import com.data.udh.processor.TaskParam;
import com.data.udh.service.CommandHandler;
import com.data.udh.utils.*;
import com.google.common.collect.Lists;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.powerjob.common.response.ResultDTO;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.data.udh.utils.Constant.AdminUserId;

/**
 * 集群服务相关接口
 */
@RestController
@RequestMapping("/service")
@Slf4j
public class ClusterServiceController {

    ExecutorService flowSchedulerThreadPool = ThreadUtil.newExecutor(5, 10, 1024);

    @Value("${udh.task.log}")
    private String taskLogPath;


    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;

    @Resource
    private ServiceRoleInstanceRepository roleInstanceRepository;

    @Resource
    private StackServiceRoleRepository stackServiceRoleRepository;

    @Resource
    private StackServiceRepository stackServiceRepository;

    @Resource
    private ServiceRoleInstanceWebuisRepository roleInstanceWebuisRepository;

    @Resource
    private ServiceInstanceConfigRepository serviceInstanceConfigRepository;

    @Resource
    private EntityManager entityManager;

    @Resource
    private CommandRepository commandRepository;
    @Resource
    private CommandTaskRepository commandTaskRepository;

    @Resource
    private CommandHandler commandHandler;

    @Resource(name = "udhActorSystem")
    private ActorSystem udhActorSystem;

    @Resource
    private ClusterNodeRepository clusterNodeRepository;

    @Resource
    private StackServiceConfRepository stackServiceConfRepository;

    //    @Transactional(value = "udhTransactionManager", rollbackFor = Exception.class)
    @PostMapping("/initService")
    public ResultDTO<Void> initService(@RequestBody InitServiceRequest req) {
        Integer clusterId = req.getClusterId();
        Integer stackId = req.getStackId();
        List<Integer> installedServiceInstanceIds = new ArrayList<>();
        List<InitServiceRequest.ServiceInfo> serviceInfos = req.getServiceInfos();
        // 校验该集群是否已经安装过相同的服务了
        String errorServiceInstanceNames = serviceInfos.stream().map(info -> {
            ServiceInstanceEntity sameStackServiceInstance = serviceInstanceRepository.findByClusterIdAndStackServiceId(clusterId, info.getStackServiceId());
            if (sameStackServiceInstance != null) {
                return sameStackServiceInstance.getServiceName();
            }
            return null;
        }).filter(StrUtil::isNotBlank).collect(Collectors.joining(","));

        if (StrUtil.isNotBlank(errorServiceInstanceNames)) {
            return ResultDTO.failed("该集群已经安装过相同的服务实例：" + errorServiceInstanceNames);
        }
        for (InitServiceRequest.ServiceInfo serviceInfo : serviceInfos) {

            Integer stackServiceId = serviceInfo.getStackServiceId();
            // 查询实例表获取新增的实例序号
            Integer maxInstanceSeq = (Integer) entityManager.createNativeQuery("select max(instance_sequence) from udh_service_instance where stack_service_id = 1=" + stackServiceId).getSingleResult();
            if (maxInstanceSeq == null) {
                maxInstanceSeq = 0;
            }
            Integer newInstanceSeq = maxInstanceSeq + 1;

            ServiceInstanceEntity serviceInstanceEntity = new ServiceInstanceEntity();
            serviceInstanceEntity.setInstanceSequence(newInstanceSeq);
            String serviceName = serviceInfo.getStackServiceName() + newInstanceSeq;
            serviceInstanceEntity.setSid(serviceName);
            serviceInstanceEntity.setServiceName(serviceName);
            serviceInstanceEntity.setLabel(serviceInfo.getStackServiceLabel());
            serviceInstanceEntity.setClusterId(clusterId);
            serviceInstanceEntity.setCreateTime(new Date());
            serviceInstanceEntity.setUpdateTime(new Date());
            serviceInstanceEntity.setEnableKerberos(req.getEnableKerberos());
            serviceInstanceEntity.setStackServiceId(stackServiceId);
            serviceInstanceEntity.setServiceState(ServiceState.OPERATING);
            // 生成持久化宿主机路径
            String persistencePaths = stackServiceRepository.findById(stackServiceId).get().getPersistencePaths();
            serviceInstanceEntity.setPersistencePaths(genPersistencePaths(persistencePaths, serviceName));

            // 持久化service信息
            serviceInstanceRepository.save(serviceInstanceEntity);
            // 获取持久化后的service 实例id
            Integer serviceInstanceEntityId = serviceInstanceEntity.getId();
            installedServiceInstanceIds.add(serviceInstanceEntityId);

            // 获取service 所有角色
            List<InitServiceRequest.InitServiceRole> roles = serviceInfo.getRoles();
            for (InitServiceRequest.InitServiceRole role : roles) {
                String stackRoleName = role.getStackRoleName();
                StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(stackServiceId, stackRoleName);
                if (stackServiceRoleEntity == null) {
                    throw new IllegalArgumentException("can't find stack service role by role name:" + stackRoleName + " and stack service id: " + stackServiceId);
                }

                // 遍历该角色分布的节点，生成serviceRoleInstanceEntities
                List<ServiceRoleInstanceEntity> serviceRoleInstanceEntities = role.getNodeIds().stream().map(new Function<Integer, ServiceRoleInstanceEntity>() {
                    @Override
                    public ServiceRoleInstanceEntity apply(Integer nodeId) {
                        ServiceRoleInstanceEntity roleInstanceEntity = new ServiceRoleInstanceEntity();
                        roleInstanceEntity.setClusterId(clusterId);
                        roleInstanceEntity.setCreateTime(new Date());
                        roleInstanceEntity.setUpdateTime(new Date());
                        roleInstanceEntity.setServiceInstanceId(serviceInstanceEntityId);
                        roleInstanceEntity.setStackServiceRoleId(stackServiceRoleEntity.getId());
                        roleInstanceEntity.setServiceRoleName(stackRoleName);
                        roleInstanceEntity.setServiceRoleState(ServiceRoleState.OPERATING);
                        roleInstanceEntity.setNodeId(nodeId);
                        return roleInstanceEntity;
                    }
                }).collect(Collectors.toList());

                // 批量持久化role实例
                List<ServiceRoleInstanceEntity> serviceRoleInstanceEntitiesAfter = roleInstanceRepository.saveAllAndFlush(serviceRoleInstanceEntities);

                // 为每个角色分布的节点，都生成service RoleUi地址
                List<ServiceRoleInstanceWebuisEntity> roleInstanceWebuisEntities = serviceRoleInstanceEntitiesAfter.stream().map(new Function<ServiceRoleInstanceEntity, ServiceRoleInstanceWebuisEntity>() {
                    @Override
                    public ServiceRoleInstanceWebuisEntity apply(ServiceRoleInstanceEntity serviceRoleInstanceEntity) {
                        String roleLinkExpression = stackServiceRoleEntity.getLinkExpression();
                        // 持久化service Role UI信息
                        ServiceRoleInstanceWebuisEntity serviceRoleInstanceWebuisEntity = new ServiceRoleInstanceWebuisEntity();
                        serviceRoleInstanceWebuisEntity.setName("UI地址");
                        serviceRoleInstanceWebuisEntity.setServiceInstanceId(serviceInstanceEntityId);
                        serviceRoleInstanceWebuisEntity.setServiceRoleInstanceId(serviceRoleInstanceEntity.getServiceInstanceId());
                        serviceRoleInstanceWebuisEntity.setWebHostUrl(roleLinkExpression);
                        serviceRoleInstanceWebuisEntity.setWebIpUrl(roleLinkExpression);
                        return serviceRoleInstanceWebuisEntity;
                    }
                }).collect(Collectors.toList());

                // 批量持久化role web ui
                roleInstanceWebuisRepository.saveAll(roleInstanceWebuisEntities);


            }

            List<InitServiceRequest.InitServicePresetConf> presetConfList = serviceInfo.getPresetConfList();
            List<ServiceInstanceConfigEntity> serviceInstanceConfigEntities = presetConfList.stream().map(new Function<InitServiceRequest.InitServicePresetConf, ServiceInstanceConfigEntity>() {
                @Override
                public ServiceInstanceConfigEntity apply(InitServiceRequest.InitServicePresetConf initServicePresetConf) {
                    ServiceInstanceConfigEntity serviceInstanceConfigEntity = new ServiceInstanceConfigEntity();
                    BeanUtil.copyProperties(initServicePresetConf, serviceInstanceConfigEntity);
                    // 查询框架服务配置，补全属性
                    StackServiceConfEntity stackServiceConfEntity = stackServiceConfRepository.findByStackIdAndNameAndServiceId(stackId, initServicePresetConf.getName(), stackServiceId);
                    if (StrUtil.isNotBlank(stackServiceConfEntity.getGroups())) {
                        serviceInstanceConfigEntity.setCustomConfFile(stackServiceConfEntity.getGroups());
                    }
                    serviceInstanceConfigEntity.setUpdateTime(new Date());
                    serviceInstanceConfigEntity.setCreateTime(new Date());
                    serviceInstanceConfigEntity.setServiceInstanceId(serviceInstanceEntityId);
                    serviceInstanceConfigEntity.setUserId(AdminUserId);
                    return serviceInstanceConfigEntity;
                }
            }).collect(Collectors.toList());
            //  除初始化时页面上的配置，还得加载框架本身的默认配置
            List<StackServiceConfEntity> configNotInWizard = stackServiceConfRepository.findByServiceIdAndConfigurableInWizard(stackServiceId, false);
            List<ServiceInstanceConfigEntity> instanceConfigEntities = configNotInWizard.stream().map(new Function<StackServiceConfEntity, ServiceInstanceConfigEntity>() {
                @Override
                public ServiceInstanceConfigEntity apply(StackServiceConfEntity stackServiceConfEntity) {
                    ServiceInstanceConfigEntity serviceInstanceConfigEntity = new ServiceInstanceConfigEntity();
                    serviceInstanceConfigEntity.setName(stackServiceConfEntity.getName());
                    // 用默认值作为value
                    serviceInstanceConfigEntity.setValue(stackServiceConfEntity.getRecommendExpression());
                    serviceInstanceConfigEntity.setRecommendedValue(stackServiceConfEntity.getRecommendExpression());
                    serviceInstanceConfigEntity.setUpdateTime(new Date());
                    serviceInstanceConfigEntity.setCreateTime(new Date());
                    if (StrUtil.isNotBlank(stackServiceConfEntity.getGroups())) {
                        serviceInstanceConfigEntity.setCustomConfFile(stackServiceConfEntity.getGroups());
                    }
                    serviceInstanceConfigEntity.setServiceInstanceId(serviceInstanceEntityId);
                    serviceInstanceConfigEntity.setUserId(AdminUserId);
                    return serviceInstanceConfigEntity;
                }
            }).collect(Collectors.toList());

            // 批量持久化service Conf信息
            serviceInstanceConfigEntities.addAll(instanceConfigEntities);
            serviceInstanceConfigRepository.saveAll(serviceInstanceConfigEntities);

        }

        // 根据需要安装的服务在实例表中找到依赖的服务id，并更新service信息
        List<Integer> stackServiceIds = req.getServiceInfos().stream().map(e -> e.getStackServiceId()).collect(Collectors.toList());
        // 过滤出有依赖的服务
        List<StackServiceEntity> installStackServiceEntities = stackServiceRepository.findAllById(stackServiceIds).stream().filter(e -> StrUtil.isNotBlank(e.getDependencies())).collect(Collectors.toList());
        for (StackServiceEntity stackServiceEntity : installStackServiceEntities) {
            String[] depStr = stackServiceEntity.getDependencies().split(",");
            List<String> depInstanceIds = Arrays.stream(depStr).map(new Function<String, String>() {
                @Override
                public String apply(String depStackServiceName) {
                    //查找集群内该服务依赖的服务实例
                    Integer depServiceInstanceId = serviceInstanceRepository.findByClusterIdAndStackServiceName(clusterId, depStackServiceName);
                    return depServiceInstanceId + "";
                }
            }).collect(Collectors.toList());

            String depInstanceIdStr = StrUtil.join(",", depInstanceIds);
            // 更新需要安装的服务实例，将依赖服务实例id写入
            ServiceInstanceEntity updateServiceInstanceEntity = serviceInstanceRepository.findByClusterIdAndStackServiceId(clusterId, stackServiceEntity.getId());
            updateServiceInstanceEntity.setDependenceServiceInstanceIds(depInstanceIdStr);
            serviceInstanceRepository.save(updateServiceInstanceEntity);
        }


        //  生成新增服务command
        List<ServiceInstanceEntity> serviceInstanceEntities = serviceInstanceRepository.findAllById(installedServiceInstanceIds);
        Integer commandId = buildServiceCommand(serviceInstanceEntities, clusterId, CommandType.INSTALL_SERVICE);

        //  调用workflow
        udhActorSystem.actorOf(CommandExecuteActor.props()).tell(commandId, ActorRef.noSender());


        return ResultDTO.success(null);
    }


    @PostMapping("/stopService")
    public ResultDTO<Void> stopService(Integer serviceInstanceId) {
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        //  生成停止服务command
        List<ServiceInstanceEntity> serviceInstanceEntities = Lists.newArrayList(serviceInstanceEntity);
        Integer commandId = buildServiceCommand(serviceInstanceEntities, serviceInstanceEntity.getClusterId(), CommandType.STOP_SERVICE);

        //  调用workflow
        udhActorSystem.actorOf(CommandExecuteActor.props()).tell(commandId, ActorRef.noSender());


        return ResultDTO.success(null);
    }

    @PostMapping("/restartService")
    public ResultDTO<Void> restartService(Integer serviceInstanceId) {
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        //  生成重启服务command
        List<ServiceInstanceEntity> serviceInstanceEntities = Lists.newArrayList(serviceInstanceEntity);
        Integer commandId = buildServiceCommand(serviceInstanceEntities, serviceInstanceEntity.getClusterId(), CommandType.RESTART_SERVICE);

        //  调用workflow
        udhActorSystem.actorOf(CommandExecuteActor.props()).tell(commandId, ActorRef.noSender());

        return ResultDTO.success(null);
    }

    @PostMapping("/startService")
    public ResultDTO<Void> startService(Integer serviceInstanceId) {
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        //  生成启动服务command
        List<ServiceInstanceEntity> serviceInstanceEntities = Lists.newArrayList(serviceInstanceEntity);
        Integer commandId = buildServiceCommand(serviceInstanceEntities, serviceInstanceEntity.getClusterId(), CommandType.START_SERVICE);

        //  调用workflow
        udhActorSystem.actorOf(CommandExecuteActor.props()).tell(commandId, ActorRef.noSender());


        return ResultDTO.success(null);
    }

    /**
     * 通过模板生成服务实例持久化到宿主机的目录
     */
    private String genPersistencePaths(String persistencePaths, String serviceInstanceId) {
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
        String result = Arrays.stream(persistencePaths.split(",")).map(new Function<String, String>() {
            @Override
            public String apply(String pathTemplate) {
                Configuration cfg = new Configuration();
                StringTemplateLoader stringLoader = new StringTemplateLoader();
                stringLoader.putTemplate("myTemplate", pathTemplate);
                cfg.setTemplateLoader(stringLoader);
                try (Writer out = new StringWriter(2048);) {
                    Template temp = cfg.getTemplate("myTemplate", "utf-8");
                    temp.process(Dict.create().set("serviceInstanceId", serviceInstanceId), out);
                    return out.toString();
                } catch (IOException | TemplateException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }).collect(Collectors.joining(","));


        return result;
    }


    private Integer buildServiceCommand(List<ServiceInstanceEntity> serviceInstanceEntities, Integer ClusterId, CommandType commandType) {

        // 创建 command
        CommandEntity commandEntity = new CommandEntity();
        commandEntity.setCommandState(CommandState.RUNNING);
        commandEntity.setCurrentProgress(0);
        commandEntity.setClusterId(ClusterId);
        commandEntity.setName(commandType.getName());
        commandEntity.setSubmitTime(new Date());
        commandEntity.setOperateUserId(AdminUserId);
        // 持久化 command
        commandRepository.saveAndFlush(commandEntity);

        // todo 根据服务依赖进行调整顺序
        //  遍历command 涉及的服务实例
        AtomicInteger taskModelId = new AtomicInteger(1);
        for (ServiceInstanceEntity serviceInstanceEntity : serviceInstanceEntities) {
            StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();
            // 生成TaskGroupTypes
            List<TaskGroupType> taskGroupTypes = commandHandler.buildTaskGroupTypes(commandType, stackServiceEntity.getName());

            LinkedHashMap<String, List<NodeInfo>> roleHostMaps = new LinkedHashMap<>();
            // 查出该服务有的角色
            List<StackServiceRoleEntity> stackServiceRoleEntities = stackServiceRoleRepository.findByServiceIdOrderBySortNum(serviceInstanceEntity.getStackServiceId());
            // 遍历每个角色
            for (StackServiceRoleEntity stackServiceRoleEntity : stackServiceRoleEntities) {
                // 查出该角色的各个节点实例
                List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceIdAndStackServiceRoleId(serviceInstanceEntity.getId(), stackServiceRoleEntity.getId());

                List<NodeInfo> nodeInfos = roleInstanceEntities.stream().map(new Function<ServiceRoleInstanceEntity, NodeInfo>() {
                    @Override
                    public NodeInfo apply(ServiceRoleInstanceEntity serviceRoleInstanceEntity) {
                        ClusterNodeEntity clusterNodeEntity = clusterNodeRepository.findById(serviceRoleInstanceEntity.getNodeId()).get();
                        return NodeInfo.builder().hostName(clusterNodeEntity.getHostname()).ip(clusterNodeEntity.getIp()).build();
                    }
                }).collect(Collectors.toList());
                roleHostMaps.put(stackServiceRoleEntity.getName(), nodeInfos);
            }


            ServiceTaskGroupType serviceTaskGroupType = ServiceTaskGroupType.builder()
                    .serviceName(serviceInstanceEntity.getServiceName())
                    .stackServiceName(stackServiceEntity.getName())
                    .taskGroupTypes(taskGroupTypes)
                    .roleHostMaps(roleHostMaps).build();

            List<TaskModel> models = commandHandler.buildTaskModels(serviceTaskGroupType).stream().map(e -> {
                e.setTaskSortNum(taskModelId.getAndIncrement());
                return e;
            }).collect(Collectors.toList());

            // 根据taskModels生成command task，并持久化数据库
            for (TaskModel taskModel : models) {
                CommandTaskEntity commandTaskEntity = new CommandTaskEntity();
                commandTaskEntity.setCommandId(commandEntity.getId());
                commandTaskEntity.setProgress(0);
                commandTaskEntity.setProcessorClassName(taskModel.getProcessorClassName());
                commandTaskEntity.setTaskName(taskModel.getTaskName());
                commandTaskEntity.setTaskShowSortNum(taskModel.getTaskSortNum());
                commandTaskEntity.setCommandState(CommandState.WAITING);
                commandTaskEntity.setServiceInstanceId(serviceInstanceEntity.getId());
                commandTaskEntity.setServiceInstanceName(serviceInstanceEntity.getServiceName());
                commandTaskRepository.saveAndFlush(commandTaskEntity);
                // 更新日志路径
                commandTaskEntity.setTaskLogPath(taskLogPath + File.separator + commandEntity.getId() + "-" + commandTaskEntity.getId() + ".log");
                // 更新任务参数
                TaskParam taskParam = buildTaskParam(taskModel, commandEntity, serviceInstanceEntity, commandTaskEntity);
                commandTaskEntity.setTaskParam(JSONObject.toJSONString(taskParam));
                commandTaskRepository.saveAndFlush(commandTaskEntity);
            }
        }

        return commandEntity.getId();
    }

    private TaskParam buildTaskParam(TaskModel taskModel, CommandEntity commandEntity,
                                     ServiceInstanceEntity serviceInstanceEntity, CommandTaskEntity commandTaskEntity) {
        TaskParam taskParam = new TaskParam();
        BeanUtil.copyProperties(taskModel, taskParam);
        taskParam.setCommandTaskId(commandTaskEntity.getId());
        taskParam.setCommandId(commandEntity.getId());
        taskParam.setServiceInstanceId(serviceInstanceEntity.getId());
        taskParam.setStackServiceId(serviceInstanceEntity.getStackServiceId());
        return taskParam;
    }


    /**
     * 校验要安装的服务是否需要Kerberos配置
     */
    @PostMapping("/validInstallServiceHasKerberos")
    ResultDTO<Boolean> installServiceHasKerberos(@RequestBody List<Integer> InstallStackServiceIds) {
        for (StackServiceEntity stackServiceEntity : stackServiceRepository.findAllById(InstallStackServiceIds)) {
            if (stackServiceEntity.isSupportKerberos()) {
                return ResultDTO.success(true);
            }
        }
        return ResultDTO.success(false);
    }


    /**
     * 服务实例列表
     */
    @GetMapping("/listServiceInstance")
    public ResultDTO<List<ServiceInstanceVO>> listServiceInstance(Integer clusterId) {
        List<ServiceInstanceVO> result = serviceInstanceRepository.findByClusterId(clusterId).stream().map(instanceEntity -> {
            ServiceInstanceVO serviceInstanceVO = new ServiceInstanceVO();
            BeanUtil.copyProperties(instanceEntity, serviceInstanceVO);
            ServiceState serviceState = instanceEntity.getServiceState();
            serviceInstanceVO.setServiceStateValue(serviceState.name());

            // 根据状态查询icon
            StackServiceEntity stackServiceEntity = stackServiceRepository.findById(instanceEntity.getStackServiceId()).get();
            if (serviceState == ServiceState.OPERATING) {
                serviceInstanceVO.setIcon(stackServiceEntity.getIconApp());
            } else if (serviceState == ServiceState.WARN || serviceState == ServiceState.DANGER) {
                serviceInstanceVO.setIcon(stackServiceEntity.getIconDanger());
            } else {
                serviceInstanceVO.setIcon(stackServiceEntity.getIconDefault());
            }

            return serviceInstanceVO;
        }).collect(Collectors.toList());


        return ResultDTO.success(result);
    }

    /**
     * 删除服务实例
     */
    @GetMapping("/deleteServiceInstance")
    @Transactional(value = "udhTransactionManager", rollbackFor = Exception.class)
    public ResultDTO<Void> deleteServiceInstance(Integer serviceInstanceId) {

        // 删除服务实例表
        serviceInstanceRepository.deleteById(serviceInstanceId);
        // 删除服务角色实例表
        roleInstanceRepository.deleteByServiceInstanceId(serviceInstanceId);
        // 删除服务角色配置表
        serviceInstanceConfigRepository.deleteByServiceInstanceId(serviceInstanceId);
        // 删除服务ui表
        serviceInstanceConfigRepository.deleteByServiceInstanceId(serviceInstanceId);


        return ResultDTO.success(null);
    }
}
