package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.ServiceInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceInstanceRepository extends JpaRepository<ServiceInstanceEntity, Integer> {

    @Query(value = "select a.id  from ServiceInstanceEntity a join StackServiceEntity b on a.stackServiceId = b.id where a.clusterId =:clusterId and  b.name = :stackServiceName")
    Integer findByClusterIdAndStackServiceName(@Param("clusterId") Integer clusterId, @Param("stackServiceName") String stackServiceName);

    ServiceInstanceEntity findByClusterIdAndStackServiceId(Integer clusterId, Integer stackServiceId);

}