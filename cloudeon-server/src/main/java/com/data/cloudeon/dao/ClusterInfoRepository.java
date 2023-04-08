package com.data.cloudeon.dao;

import com.data.cloudeon.entity.ClusterInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClusterInfoRepository extends JpaRepository<ClusterInfoEntity, Integer> {
}