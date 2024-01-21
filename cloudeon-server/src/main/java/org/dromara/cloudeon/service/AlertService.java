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

import cn.hutool.core.io.FileUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.dromara.cloudeon.config.CloudeonConfigProp;
import org.dromara.cloudeon.dao.ClusterAlertRuleRepository;
import org.dromara.cloudeon.dao.ClusterNodeRepository;
import org.dromara.cloudeon.dao.ServiceInstanceRepository;
import org.dromara.cloudeon.dao.ServiceRoleInstanceRepository;
import org.dromara.cloudeon.entity.ClusterAlertRuleEntity;
import org.dromara.cloudeon.entity.ClusterNodeEntity;
import org.dromara.cloudeon.entity.ServiceInstanceEntity;
import org.dromara.cloudeon.entity.ServiceRoleInstanceEntity;
import org.dromara.cloudeon.utils.Constant;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.dromara.cloudeon.utils.Constant.MONITOR_ROLE_PROMETHEUS;
import static org.dromara.cloudeon.utils.Constant.MONITOR_SERVICE_NAME;

@Service
public class AlertService {
    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Resource
    private CloudeonConfigProp cloudeonConfigProp;

    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;

    @Resource
    private ServiceRoleInstanceRepository roleInstanceRepository;

    @Resource
    private ClusterAlertRuleRepository clusterAlertRuleRepository;

    @Resource
    private ClusterNodeRepository clusterNodeRepository;


    public void upgradeMonitorAlertRule(Integer clusterId, Logger log) {
        String workHome = cloudeonConfigProp.getWorkHome();
        // 创建本地告警规则资源工作目录  ${workHome}/alert-rule/1/
        String alertRuleOutputPath = workHome + File.separator + Constant.ALERT_RULE_RESOURCE_DIR + File.separator + clusterId;
        log.info("开始集群告警规则资源文件生成：" + alertRuleOutputPath);

        if (!FileUtil.exist(alertRuleOutputPath)) {
            log.info("目录{}不存在，创建目录...", alertRuleOutputPath);
            FileUtil.mkdir(alertRuleOutputPath);
        }
        List<ClusterAlertRuleEntity> alertRuleEntityList = clusterAlertRuleRepository.findByClusterId(clusterId);
        Map<String, List<ClusterAlertRuleEntity>> map = alertRuleEntityList.stream().collect(Collectors.groupingBy(ClusterAlertRuleEntity::getStackServiceName));
        // 根据服务生成对应的告警规则文件
        map.entrySet().stream().forEach(new Consumer<Map.Entry<String, List<ClusterAlertRuleEntity>>>() {
            @Override
            public void accept(Map.Entry<String, List<ClusterAlertRuleEntity>> stringListEntry) {
                String serviceName = stringListEntry.getKey();
                try {
                    String outputFileName = serviceName + ".yml";
                    String outPutFile = alertRuleOutputPath + File.separator + outputFileName;
                    Template template = freeMarkerConfigurer.getConfiguration().getTemplate("alert-rule-template.ftl");
                    // 处理模板
                    Map<String, Object> data = new HashMap<>();
                    data.put("serviceName", serviceName);
                    data.put("ruleList", stringListEntry.getValue());
                    FileWriter out = new FileWriter(outPutFile);
                    template.process(data, out);
                    log.info("完成服务告警规则资源文件生成：" + outPutFile);
                    out.close();
                } catch (IOException | TemplateException e) {
                    e.printStackTrace();
                }
            }
        });

        // 查找promethus服务所在节点，将本地目录里该集群的告警规则文件全部上传
        ServiceInstanceEntity monitorServiceInstance = serviceInstanceRepository.findEntityByClusterIdAndStackServiceName(clusterId, MONITOR_SERVICE_NAME);
        ServiceRoleInstanceEntity prometheus = roleInstanceRepository.findByServiceInstanceIdAndServiceRoleName(monitorServiceInstance.getId(), MONITOR_ROLE_PROMETHEUS).get(0);
        ClusterNodeEntity prometheusNode = clusterNodeRepository.findById(prometheus.getNodeId()).get();

    }
}
