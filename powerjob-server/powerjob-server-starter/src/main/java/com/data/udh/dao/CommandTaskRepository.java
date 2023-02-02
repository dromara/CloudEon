package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.CommandTaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandTaskRepository extends JpaRepository<CommandTaskEntity, Integer> {
}