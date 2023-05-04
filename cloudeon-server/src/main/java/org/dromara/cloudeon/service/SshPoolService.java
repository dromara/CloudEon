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
package org.dromara.cloudeon.service;

import com.google.common.collect.Lists;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;
import org.dromara.cloudeon.dto.HostSshPoolMetrics;
import org.dromara.cloudeon.utils.SftpFilesystemPool;
import org.dromara.cloudeon.utils.SshConnectionPool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SshPoolService {
    private static final Map<String, SshConnectionPool> pools = new ConcurrentHashMap<>();
    private static final Map<String, SftpFilesystemPool> sftpPools = new ConcurrentHashMap<>();

    public ClientSession openSession(String server, int port, String username, String password) {
        SshConnectionPool pool = pools.get(server);
        if (pool == null) {
            pools.put(server, new SshConnectionPool(server, port, username, password));
            pool = pools.get(server);
        }
        ClientSession session = null;
        try {
            session = pool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return session;
    }

    public List<HostSshPoolMetrics> metrics() {
        List<HostSshPoolMetrics> hostSshPoolMetricsList = Lists.newArrayList();
        HostSshPoolMetrics hostSshPoolMetrics = new HostSshPoolMetrics();
        // 根据pool获取所有HostSshPoolMetrics列表
        for (Map.Entry<String, SshConnectionPool> entry : pools.entrySet()) {
            String host = entry.getKey();
            SshConnectionPool pool = entry.getValue();
            hostSshPoolMetrics.setHostname(host);
            hostSshPoolMetrics.setSshPoolMetrics(pool.poolMetrics());
            hostSshPoolMetricsList.add(hostSshPoolMetrics);
        }
        return hostSshPoolMetricsList;
    }

    public SftpFileSystem openSftpFileSystem(String server) {
        SftpFilesystemPool sftpPool = sftpPools.get(server);
        SftpFileSystem fileSystem = null;
        if (sftpPool == null) {
            // 获取该服务器地址的ssh池
            SshConnectionPool sshConnectionPool = pools.get(server);
            sftpPools.put(server, new SftpFilesystemPool(sshConnectionPool));
            sftpPool = sftpPools.get(server);
        }
        try {
            fileSystem = sftpPool.borrowObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return fileSystem;
    }

    public void returnSession(ClientSession session, String host) {
        SshConnectionPool pool = pools.get(host);
        if (pool != null) {
            pool.returnObject(session);
        }

    }

    public void returnSftp(SftpFileSystem fileSystem, String host) {
        SftpFilesystemPool pool = sftpPools.get(host);
        if (pool != null) {
            pool.returnObject(fileSystem);
        }

    }

}
