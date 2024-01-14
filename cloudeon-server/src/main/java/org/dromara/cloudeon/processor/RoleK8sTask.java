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
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dromara.cloudeon.config.CloudeonConfigProp;
import org.dromara.cloudeon.crd.helmchart.HelmChart;
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
import java.util.Objects;
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
        String roleFullName = K8sUtil.formatK8sNameStr(stackServiceRoleEntity.getRoleFullName());

        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        String roleType = stackServiceRoleEntity.getType();

        // 查询本服务实例拥有的指定角色节点数
        int roleNodeCnt = serviceRoleInstanceRepository.countByServiceInstanceIdAndServiceRoleName(serviceInstanceId, roleName);

        //当角色没有分配节点时，不执行部署
        if ("EMPTY".equalsIgnoreCase(roleType) || roleNodeCnt == 0) {
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
        String stackServiceName = stackServiceEntity.getName().toLowerCase().replace('_', '-');

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

        // 构建数据模型
        Map<String, Object> dataModel = new HashMap<>();
        String roleServiceFullName = roleFullName + "-" + K8sUtil.formatK8sNameStr(serviceInstanceEntity.getServiceName());
        dataModel.put("roleFullName", roleFullName);
        dataModel.put("roleServiceFullName", roleServiceFullName);
        dataModel.put("serviceFullName", K8sUtil.formatK8sNameStr(serviceInstanceEntity.getServiceName()));
        dataModel.put("service", serviceInstanceEntity);
        dataModel.put("roleNodeCnt", roleNodeCnt);
        dataModel.put("runAs", stackServiceEntity.getRunAs());
        dataModel.put("conf", allConfigEntityList.stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue)));
        dataModel.put("namespace", namespace);
        // 获取该服务支持的自定义配置文件名
        String customConfigFiles = stackServiceEntity.getCustomConfigFiles();
        Map<String, Map<String, String>> confFiles = new HashMap<>();

        if (StringUtils.isNoneBlank(customConfigFiles)) {
            for (String confFileName : customConfigFiles.split(",")) {
                List<ServiceInstanceConfigEntity> groupConfEntities = configRepository.findByServiceInstanceIdAndConfFile(serviceInstanceId, confFileName);
                HashMap<String, String> map = new HashMap<>();
                for (ServiceInstanceConfigEntity groupConf : groupConfEntities) {
                    map.put(groupConf.getName(), groupConf.getValue());
                }
                confFiles.put(confFileName, map);
            }
        }
        dataModel.put("confFiles", confFiles);

        // 根据角色名称获取最合适的k8s模板文件
        String k8sTemplateFileName = "";
        for (String fileName : FileUtil.listFileNames(k8sTemplateDir)) {
            if (roleFullName.startsWith(fileName.substring(0, fileName.indexOf('.'))) && fileName.length() > k8sTemplateFileName.length()) {
                k8sTemplateFileName = fileName;
            }
        }
        String modelYamlStr = FreemarkerUtil.templateEval(FileUtil.readUtf8String(k8sTemplateDir + File.separator + k8sTemplateFileName), dataModel);
        // 当生成内容为空白时，则不需执行
        if (StringUtils.isBlank(modelYamlStr)) {
            // 更新角色实例状态
            List<ServiceRoleInstanceEntity> roleInstanceEntities = serviceRoleInstanceRepository.findByServiceInstanceIdAndServiceRoleName(serviceInstanceId, stackServiceRoleEntity.getName());
            roleInstanceEntities.forEach(r -> {
                r.setServiceRoleState(getTargetState());
                serviceRoleInstanceRepository.save(r);
            });
            return;
        }
        // 调用k8s命令启动资源
        KubernetesClient client = kubeService.getKubeClient(serviceInstanceEntity.getClusterId());

        try {
            ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata> loadResource = client.load(IoUtil.toUtf8Stream(modelYamlStr));
            if (!isApplyTask()) {
                if (roleType.equalsIgnoreCase("HELM_CHART")) {
                    HasMetadata firstResource = loadResource.get().get(0);
                    // 如果helmChart不存在，那么不会创建delete job，不能使用waitForJobCompleted
                    if (firstResource == null) {
                        loadResource.delete();
                        loadResource.waitUntilCondition(Objects::isNull, 3, TimeUnit.MINUTES);
                    } else {
                        HelmChart helmChart = client.resources(HelmChart.class).load(IoUtil.toUtf8Stream(modelYamlStr)).item();
                        Job helmChartJob = K8sUtil.getDeleteJobByHelmChart(client, helmChart);
                        K8sUtil.waitForJobCompleted(() -> {
                            loadResource.delete();
                            loadResource.waitUntilCondition(Objects::isNull, 3, TimeUnit.MINUTES);
                        }, taskParam, client, helmChartJob, 300);
                    }
                } else {
                    loadResource.delete();
                    loadResource.waitUntilCondition(Objects::isNull, 3, TimeUnit.MINUTES);
                }
            } else {
                try {
                    long waitSeconds = 600;
                    switch (roleType.toUpperCase()) {
                        case "JOB":
                            Job job = client.batch().v1().jobs().load(IoUtil.toUtf8Stream(modelYamlStr)).item();
                            K8sUtil.waitForJobCompleted(() -> loadResource.forceConflicts().serverSideApply(), taskParam, client, job, waitSeconds);
                            break;
                        case "HELM_CHART":
                            HelmChart helmChart = client.resources(HelmChart.class).load(IoUtil.toUtf8Stream(modelYamlStr)).item();
                            Job helmChartInstallJob = K8sUtil.getInstallJobByHelmChart(client, helmChart);
                            K8sUtil.waitForJobCompleted(() -> loadResource.forceConflicts().serverSideApply(), taskParam, client, helmChartInstallJob, waitSeconds);
                            break;
                        case "DEPLOYMENT":
                            Deployment deployment = client.apps().deployments().load(IoUtil.toUtf8Stream(modelYamlStr)).item();
                            K8sUtil.waitForDeploymentReady(() -> loadResource.forceConflicts().serverSideApply(), taskParam, client, deployment, waitSeconds);
                            break;
                        default:
                            loadResource.forceConflicts().serverSideApply();
                            loadResource.waitUntilReady(waitSeconds, TimeUnit.SECONDS);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage() + ":\n" + modelYamlStr);
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
