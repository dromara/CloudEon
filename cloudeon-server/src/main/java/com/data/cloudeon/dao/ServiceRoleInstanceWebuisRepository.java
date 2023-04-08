package com.data.cloudeon.dao;

import com.data.cloudeon.entity.ServiceRoleInstanceWebuisEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRoleInstanceWebuisRepository extends JpaRepository<ServiceRoleInstanceWebuisEntity, Integer> {
    int deleteByServiceInstanceId(Integer serviceInstanceId);

    ServiceRoleInstanceWebuisEntity findByServiceRoleInstanceId(Integer serviceRoleInstanceId);
}