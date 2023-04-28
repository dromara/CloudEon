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

import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.handler.StringHandler;
import cn.hutool.db.sql.SqlExecutor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SqlTest {

    @Test
    public void test() {
        String url = "jdbc:mysql://10.81.17.8:33066/hive_db_ce?createDatabaseIfNotExist=true&amp;useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false";
        String substring = url.substring(0, url.indexOf("?"));
        DataSource ds = new SimpleDataSource(substring, "root", "bigdata");
            try (Connection conn = ds.getConnection();) {
                String sql = " select SCHEMA_VERSION from VERSION";
                log.info("执行sql：{}", sql);
                String query = SqlExecutor.query(conn, sql, new StringHandler());
                System.out.println(query);

            } catch (SQLException e) {
                if (e.getMessage().contains("doesn't exist")){
                    System.out.println("hhh");
                }
                e.printStackTrace();
            }
    }
}
