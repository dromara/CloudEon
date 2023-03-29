package com.data.udh.dao;

import com.data.udh.entity.AlertMessageEntity;
import com.data.udh.entity.ClusterAlertQuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlertMessageRepository extends JpaRepository<AlertMessageEntity, Integer> {
    AlertMessageEntity findByFireTimeAndAlertName(String fireTime, String alertName);

    @Query(value = "select a  from AlertMessageEntity a  where  a.resolved =:resolved")
    List<AlertMessageEntity> findByIsResolve(@Param("resolved")Boolean resolved);

    List<AlertMessageEntity> findByServiceInstanceIdAndResolved(Integer serviceInstanceId, boolean resolved);
    List<AlertMessageEntity> findByServiceRoleInstanceIdAndResolved(Integer roleInstanceId, boolean resolved);
}