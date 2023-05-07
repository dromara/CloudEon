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

import cn.hutool.core.io.file.Tailer;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.ssh.JschSessionPool;
import cn.hutool.extra.ssh.JschUtil;
import com.google.common.collect.Lists;
import com.jcraft.jsch.Session;
import org.apache.sshd.client.session.ClientSession;
import org.dromara.cloudeon.utils.JschUtils;
import org.dromara.cloudeon.utils.SshConnectionPool;
import org.dromara.cloudeon.utils.SshUtils;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JschTest {
    private JschSessionPool jschSessionPool = JschSessionPool.INSTANCE;

    @Test
    public void executeCommandUsingPool() throws Exception {
        for (int i = 0; i < 10; i++) {
            Session session = jschSessionPool.getSession("192.168.197.173", 22, "root", "123456");
            System.out.println(session);
            Tailer.ConsoleLineHandler consoleLineHandler = new Tailer.ConsoleLineHandler();
            Tailer.ConsoleLineHandler consoleLineHandler2 = new Tailer.ConsoleLineHandler();
            String command1 = "for i in $(seq 1 10); do echo $i; sleep 1; done";
            String command2 = " sudo docker  run --net=host -v /opt/edp/dolphinscheduler1/conf:/opt/edp/dolphinscheduler1/conf -v /opt/edp/dolphinscheduler1/log:/opt/edp/dolphinscheduler1/log   -v /opt/edp/dolphinscheduler1/data:/opt/edp/dolphinscheduler1/data  registry.mufankong.top/udh/dolphinscheduler:3.0.5 sh -c \"  /opt/edp/dolphinscheduler1/conf/init-dolphinscheduler-db.sh \" ";
            JschUtils.execCallbackLine(session, Charset.defaultCharset(), 10000,command1 , null,consoleLineHandler,consoleLineHandler2 );

        }
    }


}
