package com.data.udh.dao;

import com.data.udh.entity.CommandTaskEntity;
import com.data.udh.enums.CommandState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandTaskRepository extends JpaRepository<CommandTaskEntity, Integer> {
    List<CommandTaskEntity> findByCommandId(Integer commandId);

    public Integer countByCommandStateAndCommandId(CommandState commandState, Integer commandId);
    public Integer countByCommandId( Integer commandId);

}