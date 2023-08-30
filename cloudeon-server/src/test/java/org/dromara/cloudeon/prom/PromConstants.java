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

/**
 * @Title: prometheus常量信息
 */
public class PromConstants {
    /**
     * prometheus-查询SUCCESS
     */
    public static final String SUCCESS = "success";
 
 
    /**
     * prometheus-查询参数
     */
    public static final String QUERY = "query";
 

    public static final String CPU_USED = "(((count(count(node_cpu_seconds_total{instance=~\"k8s-node2:9101\"}) by (cpu))) - avg(sum by (mode)(rate(node_cpu_seconds_total{mode='idle',instance=~\"k8s-node2:9101\"}[1m])))) * 100) / count(count(node_cpu_seconds_total{instance=~\"k8s-node2:9101\"}) by (cpu))";
 
 
}