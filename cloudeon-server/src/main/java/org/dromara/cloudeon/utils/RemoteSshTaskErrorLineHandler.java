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

import cn.hutool.core.io.LineHandler;
import com.google.common.collect.Lists;
import org.slf4j.Logger;

import java.util.List;

public class RemoteSshTaskErrorLineHandler implements LineHandler {

    private Logger logger ;
    /**
     * todo 如果执行命令过程中真的失败怎么办？这里忽略后，页面会显示这个任务是成功的，例如namenode zk format时zk挂了
     */
    private List<String> passErrorMsg = Lists.newArrayList(
            "Class path contains multiple SLF4J bindings.", // hive metadata init
            "log4j:WARN No appenders could be found for logger", // flink init
            "Starting DFSZKFailoverController", // namenode init
            "WARNING: log4j.properties is not found. HADOOP_CONF_DIR may be incomplete." // spark init
    );
    public RemoteSshTaskErrorLineHandler(Logger logger) {
        this.logger = logger;
    }
    @Override
    public void handle(String line) {
        logger.error(line);
        boolean isPass = false;
        for (String s : passErrorMsg) {
            if (line.contains(s)) {
                isPass = true;
                break;
            }
        }
        if (!isPass) {
            throw new RuntimeException("ssh 发生错误");
        }
    }
}
