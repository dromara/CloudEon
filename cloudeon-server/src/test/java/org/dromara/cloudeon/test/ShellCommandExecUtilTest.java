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
package org.dromara.cloudeon.test;

import org.dromara.cloudeon.utils.ShellCommandExecUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;

@Slf4j
public class ShellCommandExecUtilTest {

    @Test
    public void testRuntimeShell() throws IOException {
        int errCode;
        errCode = ShellCommandExecUtil.builder().log(log).build().runShellWithRuntime("/Users/huzekang/cdh5.16/zookeeper-3.4.5-cdh5.16.2/",
                new String[]{"bin/zkServer.sh", "start"}, Charset.forName("utf-8"));
        System.out.println(errCode);
    }


    @Test
    public void testProcessShell1() throws IOException {
        int errCode;
        errCode = ShellCommandExecUtil.builder().log(log).build().builder().build().runShellCommandSync("/Users/huzekang/cdh5.16/hadoop-2.6.0-cdh5.16.2",
                new String[]{"bin/hadoop", "fs", "-ls", "/tmp"}, Charset.forName("utf-8"));

//        String logPath = "/tmp/cmd.log";
        errCode = ShellCommandExecUtil.builder().log(log).build().runShellCommandSync("/Users/huzekang/cdh5.16/zookeeper-3.4.5-cdh5.16.2/",
                new String[]{"bin/zkServer.sh", "start"}, Charset.forName("utf-8"), null);
    }


}