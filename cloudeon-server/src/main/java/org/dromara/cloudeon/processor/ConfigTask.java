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
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.dsl.ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.NoArgsConstructor;
import org.dromara.cloudeon.entity.ServiceInstanceConfigEntity;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.StackServiceEntity;
import org.dromara.cloudeon.utils.Constant;
import org.dromara.cloudeon.utils.FreemarkerUtil;
import org.dromara.cloudeon.utils.K8sUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
public abstract class ConfigTask extends BaseCloudeonTask implements ApplyOrDeleteTask {

    @Override
    public void internalExecute() {
        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();
        Integer clusterId = serviceInstanceEntity.getClusterId();
        Integer globalServiceInstanceId = serviceInstanceRepository.findByClusterIdAndStackServiceName(serviceInstanceEntity.getClusterId(), "GLOBAL");
        List<ServiceInstanceConfigEntity> globalConfigEntityList = configRepository.findByServiceInstanceId(globalServiceInstanceId);

        Map<String, String> globalConfigMap = globalConfigEntityList.stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue));

        String stackCode = stackServiceEntity.getStackCode();
        String stackServiceName = K8sUtil.formatK8sNameStr(stackServiceEntity.getName());

        Map<String, Object> dataModel = serviceService.getDataModel(serviceInstanceId, taskParam.getRoleName());
        Map<String, String> labels = Maps.newHashMap();
        labels.put("name", K8sUtil.formatK8sNameStr(stackServiceEntity.getName()));

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
            Map<String, String> finalFileStrMap1 = fileStrMap;
            kubeService.executeWithKubeClient(clusterId, client -> {
                Resource<ConfigMap> configMapResource = client.configMaps().load(IoUtil.toUtf8Stream(K8sUtil.getConfigMapStr(stackServiceName + "-service-render", labels, finalFileStrMap1)));
                if (isApplyTask()) {
                    configMapResource.forceConflicts().serverSideApply();
                } else {
                    configMapResource.delete();
                }
            });
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
            Map<String, String> finalFileStrMap = fileStrMap;
            kubeService.executeWithKubeClient(clusterId, client -> {
                Resource<ConfigMap> resource = client.configMaps().load(IoUtil.toUtf8Stream(K8sUtil.getConfigMapStr(stackServiceName + "-service-common", labels, finalFileStrMap)));
                if (isApplyTask()) {
                    resource.forceConflicts().serverSideApply();
                } else {
                    resource.delete();
                }
            });
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
                kubeService.executeWithKubeClient(clusterId, client -> {
                    ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata> resource = client.load(FileUtil.getInputStream(file));
                    if (isApplyTask()) {
                        resource.forceConflicts().serverSideApply();
                    } else {
                        resource.delete();
                    }
                });
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
                kubeService.executeWithKubeClient(clusterId, client -> {
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
                });
            }
        } else {
            log.info("k8sRender目录为空");
        }
        // 加载kube-prometheus-render目录
        // 目录下的每一个文件都将视为k8s模板文件进行渲染后操作
        if (!"NONE".equals(globalConfigMap.get("global.monitor.type"))) {
            String kubePrometheusRenderDir = serviceBaseDir + File.separator + Constant.KUBE_PROMETHEUS_RENDER_DIR;
            File kubePrometheusRenderDirFile = new File(kubePrometheusRenderDir);
            if (!FileUtil.isEmpty(kubePrometheusRenderDirFile)) {
                log.info("加载kube-prometheus-render目录：" + kubePrometheusRenderDirFile);
                for (File file : Objects.requireNonNull(kubePrometheusRenderDirFile.listFiles())) {
                    if (file.isDirectory()) {
                        continue;
                    }
                    String renderStr = FreemarkerUtil.templateEval(FileUtil.readUtf8String(file), dataModel);
                    kubeService.executeWithKubeClient(clusterId, client -> {
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
                    });
                }
            } else {
                log.info("kube-prometheus-render目录为空");
            }
        }
    }

}
