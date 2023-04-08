package com.data.cloudeon.dao;

import com.data.cloudeon.entity.StackServiceConfEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StackServiceConfRepository extends JpaRepository<StackServiceConfEntity, Integer> {
    public StackServiceConfEntity findByStackIdAndNameAndServiceId(Integer stackId, String name, Integer serviceId);
    public StackServiceConfEntity findByNameAndServiceId(String name, Integer serviceId);

    List<StackServiceConfEntity> findByServiceIdAndConfigurableInWizard(Integer serviceId, Boolean configurableInWizard);
    List<StackServiceConfEntity> findByServiceId(Integer serviceId);
}