/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dromara.cloudeon.dao;

import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceInstanceRepository extends JpaRepository<ServiceInstanceEntity, Integer> {

    Integer countByClusterId(Integer clusterId);

    @Query(value = "select a.id  from ServiceInstanceEntity a join StackServiceEntity b on a.stackServiceId = b.id where a.clusterId =:clusterId and  b.name = :stackServiceName")
    Integer findByClusterIdAndStackServiceName(@Param("clusterId") Integer clusterId, @Param("stackServiceName") String stackServiceName);

    @Query(value = "select a  from ServiceInstanceEntity a join StackServiceEntity b on a.stackServiceId = b.id where a.clusterId =:clusterId and  b.name = :stackServiceName")
    ServiceInstanceEntity findEntityByClusterIdAndStackServiceName(@Param("clusterId") Integer clusterId, @Param("stackServiceName") String stackServiceName);

    ServiceInstanceEntity findByClusterIdAndStackServiceId(Integer clusterId, Integer stackServiceId);

    List<ServiceInstanceEntity> findByClusterId(Integer clusterId);

    ServiceInstanceEntity findByServiceNameAndClusterId(String serviceName,Integer clusterId);

    List<ServiceInstanceEntity> findByClusterIdAndDependenceServiceInstanceIdsNotNull(Integer clusterId);


}