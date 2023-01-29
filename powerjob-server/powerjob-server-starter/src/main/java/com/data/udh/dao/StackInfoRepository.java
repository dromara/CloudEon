package com.data.udh.dao;

import com.data.udh.entity.ClusterInfoEntity;
import com.data.udh.entity.StackInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StackInfoRepository extends JpaRepository<StackInfoEntity, Integer> {

    public StackInfoEntity findByStackCode(String stackCode);
}