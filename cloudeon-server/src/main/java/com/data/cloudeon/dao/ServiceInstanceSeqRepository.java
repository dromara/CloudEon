package com.data.cloudeon.dao;

import com.data.cloudeon.entity.ServiceInstanceSeqEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceInstanceSeqRepository extends JpaRepository<ServiceInstanceSeqEntity, Integer> {
    ServiceInstanceSeqEntity findByStackServiceId(Integer stackServiceId);

}