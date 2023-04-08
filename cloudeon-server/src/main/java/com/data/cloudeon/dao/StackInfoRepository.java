package com.data.cloudeon.dao;

import com.data.cloudeon.entity.StackInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StackInfoRepository extends JpaRepository<StackInfoEntity, Integer> {

    public StackInfoEntity findByStackCode(String stackCode);
}