package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.CommandTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandTaskRepository extends JpaRepository<CommandTaskEntity, Integer> {
    List<CommandTaskEntity> findByCommandId(Integer commandId);
}