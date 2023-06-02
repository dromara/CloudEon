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
package org.dromara.cloudeon.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.jcraft.jsch.Session;
import org.dromara.cloudeon.dao.ClusterNodeRepository;
import org.dromara.cloudeon.dao.ServiceInstanceRepository;
import org.dromara.cloudeon.dao.ServiceRoleInstanceRepository;
import org.dromara.cloudeon.dao.StackServiceRepository;
import org.dromara.cloudeon.entity.ClusterNodeEntity;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.ServiceRoleInstanceEntity;
import org.dromara.cloudeon.entity.StackServiceEntity;
import org.dromara.cloudeon.service.SshPoolService;
import org.dromara.cloudeon.utils.JschUtils;
import org.dromara.cloudeon.utils.SshUtils;
import lombok.NoArgsConstructor;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static org.dromara.cloudeon.utils.Constant.DEFAULT_JSCH_TIMEOUT;

@NoArgsConstructor
public class InitYARNDirOnHDFSTask extends BaseCloudeonTask {


    @Override
    public void internalExecute() {
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        StackServiceRepository stackServiceRepository = SpringUtil.getBean(StackServiceRepository.class);
        ServiceRoleInstanceRepository roleInstanceRepository = SpringUtil.getBean(ServiceRoleInstanceRepository.class);
        ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);
        SshPoolService sshPoolService = SpringUtil.getBean(SshPoolService.class);

        TaskParam taskParam = getTaskParam();
        Integer serviceInstanceId = taskParam.getServiceInstanceId();

        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();
        String serviceName = serviceInstanceEntity.getServiceName();
        // todo 能捕获到执行日志吗？
        String cmd = String.format("sudo docker  run --net=host -v /opt/edp/%s/conf:/opt/edp/%s/conf  %s sh -c \"  /opt/edp/%s/conf/init-yarn-hdfs-dir.sh \"   ",
                serviceName,serviceName,stackServiceEntity.getDockerImage(),serviceName);

        // 选择metastore所在节点执行
        List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceIdAndServiceRoleName(serviceInstanceId,"YARN_RESOURCEMANAGER");
        ServiceRoleInstanceEntity firstNamenode = roleInstanceEntities.get(0);
        Integer nodeId = firstNamenode.getNodeId();
        ClusterNodeEntity nodeEntity = clusterNodeRepository.findById(nodeId).get();
        String ip = nodeEntity.getIp();
        log.info("在节点"+ip+"上执行命令:" + cmd);
        Session clientSession = sshPoolService.openSession(nodeEntity);
        try {
            JschUtils.execCallbackLine(clientSession, Charset.defaultCharset(), DEFAULT_JSCH_TIMEOUT,cmd ,null,remoteSshTaskLineHandler,remoteSshTaskErrorLineHandler );

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
}
