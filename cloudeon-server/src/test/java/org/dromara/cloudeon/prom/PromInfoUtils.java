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
package org.dromara.cloudeon.prom;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import java.util.List;
import java.util.Objects;
 
/**
 * @Title: prometheus工具类
 */
public class PromInfoUtils {
    private static final Logger log = LoggerFactory.getLogger(PromInfoUtils.class);
 

    public static List<PromResultInfo> getCpuInfo(String promURL, String promQL) {
 
        JSONObject param = new JSONObject();
        param.put(PromConstants.QUERY, promQL);
        String http = null;
        try {
            http = HttpUtil.get(promURL, param);
        } catch (Exception e) {
            log.error("异常，请求地址：{}，请求QL：{}，异常信息：{}", promURL, promQL, e);
        }
        PromResponceInfo responceInfo = JSON.parseObject(http, PromResponceInfo.class);
        log.info("，请求地址：{}，请求QL：{}，返回信息：{}", promURL, promQL, responceInfo);
        if (Objects.isNull(responceInfo)) {
            return null;
        }
        String status = responceInfo.getStatus();
        if (StringUtils.isBlank(status)
                || !PromConstants.SUCCESS.equals(status)
        ) {
            return null;
        }
        List<PromResultInfo> result = responceInfo.getData().getResult();
        return result;
    }
 
 
    public static void main(String[] args) {
     List<PromResultInfo> cpuInfo = getCpuInfo("http://192.168.197.149:9090/api/v1/query", PromConstants.CPU_USED);
 
     System.out.println(cpuInfo);
 
    }
 
}