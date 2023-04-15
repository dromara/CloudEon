package org.dromara.cloudeon.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;

import java.util.concurrent.TimeUnit;
@Slf4j
public class SftpFilesystemPool {
    private final GenericObjectPool<SftpFileSystem> pool;

    public SftpFilesystemPool(ClientSession clientSession) {
        GenericObjectPoolConfig<SftpFileSystem> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(5);
        config.setMaxIdle(3);
        config.setMinIdle(1);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        config.setTimeBetweenEvictionRunsMillis(TimeUnit.MINUTES.toMillis(1));
        config.setMinEvictableIdleTimeMillis(TimeUnit.MINUTES.toMillis(5));

        pool = new GenericObjectPool<>(new SftpConnectionPool(clientSession), config);
    }

    public SftpFileSystem borrowObject() throws Exception {
        return pool.borrowObject();
    }

    public void returnObject(SftpFileSystem session) {
        pool.returnObject(session);
    }

    public void close() {
        pool.close();
    }

    private static class SftpConnectionPool extends BasePooledObjectFactory<SftpFileSystem> {
        private final ClientSession clientSession;

        public SftpConnectionPool(ClientSession clientSession) {
            this.clientSession = clientSession;
        }

        @Override
        public SftpFileSystem create() throws Exception {
            log.info("创建SftpFileSystem：{}",clientSession.getConnectAddress());
            SftpFileSystem SftpFileSystem = SftpClientFactory.instance().createSftpFileSystem(clientSession);
            return SftpFileSystem;
        }

        @Override
        public PooledObject<SftpFileSystem> wrap(SftpFileSystem session) {
            return new DefaultPooledObject<>(session);
        }

        @Override
        public void destroyObject(PooledObject<SftpFileSystem> pooledObject) throws Exception {
            pooledObject.getObject().close();
        }

        @Override
        public boolean validateObject(PooledObject<SftpFileSystem> pooledObject) {
            return pooledObject.getObject().isOpen();
        }
    }
}