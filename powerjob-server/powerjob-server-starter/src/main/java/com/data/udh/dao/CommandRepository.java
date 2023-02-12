package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.StackServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandRepository extends JpaRepository<CommandEntity, Integer> {
    List<CommandEntity> findByClusterId(Integer clusterId);
}