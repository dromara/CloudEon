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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.cloudeon.controller.response.ActiveAlertVO;
import org.dromara.cloudeon.controller.response.HistoryAlertVO;
import org.dromara.cloudeon.dao.*;
import org.dromara.cloudeon.dto.AlertLabels;
import org.dromara.cloudeon.dto.AlertMessage;
import org.dromara.cloudeon.dto.Annotations;
import org.dromara.cloudeon.dto.ResultDTO;
import org.dromara.cloudeon.entity.*;
import org.dromara.cloudeon.enums.AlertLevel;
import org.dromara.cloudeon.enums.CommandType;
import org.dromara.cloudeon.service.CommandHandler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.dromara.cloudeon.utils.Constant.VERTX_COMMAND_ADDRESS;

@Slf4j
@RestController
@RequestMapping("/alert")
public class AlertController {
    @Resource(name = "cloudeonVertx")
    private Vertx cloudeonVertx;
    @Resource
    private CommandHandler commandHandler;
    @Resource
    private AlertMessageRepository alertMessageRepository;

    @Resource
    private ServiceRoleInstanceRepository roleInstanceRepository;

    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;

    @Resource
    private ClusterNodeRepository clusterNodeRepository;
    @Resource
    private ClusterAlertRuleRepository clusterAlertRuleRepository;
    @Resource
    StackAlertRuleRepository stackAlertRuleRepository;
    @Resource
    private ClusterInfoRepository clusterInfoRepository;

