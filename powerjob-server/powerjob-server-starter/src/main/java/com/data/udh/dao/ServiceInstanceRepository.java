package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.ServiceInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceInstanceRepository extends JpaRepository<ServiceInstanceEntity, Integer> {
    @Query(value = "select max(stack_service_id) from udh_service_instance", nativeQuery = true)
    int maxByStackServiceId(Integer stackServiceId);

}