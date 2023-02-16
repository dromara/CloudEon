package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.ServiceRoleInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRoleInstanceRepository extends JpaRepository<ServiceRoleInstanceEntity, Integer> {
    List<ServiceRoleInstanceEntity> findByServiceInstanceIdAndStackServiceRoleId(Integer serviceInstanceId, Integer stackServiceRoleId);
    List<ServiceRoleInstanceEntity> findByServiceInstanceId(Integer serviceInstanceId);

    int deleteByServiceInstanceId(Integer serviceInstanceId);

    int countByServiceInstanceIdAndServiceRoleName(Integer serviceInstanceId, String serviceRoleName);
}