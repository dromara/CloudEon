package com.data.udh.dao;

import com.data.udh.entity.ClusterNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClusterNodeRepository extends JpaRepository<ClusterNodeEntity, Integer> {
}