package com.data.udh.dao;

import com.data.udh.entity.ClusterNodeEntity;
import com.data.udh.entity.ServiceInstanceSeqEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceInstanceSeqRepository extends JpaRepository<ServiceInstanceSeqEntity, Integer> {
    ServiceInstanceSeqEntity findByStackServiceId(Integer stackServiceId);

}