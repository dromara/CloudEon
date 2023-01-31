package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.ServiceInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceInstanceRepository extends JpaRepository<ServiceInstanceEntity, Integer> {
    @Query(value = "select max(stack_service_id) from udh_service_instance", nativeQuery = true)
    int maxByStackServiceId(Integer stackServiceId);

    @Query(value = "select  from udh_service_instance a join udh_stack_service b on a.stack_service_id = b.id where a.cluster_id =:clusterId and  b.name = :stackServiceName", nativeQuery = true)
    ServiceInstanceEntity findByClusterIdAndStackServiceName(Integer clusterId, String stackServiceName);

    ServiceInstanceEntity findByClusterIdAndStackServiceId(Integer clusterId, Integer stackServiceId);

}