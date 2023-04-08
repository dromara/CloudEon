package com.data.cloudeon.dao;

import com.data.cloudeon.entity.ClusterAlertQuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertQuotaRepository extends JpaRepository<ClusterAlertQuotaEntity, Integer> {
}