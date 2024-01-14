package org.dromara.cloudeon.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.cloudeon.dao.ClusterInfoRepository;
import org.dromara.cloudeon.dao.ServiceInstanceConfigRepository;
import org.dromara.cloudeon.dao.ServiceInstanceRepository;
import org.dromara.cloudeon.entity.ServiceInstanceConfigEntity;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ServiceService {

    @Resource
    private ClusterInfoRepository clusterInfoRepository;
    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;
    @Resource
    private ServiceInstanceConfigRepository serviceInstanceConfigRepository;

    public String getNamespace(Integer clusterId) {
        String namespace = clusterInfoRepository.findById(clusterId).get().getNamespace();
        if (StringUtils.isBlank(namespace)) {
            namespace = "default";
        }
        return namespace;
    }

    public String getNamespace(ServiceInstanceEntity serviceInstanceEntity) {
        return getNamespace(serviceInstanceEntity.getClusterId());
    }

    public Map<String, String> getConfigMaps(Integer clusterId, String stackServiceName) {
        return serviceInstanceConfigRepository
                .findByServiceInstanceId(serviceInstanceRepository
                        .findByClusterIdAndStackServiceName(clusterId, stackServiceName))
                .stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue));
    }

}