    /**
     * 接收alertmanager告警回调
     * 遍历告警列表alerts，依据status得到firing告警列表和resolved告警列表（注意，外层的status不可靠，不作为判断依据）
     * 对于firing告警列表：对比已有的活跃状态的告警：
     *   1. 如果已存在：忽略
     *   2. 如果不存在：新增
     *   3. 如果已有的活跃告警未在回调告警里面：视为过期（可能错过了resolved），修改状态为已解决
     * 对于resolved告警列表：将对应活跃告警状态修改为已解决
     *
     * todo 联动服务角色实例状态和节点状态
     */
    @RequestMapping("/webhook")
    public ResultDTO<Void> save(@RequestBody String alertMessage) {
        // 接收alertmanager告警:
        // {"receiver":"web\\.hook","status":"firing","alerts":[{"status":"firing","labels":{"alertname":"主机CPU使用率","clusterId":"1","instance":"k8s-node1:9101","serviceRoleName":"node","severity":"exception"},"annotations":{"description":"的k8s-node1:9101实例产生告警","summary":"444"},"startsAt":"2023-03-25T14:39:51.68943305+08:00","endsAt":"0001-01-01T00:00:00Z","generatorURL":"http://k8s-node1:9090/graph?g0.expr=%281+-+avg+by%28instance%29+%28irate%28node_cpu_seconds_total%7Bmode%3D%22idle%22%7D%5B5m%5D%29%29%29+%2A+100+%3E+95\u0026g0.tab=1","fingerprint":"72a2002704e27a2e"},{"status":"firing","labels":{"alertname":"主机CPU使用率","clusterId":"1","instance":"k8s-node2:9101","serviceRoleName":"node","severity":"exception"},"annotations":{"description":"的k8s-node2:9101实例产生告警","summary":"444"},"startsAt":"2023-03-25T14:41:21.68943305+08:00","endsAt":"0001-01-01T00:00:00Z","generatorURL":"http://k8s-node1:9090/graph?g0.expr=%281+-+avg+by%28instance%29+%28irate%28node_cpu_seconds_total%7Bmode%3D%22idle%22%7D%5B5m%5D%29%29%29+%2A+100+%3E+95\u0026g0.tab=1","fingerprint":"ba18df1a61fe8e0b"},{"status":"firing","labels":{"alertname":"主机CPU使用率","clusterId":"1","instance":"k8s-node3:9101","serviceRoleName":"node","severity":"exception"},"annotations":{"description":"的k8s-node3:9101实例产生告警","summary":"444"},"startsAt":"2023-03-25T14:41:06.68943305+08:00","endsAt":"0001-01-01T00:00:00Z","generatorURL":"http://k8s-node1:9090/graph?g0.expr=%281+-+avg+by%28instance%29+%28irate%28node_cpu_seconds_total%7Bmode%3D%22idle%22%7D%5B5m%5D%29%29%29+%2A+100+%3E+95\u0026g0.tab=1","fingerprint":"5a6e9db40eb24b04"}],"groupLabels":{"alertname":"主机CPU使用率"},"commonLabels":{"alertname":"主机CPU使用率","clusterId":"1","serviceRoleName":"node","severity":"exception"},"commonAnnotations":{"summary":"444"},"externalURL":"http://k8s-node1:9093","version":"4","groupKey":"{}:{alertname=\"主机CPU使用率\"}","truncatedAlerts":0}
        // {"receiver":"web\\.hook","status":"resolved","alerts":[{"status":"resolved","labels":{"alertLevel":"exception","alertname":"Grafana进程存活","clusterId":"1","instance":"k8s-node1:3000","job":"grafana","serviceRoleName":"Grafana"},"annotations":{"alertAdvice":"Grafana宕机，请重新启动","alertInfo":"grafana的k8s-node1:3000实例产生告警"},"startsAt":"2023-03-25T15:24:36.039625135+08:00","endsAt":"2023-03-25T15:27:51.039625135+08:00","generatorURL":"http://k8s-node1:9090/graph?g0.expr=up%7Bjob%3D%22grafana%22%7D+%21%3D+1\u0026g0.tab=1","fingerprint":"20453f227ff62f33"}],"groupLabels":{"alertname":"Grafana进程存活"},"commonLabels":{"alertLevel":"exception","alertname":"Grafana进程存活","clusterId":"1","instance":"k8s-node1:3000","job":"grafana","serviceRoleName":"Grafana"},"commonAnnotations":{"alertAdvice":"Grafana宕机，请重新启动","alertInfo":"grafana的k8s-node1:3000实例产生告警"},"externalURL":"http://k8s-node1:9093","version":"4","groupKey":"{}:{alertname=\"Grafana进程存活\"}","truncatedAlerts":0}
        AlertMessage alertMes = JSONObject.parseObject(alertMessage, AlertMessage.class);
        log.info("接收alertmanager告警:{}", alertMessage);
        Map<String, AlertMessageEntity> activeAlertMap = alertMessageRepository.findByResolved(false)
                .stream().collect(Collectors.toMap(alert -> alert.getAlertName() + alert.getFireTime() + alert.getHostname(), alert -> alert));
        Map<String, AlertMessageEntity> webhookAlertMap = alertMes.getAlerts().stream().map(alert -> {
            AlertLabels labels = alert.getLabels();
            // 告警不一定是实例级别的，不一定携带instance
            // instance 使用的不一定是hostname，也可能是ip
            String instance = labels.getInstance();
            boolean hasInstance = StringUtils.isNotEmpty(instance);
            String hostname = null;
            if (hasInstance) {
                String hostnameOrIp = instance.split(":")[0];
                ClusterNodeEntity nodeEntity = clusterNodeRepository.findByHostnameOrIp(hostnameOrIp, hostnameOrIp);
                hostname = nodeEntity.getHostname();
            }
            if (hasInstance) {
                String hostnameOrIp = instance.split(":")[0];
                ClusterNodeEntity nodeEntity = clusterNodeRepository.findByHostnameOrIp(hostnameOrIp, hostnameOrIp);
                hostname = nodeEntity.getHostname();
            }
            String serviceRoleName = labels.getServiceRoleName();
            String serviceName = labels.getServiceName();
            Annotations annotations = alert.getAnnotations();
            int clusterId = labels.getClusterId();
            ServiceInstanceEntity serviceInstance = serviceInstanceRepository.findByServiceNameAndClusterId(serviceName, clusterId);
            AlertMessageEntity alertMessageEntity = AlertMessageEntity.builder()
                    .resolved(alert.getStatus().equals("resolved"))
                    .fireTime(alert.getStartsAt())
                    .solveTime(alert.getEndsAt())
                    .serviceInstanceId(serviceInstance.getId())
                    .createTime(new Date())
                    .alertName(labels.getAlertname())
                    .alertLevel(AlertLevel.fromDesc(labels.getAlertLevel()))
                    .alertAdvice(annotations.getAlertAdvice())
                    .alertInfo(annotations.getAlertInfo())
                    .clusterId(clusterId)
                    .build();
            if (hasInstance) {
                // 根据节点hostname查询节点id
                Integer nodeId = clusterNodeRepository.findByHostname(hostname).getId();
                // 查询服务角色实例
                ServiceRoleInstanceEntity serviceRoleInstanceEntity = roleInstanceRepository.findByServiceRoleNameAndClusterIdAndHostname(clusterId, serviceRoleName, hostname);

                alertMessageEntity.setHostname(hostname);
                alertMessageEntity.setServiceRoleInstanceId(serviceRoleInstanceEntity.getServiceInstanceId());
                alertMessageEntity.setNodeId(nodeId);
            }
            return alertMessageEntity;
        }).collect(Collectors.toMap(alert -> alert.getAlertName() + alert.getFireTime() + alert.getHostname(), alert -> alert));
        Map<String, AlertMessageEntity> activeWebhookAlertMap = Maps.newHashMap();
        Map<String, AlertMessageEntity> resolvedWebhookAlertMap = Maps.newHashMap();
        for (Map.Entry<String, AlertMessageEntity> entry : webhookAlertMap.entrySet()) {
            if (entry.getValue().isResolved()) {
                resolvedWebhookAlertMap.put(entry.getKey(), entry.getValue());
            } else {
                activeWebhookAlertMap.put(entry.getKey(), entry.getValue());
            }
        }
        if (activeWebhookAlertMap.size() > 0) {
            List<AlertMessageEntity> newActiveAlertList = activeWebhookAlertMap.keySet().stream().filter(key -> !activeAlertMap.containsKey(key))
                    .map(activeWebhookAlertMap::get).collect(Collectors.toList());
            alertMessageRepository.saveAll(newActiveAlertList);

            List<AlertMessageEntity> expiredAlertList = activeAlertMap.keySet().stream().filter(key -> !activeWebhookAlertMap.containsKey(key))
                    .map(activeAlertMap::get)
                    .collect(Collectors.toList());
            expiredAlertList.forEach(expiredAlert -> {
                expiredAlert.setResolved(true);
                expiredAlert.setSolveTime(Instant.now().toString());
                expiredAlert.setUpdateTime(new Date());
            });
            alertMessageRepository.saveAll(expiredAlertList);
        }
        if (resolvedWebhookAlertMap.size() > 0) {
            List<AlertMessageEntity> resolvedAlertList = resolvedWebhookAlertMap.keySet().stream().filter(activeAlertMap::containsKey)
                    .map(activeAlertMap::get).collect(Collectors.toList());
            resolvedAlertList.forEach(resolvedAlert -> {
                AlertMessageEntity webhookAlert = resolvedWebhookAlertMap.get(resolvedAlert.getAlertName() + resolvedAlert.getFireTime() + resolvedAlert.getHostname());
                resolvedAlert.setResolved(true);
                resolvedAlert.setSolveTime(webhookAlert.getSolveTime());
                resolvedAlert.setUpdateTime(new Date());
            });
            alertMessageRepository.saveAll(resolvedAlertList);
        }
        return ResultDTO.success(null);
    }


