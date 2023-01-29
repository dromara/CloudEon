package com.data.udh.dao;

import com.data.udh.entity.StackServiceConfEntity;
import com.data.udh.entity.StackServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StackServiceConfRepository extends JpaRepository<StackServiceConfEntity, Integer> {
    public StackServiceConfEntity findByStackIdAndNameAndServiceId(Integer stackId, String name, Integer serviceId);

    List<StackServiceConfEntity> findByServiceIdAndConfigurableInWizard(Integer serviceId, Boolean configurableInWizard);
    List<StackServiceConfEntity> findByServiceId(Integer serviceId);
}