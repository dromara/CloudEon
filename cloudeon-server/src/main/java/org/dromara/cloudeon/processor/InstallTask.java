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
import cn.hutool.extra.ssh.Sftp;
import com.jcraft.jsch.Session;
import org.dromara.cloudeon.dao.ClusterNodeRepository;
import org.dromara.cloudeon.dao.ServiceInstanceRepository;
import org.dromara.cloudeon.entity.ClusterNodeEntity;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.service.SshPoolService;
import org.dromara.cloudeon.utils.SshUtils;
import lombok.NoArgsConstructor;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

@NoArgsConstructor
public class InstallTask extends BaseCloudeonTask {


    @Override
    public void internalExecute() {
        // 查询服务实例需要创建的目录
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);
        SshPoolService sshPoolService = SpringUtil.getBean(SshPoolService.class);

        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(taskParam.getServiceInstanceId()).get();
        String persistencePaths = serviceInstanceEntity.getPersistencePaths();
        String[] paths = persistencePaths.split(",");

        ClusterNodeEntity nodeEntity = clusterNodeRepository.findByHostname(taskParam.getHostName());
        Session clientSession = sshPoolService.openSession(nodeEntity.getIp(), nodeEntity.getSshPort(), nodeEntity.getSshUser(), nodeEntity.getSshPassword());
        Sftp sftp = JschUtil.createSftp(clientSession);
        // 查询节点
        Arrays.stream(paths).forEach(new Consumer<String>() {
            @Override
            public void accept(String path) {
                log.info("节点：" + taskParam.getHostName() + " 上创建目录：" + path);
                // ssh执行创建目录
                sftp.mkDirs(path);
                String command = "chmod 777 -R " + path;
                log.info("执行远程命令：" + command);
                JschUtil.exec(clientSession, command, CharsetUtil.CHARSET_UTF_8);
                log.info("成功在节点：" + taskParam.getHostName() + " 上创建目录：" + path);

            }
        });
        JschUtil.close(sftp.getClient());


        // todo 服务日志采集的相关执行

    }
}
