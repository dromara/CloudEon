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

import org.dromara.cloudeon.utils.ShellUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

@Slf4j
public class ShellTest {

    @Test
    public void cpu() {

        System.out.println(ShellUtils.builder().logger(log).build().getCpuArchitecture());
    }

    @Test
    public void exec() {

//        System.out.println(ShellUtils.builder().logger(log).build().exceShell("echo mntr |  nc  localhost 2181"));
        System.out.println("=====================");
        System.out.println(ShellUtils.builder().build().execWithStatus("/Users/huzekang/cdh5.16/zookeeper-3.4.5-cdh5.16.2/", Lists.newArrayList("sh", "1.sh"), 100000));
    }
}
