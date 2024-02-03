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
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.cloudeon.controller.response.ActiveAlertVO;
import org.dromara.cloudeon.controller.response.HistoryAlertVO;
import org.dromara.cloudeon.dao.*;
import org.dromara.cloudeon.dto.*;
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
     * todo 联动服务角色实例状态和节点状态
     */
    @RequestMapping("/webhook")
    public ResultDTO<Void> save(@RequestBody String alertMessage) {
        // 接收alertmanager告警:
        // {"receiver":"web\\.hook","status":"firing","alerts":[{"status":"firing","labels":{"alertname":"主机CPU使用率","clusterId":"1","instance":"k8s-node1:9101","serviceRoleName":"node","severity":"exception"},"annotations":{"description":"的k8s-node1:9101实例产生告警","summary":"444"},"startsAt":"2023-03-25T14:39:51.68943305+08:00","endsAt":"0001-01-01T00:00:00Z","generatorURL":"http://k8s-node1:9090/graph?g0.expr=%281+-+avg+by%28instance%29+%28irate%28node_cpu_seconds_total%7Bmode%3D%22idle%22%7D%5B5m%5D%29%29%29+%2A+100+%3E+95\u0026g0.tab=1","fingerprint":"72a2002704e27a2e"},{"status":"firing","labels":{"alertname":"主机CPU使用率","clusterId":"1","instance":"k8s-node2:9101","serviceRoleName":"node","severity":"exception"},"annotations":{"description":"的k8s-node2:9101实例产生告警","summary":"444"},"startsAt":"2023-03-25T14:41:21.68943305+08:00","endsAt":"0001-01-01T00:00:00Z","generatorURL":"http://k8s-node1:9090/graph?g0.expr=%281+-+avg+by%28instance%29+%28irate%28node_cpu_seconds_total%7Bmode%3D%22idle%22%7D%5B5m%5D%29%29%29+%2A+100+%3E+95\u0026g0.tab=1","fingerprint":"ba18df1a61fe8e0b"},{"status":"firing","labels":{"alertname":"主机CPU使用率","clusterId":"1","instance":"k8s-node3:9101","serviceRoleName":"node","severity":"exception"},"annotations":{"description":"的k8s-node3:9101实例产生告警","summary":"444"},"startsAt":"2023-03-25T14:41:06.68943305+08:00","endsAt":"0001-01-01T00:00:00Z","generatorURL":"http://k8s-node1:9090/graph?g0.expr=%281+-+avg+by%28instance%29+%28irate%28node_cpu_seconds_total%7Bmode%3D%22idle%22%7D%5B5m%5D%29%29%29+%2A+100+%3E+95\u0026g0.tab=1","fingerprint":"5a6e9db40eb24b04"}],"groupLabels":{"alertname":"主机CPU使用率"},"commonLabels":{"alertname":"主机CPU使用率","clusterId":"1","serviceRoleName":"node","severity":"exception"},"commonAnnotations":{"summary":"444"},"externalURL":"http://k8s-node1:9093","version":"4","groupKey":"{}:{alertname=\"主机CPU使用率\"}","truncatedAlerts":0}
        // {"receiver":"web\\.hook","status":"resolved","alerts":[{"status":"resolved","labels":{"alertLevel":"exception","alertname":"Grafana进程存活","clusterId":"1","instance":"k8s-node1:3000","job":"grafana","serviceRoleName":"Grafana"},"annotations":{"alertAdvice":"Grafana宕机，请重新启动","alertInfo":"grafana的k8s-node1:3000实例产生告警"},"startsAt":"2023-03-25T15:24:36.039625135+08:00","endsAt":"2023-03-25T15:27:51.039625135+08:00","generatorURL":"http://k8s-node1:9090/graph?g0.expr=up%7Bjob%3D%22grafana%22%7D+%21%3D+1\u0026g0.tab=1","fingerprint":"20453f227ff62f33"}],"groupLabels":{"alertname":"Grafana进程存活"},"commonLabels":{"alertLevel":"exception","alertname":"Grafana进程存活","clusterId":"1","instance":"k8s-node1:3000","job":"grafana","serviceRoleName":"Grafana"},"commonAnnotations":{"alertAdvice":"Grafana宕机，请重新启动","alertInfo":"grafana的k8s-node1:3000实例产生告警"},"externalURL":"http://k8s-node1:9093","version":"4","groupKey":"{}:{alertname=\"Grafana进程存活\"}","truncatedAlerts":0}
        AlertMessage alertMes = JSONObject.parseObject(alertMessage, AlertMessage.class);
        log.info("接收alertmanager告警:" + alertMessage);
        // 判断告警状态
        if (alertMes.getStatus().equals("firing")) {
            List<AlertMessageEntity> alertMessageEntities = alertMes.getAlerts().stream().map(new Function<Alert, AlertMessageEntity>() {
                @Override
                public AlertMessageEntity apply(Alert alert) {
                    String startsAt = alert.getStartsAt();
                    AlertLabels labels = alert.getLabels();
                    String alertname = labels.getAlertname();

                    int clusterId = labels.getClusterId();
                    // 告警不一定是实例级别的，不一定携带instance
                    String instance = labels.getInstance();
                    boolean hasInstance = StringUtils.isNotEmpty(instance);
                    String hostname = hasInstance ? instance.split(":")[0] : null;
                    String serviceRoleName = labels.getServiceRoleName();
                    String serviceName = labels.getServiceName();
                    log.info("接收到firing告警，根据告警信息查找活跃告警, alertName:{} , startsAt: {} ,hostname:{} , serviceRoleName:{}", alertname, startsAt,hostname,serviceRoleName);
                    // 判断是否已经保存过了
                    AlertMessageEntity messageEntity = alertMessageRepository.findByFireTimeAndAlertNameAndHostname(startsAt, alertname,hostname);
                    if (messageEntity != null) {
                        // 之前已经保存过的就不需要了
                        return null;
                    }
                    ServiceInstanceEntity serviceInstance = serviceInstanceRepository.findByServiceNameAndClusterId(serviceName, clusterId);

                    String severity = labels.getAlertLevel();
                    AlertLevel alertLevel = AlertLevel.fromDesc(severity);
                    Annotations annotations = alert.getAnnotations();
                    String alertAdvice = annotations.getAlertAdvice();
                    String alertInfo = annotations.getAlertInfo();
                    AlertMessageEntity alertMessageEntity = AlertMessageEntity.builder()
                            .fireTime(startsAt)
                            .serviceInstanceId(serviceInstance.getId())
                            .createTime(new Date())
                            .alertName(alertname)
                            .alertLevel(alertLevel)
                            .alertAdvice(alertAdvice)
                            .alertInfo(alertInfo)
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
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());

            alertMessageRepository.saveAll(alertMessageEntities);

        } else if (alertMes.getStatus().equals("resolved")) {

            // 根据开始时间和alertname找回原来的告警，更新endAt和状态
            alertMes.getAlerts().stream().forEach(alert -> {
                String startsAt = alert.getStartsAt();
                AlertLabels labels = alert.getLabels();
                String alertname = labels.getAlertname();
                String instance = labels.getInstance();
                boolean hasInstance = StringUtils.isNotEmpty(instance);
                String hostname = hasInstance ? instance.split(":")[0] : null;

                log.info("接收到已处理的告警，根据告警信息查找活跃告警, alertName:{} , startsAt: {} ,hostname:{}", alertname, startsAt,hostname);
                AlertMessageEntity alertMessageEntity =alertMessageRepository.findByFireTimeAndAlertNameAndHostname(startsAt, alertname,hostname);
                if (alertMessageEntity != null) {
                    String endsAt = alert.getEndsAt();
                    alertMessageEntity.setResolved(true);
                    alertMessageEntity.setSolveTime(endsAt);
                    alertMessageEntity.setUpdateTime(new Date());
                    alertMessageRepository.save(alertMessageEntity);
                }

            });
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

        ClusterAlertRuleEntity updateClusterAlertRuleEntity = clusterAlertRuleRepository.findById(clusterAlertRuleEntity.getId()).get();
        if (updateClusterAlertRuleEntity != null) {
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
