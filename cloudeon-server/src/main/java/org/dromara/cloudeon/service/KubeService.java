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

import org.dromara.cloudeon.dao.ClusterInfoRepository;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class KubeService {

    @Resource
    private ClusterInfoRepository clusterInfoRepository;

    public KubernetesClient getKubeClient(Integer clusterId) {
        String kubeConfig = clusterInfoRepository.findById(clusterId).get().getKubeConfig();
        KubernetesClient client = getKubernetesClient(kubeConfig);
        testConnect(client);
        return client;
    }

    public KubernetesClient getKubernetesClient(String kubeConfig) {
        Config config = Config.fromKubeconfig(kubeConfig);
        KubernetesClient client = new KubernetesClientBuilder().withConfig(config).build();
        return client;
    }

    public void testConnect(KubernetesClient client) {
        String minor = client.getKubernetesVersion().getMinor();
        log.info("成功连接k8s集群：{}", client.getMasterUrl());
    }
}
