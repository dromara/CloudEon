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

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import io.fabric8.kubernetes.api.model.ContainerState;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.dsl.PodResource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.dromara.cloudeon.dao.ClusterNodeRepository;
import org.dromara.cloudeon.dao.ServiceInstanceConfigRepository;
import org.dromara.cloudeon.dao.ServiceInstanceRepository;
import org.dromara.cloudeon.dao.ServiceRoleInstanceRepository;
import org.dromara.cloudeon.dto.ServiceRoleLog;
import org.dromara.cloudeon.dto.ServiceRoleLogPage;
import org.dromara.cloudeon.dto.WsSessionBean;
import org.dromara.cloudeon.entity.ClusterNodeEntity;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.ServiceRoleInstanceEntity;
import org.dromara.cloudeon.enums.RoleType;
import org.dromara.cloudeon.utils.K8sUtil;
import org.dromara.cloudeon.utils.WebSocketSessionOutputStream;
import org.dromara.cloudeon.utils.k8s.PodLogWatcher;
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
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LogService {

    @Resource
    private ServiceService serviceService;
    @Resource
    private ServiceRoleInstanceRepository roleInstanceRepository;

    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;

    @Resource
    private ClusterNodeRepository clusterNodeRepository;

    @Resource
    private ServiceInstanceConfigRepository serviceInstanceConfigRepository;
    @Resource
    private KubeService kubeService;

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
        WebSocketSession wsSession = wsSessionBean.getWebSocketSession();
        String roleServiceFullName = serviceService.getRoleServiceFullName(roleInstanceEntity);
        RoleType roleType = serviceService.getRoleType(roleInstanceEntity);
        if (!roleType.isSupportLogs()) {
            wsSession.sendMessage(new TextMessage(StrUtil.format("该类型角色（{}）不支持日志查询", roleType)));
            return;
        }
        String namespace = serviceService.getNamespace(serviceInstanceEntity.getClusterId());
        kubeService.executeWithKubeClient(serviceInstanceEntity.getClusterId(), client -> {
            Pod pod;
            switch (roleType) {
                case JOB:
                    Job job = client.batch().v1().jobs().inNamespace(namespace).withName(roleServiceFullName).get();
                    Optional<Pod> jobPodOptional = client.pods().inNamespace(namespace)
                            .withLabels(job.getSpec().getTemplate().getMetadata().getLabels())
                            .list().getItems()
                            .stream().max(Comparator.comparingLong(o -> K8sUtil.parseDateStrToLong(o.getStatus().getStartTime())));
                    if (!jobPodOptional.isPresent()) {
                        wsSession.sendMessage(new TextMessage(StrUtil.format("未找到Job {} 对应的Pod", roleServiceFullName)));
                        return;
                    }
                    pod = jobPodOptional.get();
                    break;
                case DEPLOYMENT:
                    Deployment deployment = client.apps().deployments().inNamespace(namespace).withName(roleServiceFullName).get();
                    Optional<Pod> podOptional = client.pods().inNamespace(namespace).withLabels(deployment.getSpec().getTemplate().getMetadata().getLabels()).list().getItems().stream().filter(
                            p -> {
                                if (p.getSpec() == null) {
                                    return false;
                                }
                                return hostname.equals(p.getSpec().getNodeName());
                            }
                    ).findFirst();
                    if (!podOptional.isPresent()) {
                        wsSession.sendMessage(new TextMessage(StrUtil.format("未找到Pod app标签值为{}且所在节点名为{}的Pod", roleServiceFullName, hostname)));
                        return;
                    }
                    pod = podOptional.get();
                    break;
                default:
                    wsSession.sendMessage(new TextMessage(StrUtil.format("该类型角色（{}）暂未支持日志查询", roleType)));
                    return;
            }
            String podName = pod.getMetadata().getName();
            PodResource podResource = client.pods().inNamespace(namespace).resource(pod);
            wsSession.sendMessage(new TextMessage(StrUtil.format("当前Pod名称为： {}", podName)));
            for (ContainerStatus containerStatus : pod.getStatus().getContainerStatuses()) {
                String containerName = containerStatus.getName();
                ContainerState state = containerStatus.getState();
                wsSession.sendMessage(new TextMessage(StrUtil.format("容器 {} 状态为： {}", containerName, state.toString())));
            }
            try (Watch ignored = podResource.watch(new PodLogWatcher(client, (runPod, containerStatus) -> new WebSocketSessionOutputStream(wsSession,
                    runPod.getStatus().getContainerStatuses().size() > 1
                            ? s -> StrUtil.format("{} > {}", containerStatus.getName(), s)
                            : s -> s)))) {
                while (wsSession.isOpen()) {
                    ThreadUtil.safeSleep(1000);
                }
            }
        });
        log.info("退出监听服务器日志: sessionID {}", wsSessionBean.getWsSessionId());
    }


    public ServiceRoleLogPage getServiceLog(Integer clusterId, String roleName, String logLevel, int from, int pageSize) {
        ServiceRoleLogPage result = ServiceRoleLogPage.builder().build();
        if (StringUtils.isEmpty(roleName)) {
            return result;
        }
//        查询es服务实例
        Integer serviceInstanceId = serviceInstanceRepository.findByServiceNameAndClusterId("ELASTICSEARCH",clusterId).getId();
        List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceIdAndServiceRoleName(serviceInstanceId, "ELASTICSEARCH_NODE");
        int esPort = Integer.parseInt(serviceInstanceConfigRepository.findByServiceInstanceIdAndName(serviceInstanceId, "elasticsearch.http.listeners.port").getValue());
        HttpHost[] esHttpHostArr = roleInstanceEntities.stream().map(entity -> new HttpHost(
                clusterNodeRepository.findById(entity.getNodeId()).get().getIp(),
                esPort
        )).toArray(HttpHost[]::new);

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(esHttpHostArr));

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
                    String hostip = (String) documentFields.getSourceAsMap().get("nodename");
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
