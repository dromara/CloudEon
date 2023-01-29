package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.ServiceInstanceConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceInstanceConfigRepository extends JpaRepository<ServiceInstanceConfigEntity, Integer> {
}