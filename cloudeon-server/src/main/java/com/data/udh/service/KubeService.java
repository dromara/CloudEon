package com.data.udh.service;

import com.data.udh.dao.ClusterInfoRepository;
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
        KubernetesClient client = new KubernetesClientBuilder().withConfig(kubeConfig).build();
        String minor = client.getKubernetesVersion().getMinor();
        log.info("成功通过集群id：{}的kubeconfig连接k8s集群：{}", clusterId, minor);
        return client;
    }
}
