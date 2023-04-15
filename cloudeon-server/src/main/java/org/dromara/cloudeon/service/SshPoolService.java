package org.dromara.cloudeon.service;

import org.apache.sshd.client.session.ClientSession;
import org.dromara.cloudeon.utils.SshConnectionPool;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SshPoolService {
    private static final Map<String, SshConnectionPool> pools = new ConcurrentHashMap<>();

    public ClientSession openSession(String server, int port, String username, String password ) {
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
        }
        return session;
    }

    public void returnSession(ClientSession session,String host) {
        SshConnectionPool pool = pools.get(host);
        if (pool != null) {
            pool.returnObject(session);
        }

    }

}
