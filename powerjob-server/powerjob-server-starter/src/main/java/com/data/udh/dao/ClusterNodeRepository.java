package com.data.udh.dao;

import com.data.udh.entity.ClusterNodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClusterNodeRepository extends JpaRepository<ClusterNodeEntity, Integer> {
    Integer countByIp(String ip);

    public List<ClusterNodeEntity> findByClusterId(Integer clusterId);
}