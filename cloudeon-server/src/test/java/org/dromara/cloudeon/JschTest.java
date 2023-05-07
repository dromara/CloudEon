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
package org.dromara.cloudeon;

import cn.hutool.core.io.LineHandler;
import cn.hutool.core.io.file.Tailer;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.ssh.JschSessionPool;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.extra.ssh.Sftp;
import com.google.common.collect.Lists;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.session.ClientSession;
import org.dromara.cloudeon.utils.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.dromara.cloudeon.utils.Constant.DEFAULT_JSCH_TIMEOUT;
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JschTest {
    private JschSessionPool jschSessionPool = JschSessionPool.INSTANCE;

    @Test
    public void executeCommandUsingPool() throws Exception {
        for (int i = 0; i < 10; i++) {
            Session session = jschSessionPool.getSession("192.168.197.173", 22, "root", "123456");
            System.out.println(session);
            MyLineHandler consoleLineHandler = new MyLineHandler();
            MyLineHandler2 consoleLineHandler2 = new MyLineHandler2();
            String command1 = "for i in $(seq 1 10); do echo $i; sleep 1; done";
            String command2 = " sudo docker  run --net=host -v /opt/edp/dolphinscheduler1/conf:/opt/edp/dolphinscheduler1/conf -v /opt/edp/dolphinscheduler1/log:/opt/edp/dolphinscheduler1/log   -v /opt/edp/dolphinscheduler1/data:/opt/edp/dolphinscheduler1/data  registry.mufankong.top/udh/dolphinscheduler:3.0.5 sh -c \"  /opt/edp/dolphinscheduler1/conf/init-dolphinscheduler-db.sh \" ";
            JschUtils.execCallbackLine(session, Charset.defaultCharset(), 10000,command2 , null,consoleLineHandler,consoleLineHandler2 );

        }
    }

    @Test
    public void execError() throws IOException {
        RemoteSshTaskLineHandler remoteSshTaskLineHandler = new RemoteSshTaskLineHandler(log);
        RemoteSshTaskErrorLineHandler remoteSshTaskErrorLineHandler = new RemoteSshTaskErrorLineHandler(log);
        Session session = jschSessionPool.getSession("192.168.197.173", 22, "root", "123456");
        JschUtils.execCallbackLine(session, Charset.defaultCharset(), DEFAULT_JSCH_TIMEOUT,"ls /nonexistent-directory" ,null,remoteSshTaskLineHandler,remoteSshTaskErrorLineHandler );

    }

    @Test
    public void jschError() {
        String host = "192.168.197.175";
        String user = "root";
        String password = "123456";
        String command = "ls /nonexistent-directory";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            ByteArrayOutputStream errStream = new ByteArrayOutputStream();
            channel.setErrStream(errStream);

            channel.connect();

            InputStream in = channel.getInputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                System.out.println(new String(buffer, 0, len));
            }

            channel.disconnect();
            session.disconnect();

            String errOutput = new String(errStream.toByteArray());
            if (!errOutput.isEmpty()) {
                // 处理错误输出
                System.err.println("发生错误：" + errOutput);
            }
        } catch (JSchException | IOException e) {
            // 处理异常
            e.printStackTrace();
        }
    }
    @Test
    public void sftpTest() {
        Session session = jschSessionPool.getSession("192.168.197.173", 22, "root", "123456");
        Sftp sftp = new Sftp(session, Charset.defaultCharset(), 10000);
        // 单文件上传指定目录中
//        sftp.put("E:\\workspace\\CloudEon\\cloudeon-docs\\mkdocs.yml","/tmp");

        // 多个符合后缀的文件上传到指定目录
        sftp.put("E:\\workspace\\CloudEon\\cloudeon-docs\\docs\\安装部署\\*.md","/tmp");

        // 整个文件夹同步到服务器上
//        sftp.mkDirs("/tmp/cloudeon-docs");
//        sftp.syncUpload(new File("E:\\workspace\\CloudEon\\cloudeon-docs\\"),"/tmp/cloudeon-docs");
    }

    public static class MyLineHandler implements LineHandler {
        @Override
        public void handle(String line) {
            System.out.println(line);
        }
    }

    public static class MyLineHandler2 implements LineHandler {
        @Override
        public void handle(String line) {
            System.err.println(line);
        }
    }
}
