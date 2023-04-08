package com.data.cloudeon.dao;

import com.data.cloudeon.entity.ServiceInstanceConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceInstanceConfigRepository extends JpaRepository<ServiceInstanceConfigEntity, Integer> {

    List<ServiceInstanceConfigEntity> findByServiceInstanceId(Integer serviceInstanceId);
    ServiceInstanceConfigEntity findByServiceInstanceIdAndName(Integer serviceInstanceId, String name);

    List<ServiceInstanceConfigEntity> findByServiceInstanceIdAndConfFile(Integer serviceInstanceId, String group);

    int deleteByServiceInstanceId(Integer serviceInstanceId);

}