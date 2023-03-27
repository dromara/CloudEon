package com.data.udh.dao;

import com.data.udh.entity.AlertMessageEntity;
import com.data.udh.entity.ClusterAlertQuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertMessageRepository extends JpaRepository<AlertMessageEntity, Integer> {
    AlertMessageEntity findByFireTimeAndAlertName(String fireTime, String alertName);
}