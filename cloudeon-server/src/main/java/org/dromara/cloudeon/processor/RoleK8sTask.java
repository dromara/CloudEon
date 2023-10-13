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
package org.dromara.cloudeon.processor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.spring.SpringUtil;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.RollableScalableResource;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dromara.cloudeon.config.CloudeonConfigProp;
import org.dromara.cloudeon.dao.*;
import org.dromara.cloudeon.entity.*;
import org.dromara.cloudeon.enums.ServiceRoleState;
import org.dromara.cloudeon.service.KubeService;
import org.dromara.cloudeon.utils.Constant;
import org.dromara.cloudeon.utils.FreemarkerUtil;
import org.dromara.cloudeon.utils.K8sUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 为角色实例创建k8s 资源
 */
@NoArgsConstructor
public abstract class RoleK8sTask extends BaseCloudeonTask implements ApplyOrDeleteTask {

    private ServiceRoleState getTargetState() {
        if (isApplyTask()) {
            return ServiceRoleState.ROLE_STARTED;
        } else {
            return ServiceRoleState.ROLE_STOPPED;
        }
    }

    @Override
    public void internalExecute() {
        StackServiceRepository stackServiceRepository = SpringUtil.getBean(StackServiceRepository.class);
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        StackServiceRoleRepository stackServiceRoleRepository = SpringUtil.getBean(StackServiceRoleRepository.class);
        ServiceRoleInstanceRepository serviceRoleInstanceRepository = SpringUtil.getBean(ServiceRoleInstanceRepository.class);
        ServiceInstanceConfigRepository configRepository = SpringUtil.getBean(ServiceInstanceConfigRepository.class);
        KubeService kubeService = SpringUtil.getBean(KubeService.class);
        ClusterInfoRepository clusterInfoRepository = SpringUtil.getBean(ClusterInfoRepository.class);


        CloudeonConfigProp cloudeonConfigProp = SpringUtil.getBean(CloudeonConfigProp.class);

        // 查询框架服务角色名获取模板名
        String roleName = taskParam.getRoleName();
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), roleName);
        String roleFullName = stackServiceRoleEntity.getRoleFullName();

        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        String roleType = stackServiceRoleEntity.getType();
        //当角色类型为EMPTY，不进行任何具体操作
        if ("EMPTY".equalsIgnoreCase(roleType)) {
            // 更新角色实例状态
            List<ServiceRoleInstanceEntity> roleInstanceEntities = serviceRoleInstanceRepository.findByServiceInstanceIdAndServiceRoleName(serviceInstanceId, stackServiceRoleEntity.getName());
            roleInstanceEntities.forEach(r -> {
                r.setServiceRoleState(getTargetState());
                serviceRoleInstanceRepository.save(r);
            });
            return;
        }

        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();

        String stackCode = stackServiceEntity.getStackCode();
        String stackServiceName = stackServiceEntity.getName().toLowerCase();

        // 查询服务实例所有配置项
        List<ServiceInstanceConfigEntity> allConfigEntityList = configRepository.findByServiceInstanceId(serviceInstanceId);
        if (!"GLOBAL".equalsIgnoreCase(stackServiceEntity.getName())) {
            Integer globalServiceInstanceId = serviceInstanceRepository.findByClusterIdAndStackServiceName(serviceInstanceEntity.getClusterId(), "GLOBAL");
            allConfigEntityList.addAll(configRepository.findByServiceInstanceId(globalServiceInstanceId));
        }


        // 获取集群的namespace
        String namespace = clusterInfoRepository.findById(serviceInstanceEntity.getClusterId()).get().getNamespace();
        if (StringUtils.isBlank(namespace)) {
            namespace = "default";
        }

        // 渲染生成k8s资源
        String k8sTemplateDir = cloudeonConfigProp.getStackLoadPath() + File.separator + stackCode + File.separator + stackServiceName + File.separator + Constant.K8S_DIR;
        log.info("加载服务实例角色k8s资源模板目录：" + k8sTemplateDir);

        // 查询本服务实例拥有的指定角色节点数
        int roleNodeCnt = serviceRoleInstanceRepository.countByServiceInstanceIdAndServiceRoleName(serviceInstanceId, roleName);


        // 构建数据模型
        Map<String, Object> dataModel = new HashMap<>();
        String roleServiceFullName = roleFullName + "-" + serviceInstanceEntity.getServiceName().toLowerCase();
        dataModel.put("roleFullName", roleFullName);
        dataModel.put("roleServiceFullName", roleServiceFullName);
        dataModel.put("serviceFullName", serviceInstanceEntity.getServiceName().toLowerCase());
        dataModel.put("service", serviceInstanceEntity);
        dataModel.put("roleNodeCnt", roleNodeCnt);
        dataModel.put("runAs", stackServiceEntity.getRunAs());
        dataModel.put("conf", allConfigEntityList.stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue)));
        dataModel.put("namespace", namespace);

        // 根据角色名称获取最合适的k8s模板文件
        String k8sTemplateFileName = "";
        for (String fileName : FileUtil.listFileNames(k8sTemplateDir)) {
            if (roleFullName.startsWith(fileName.substring(0, fileName.indexOf('.'))) && fileName.length() > k8sTemplateFileName.length()) {
                k8sTemplateFileName = fileName;
            }
        }
        String modelYamlStr = FreemarkerUtil.templateEval(FileUtil.readUtf8String(k8sTemplateDir + File.separator + k8sTemplateFileName), dataModel);

        // 调用k8s命令启动资源
        KubernetesClient client = kubeService.getKubeClient(serviceInstanceEntity.getClusterId());
        String resourceName = "";

        try {
            ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata> loadResource = client.load(IoUtil.toUtf8Stream(modelYamlStr));
            if (!isApplyTask()) {
                loadResource.delete();
            } else {
                try {
                    List<HasMetadata> metadata = loadResource.forceConflicts().serverSideApply();
                    resourceName = metadata.get(0).getMetadata().getName();
                    switch (roleType.toUpperCase()) {
                        case "JOB":
                            long waitSeconds = 300;
                            K8sUtil.waitForJobCompleted(namespace, resourceName, client, log, waitSeconds);
                            break;
                        default:
                            final Deployment deployment = client.apps().deployments().withName(resourceName).get();
                            Resource<Deployment> resource = client.resource(deployment);
                            int amount = 10;
                            log.info("在k8s上启动deployment: {}  ,持续等待 {} 分钟", resourceName, amount);
                            resource.waitUntilReady(amount, TimeUnit.MINUTES);

                            // 打印deployment的输出日志
                            RollableScalableResource<Deployment> scalableResource = client.apps().deployments().withName(resourceName);
                            log.info(scalableResource.getLog());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage() + ":\n" + modelYamlStr);
                    if (!"JOB".equalsIgnoreCase(roleType)) {
                        // 打印deployment的输出日志
                        if (StringUtils.isNotEmpty(resourceName)) {
                            RollableScalableResource<Deployment> scalableResource = client.apps().deployments().withName(resourceName);
                            log.error(scalableResource.getLog());
                        }
                    }
                    throw new RuntimeException(e);
                }
            }
        } finally {
            client.close();
        }

        // 更新角色实例状态
        List<ServiceRoleInstanceEntity> roleInstanceEntities = serviceRoleInstanceRepository.findByServiceInstanceIdAndServiceRoleName(serviceInstanceId, stackServiceRoleEntity.getName());
        roleInstanceEntities.forEach(r -> {
            r.setServiceRoleState(getTargetState());
            serviceRoleInstanceRepository.save(r);
        });

    }
}
