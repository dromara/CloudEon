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
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dromara.cloudeon.config.CloudeonConfigProp;
import org.dromara.cloudeon.dao.*;
import org.dromara.cloudeon.dto.RoleNodeInfo;
import org.dromara.cloudeon.entity.*;
import org.dromara.cloudeon.service.KubeService;
import org.dromara.cloudeon.utils.Constant;
import org.dromara.cloudeon.utils.FreemarkerUtil;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
public abstract class ConfigTask extends BaseCloudeonTask implements ApplyOrDeleteTask {

    private StackServiceRepository stackServiceRepository = SpringUtil.getBean(StackServiceRepository.class);
    private ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
    private ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);
    private ServiceRoleInstanceRepository roleInstanceRepository = SpringUtil.getBean(ServiceRoleInstanceRepository.class);
    private ServiceInstanceConfigRepository configRepository = SpringUtil.getBean(ServiceInstanceConfigRepository.class);
    private CloudeonConfigProp cloudeonConfigProp = SpringUtil.getBean(CloudeonConfigProp.class);
    private Environment environment = SpringUtil.getBean(Environment.class);


    @Override
    public void internalExecute() {
        TaskParam taskParam = getTaskParam();
        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();
        // 查询服务实例所有配置项
        List<ServiceInstanceConfigEntity> allConfigEntityList = configRepository.findByServiceInstanceId(serviceInstanceId);

        Integer globalServiceInstanceId = serviceInstanceRepository.findByClusterIdAndStackServiceName(serviceInstanceEntity.getClusterId(), "GLOBAL");
        List<ServiceInstanceConfigEntity> globalConfigEntityList = configRepository.findByServiceInstanceId(globalServiceInstanceId);
        Map<String, String> globalConfigMap = globalConfigEntityList.stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue));
        if (!"GLOBAL".equalsIgnoreCase(stackServiceEntity.getName())) {
            allConfigEntityList.addAll(globalConfigEntityList);
        }

        String stackCode = stackServiceEntity.getStackCode();
        String stackServiceName = stackServiceEntity.getName().toLowerCase();

        KubeService kubeService = SpringUtil.getBean(KubeService.class);
        KubernetesClient client = kubeService.getKubeClient(serviceInstanceEntity.getClusterId());

        Map<String, Object> dataModel = getDataModel(serviceInstanceId);
        Map<String, String> labels = Maps.newHashMap();
        labels.put("name", stackServiceEntity.getName().toLowerCase());

        // 设置加载的目录
        String serviceBaseDir = cloudeonConfigProp.getStackLoadPath() + File.separator + stackCode + File.separator + stackServiceName;
        Map<String, String> fileStrMap = Maps.newHashMap();

        // 加载serviceRender目录
        // 目录下的文件将用于生成一个configmap，等待容器挂载后将其渲染
        String serviceRenderDir = serviceBaseDir + File.separator + Constant.SERVICE_RENDER_DIR;
        File serviceRenderDirFile = new File(serviceRenderDir);
        if (!FileUtil.isEmpty(serviceRenderDirFile)) {
            log.info("加载serviceRender目录：" + serviceRenderDir);
            for (File renderFile : Objects.requireNonNull(serviceRenderDirFile.listFiles())) {
                if (renderFile.isDirectory()) {
                    continue;
                }
                fileStrMap.put(renderFile.getName(), FileUtil.readUtf8String(renderFile));
            }
            Resource<ConfigMap> configMapResource = client.configMaps().load(IoUtil.toUtf8Stream(ConfigTask.getConfigMapStr(stackServiceName + "-service-render", labels, fileStrMap)));
            if (isApplyTask()) {
                configMapResource.forceConflicts().serverSideApply();
            } else {
                configMapResource.delete();
            }
        } else {
            log.info("serviceRender目录为空");
        }

        // 加载serviceCommon目录
        // 目录下的文件+values.json，将用于生成一个configmap
        String serviceCommonDir = serviceBaseDir + File.separator + Constant.SERVICE_COMMON_DIR;
        File serviceCommonDirFile = new File(serviceCommonDir);
        if (!FileUtil.isEmpty(serviceCommonDirFile)) {
            fileStrMap = Maps.newHashMap();
            log.info("加载serviceCommon目录：" + serviceCommonDir);
            for (File file : Objects.requireNonNull(serviceCommonDirFile.listFiles())) {
                if (file.isDirectory()) {
                    continue;
                }
                fileStrMap.put(file.getName(), FileUtil.readUtf8String(file));
            }
            fileStrMap.put("values.json", JSONUtil.toJsonStr(dataModel));
            Resource<ConfigMap> resource = client.configMaps().load(IoUtil.toUtf8Stream(ConfigTask.getConfigMapStr(stackServiceName + "-service-common", labels, fileStrMap)));
            if (isApplyTask()) {
                resource.forceConflicts().serverSideApply();
            } else {
                resource.delete();
            }
        } else {
            log.info("serviceCommon目录为空");
        }

        // 加载k8sCommon目录
        // 目录下的每一个文件都将视为k8s部署文件直接进行操作
        String k8sCommonDir = serviceBaseDir + File.separator + Constant.K8S_COMMON_DIR;
        File k8sCommonDirFile = new File(k8sCommonDir);
        if (!FileUtil.isEmpty(k8sCommonDirFile)) {
            log.info("加载k8sCommon目录：" + k8sCommonDirFile);
            for (File file : Objects.requireNonNull(k8sCommonDirFile.listFiles())) {
                if (file.isDirectory()) {
                    continue;
                }
                ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata> resource = client.load(FileUtil.getInputStream(file));
                if (isApplyTask()) {
                    resource.forceConflicts().serverSideApply();
                } else {
                    resource.delete();
                }
            }
        } else {
            log.info("k8sCommon目录为空");
        }

        // 加载k8sRender目录
        // 目录下的每一个文件都将视为k8s模板文件进行渲染后操作
        String k8sRenderDir = serviceBaseDir + File.separator + Constant.K8S_RENDER_DIR;
        File k8sRenderDirFile = new File(k8sRenderDir);
        if (!FileUtil.isEmpty(k8sRenderDirFile)) {
            log.info("加载k8sRender目录：" + k8sRenderDirFile);
            for (File file : Objects.requireNonNull(k8sRenderDirFile.listFiles())) {
                if (file.isDirectory()) {
                    continue;
                }
                String renderStr = FreemarkerUtil.templateEval(FileUtil.readUtf8String(file), dataModel);
                ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata> resource = null;
                try (InputStream in = IoUtil.toUtf8Stream(renderStr)) {
                    resource = client.load(in);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (isApplyTask()) {
                    resource.forceConflicts().serverSideApply();
                } else {
                    resource.delete();
                }
            }
        } else {
            log.info("k8sRender目录为空");
        }

        if (Boolean.parseBoolean(globalConfigMap.get("global.kube-prometheus.enable"))) {
            // 加载kube-prometheus-render目录
            // 目录下的每一个文件都将视为k8s模板文件进行渲染后操作
            String kubePrometheusRenderDir = serviceBaseDir + File.separator + Constant.KUBE_PROMETHEUS_RENDER_DIR;
            File kubePrometheusRenderDirFile = new File(kubePrometheusRenderDir);
            if (!FileUtil.isEmpty(kubePrometheusRenderDirFile)) {
                log.info("加载kube-prometheus-render目录：" + kubePrometheusRenderDirFile);
                for (File file : Objects.requireNonNull(kubePrometheusRenderDirFile.listFiles())) {
                    if (file.isDirectory()) {
                        continue;
                    }
                    String renderStr = FreemarkerUtil.templateEval(FileUtil.readUtf8String(file), dataModel);
                    ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata> resource = null;
                    try (InputStream in = IoUtil.toUtf8Stream(renderStr)) {
                        resource = client.load(in);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (isApplyTask()) {
                        resource.forceConflicts().serverSideApply();
                    } else {
                        resource.delete();
                    }
                }
            } else {
                log.info("kube-prometheus-render目录为空");
            }
        }


    }

    private Map<String, Object> getDataModel(Integer serviceInstanceId) {
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();
        List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceId(serviceInstanceId);

        Map<String, Object> dataModel = new HashMap<>();

        // 查询服务实例所有配置项,包括全局
        List<ServiceInstanceConfigEntity> allConfigEntityList = configRepository.findByServiceInstanceId(serviceInstanceId);
        if (!"GLOBAL".equalsIgnoreCase(stackServiceEntity.getName())) {
            Integer globalServiceInstanceId = serviceInstanceRepository.findByClusterIdAndStackServiceName(serviceInstanceEntity.getClusterId(), "GLOBAL");
            allConfigEntityList.addAll(configRepository.findByServiceInstanceId(globalServiceInstanceId));
        }
        dataModel.put("serviceFullName", serviceInstanceEntity.getServiceName().toLowerCase());
        dataModel.put("service", serviceInstanceEntity);
        dataModel.put("conf", allConfigEntityList.stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue)));
        dataModel.put("serviceRoles", getServiceRoles(roleInstanceEntities, clusterNodeRepository));
        try {
            dataModel.put("cloudeonURL", "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + environment.getProperty("server.port"));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

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

        String dependenceServiceInstanceIds = serviceInstanceEntity.getDependenceServiceInstanceIds();
        if (StringUtils.isNotBlank(dependenceServiceInstanceIds)) {
            String[] depServiceInstanceIds = dependenceServiceInstanceIds.split(",");
            buildDependenceServiceInModel(dataModel, depServiceInstanceIds);
        }

        return dataModel;

    }


    public static String getConfigMapStr(String configmapName, Map<String, String> labels, Map<String, String> fileStrMap) {
        Map<String, Object> dataModel = Maps.newHashMapWithExpectedSize(3);
        dataModel.put("configmapName", configmapName);
        dataModel.put("labels", labels);
        for (Map.Entry<String, String> dataEntry : fileStrMap.entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String line : dataEntry.getValue().split("\\R")) {
                sb.append("    ").append(line).append("\n");
            }
            fileStrMap.put(dataEntry.getKey(), sb.toString());
        }
        dataModel.put("fileStrMap", fileStrMap);
        return FreemarkerUtil.templateEval(ResourceUtil.readUtf8Str("templates/configmap.yaml.ftl"), dataModel);
    }


    /**
     * 构建依赖服务进入Model中
     */
    private void buildDependenceServiceInModel(Map<String, Object> dataModel, String[] depServiceInstanceIds) {
        Map<String, Object> services = new HashMap<>();
        Arrays.stream(depServiceInstanceIds).forEach(id -> {
            Integer serviceInstanceId = Integer.valueOf(id);
            ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
            Integer stackServiceId = serviceInstanceEntity.getStackServiceId();
            String stackServiceName = stackServiceRepository.findById(stackServiceId).get().getName();

            // 查询服务实例所有配置项
            List<ServiceInstanceConfigEntity> allConfigEntityList = configRepository.findByServiceInstanceId(serviceInstanceId);
            // 查出所有角色
            List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceId(serviceInstanceId);
            Map<String, List<RoleNodeInfo>> serviceRoles = getServiceRoles(roleInstanceEntities, clusterNodeRepository);
            services.put(stackServiceName, ImmutableMap.of(
                    "conf", allConfigEntityList.stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue)),
                    "serviceRoles", serviceRoles,
                    "service", serviceInstanceEntity));
        });

        dataModel.put("dependencies", services);
    }

    private Map<String, List<RoleNodeInfo>> getServiceRoles(List<ServiceRoleInstanceEntity> roleInstanceEntities, ClusterNodeRepository clusterNodeRepository) {
        Map<String, List<RoleNodeInfo>> serviceRoles = roleInstanceEntities.stream().map(new Function<ServiceRoleInstanceEntity, RoleNodeInfo>() {
            @Override
            public RoleNodeInfo apply(ServiceRoleInstanceEntity serviceRoleInstanceEntity) {
                ClusterNodeEntity nodeEntity = clusterNodeRepository.findById(serviceRoleInstanceEntity.getNodeId()).get();
                return new RoleNodeInfo(serviceRoleInstanceEntity.getId(), nodeEntity.getHostname(), serviceRoleInstanceEntity.getServiceRoleName());
            }
        }).collect(Collectors.groupingBy(RoleNodeInfo::getRoleName));
        return serviceRoles;
    }

}
