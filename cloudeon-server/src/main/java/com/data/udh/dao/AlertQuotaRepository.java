package com.data.udh.dao;

import com.data.udh.entity.ClusterAlertQuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertQuotaRepository extends JpaRepository<ClusterAlertQuotaEntity, Integer> {
}