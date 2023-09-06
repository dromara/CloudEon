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
package org.dromara.cloudeon.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import org.apache.http.HttpHost;
import org.dromara.cloudeon.dao.*;
import org.dromara.cloudeon.dto.ServiceRoleLog;
import org.dromara.cloudeon.dto.ServiceRoleLogPage;
import org.dromara.cloudeon.dto.WsSessionBean;
import org.dromara.cloudeon.entity.ClusterNodeEntity;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.ServiceRoleInstanceEntity;
import org.dromara.cloudeon.entity.StackServiceRoleEntity;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.utils.Constant;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LogService {


    @Resource
    private ServiceRoleInstanceRepository roleInstanceRepository;

    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;
    @Resource
    private StackServiceRoleRepository stackServiceRoleRepository;

    @Resource
    private ClusterNodeRepository clusterNodeRepository;

    @Resource
    private ServiceInstanceConfigRepository serviceInstanceConfigRepository;

    /**
     * 主要逻辑
     * 1. 准备要执行的Shell命令：tail -1f 日志文件的绝对路径，例如：tail -1f /data/blog.hackyle.com/blog-business-logs/blog-business.log
     * 2. 获取sshSession，创建一个执行Shell命令的Channel
     * 3. 从Channel中读取流，包装为字符流，一次读取一行日志数据
     * 4. 获取WebSocket Session，只要它没有被关闭，就将日志数据通过该Session推送出去
     *
     * @param wsSessionBean 前端Client与后端WebSocketServer建立的连接实例
     */
    public void sendLog2BrowserClient(WsSessionBean wsSessionBean, Integer roleId) throws Exception {
        ServiceRoleInstanceEntity roleInstanceEntity = roleInstanceRepository.findById(roleId).get();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(roleInstanceEntity.getServiceInstanceId()).get();
        Integer nodeId = roleInstanceEntity.getNodeId();
        ClusterNodeEntity clusterNodeEntity = clusterNodeRepository.findById(nodeId).get();
        String hostname = clusterNodeEntity.getHostname();
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findById(roleInstanceEntity.getStackServiceRoleId()).get();
        String logFile = stackServiceRoleEntity.getLogFile();

        // 用模板生成获取日志文件名
        String logFileName = genRoleInstanceLogFileName(logFile, hostname);

        if (StrUtil.isNotBlank(logFileName)) {
            WebSocketSession wsSession = wsSessionBean.getWebSocketSession();
            Session sshSession = wsSessionBean.getSshSession();
            String host = sshSession.getHost();

            //String command = "ssh tpbbsc01 \"tail -" +count+ "f " +logPath+ "\""; //二级SSH跳板机在这里修改
            String command = String.format("tail -20f  /opt/edp/%s/log/%s", serviceInstanceEntity.getServiceName(), logFileName);
            log.info("查看服务器" + host + "上的角色实例日志，command: " + command);

            //创建一个执行Shell命令的Channel
            ChannelExec channelExec = (ChannelExec) sshSession.openChannel("exec");
            channelExec.setCommand(command);
            channelExec.connect();
            InputStream inputStream = channelExec.getInputStream();

            //包装为字符流，方便每次读取一行
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String buf = "";
            while ((buf = reader.readLine()) != null && wsSession.isOpen()) {
                //往WebSocket中推送数据
                wsSession.sendMessage(new TextMessage(buf));
            }
            log.info("退出监听服务器日志: sessionID {}",wsSessionBean.getWsSessionId());
        }

    }

    private String genRoleInstanceLogFileName(String logFileTemplate, String hostname) {
        // 渲染
        Configuration cfg = new Configuration();
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        String template = "webUITemplate";
        stringLoader.putTemplate(template, logFileTemplate);
        cfg.setTemplateLoader(stringLoader);
        try (Writer out = new StringWriter(2048);) {
            Template temp = cfg.getTemplate(template, "utf-8");
            temp.process(Dict.create().set("localhostname", hostname), out);
            return out.toString();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ServiceRoleLogPage getServiceLog(Integer clusterId,String roleName,String logLevel,int from,int pageSize) {
        ServiceRoleLogPage result = ServiceRoleLogPage.builder().build();
        // 查询es服务实例
        Integer serviceInstanceId = serviceInstanceRepository.findByServiceNameAndClusterId("ELASTICSEARCH",clusterId).getId();
        List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceIdAndServiceRoleName(serviceInstanceId, "ELASTICSEARCH_NODE");
        ServiceRoleInstanceEntity serviceRoleInstanceEntity = roleInstanceEntities.get(0);
        String ip = clusterNodeRepository.findById(serviceRoleInstanceEntity.getNodeId()).get().getIp();
        String value = serviceInstanceConfigRepository.findByServiceInstanceIdAndName(serviceInstanceId, "elasticsearch.http.listeners.port").getValue();


        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                new HttpHost(ip, Integer.parseInt(value), "http") // Elasticsearch主机和端口
            )
        );

        // 构建查询条件
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
            .must(QueryBuilders.termQuery("roleName", roleName))
            .must(QueryBuilders.termQuery("logLevel", logLevel));

        SearchRequest request = new SearchRequest("filebeat-7.16.3");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
            .query(boolQuery)
            .sort(SortBuilders.fieldSort("bizTime").order(SortOrder.DESC))
            .from(from)
            .size(pageSize);

        request.source(sourceBuilder);

        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
            long totalHits = response.getHits().getTotalHits().value; // 获取总匹配的文档数量
            long totalPages = (totalHits + pageSize - 1) / pageSize; // 计算总页数
            result.setTotalPage(totalPages);

            List<ServiceRoleLog> serviceRoleLogs = Arrays.stream(response.getHits().getHits()).map(new Function<SearchHit, ServiceRoleLog>() {
                @Override
                public ServiceRoleLog apply(SearchHit documentFields) {
                    String message = (String) documentFields.getSourceAsMap().get("message");
                    String hostip = (String) documentFields.getSourceAsMap().get("hostip");
                    String bizTime = (String) documentFields.getSourceAsMap().get("bizTime");
                    return ServiceRoleLog.builder().message(message).hostip(hostip).bizTime(bizTime).build();
                }
            }).collect(Collectors.toList());
            result.setLogs(serviceRoleLogs);
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
