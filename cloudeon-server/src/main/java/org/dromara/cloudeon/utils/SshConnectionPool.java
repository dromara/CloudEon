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
package org.dromara.cloudeon.utils;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.sshd.client.session.ClientSession;
import org.dromara.cloudeon.dto.SshPoolMetrics;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class SshConnectionPool {
    private final GenericObjectPool<ClientSession> pool;

    public SshConnectionPool(String host, int port, String username, String password) {
        GenericObjectPoolConfig<ClientSession> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(5);
        config.setMaxIdle(3);
        config.setMinIdle(1);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        config.setTimeBetweenEvictionRuns(Duration.ofMinutes(30));
        config.setMinEvictableIdleTime(Duration.ofMinutes(5));

        pool = new GenericObjectPool<>(new SshConnectionFactory(host, port, username, password), config);
    }

    public SshPoolMetrics poolMetrics() {
        pool.getNumActive();
        pool.getNumIdle();
        pool.getNumWaiters();
        SshPoolMetrics sshPoolMetrics = new SshPoolMetrics(pool.getNumActive(), pool.getNumIdle(), pool.getNumWaiters());
        return sshPoolMetrics;
    }

    public ClientSession borrowObject() throws Exception {
        return pool.borrowObject();
    }

    public void returnObject(ClientSession session) {
        pool.returnObject(session);
    }

    public void close() {
        pool.close();
    }

    private static class SshConnectionFactory extends BasePooledObjectFactory<ClientSession> {
        private final String host;
        private final int port;
        private final String username;
        private final String password;

        public SshConnectionFactory(String host, int port, String username, String password) {
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
        }

        @Override
        public ClientSession create() throws Exception {
            ClientSession clientSession = SshUtils.openConnectionByPassword(host,port,username,password);
            return clientSession;
        }

        @Override
        public PooledObject<ClientSession> wrap(ClientSession session) {
            return new DefaultPooledObject<>(session);
        }

        @Override
        public void destroyObject(PooledObject<ClientSession> pooledObject) throws Exception {
            pooledObject.getObject().close();
        }

        @Override
        public boolean validateObject(PooledObject<ClientSession> pooledObject) {
            return pooledObject.getObject().isOpen();
        }
    }
}