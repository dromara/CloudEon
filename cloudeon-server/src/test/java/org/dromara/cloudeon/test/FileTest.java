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

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;

@Slf4j
public class FileTest {
    @Test
    public void mkdir() {
        // 只删除conf目录
        FileUtil.del("/Volumes/Samsung_T5/opensource/e-mapreduce/work" + File.separator + "zookeeper1" + File.separator + "conf");
        // 只创建到全路径的父目录
//        FileUtil.mkParentDirs("/Volumes/Samsung_T5/opensource/e-mapreduce/work"+File.separator+"zookeeper1"+File.separator+"conf");

        // 创建全目录
//        FileUtil.mkdir("/Volumes/Samsung_T5/opensource/e-mapreduce/work"+File.separator+"zookeeper1"+File.separator+"conf");

    }





}
