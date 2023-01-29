package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.ServiceInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceInstanceRepository extends JpaRepository<ServiceInstanceEntity, Integer> {
}