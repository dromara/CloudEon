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
package org.dromara.cloudeon.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.dto.ResultDTO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResultDTO<Void> globalException(HttpServletResponse response, Exception ex) {
        log.info("GlobalExceptionHandler...");
        log.info("错误代码：" + response.getStatus());
        log.error(ex.getMessage(), ex);
        if (ex.getCause() != null) {
            return ResultDTO.failed(ex.getCause().getMessage());
        }
        return ResultDTO.failed(ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler( cn.dev33.satoken.exception.NotLoginException.class)
    public ResultDTO<Void> checkTokenException(HttpServletResponse response, Exception ex) {
        log.info("checkTokenException...");
        log.info("错误代码：" + response.getStatus());
        ex.printStackTrace();
        ResultDTO<Void> resultDTO = ResultDTO.failed(ex.getMessage());
        return resultDTO;
    }
}