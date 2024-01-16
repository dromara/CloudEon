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
package org.dromara.cloudeon.service;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.VoidFunc1;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.dao.ClusterInfoRepository;
import org.dromara.cloudeon.entity.ClusterInfoEntity;
import org.dromara.cloudeon.utils.K8sUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class KubeService {

    @Resource
    private ClusterInfoRepository clusterInfoRepository;

    public KubernetesClient getKubeClient(Integer clusterId) {
        ClusterInfoEntity clusterInfo = clusterInfoRepository.findById(clusterId).get();
        KubernetesClient client = K8sUtil.getKubernetesClient(clusterInfo.getKubeConfig(), clusterInfo.getNamespace());
        testConnect(client);
        return client;
    }

    public void executeWithKubeClient(Integer clusterId, VoidFunc1<KubernetesClient> func) {
        try (KubernetesClient client = getKubeClient(clusterId)) {
            func.callWithRuntimeException(client);
        }
    }

    public <T> T executeWithKubeClient(Integer clusterId, Func1<KubernetesClient, T> func) {
        try (KubernetesClient client = getKubeClient(clusterId)) {
            return func.callWithRuntimeException(client);
        }
    }


    public void testConnect(KubernetesClient client) {
        String minor = client.getKubernetesVersion().getMinor();
        log.debug("成功连接k8s集群：{}", client.getMasterUrl());
    }

    public boolean checkNamespace(KubernetesClient kubernetesClient, String namespace) {
        return kubernetesClient.namespaces().withName(namespace).get() != null;
    }
}
