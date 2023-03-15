package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandRepository extends JpaRepository<CommandEntity, Integer> {
    List<CommandEntity> findByClusterIdOrderBySubmitTimeDesc(Integer clusterId);
}