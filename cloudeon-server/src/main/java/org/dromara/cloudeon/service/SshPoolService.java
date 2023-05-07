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

import cn.hutool.extra.ssh.JschSessionPool;
import com.google.common.collect.Lists;
import com.jcraft.jsch.Session;
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
    private JschSessionPool jschSessionPool = JschSessionPool.INSTANCE;

    public Session openSession(String server, int port, String username, String password) {
        Session session = jschSessionPool.getSession(server, port, username, password);
        return session;
    }







}
