package com.data.udh.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.data.udh.controller.request.InitServiceRequest;
import com.data.udh.controller.response.ServiceInstanceVO;
import com.data.udh.dao.*;
import com.data.udh.dto.NodeInfo;
import com.data.udh.dto.ServiceTaskGroupType;
import com.data.udh.dto.TaskModel;
import com.data.udh.entity.*;
import com.data.udh.service.CommandHandler;
import com.data.udh.utils.*;
import com.google.common.collect.Lists;
import com.sun.xml.bind.v2.TODO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.powerjob.common.response.ResultDTO;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.data.udh.utils.Constant.AdminUserId;

/**
 * 集群服务相关接口
 */
@RestController
@RequestMapping("/service")
public class ClusterServiceController {

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

    @Resource
    private ClusterNodeRepository clusterNodeRepository;

    @Transactional(value = "udhTransactionManager", rollbackFor = Exception.class)
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
            serviceInstanceEntity.setSid(serviceInfo.getStackServiceName() + newInstanceSeq);
            serviceInstanceEntity.setServiceName(serviceInfo.getStackServiceName() + newInstanceSeq);
            serviceInstanceEntity.setLabel(serviceInfo.getStackServiceLabel());
            serviceInstanceEntity.setClusterId(clusterId);
            serviceInstanceEntity.setCreateTime(new Date());
            serviceInstanceEntity.setUpdateTime(new Date());
            serviceInstanceEntity.setEnableKerberos(req.getEnableKerberos());
            serviceInstanceEntity.setStackServiceId(stackServiceId);
            serviceInstanceEntity.setServiceState(ServiceState.OPERATING);

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
                    serviceInstanceConfigEntity.setUpdateTime(new Date());
                    serviceInstanceConfigEntity.setCreateTime(new Date());
                    serviceInstanceConfigEntity.setServiceInstanceId(serviceInstanceEntityId);
                    serviceInstanceConfigEntity.setUserId(AdminUserId);
                    return serviceInstanceConfigEntity;
                }
            }).collect(Collectors.toList());

            // 批量持久化service Conf信息
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
        buildInstallServiceCommand(serviceInstanceEntities, clusterId);

//        TODO  和调用workflow


        return ResultDTO.success(null);
    }

    private void buildInstallServiceCommand(List<ServiceInstanceEntity> serviceInstanceEntities, Integer ClusterId) {
        // 创建 command
        CommandEntity commandEntity = new CommandEntity();
        commandEntity.setCommandState(CommandState.RUNNING);
        commandEntity.setTotalProgress(0);
        commandEntity.setClusterId(ClusterId);
        commandEntity.setName(CommandType.INSTALL_SERVICE.getName());
        commandEntity.setSubmitTime(new Date());
        commandEntity.setOperateUserId(AdminUserId);
        // 持久化 command
        commandRepository.save(commandEntity);

        // 遍历command 涉及的服务实例
        AtomicInteger taskModelId = new AtomicInteger(1);
        List<TaskModel> taskModels = new LinkedList<>();
        for (ServiceInstanceEntity serviceInstanceEntity : serviceInstanceEntities) {
            StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();
            // 生成TaskGroupTypes
            List<TaskGroupType> taskGroupTypes = commandHandler.buildTaskGroupTypes(CommandType.INSTALL_SERVICE, stackServiceEntity.getName());

            // todo 逻辑错了，角色实例表相同角色会有多条记录
            LinkedHashMap<String, List<NodeInfo>> roleHostMaps = new LinkedHashMap<>();
            // 查出该服务有的角色
            List<StackServiceRoleEntity> stackServiceRoleEntities = stackServiceRoleRepository.findByServiceIdOrderBySortNum(serviceInstanceEntity.getStackServiceId());
            // 遍历每个角色
            for (StackServiceRoleEntity stackServiceRoleEntity : stackServiceRoleEntities) {
                // 查出该角色的各个节点实例
                List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceIdAndStackServiceRoleId(serviceInstanceEntity.getId(),stackServiceRoleEntity.getId());

                List<NodeInfo> nodeInfos = roleInstanceEntities.stream().map(new Function<ServiceRoleInstanceEntity, NodeInfo>() {
                    @Override
                    public NodeInfo apply(ServiceRoleInstanceEntity serviceRoleInstanceEntity) {
                        ClusterNodeEntity clusterNodeEntity = clusterNodeRepository.findById(serviceRoleInstanceEntity.getNodeId()).get();
                        return NodeInfo.builder().hostName(clusterNodeEntity.getHostname()).ip(clusterNodeEntity.getIp()).build();
                    }
                }).collect(Collectors.toList());
                roleHostMaps.put(stackServiceRoleEntity.getName(),nodeInfos);
            }


            ServiceTaskGroupType serviceTaskGroupType = ServiceTaskGroupType.builder()
                    .serviceName(serviceInstanceEntity.getServiceName())
                    .stackServiceName(stackServiceEntity.getName())
                    .taskGroupTypes(taskGroupTypes)
                    .roleHostMaps(roleHostMaps).build();

            List<TaskModel> models = commandHandler.buildTaskModels(serviceTaskGroupType).stream().map(e -> {
                e.setTaskId(taskModelId.getAndIncrement());
                return e;
            }).collect(Collectors.toList());
            taskModels.addAll(models);

        }

        // 根据taskModels生成command task，并持久化数据库
        for (TaskModel taskModel : taskModels) {
            CommandTaskEntity commandTaskEntity = new CommandTaskEntity();
            commandTaskEntity.setCommandId(commandEntity.getId());
            commandTaskEntity.setProgress(0);
            commandTaskEntity.setTaskName(taskModel.getTaskName());
            commandTaskEntity.setTaskShowSortNum(taskModel.getTaskId());
            commandTaskEntity.setCommandState(CommandState.WAITING);
            commandTaskRepository.save(commandTaskEntity);
        }

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
}
