package com.data.udh.dao;

import com.data.udh.entity.ServiceInstanceSeqEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceInstanceSeqRepository extends JpaRepository<ServiceInstanceSeqEntity, Integer> {
    ServiceInstanceSeqEntity findByStackServiceId(Integer stackServiceId);

}