package com.data.udh.dao;

import com.data.udh.entity.StackInfoEntity;
import com.data.udh.entity.StackServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StackServiceRepository extends JpaRepository<StackServiceEntity, Integer> {
    public StackServiceEntity findByStackIdAndName(Integer stackId, String name);

    List<StackServiceEntity> findByStackId(Integer stackId);
}