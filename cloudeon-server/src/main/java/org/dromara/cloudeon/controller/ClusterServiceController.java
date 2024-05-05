/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dromara.cloudeon.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.fabric8.kubernetes.api.model.EventList;
import io.fabric8.kubernetes.api.model.Pod;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.cloudeon.controller.request.InitServiceRequest;
import org.dromara.cloudeon.controller.request.ServiceConfUpgradeRequest;
import org.dromara.cloudeon.controller.response.*;
import org.dromara.cloudeon.dao.*;
import org.dromara.cloudeon.dto.ResultDTO;
import org.dromara.cloudeon.dto.ServiceConfiguration;
import org.dromara.cloudeon.dto.ServiceCustomConf;
import org.dromara.cloudeon.dto.ServicePresetConf;
import org.dromara.cloudeon.entity.*;
import org.dromara.cloudeon.enums.*;
import org.dromara.cloudeon.service.CommandHandler;
import org.dromara.cloudeon.service.KubeService;
import org.dromara.cloudeon.service.ServiceService;
import org.dromara.cloudeon.utils.Constant;
import org.dromara.cloudeon.utils.DAG;
import org.dromara.cloudeon.utils.K8sUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.dromara.cloudeon.utils.Constant.AdminUserId;
import static org.dromara.cloudeon.utils.Constant.VERTX_COMMAND_ADDRESS;

/**
 * 集群服务相关接口
 */
@RestController
@RequestMapping("/service")
@Slf4j
public class ClusterServiceController {

    @Resource
    private ServiceService serviceService;


    @Resource(name = "cloudeonVertx")
    private Vertx cloudeonVertx;

    @Resource
    private AlertMessageRepository alertMessageRepository;

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
    private CommandRepository commandRepository;


    @Resource
    private CommandHandler commandHandler;

    @Resource
    private ClusterNodeRepository clusterNodeRepository;

    @Resource
    private StackServiceConfRepository stackServiceConfRepository;

    @Resource
    private ServiceInstanceSeqRepository serviceInstanceSeqRepository;

    @Resource
    private ClusterInfoRepository clusterInfoRepository;

    @Resource
    private KubeService kubeService;

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
        // 根据服务间依赖关系调整安装顺序
        List<InitServiceRequest.ServiceInfo> sortedServiceInfos = changeInstallSortByDependence(req);

