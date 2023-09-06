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
package org.dromara.cloudeon.controller;

import cn.hutool.core.io.FileUtil;
import org.dromara.cloudeon.dao.CommandTaskRepository;
import org.dromara.cloudeon.dto.ResultDTO;
import org.dromara.cloudeon.dto.ServiceRoleLogPage;
import org.dromara.cloudeon.entity.CommandTaskEntity;
import org.dromara.cloudeon.service.LogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;

@RestController
@RequestMapping("/log")
public class LogController {

    @Resource
    private CommandTaskRepository commandTaskRepository;

    @Resource
    private LogService logService;

    @GetMapping("/task")
    public ResultDTO<String> commandTaskLog(Integer commandTaskId) {
        CommandTaskEntity commandTaskEntity = commandTaskRepository.findById(commandTaskId).get();
        String taskLogPath = commandTaskEntity.getTaskLogPath();
        String result = FileUtil.readUtf8String(new File(taskLogPath));
        return ResultDTO.success(result);
    }

    @GetMapping("/serviceRoleLog")
    public ResultDTO<ServiceRoleLogPage> serviceRoleLog(Integer clusterId,String roleName,String logLevel,Integer from,Integer pageSize) {
        if (pageSize==null){
            pageSize = 10;
        }
        if (from==null){
            from = 0;
        }
        ServiceRoleLogPage serviceLog = logService.getServiceLog(clusterId, roleName, logLevel, from, pageSize);
        return ResultDTO.success(serviceLog);
    }

}
