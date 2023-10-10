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

import org.dromara.cloudeon.entity.ServiceRoleInstanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServiceRoleInstanceRepository extends JpaRepository<ServiceRoleInstanceEntity, Integer> {
    List<ServiceRoleInstanceEntity> findByServiceInstanceIdAndStackServiceRoleId(Integer serviceInstanceId, Integer stackServiceRoleId);
    List<ServiceRoleInstanceEntity> findByServiceInstanceIdAndServiceRoleName(Integer serviceInstanceId, String serviceRoleName);
    List<ServiceRoleInstanceEntity> findByServiceInstanceId(Integer serviceInstanceId);

    ServiceRoleInstanceEntity findByServiceInstanceIdAndNodeIdAndServiceRoleName(Integer serviceInstanceId, Integer nodeId, String serviceRoleName);

    int deleteByServiceInstanceId(Integer serviceInstanceId);

    int countByServiceInstanceIdAndServiceRoleName(Integer serviceInstanceId, String serviceRoleName);

    @Query(value = "select a  from ServiceRoleInstanceEntity a join ClusterNodeEntity b on a.nodeId = b.id where a.clusterId =:clusterId and  a.serviceRoleName = :roleName and b.hostname =:hostname")
    ServiceRoleInstanceEntity findByServiceRoleNameAndClusterIdAndHostname(@Param("clusterId") Integer clusterId, @Param("roleName") String roleName, @Param("hostname")  String hostname);

    @Query(value = "select b.label  from ServiceRoleInstanceEntity a join StackServiceRoleEntity b on a.stackServiceRoleId = b.id where  a.id =:roleInstanceId")
    String getRoleInstanceLabel(@Param("roleInstanceId") Integer roleInstanceId);

    List<ServiceRoleInstanceEntity> findByClusterId(Integer clusterId);
}