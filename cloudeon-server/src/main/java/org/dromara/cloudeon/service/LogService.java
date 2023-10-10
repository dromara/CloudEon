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

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.ContainerResource;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import io.fabric8.kubernetes.client.dsl.PodResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.dromara.cloudeon.dao.*;
import org.dromara.cloudeon.dto.ServiceRoleLog;
import org.dromara.cloudeon.dto.ServiceRoleLogPage;
import org.dromara.cloudeon.dto.WsSessionBean;
import org.dromara.cloudeon.entity.ClusterNodeEntity;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.ServiceRoleInstanceEntity;
import org.dromara.cloudeon.entity.StackServiceRoleEntity;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
     * 1. 根据roleServiceFullName寻找pod
     * 2. 如果pod内有多个容器，则优先使用名称为server的容器，否则使用VolumeMount数量最多的容器
     * 3. 读取容器日志
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
        KubeService kubeService = SpringUtil.getBean(KubeService.class);

        WebSocketSession wsSession = wsSessionBean.getWebSocketSession();

        String roleServiceFullName = stackServiceRoleEntity.getRoleFullName() + "-" + serviceInstanceEntity.getServiceName().toLowerCase();

        KubernetesClient client = kubeService.getKubeClient(serviceInstanceEntity.getClusterId());
        Optional<Pod> podOptional = client.pods().withLabel("app", roleServiceFullName).list().getItems().stream().filter(
                pod -> {
                    if (pod.getSpec() == null) {
                        return false;
                    }
                    return hostname.equals(pod.getSpec().getNodeName());
                }
        ).findFirst();
        if (!podOptional.isPresent()) {
            wsSession.sendMessage(new TextMessage(StrUtil.format("未找到Pod app标签值为{}且所在节点名为{}的Pod", roleServiceFullName, hostname)));
            return;
        }
        Pod pod = podOptional.get();
        PodResource podResource = client.pods().resource(pod);
        List<Container> containers = pod.getSpec().getContainers();
        Container mainContainer = containers.get(0);
        if (containers.size() > 1) {
            Optional<Container> serverContainerOp = containers.stream().filter(c -> c.getName().equalsIgnoreCase("server")).findFirst();
            if (serverContainerOp.isPresent()) {
                mainContainer = serverContainerOp.get();
            } else {
                int lastVolumnMountSize = 0;
                for (Container container : containers) {
                    if (container.getVolumeMounts().size() > lastVolumnMountSize) {
                        lastVolumnMountSize = container.getVolumeMounts().size();
                        mainContainer = container;
                    }
                }
            }
        }
        ContainerResource containerResource = podResource.inContainer(mainContainer.getName());

        try (LogWatch logWatch = containerResource.tailingLines(200).watchLog();
             BufferedReader reader = new BufferedReader(new InputStreamReader(logWatch.getOutput()))) {
            while (true) {
                String line = reader.readLine();
                if (line == null || !wsSession.isOpen()) {
                    break;
                } else {
                    wsSession.sendMessage(new TextMessage(line));
                }
            }
        }
        log.info("退出监听服务器日志: sessionID {}", wsSessionBean.getWsSessionId());
    }


    public ServiceRoleLogPage getServiceLog(Integer clusterId, String roleName, String logLevel, int from, int pageSize) {
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
