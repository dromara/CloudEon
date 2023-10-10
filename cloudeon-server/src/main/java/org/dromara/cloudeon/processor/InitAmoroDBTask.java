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
package org.dromara.cloudeon.processor;

import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.extra.spring.SpringUtil;
import org.dromara.cloudeon.config.CloudeonConfigProp;
import org.dromara.cloudeon.dao.ServiceInstanceConfigRepository;
import org.dromara.cloudeon.dao.ServiceInstanceRepository;
import org.dromara.cloudeon.dao.StackServiceRepository;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.StackServiceEntity;
import org.dromara.cloudeon.utils.Constant;
import org.dromara.cloudeon.utils.ScriptRunner;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;

public class InitAmoroDBTask  extends BaseCloudeonTask  {
    @Override
    public void internalExecute() {
        CloudeonConfigProp cloudeonConfigProp = SpringUtil.getBean(CloudeonConfigProp.class);
        String stackLoadPath = cloudeonConfigProp.getStackLoadPath();
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        StackServiceRepository stackServiceRepository = SpringUtil.getBean(StackServiceRepository.class);
        ServiceInstanceConfigRepository configRepository = SpringUtil.getBean(ServiceInstanceConfigRepository.class);
        TaskParam taskParam = getTaskParam();
        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();

        // 获取amoro 数据库信息
        String username = configRepository.findByServiceInstanceIdAndName(serviceInstanceId, "ams.mysql.ConnectionUserName").getValue();
        String password = configRepository.findByServiceInstanceIdAndName(serviceInstanceId, "ams.mysql.ConnectionPassword").getValue();
        String url = configRepository.findByServiceInstanceIdAndName(serviceInstanceId, "ams.mysql.ConnectionURL").getValue();
        DataSource ds = new SimpleDataSource(url, username, password);

        String sqlPath = stackLoadPath+ File.separator+ stackServiceEntity.getStackCode() + File.separator + stackServiceEntity.getName().toLowerCase() + File.separator + Constant.StackPackageRenderDir + File.separator+ "mysql"+ File.separator+ "ams-mysql-init.sql";
        try (Connection conn = ds.getConnection()) {
            FileInputStream sqlFileStream = new FileInputStream(sqlPath);
            ScriptRunner initScriptRunner = new ScriptRunner(conn, true, true,log);
            Reader initSqlReader = new InputStreamReader(sqlFileStream);
            initScriptRunner.runScript(initSqlReader);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
