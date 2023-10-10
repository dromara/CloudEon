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

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Session;
import org.dromara.cloudeon.dao.ClusterNodeRepository;
import org.dromara.cloudeon.entity.ClusterNodeEntity;
import org.dromara.cloudeon.service.SshPoolService;
import org.dromara.cloudeon.utils.SshUtils;
import lombok.NoArgsConstructor;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;

@NoArgsConstructor
public class DeleteServiceDataDirTask extends BaseCloudeonTask {

    @Override
    public void internalExecute() {
        ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);
        String serviceInstanceName = taskParam.getServiceInstanceName();
        ClusterNodeEntity nodeEntity = clusterNodeRepository.findByHostname(taskParam.getHostName());
        SshPoolService sshPoolService = SpringUtil.getBean(SshPoolService.class);

        Session clientSession = sshPoolService.openSession(nodeEntity);
        String remoteDataDirPath = "/opt/edp/" + serviceInstanceName;
        String command = "rm -rf " + remoteDataDirPath;
        log.info("执行远程命令：" + command);
        JschUtil.exec(clientSession, command, CharsetUtil.CHARSET_UTF_8);

    }
}
