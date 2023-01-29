package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.ServiceRoleInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRoleInstanceRepository extends JpaRepository<ServiceRoleInstanceEntity, Integer> {
}