package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.ServiceInstanceConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceInstanceConfigRepository extends JpaRepository<ServiceInstanceConfigEntity, Integer> {

    List<ServiceInstanceConfigEntity> findByServiceInstanceId(Integer serviceInstanceId);

    List<ServiceInstanceConfigEntity> findByServiceInstanceIdAndCustomConfFile(Integer serviceInstanceId, String customConfFile);

    int deleteByServiceInstanceId(Integer serviceInstanceId);

}