    @GetMapping("/active")
    public ResultDTO<List<ActiveAlertVO>> getActiveMessage(Integer clusterId) {
        List<ActiveAlertVO> activeAlertVOS = alertMessageRepository.findByIsResolve(false, clusterId).stream().map(new Function<AlertMessageEntity, ActiveAlertVO>() {
            @Override
            public ActiveAlertVO apply(AlertMessageEntity alertMessageEntity) {
                Integer serviceInstanceId = alertMessageEntity.getServiceInstanceId();
                Integer roleInstanceId = alertMessageEntity.getServiceRoleInstanceId();

                Optional<ServiceInstanceEntity> optionalServiceInstance = serviceInstanceRepository.findById(serviceInstanceId);
                boolean present = optionalServiceInstance.isPresent();
                if (present) {
                    ServiceInstanceEntity serviceInstanceEntity = optionalServiceInstance.get();
                    String serviceLabel = serviceInstanceEntity.getLabel();
                    String roleInstanceLabel = roleInstanceRepository.getRoleInstanceLabel(roleInstanceId);

                    return ActiveAlertVO.builder()
                            .alertId(alertMessageEntity.getId())
                            .advice(alertMessageEntity.getAlertAdvice())
                            .alertLevelMsg(alertMessageEntity.getAlertLevel().getDesc())
                            .alertName(alertMessageEntity.getAlertName())
                            .createTime(alertMessageEntity.getCreateTime())
                            .info(alertMessageEntity.getAlertInfo())
                            .serviceInstanceName(serviceInstanceEntity.getServiceName())
                            .serviceRoleLabel(roleInstanceLabel)
                            .serviceInstanceId(serviceInstanceId)
                            .hostname(alertMessageEntity.getHostname())
                            .serviceRoleInstanceId(roleInstanceId)
                            .build();
                }else {
                    return null;
                }

            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return ResultDTO.success(activeAlertVOS);
    }

    @GetMapping("/history")
    public ResultDTO<List<HistoryAlertVO>> getHistoryMessage(Integer clusterId) {
        List<HistoryAlertVO> historyAlertVOS = alertMessageRepository.findByIsResolve(true, clusterId).stream().map(new Function<AlertMessageEntity, HistoryAlertVO>() {
            @Override
            public HistoryAlertVO apply(AlertMessageEntity alertMessageEntity) {
                Integer serviceInstanceId = alertMessageEntity.getServiceInstanceId();
                Integer roleInstanceId = alertMessageEntity.getServiceRoleInstanceId();

                Optional<ServiceInstanceEntity> optionalServiceInstance = serviceInstanceRepository.findById(serviceInstanceId);
                boolean present = optionalServiceInstance.isPresent();
                if (present) {
                    ServiceInstanceEntity serviceInstanceEntity =optionalServiceInstance.get();
                    String serviceLabel = serviceInstanceEntity.getLabel();
                    String roleInstanceLabel = roleInstanceRepository.getRoleInstanceLabel(roleInstanceId);
                    return HistoryAlertVO.builder()
                            .alertId(alertMessageEntity.getId())
                            .alertLevelMsg(alertMessageEntity.getAlertLevel().getDesc())
                            .alertName(alertMessageEntity.getAlertName())
                            .createTime(alertMessageEntity.getCreateTime())
                            .updateTime(alertMessageEntity.getUpdateTime())
                            .serviceInstanceName(serviceInstanceEntity.getServiceName())
                            .serviceRoleLabel(roleInstanceLabel)
                            .serviceInstanceId(serviceInstanceId)
                            .hostname(alertMessageEntity.getHostname())
                            .serviceRoleInstanceId(roleInstanceId)
                            .solveTime(LocalDateTimeUtil.of(Instant.parse(alertMessageEntity.getSolveTime())))
                            .fireTime(LocalDateTimeUtil.of(Instant.parse(alertMessageEntity.getFireTime())))
                            .build();
                }else {
                    return null;
                }

            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        return ResultDTO.success(historyAlertVOS);
    }

    @GetMapping("/listRule")
    public ResultDTO<List<ClusterAlertRuleEntity>> listRule(Integer clusterId) {
        List<ClusterAlertRuleEntity> clusterAlertRuleEntities = clusterAlertRuleRepository.findByClusterId(clusterId);

        return ResultDTO.success(clusterAlertRuleEntities);
    }

    @PostMapping("saveRule")
    public ResultDTO<Void> saveRule(@RequestBody ClusterAlertRuleEntity clusterAlertRuleEntity) {
        Optional<ClusterAlertRuleEntity> updateClusterAlertRuleEntityOp = clusterAlertRuleRepository.findById(clusterAlertRuleEntity.getId());
        if (updateClusterAlertRuleEntityOp.isPresent()) {
            ClusterAlertRuleEntity updateClusterAlertRuleEntity = updateClusterAlertRuleEntityOp.get();
            BeanUtil.copyProperties(clusterAlertRuleEntity, updateClusterAlertRuleEntity);
            updateClusterAlertRuleEntity.setUpdateTime(new Date());
            clusterAlertRuleRepository.save(updateClusterAlertRuleEntity);
        }else {
            Date createTime = new Date();
            clusterAlertRuleEntity.setCreateTime(createTime);
            clusterAlertRuleEntity.setUpdateTime(createTime);
            clusterAlertRuleRepository.save(clusterAlertRuleEntity);
        }
        Integer clusterId = clusterAlertRuleEntity.getClusterId();
        String stackServiceName = clusterAlertRuleEntity.getStackServiceName();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findEntityByClusterIdAndStackServiceName(clusterId, stackServiceName);
        //  生成刷新服务配置command
        List<ServiceInstanceEntity> serviceInstanceEntities = Lists.newArrayList(serviceInstanceEntity);
        Integer commandId = commandHandler.buildServiceCommand(serviceInstanceEntities, serviceInstanceEntity.getClusterId(), CommandType.UPGRADE_SERVICE_CONFIG);
        //  调用workflow
        cloudeonVertx.eventBus().request(VERTX_COMMAND_ADDRESS, commandId);
        
        return ResultDTO.success(null);
    }

    @PostMapping("/loadDefaultRule")
    public ResultDTO<Void> loadDefaultRule(Integer clusterId) {
        ClusterInfoEntity clusterInfo = clusterInfoRepository.findById(clusterId).orElseThrow(() -> new IllegalArgumentException("can't find cluster info by id:" + clusterId));
        Set<String> clusterAlertRuleSet = clusterAlertRuleRepository.findByClusterId(clusterId).stream().map(
                clusterAlertRuleEntity -> clusterAlertRuleEntity.getStackRoleName() + "-" + clusterAlertRuleEntity.getRuleName()
        ).collect(Collectors.toSet());
        // 加载默认规则到集群中
        stackAlertRuleRepository.findByStackId(clusterInfo.getStackId()).forEach(stackAlertRuleEntity -> {
            String key = stackAlertRuleEntity.getStackRoleName() + "-" + stackAlertRuleEntity.getRuleName();
            if (clusterAlertRuleSet.contains(key)) {
                return;
            }
            ClusterAlertRuleEntity clusterAlertRuleEntity = new ClusterAlertRuleEntity();
            BeanUtil.copyProperties(stackAlertRuleEntity, clusterAlertRuleEntity);
            clusterAlertRuleEntity.setClusterId(clusterInfo.getId());
            clusterAlertRuleEntity.setCreateTime(new Date());
            clusterAlertRuleEntity.setUpdateTime(new Date());
            clusterAlertRuleRepository.save(clusterAlertRuleEntity);
        });
        return ResultDTO.success(null);
    }
}
