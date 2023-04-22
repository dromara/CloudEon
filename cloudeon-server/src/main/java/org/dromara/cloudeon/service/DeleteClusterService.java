package org.dromara.cloudeon.service;

import org.dromara.cloudeon.dao.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class DeleteClusterService {
    @Resource
    ServiceInstanceRepository serviceInstanceRepository ;
    @Resource
    ServiceInstanceConfigRepository serviceInstanceConfigRepository;
    @Resource
    ServiceRoleInstanceRepository roleInstanceRepository ;
    @Resource
    ServiceRoleInstanceWebuisRepository roleInstanceWebuisRepository;
    @Resource
    private AlertMessageRepository alertMessageRepository;

    @Transactional(rollbackFor = Exception.class)
    public void deleteOneService(Integer serviceInstanceId) {
        // 删除服务实例表
        serviceInstanceRepository.deleteById(serviceInstanceId);
        // 删除服务角色实例表
        roleInstanceRepository.deleteByServiceInstanceId(serviceInstanceId);
        // 删除服务角色配置表
        serviceInstanceConfigRepository.deleteByServiceInstanceId(serviceInstanceId);
        // 删除服务ui表
        roleInstanceWebuisRepository.deleteByServiceInstanceId(serviceInstanceId);
        // 删除告警信息表
        alertMessageRepository.deleteByServiceInstanceId(serviceInstanceId);

    }
}