        for (InitServiceRequest.ServiceInfo serviceInfo : sortedServiceInfos) {

            Integer stackServiceId = serviceInfo.getStackServiceId();

            ServiceInstanceEntity serviceInstanceEntity = new ServiceInstanceEntity();
            String stackServiceName = K8sUtil.formatK8sNameStr(serviceInfo.getStackServiceName());
            serviceInstanceEntity.setServiceName(stackServiceName);
            serviceInstanceEntity.setLabel(serviceInfo.getStackServiceLabel());
            serviceInstanceEntity.setClusterId(clusterId);
            serviceInstanceEntity.setCreateTime(new Date());
            serviceInstanceEntity.setUpdateTime(new Date());
            serviceInstanceEntity.setEnableKerberos(req.getEnableKerberos());
            serviceInstanceEntity.setStackServiceId(stackServiceId);
            serviceInstanceEntity.setServiceState(ServiceState.INIT_SERVICE);

            // 持久化service信息
            serviceInstanceRepository.save(serviceInstanceEntity);
            // 获取持久化后的service 实例id
            Integer serviceInstanceEntityId = serviceInstanceEntity.getId();
            installedServiceInstanceIds.add(serviceInstanceEntityId);

            List<ServiceInstanceConfigEntity> serviceInstanceConfigEntities = new ArrayList<>();

            // 页面上的预设配置
            List<ServicePresetConf> presetConfList = serviceInfo.getPresetConfList();
            // 用户自定义配置
            List<ServiceCustomConf> customConfList = serviceInfo.getCustomConfList();
            fillInstanceConfigEntities(stackId, stackServiceId, serviceInstanceEntityId, serviceInstanceConfigEntities, presetConfList, customConfList, true);
            serviceInstanceConfigRepository.saveAllAndFlush(serviceInstanceConfigEntities);

            // 获取需要安装的service 所有角色
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
                        roleInstanceEntity.setServiceRoleState(ServiceRoleState.INIT_ROLE);
                        roleInstanceEntity.setNodeId(nodeId);
                        return roleInstanceEntity;
                    }
                }).collect(Collectors.toList());

                // 批量持久化role实例
                List<ServiceRoleInstanceEntity> serviceRoleInstanceEntitiesAfter = roleInstanceRepository.saveAllAndFlush(serviceRoleInstanceEntities);

                // 为每个角色分布的节点，都生成service RoleUi地址
                List<ServiceRoleInstanceWebuisEntity> roleInstanceWebuisEntities = serviceRoleInstanceEntitiesAfter.stream()
                        .filter(e -> {
                            return StrUtil.isNotBlank(stackServiceRoleEntity.getLinkExpression());
                        }).map(new Function<ServiceRoleInstanceEntity, ServiceRoleInstanceWebuisEntity>() {
                            @Override
                            public ServiceRoleInstanceWebuisEntity apply(ServiceRoleInstanceEntity serviceRoleInstanceEntity) {
                                String roleLinkExpression = stackServiceRoleEntity.getLinkExpression();
                                // 持久化service Role UI信息
                                ServiceRoleInstanceWebuisEntity serviceRoleInstanceWebuisEntity = new ServiceRoleInstanceWebuisEntity();
                                serviceRoleInstanceWebuisEntity.setName(serviceRoleInstanceEntity.getServiceRoleName() + "UI地址");
                                serviceRoleInstanceWebuisEntity.setServiceInstanceId(serviceInstanceEntityId);
                                serviceRoleInstanceWebuisEntity.setServiceRoleInstanceId(serviceRoleInstanceEntity.getId());
                                serviceRoleInstanceWebuisEntity.setWebHostUrl(genWebUI(roleLinkExpression, serviceInstanceEntityId, serviceRoleInstanceEntity, false));
                                serviceRoleInstanceWebuisEntity.setWebIpUrl(genWebUI(roleLinkExpression, serviceInstanceEntityId, serviceRoleInstanceEntity, true));
                                return serviceRoleInstanceWebuisEntity;
                            }
                        }).collect(Collectors.toList());

                // 批量持久化role web ui
                roleInstanceWebuisRepository.saveAll(roleInstanceWebuisEntities);


            }

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


        //  生成新增服务command （按照安装服务间的依赖生成）
        List<ServiceInstanceEntity> serviceInstanceEntities = installedServiceInstanceIds.stream().map(e -> serviceInstanceRepository.findById(e).get()).collect(Collectors.toList());
        Integer commandId = commandHandler.buildServiceCommand(serviceInstanceEntities, clusterId, CommandType.INSTALL_SERVICE);

        //  调用workflow
        cloudeonVertx.eventBus().request(VERTX_COMMAND_ADDRESS, commandId);


        return ResultDTO.success(null);
    }

    /**
     * 根据服务间依赖关系调整安装顺序
     */
    private List<InitServiceRequest.ServiceInfo> changeInstallSortByDependence(InitServiceRequest req) {
        List<InitServiceRequest.ServiceInfo> sortedServiceInfos = new LinkedList<>();
        // 根据服务间依赖构建Dag图
        DAG<String, Object, Object> dag = new DAG<>();
        List<InitServiceRequest.ServiceInfo> serviceInfos = req.getServiceInfos();
        List<Integer> stackServiceIds = serviceInfos.stream().map(InitServiceRequest.ServiceInfo::getStackServiceId).collect(Collectors.toList());
        // 构建node
        List<StackServiceEntity> stackServiceEntities = stackServiceRepository.findAllById(stackServiceIds);
        stackServiceEntities.forEach(stackServiceEntity -> {
            String stackServiceEntityName = stackServiceEntity.getName();
            dag.addNode(stackServiceEntityName, null);
            log.info("Dag添加node：{}", stackServiceEntityName);
        });
        // 构建边
        stackServiceEntities.forEach(stackServiceEntity -> {
            String stackServiceEntityName = stackServiceEntity.getName();
            String dependencies = stackServiceEntity.getDependencies();
            if (StrUtil.isNotBlank(dependencies)) {
                for (String depServiceName : dependencies.split(",")) {
                    dag.addEdge(depServiceName, stackServiceEntityName);
                    log.info("Dag添加边：{} -> {}", depServiceName, stackServiceEntityName);
                }
            }
        });
        try {
            List<String> stackServiceNameOrderByDependence = dag.topologicalSort();
            Map<String, InitServiceRequest.ServiceInfo> map = serviceInfos.stream().collect(Collectors.toMap(InitServiceRequest.ServiceInfo::getStackServiceName, e -> e, (k1, k2) -> k2));
            for (String name : stackServiceNameOrderByDependence) {
                sortedServiceInfos.add(map.get(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sortedServiceInfos;
    }

    /**
     * 生成最终要持久化的服务实例配置
     */
    private void fillInstanceConfigEntities(Integer stackId, Integer stackServiceId, Integer serviceInstanceEntityId,
                                            List<ServiceInstanceConfigEntity> serviceInstanceConfigEntities, List<ServicePresetConf> presetConfList,
                                            List<ServiceCustomConf> customConfList, boolean isInit) {
        List<ServiceInstanceConfigEntity> presetInstanceConfigEntities = presetConfList.stream().map(new Function<ServicePresetConf, ServiceInstanceConfigEntity>() {
            @Override
            public ServiceInstanceConfigEntity apply(ServicePresetConf initServicePresetConf) {
                ServiceInstanceConfigEntity serviceInstanceConfigEntity = new ServiceInstanceConfigEntity();
                BeanUtil.copyProperties(initServicePresetConf, serviceInstanceConfigEntity);
                // 查询框架服务配置，补全属性
                StackServiceConfEntity stackServiceConfEntity = stackServiceConfRepository.findByStackIdAndNameAndServiceId(stackId, initServicePresetConf.getName(), stackServiceId);
                if (StrUtil.isNotBlank(stackServiceConfEntity.getConfFile())) {
                    serviceInstanceConfigEntity.setConfFile(stackServiceConfEntity.getConfFile());
                }
                serviceInstanceConfigEntity.setTag(stackServiceConfEntity.getTag());
                serviceInstanceConfigEntity.setUpdateTime(new Date());
                serviceInstanceConfigEntity.setCreateTime(new Date());
                serviceInstanceConfigEntity.setServiceInstanceId(serviceInstanceEntityId);
                serviceInstanceConfigEntity.setUserId(AdminUserId);
                return serviceInstanceConfigEntity;
            }
        }).collect(Collectors.toList());

        if (isInit) {
            //  除初始化时页面上的配置，还得加载框架本身的默认配置
            List<StackServiceConfEntity> configNotInWizard = stackServiceConfRepository.findByServiceIdAndConfigurableInWizard(stackServiceId, false);
            List<ServiceInstanceConfigEntity> configNotInWizardInstanceConfigEntities = configNotInWizard.stream().map(new Function<StackServiceConfEntity, ServiceInstanceConfigEntity>() {
                @Override
                public ServiceInstanceConfigEntity apply(StackServiceConfEntity stackServiceConfEntity) {
                    ServiceInstanceConfigEntity serviceInstanceConfigEntity = new ServiceInstanceConfigEntity();
                    serviceInstanceConfigEntity.setName(stackServiceConfEntity.getName());
                    // 用默认值作为value
                    serviceInstanceConfigEntity.setValue(stackServiceConfEntity.getRecommendExpression());
                    serviceInstanceConfigEntity.setRecommendedValue(stackServiceConfEntity.getRecommendExpression());
                    serviceInstanceConfigEntity.setUpdateTime(new Date());
                    serviceInstanceConfigEntity.setCreateTime(new Date());
                    if (StrUtil.isNotBlank(stackServiceConfEntity.getConfFile())) {
                        serviceInstanceConfigEntity.setConfFile(stackServiceConfEntity.getConfFile());
                    }
                    serviceInstanceConfigEntity.setTag(stackServiceConfEntity.getTag());
                    serviceInstanceConfigEntity.setServiceInstanceId(serviceInstanceEntityId);
                    serviceInstanceConfigEntity.setUserId(AdminUserId);
                    return serviceInstanceConfigEntity;
                }
            }).collect(Collectors.toList());
            serviceInstanceConfigEntities.addAll(configNotInWizardInstanceConfigEntities);
        }

        List<ServiceInstanceConfigEntity> customInstanceConfigEntityStream = customConfList.stream().map(new Function<ServiceCustomConf, ServiceInstanceConfigEntity>() {
            @Override
            public ServiceInstanceConfigEntity apply(ServiceCustomConf initServiceCustomConf) {
                ServiceInstanceConfigEntity serviceInstanceConfigEntity = new ServiceInstanceConfigEntity();
                BeanUtil.copyProperties(initServiceCustomConf, serviceInstanceConfigEntity);
                serviceInstanceConfigEntity.setCustomConf(true);
                serviceInstanceConfigEntity.setTag("自定义");
                serviceInstanceConfigEntity.setCreateTime(new Date());
                serviceInstanceConfigEntity.setUpdateTime(new Date());
                serviceInstanceConfigEntity.setServiceInstanceId(serviceInstanceEntityId);
                serviceInstanceConfigEntity.setUserId(AdminUserId);
                return serviceInstanceConfigEntity;
            }
        }).collect(Collectors.toList());

        // 批量持久化service Conf信息
        serviceInstanceConfigEntities.addAll(presetInstanceConfigEntities);
        serviceInstanceConfigEntities.addAll(customInstanceConfigEntityStream);
    }

    private String genWebUI(String roleLinkExpression, Integer serviceInstanceEntityId, ServiceRoleInstanceEntity serviceRoleInstanceEntity, boolean isUseIp) {
        String localhostname = "";
        // 查询角色的部署节点
        ClusterNodeEntity clusterNodeEntity = clusterNodeRepository.findById(serviceRoleInstanceEntity.getNodeId()).get();
        if (isUseIp) {
            localhostname = clusterNodeEntity.getIp();
        } else {
            localhostname = clusterNodeEntity.getHostname();
        }

        // 查询服务实例所有配置项
        List<ServiceInstanceConfigEntity> allConfigEntityList = serviceInstanceConfigRepository.findByServiceInstanceId(serviceInstanceEntityId);
        Map<String, String> confMap = allConfigEntityList.stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue));
        // 渲染
        Configuration cfg = new Configuration();
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        String template = "webUITemplate";
        stringLoader.putTemplate(template, roleLinkExpression);
        cfg.setTemplateLoader(stringLoader);
        try (Writer out = new StringWriter(2048);) {
            Template temp = cfg.getTemplate(template, "utf-8");
            temp.process(Dict.create().set("localhostname", localhostname).set("conf", confMap), out);
            return out.toString();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }


    @PostMapping("/stopService")
    public ResultDTO<Void> stopService(Integer serviceInstanceId) {
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        //  生成停止服务command
        List<ServiceInstanceEntity> serviceInstanceEntities = Lists.newArrayList(serviceInstanceEntity);
        Integer commandId = commandHandler.buildServiceCommand(serviceInstanceEntities, serviceInstanceEntity.getClusterId(), CommandType.STOP_SERVICE);

        //  调用workflow
        cloudeonVertx.eventBus().request(VERTX_COMMAND_ADDRESS, commandId);

        // 更新服务实例状态
        serviceInstanceEntity.setServiceState(ServiceState.STOPPING_SERVICE);
        serviceInstanceRepository.save(serviceInstanceEntity);


        return ResultDTO.success(null);
    }

    @PostMapping("/upgradeServiceConfig")
    public ResultDTO<Void> upgradeServiceConfig(Integer serviceInstanceId) {
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        //  生成刷新服务配置command
        List<ServiceInstanceEntity> serviceInstanceEntities = Lists.newArrayList(serviceInstanceEntity);
        Integer commandId = commandHandler.buildServiceCommand(serviceInstanceEntities, serviceInstanceEntity.getClusterId(), CommandType.UPGRADE_SERVICE_CONFIG);

        //  调用workflow
        cloudeonVertx.eventBus().request(VERTX_COMMAND_ADDRESS, commandId);


        return ResultDTO.success(null);
    }

    @PostMapping("/restartService")
    public ResultDTO<Void> restartService(Integer serviceInstanceId) {
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        //  生成重启服务command
        List<ServiceInstanceEntity> serviceInstanceEntities = Lists.newArrayList(serviceInstanceEntity);
        Integer commandId = commandHandler.buildServiceCommand(serviceInstanceEntities, serviceInstanceEntity.getClusterId(), CommandType.RESTART_SERVICE);

        //  调用workflow
        cloudeonVertx.eventBus().request(VERTX_COMMAND_ADDRESS, commandId);
        // 更新服务实例状态
        serviceInstanceEntity.setServiceState(ServiceState.RESTARTING_SERVICE);
        serviceInstanceRepository.save(serviceInstanceEntity);

        return ResultDTO.success(null);
    }

    @PostMapping("/startService")
    public ResultDTO<Void> startService(Integer serviceInstanceId) {
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        //  生成启动服务command
        List<ServiceInstanceEntity> serviceInstanceEntities = Lists.newArrayList(serviceInstanceEntity);
        Integer commandId = commandHandler.buildServiceCommand(serviceInstanceEntities, serviceInstanceEntity.getClusterId(), CommandType.START_SERVICE);

        //  调用workflow
        cloudeonVertx.eventBus().request(VERTX_COMMAND_ADDRESS, commandId);

        // 更新服务实例状态
        serviceInstanceEntity.setServiceState(ServiceState.STARTING_SERVICE);
        serviceInstanceRepository.save(serviceInstanceEntity);

        return ResultDTO.success(null);
    }


    @PostMapping("/stopRole")
    public ResultDTO<Void> stopRoles(Integer roleInstanceId) {

        ServiceRoleInstanceEntity roleInstanceEntity = roleInstanceRepository.findById(roleInstanceId).get();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(roleInstanceEntity.getServiceInstanceId()).get();
        if (roleInstanceEntity.getServiceRoleState() != ServiceRoleState.ROLE_STARTED) {
            throw new RuntimeException("角色未启动,无法执行停止!");
        }
        // 更新角色实例状态
        roleInstanceEntity.setServiceRoleState(ServiceRoleState.STOPPING_ROLE);
        roleInstanceRepository.save(roleInstanceEntity);

        //  生成停止角色command
        List<ServiceInstanceEntity> serviceInstanceEntities = Lists.newArrayList(serviceInstanceEntity);
        Integer commandId = commandHandler.buildRoleCommand(serviceInstanceEntities, Lists.newArrayList(roleInstanceEntity),
                serviceInstanceEntity.getClusterId(), CommandType.STOP_ROLE);
        //  调用workflow
        cloudeonVertx.eventBus().request(VERTX_COMMAND_ADDRESS, commandId);

        return ResultDTO.success(null);
    }

    @PostMapping("/startRole")
    public ResultDTO<Void> startRole(Integer roleInstanceId) {

        ServiceRoleInstanceEntity roleInstanceEntity = roleInstanceRepository.findById(roleInstanceId).get();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(roleInstanceEntity.getServiceInstanceId()).get();
        if (roleInstanceEntity.getServiceRoleState() != ServiceRoleState.ROLE_STOPPED) {
            throw new RuntimeException("角色未停止,无法执行启动!");
        }
        // 更新角色实例状态
        roleInstanceEntity.setServiceRoleState(ServiceRoleState.STARTING_ROLE);
        roleInstanceRepository.save(roleInstanceEntity);
        //  生成启动角色command
        List<ServiceInstanceEntity> serviceInstanceEntities = Lists.newArrayList(serviceInstanceEntity);
        Integer commandId = commandHandler.buildRoleCommand(serviceInstanceEntities, Lists.newArrayList(roleInstanceEntity),
                serviceInstanceEntity.getClusterId(), CommandType.START_ROLE);
        //  调用workflow
        cloudeonVertx.eventBus().request(VERTX_COMMAND_ADDRESS, commandId);

        return ResultDTO.success(null);
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
            serviceInstanceVO.setServiceStateValue(serviceState.getValue());
            serviceInstanceVO.setServiceState(serviceState.getDesc());
            // 查询相关告警数量
            List<String> alertNames = alertMessageRepository.findByServiceInstanceIdAndResolved(instanceEntity.getId(), false)
                    .stream().map(AlertMessageEntity::getAlertName).collect(Collectors.toList());

            serviceInstanceVO.setAlertMsgCnt(alertNames.size());
            serviceInstanceVO.setAlertMsgName(alertNames);

            // 查询icon
            StackServiceEntity stackServiceEntity = stackServiceRepository.findById(instanceEntity.getStackServiceId()).get();
            serviceInstanceVO.setIcon(stackServiceEntity.getIconApp());


            return serviceInstanceVO;
        }).collect(Collectors.toList());


        return ResultDTO.success(result);
    }

    /**
     * 查询服务实例配置
     */
    @GetMapping("/listConfs")
    public ResultDTO<ServiceInstanceConfVO> listConfs(Integer serviceInstanceId) {
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        Integer stackServiceId = serviceInstanceEntity.getStackServiceId();
        ServiceInstanceConfVO result = new ServiceInstanceConfVO();
        // 数据库中查询服务实例配置
        List<ServiceConfiguration> serviceConfigurations = serviceInstanceConfigRepository.findByServiceInstanceId(serviceInstanceId)
                .stream().map(serviceInstanceConfig -> {
                    ServiceConfiguration serviceConfiguration = new ServiceConfiguration();

                    // 查询框架服务配置补全校验
                    StackServiceConfEntity stackConfEntity = stackServiceConfRepository.findByNameAndServiceId(serviceInstanceConfig.getName(), stackServiceId);
                    if (stackConfEntity != null) {
                        BeanUtil.copyProperties(stackConfEntity, serviceConfiguration);
                        serviceConfiguration.setConfFile(serviceInstanceConfig.getConfFile());
                        serviceConfiguration.setOptions(JSONObject.parseArray(stackConfEntity.getOptions(), String.class));
                    }
                    BeanUtil.copyProperties(serviceInstanceConfig, serviceConfiguration);
                    // 为自定义配置添加默认valueType
                    if (serviceInstanceConfig.isCustomConf()) {
                        serviceConfiguration.setValueType(ConfValueType.InputString.name());
                    }
                    return serviceConfiguration;
                }).collect(Collectors.toList());

        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(stackServiceId).get();
        // 查找该服务的自定义配置文件
        ArrayList<String> customFileNames = ListUtil.toList(stackServiceEntity.getCustomConfigFiles().split(","));

        Map<String, List<String>> treeMap = new LinkedHashMap<>();        // all tags
        List<String> allTags = serviceConfigurations.stream().map(e -> e.getTag()).distinct().collect(Collectors.toList());
        treeMap.put("全部", allTags);
        // fileGroup tags
        Map<String, List<ServiceConfiguration>> collect = serviceConfigurations.stream().filter(e->StrUtil.isNotBlank(e.getConfFile())).collect(Collectors.groupingBy(ServiceConfiguration::getConfFile));
        Map<String, List<String>> fileGroup = collect.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, // key使用原始key
                        stringListEntry -> {  // 单独转换value
                            List<String> strings = stringListEntry.getValue().stream().map(ServiceConfiguration::getTag).distinct().collect(Collectors.toList());
                            return strings;
                        }));
        treeMap.putAll(fileGroup);

        result.setFileGroupMap(treeMap);
        result.setConfs(serviceConfigurations);
        result.setCustomFileNames(customFileNames);
        return ResultDTO.success(result);
    }

    /**
     * 服务实例详情
     */
    @GetMapping("/serviceInstanceInfo")
    public ResultDTO<ServiceInstanceDetailVO> serviceInstanceInfo(Integer serviceInstanceId) {

        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        Integer stackServiceId = serviceInstanceEntity.getStackServiceId();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(stackServiceId).get();

        ServiceState serviceState = serviceInstanceEntity.getServiceState();
        ServiceInstanceDetailVO instanceDetailVO = ServiceInstanceDetailVO.builder()
                .id(serviceInstanceEntity.getId())
                .name(serviceInstanceEntity.getServiceName())
                .stackServiceDesc(stackServiceEntity.getDescription())
                .dockerImage(stackServiceEntity.getDockerImage())
                .stackServiceId(stackServiceId)
                .stackServiceName(stackServiceEntity.getName())
                .version(stackServiceEntity.getVersion())
                .serviceState(serviceState.getDesc())
                .serviceStateValue(serviceState.getValue())
                .build();
        return ResultDTO.success(instanceDetailVO);
    }

    /**
     * 服务实例角色列表
     */
    @GetMapping("/serviceInstanceRoles")
    public ResultDTO<List<ServiceInstanceRoleVO>> serviceInstanceRoles(Integer serviceInstanceId) {

        List<ServiceInstanceRoleVO> result = roleInstanceRepository.findByServiceInstanceId(serviceInstanceId).stream().map(new Function<ServiceRoleInstanceEntity, ServiceInstanceRoleVO>() {
            @Override
            public ServiceInstanceRoleVO apply(ServiceRoleInstanceEntity roleInstanceEntity) {
                ClusterNodeEntity nodeEntity = clusterNodeRepository.findById(roleInstanceEntity.getNodeId()).get();
                // 查找该角色实例绑定的web地址
                ServiceRoleInstanceWebuisEntity webuisEntity = roleInstanceWebuisRepository.findByServiceRoleInstanceId(roleInstanceEntity.getId());
                StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findById(roleInstanceEntity.getStackServiceRoleId()).get();
                ServiceRoleState serviceRoleState = roleInstanceEntity.getServiceRoleState();
                // 查询角色实例相关告警
                List<String> alertNames = alertMessageRepository.findByServiceRoleInstanceIdAndResolved(roleInstanceEntity.getId(), false)
                        .stream().map(AlertMessageEntity::getAlertName).collect(Collectors.toList());
                ServiceInstanceRoleVO serviceInstanceRoleVO = ServiceInstanceRoleVO.builder()
                        .roleStatus(serviceRoleState.getDesc())
                        .roleStatusValue(serviceRoleState.getValue())
                        .id(roleInstanceEntity.getId())
                        .nodeHostIp(nodeEntity.getIp())
                        .nodeHostname(nodeEntity.getHostname())
                        .alertMsgCnt(alertNames.size())
                        .alertMsgName(alertNames)
                        .nodeId(nodeEntity.getId())
                        // 用 stackServiceRoleEntity label更清晰 （如：Doris Be）
                        .name(stackServiceRoleEntity.getLabel())
                        .build();
                if (webuisEntity != null) {
                    serviceInstanceRoleVO.setUiUrls(Lists.newArrayList(webuisEntity.getWebHostUrl(), webuisEntity.getWebIpUrl()));
                }
                return serviceInstanceRoleVO;
            }
        }).collect(Collectors.toList());

        return ResultDTO.success(result);
    }

    /**
     * 删除服务实例
     */
    @GetMapping("/deleteServiceInstance")
    @Transactional(rollbackFor = Exception.class)
    public ResultDTO<Void> deleteServiceInstance(Integer serviceInstanceId) {

        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        // 查出有依赖此服务的服务实例
        List<ServiceInstanceEntity> dep = serviceInstanceRepository.findByClusterIdAndDependenceServiceInstanceIdsNotNull(serviceInstanceEntity.getClusterId());
        List<ServiceInstanceEntity> depServiceInstanceList = dep.stream().filter(new Predicate<ServiceInstanceEntity>() {
            @Override
            public boolean test(ServiceInstanceEntity serviceInstanceEntity) {
                List<String> ids = Arrays.stream(serviceInstanceEntity.getDependenceServiceInstanceIds().split(",")).collect(Collectors.toList());
                return ids.contains(serviceInstanceId.toString());
            }
        }).collect(Collectors.toList());
        if (depServiceInstanceList.size() > 0) {
            String depServiceNames = depServiceInstanceList.stream().map(ServiceInstanceEntity::getServiceName).collect(Collectors.joining(","));
            return ResultDTO.failed("请先删除依赖此服务的服务实例：" + depServiceNames);
        }

        //  生成删除服务command
        List<ServiceInstanceEntity> serviceInstanceEntities = Lists.newArrayList(serviceInstanceEntity);
        Integer commandId = commandHandler.buildServiceCommand(serviceInstanceEntities, serviceInstanceEntity.getClusterId(), CommandType.DELETE_SERVICE);
        //  调用workflow
        cloudeonVertx.eventBus().request(VERTX_COMMAND_ADDRESS, commandId);

        // 更新服务实例状态
        serviceInstanceEntity.setServiceState(ServiceState.DELETING_SERVICE);
        serviceInstanceRepository.save(serviceInstanceEntity);


        return ResultDTO.success(null);
    }

    /**
     * 获取服务实例监控看板地址
     */
    @GetMapping("/getDashboardUrl")
    public ResultDTO<String> getDashboardUrl(Integer serviceInstanceId) {
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();

        // 通过服务框架的dashboard和Grafana地址拼接完整url
        String dashboardUid = stackServiceEntity.getDashboardUid();
        if (StringUtils.isBlank(dashboardUid)) {
            return ResultDTO.success("该组件未配置监控面板");
        }
        Integer globalServiceInstanceId = serviceInstanceRepository.findByClusterIdAndStackServiceName(serviceInstanceEntity.getClusterId(), "GLOBAL");
        List<ServiceInstanceConfigEntity> globalConfigEntityList = serviceInstanceConfigRepository.findByServiceInstanceId(globalServiceInstanceId);
        Map<String, String> globalConfigMap = globalConfigEntityList.stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue));

        String baseGrafanaUrl;
        switch (globalConfigMap.get("global.monitor.type")) {
            case "NONE":
                return ResultDTO.success("请先安装启用/接入kube-prometheus服务");
            case "EXTERNAL_KUBE_PROMETHEUS":
                baseGrafanaUrl = globalConfigMap.get("global.kube-prometheus.external.grafana.url");
                break;
            case "INTERNAL_HELM_KUBE_PROMETHEUS":
                String grafanaNodePort = serviceService
                        .getConfigMaps(serviceInstanceEntity.getClusterId(), "KUBE_PROMETHEUS_STACK")
                        .get("grafana.service.nodePort");
                String grafanaHost = RandomUtil.randomEle(clusterNodeRepository.findAll()).getIp();
                baseGrafanaUrl = StrUtil.format("http://{}:{}", grafanaHost, grafanaNodePort);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + globalConfigMap.get("global.monitor.type"));
        }
        Map<String, String> grafanaDashboardParams = new HashMap<>();
        grafanaDashboardParams.put("var-namespace", serviceService.getNamespace(serviceInstanceEntity));
        grafanaDashboardParams.put("orgId", "1");
        grafanaDashboardParams.put("from", "now-5m");
        grafanaDashboardParams.put("to", "now");
        grafanaDashboardParams.put("kiosk", "tv");

        String paramStr = grafanaDashboardParams.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
        String grafanaUrl = StrUtil.format("{}/d/{}/?{}", baseGrafanaUrl, dashboardUid, paramStr);
        return ResultDTO.success(grafanaUrl);

    }

    /**
     * 保存实例配置
     */
    @PostMapping("/serviceInstanceSaveConf")
    ResultDTO<Boolean> serviceInstanceSaveConf(@RequestBody ServiceConfUpgradeRequest serviceConfUpgradeRequest) {
        Integer serviceInstanceId = serviceConfUpgradeRequest.getServiceInstanceId();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        Integer stackServiceId = serviceInstanceEntity.getStackServiceId();
        Integer stackId = stackServiceRepository.findById(stackServiceId).get().getStackId();
        List<ServiceInstanceConfigEntity> serviceInstanceConfigEntities = new ArrayList<>();
        List<ServiceCustomConf> customConfList = serviceConfUpgradeRequest.getCustomConfList();
        List<ServicePresetConf> presetConfList = serviceConfUpgradeRequest.getPresetConfList();
        fillInstanceConfigEntities(stackId, stackServiceId, serviceInstanceId, serviceInstanceConfigEntities, presetConfList, customConfList, false);
        serviceInstanceConfigRepository.saveAllAndFlush(serviceInstanceConfigEntities);
        return ResultDTO.success(null);
    }

    /**
     * 服务实例角色列表
     */
    @GetMapping("/listWebURLs")
    public ResultDTO<List<ServiceInstanceWebUrlVO>> listWebURLs(Integer serviceInstanceId) {
        List<ServiceInstanceWebUrlVO> result = roleInstanceRepository.findByServiceInstanceId(serviceInstanceId).stream().map(new Function<ServiceRoleInstanceEntity, ServiceInstanceWebUrlVO>() {
            @Override
            public ServiceInstanceWebUrlVO apply(ServiceRoleInstanceEntity roleInstanceEntity) {
                ClusterNodeEntity nodeEntity = clusterNodeRepository.findById(roleInstanceEntity.getNodeId()).get();
                // 查找该角色实例绑定的web地址
                ServiceRoleInstanceWebuisEntity webuisEntity = roleInstanceWebuisRepository.findByServiceRoleInstanceId(roleInstanceEntity.getId());
                if (webuisEntity != null) {
                    StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findById(roleInstanceEntity.getStackServiceRoleId()).get();
                    return ServiceInstanceWebUrlVO.builder()
                            .name(stackServiceRoleEntity.getLabel() + " Web UI (" + nodeEntity.getHostname() + ")")
                            .ipUrl(webuisEntity.getWebIpUrl())
                            .hostnameUrl(webuisEntity.getWebHostUrl())
                            .build();
                }
                return null;
            }
        }).filter(new Predicate<ServiceInstanceWebUrlVO>() {
            @Override
            public boolean test(ServiceInstanceWebUrlVO serviceInstanceWebUrlVO) {
                return serviceInstanceWebUrlVO != null;
            }
        }).collect(Collectors.toList());

        return ResultDTO.success(result);
    }


    @GetMapping("/rolePodEvents")
    public ResultDTO<List<RolePodEventVO>> rolePodEvents(Integer roleId,Integer clusterId) {
        ServiceRoleInstanceEntity roleInstanceEntity = roleInstanceRepository.findById(roleId).get();
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findById(roleInstanceEntity.getStackServiceRoleId()).get();
        Integer nodeId = roleInstanceEntity.getNodeId();
        String hostIp = clusterNodeRepository.findById(nodeId).get().getIp();
        String namespace = clusterInfoRepository.findById(clusterId).get().getNamespace();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(roleInstanceEntity.getServiceInstanceId()).get();
        return kubeService.executeWithKubeClient(clusterId, client -> {
            String roleServiceFullName = stackServiceRoleEntity.getRoleFullName() + "-" + serviceInstanceEntity.getServiceName().toLowerCase();

            // 带有标签的pod
            List<Pod> podList = client.pods().inNamespace(namespace).withLabel("app", roleServiceFullName).list().getItems();
            // 指定节点的pod
            Pod pod = podList.stream().filter(pod1 -> pod1.getStatus().getHostIP().equals(hostIp)).findFirst().get();
            
            EventList eventList = client.v1().events()
                    .inNamespace(namespace)
                    .withField("involvedObject.name", pod.getMetadata().getName())
                    .list();

            List<RolePodEventVO> rolePodEventVOS = eventList.getItems().stream().map(event -> {
                RolePodEventVO eventVO = RolePodEventVO.builder()
                        .type(event.getType())
                        .message(event.getMessage())
                        .reason(event.getReason())
                        .count(event.getCount())
                        .lastTimestamp(K8sUtil.formatK8sDateStr(event.getLastTimestamp()))
                        .build();
                return eventVO;
            }).collect(Collectors.toList());
            return ResultDTO.success(rolePodEventVOS);
        });
    }

    @PostMapping("/stopCommand")
    public ResultDTO<Void> stopCommand(Integer commandId) {
        CommandEntity commandEntity = commandRepository.findById(commandId).get();
        CommandState commandState = commandEntity.getCommandState();
        if (commandState.isEnd()) {
            throw new IllegalArgumentException("指令已经结束");
        }
        cloudeonVertx.eventBus().request(Constant.VERTX_STOP_COMMAND_ADDRESS, commandId);
        return ResultDTO.success(null);
    }

    @PostMapping("/retryCommand")
    public ResultDTO<Void> retryCommand(Integer commandId) {
        CommandEntity commandEntity = commandRepository.findById(commandId).get();
        CommandState commandState = commandEntity.getCommandState();
        // 非 停止或错误 状态无法执行重试
        if (!(commandState.equals(CommandState.STOPPED) || commandState.equals(CommandState.ERROR))) {
            throw new IllegalArgumentException("指令状态为" + commandState + "，重试无效");
        }
        cloudeonVertx.eventBus().request(Constant.VERTX_RETRY_COMMAND_ADDRESS, commandId);
        return ResultDTO.success(null);
    }

}
