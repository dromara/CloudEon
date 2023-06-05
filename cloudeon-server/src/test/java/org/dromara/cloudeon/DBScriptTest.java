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

import cn.hutool.db.ds.simple.SimpleDataSource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.utils.ScriptRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DBScriptTest {
    /**
     * run init sql to init db schema
     */
    @Test
    public void runInitSql() {
        String username = "root";
        String password = "eWJmP7yvpccHCtmVb61Gxl2XLzIrRgmT";
        String url = "jdbc:mysql://192.168.197.175:3306/dinky?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
        DataSource ds = new SimpleDataSource(url, username, password);

        String sqlFile = "E:\\workspace\\CloudEon\\cloudeon-stack\\EDP-1.0.0\\dinky\\sql\\dinky.sql";
        try (Connection conn = ds.getConnection()) {
            FileInputStream sqlFileStream = new FileInputStream(sqlFile);
            ScriptRunner initScriptRunner = new ScriptRunner(conn, true, true,log);
            Reader initSqlReader = new InputStreamReader(sqlFileStream);
            initScriptRunner.runScript(initSqlReader);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
