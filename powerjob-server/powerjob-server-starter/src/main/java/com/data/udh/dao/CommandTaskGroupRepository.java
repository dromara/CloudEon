package com.data.udh.dao;

import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.CommandTaskGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandTaskGroupRepository extends JpaRepository<CommandTaskGroupEntity, Integer> {
}