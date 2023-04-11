package org.dromara.cloudeon.service;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.dromara.cloudeon.dao.ClusterAlertRuleRepository;
import org.dromara.cloudeon.entity.ClusterAlertRuleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class AlertService {
    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Resource
    private ClusterAlertRuleRepository clusterAlertRuleRepository;

    public void upgradeMonitorAlertRule(Integer clusterId) {
        List<ClusterAlertRuleEntity> alertRuleEntityList = clusterAlertRuleRepository.findByClusterId(clusterId);
        Map<String, List<ClusterAlertRuleEntity>> map = alertRuleEntityList.stream().collect(Collectors.groupingBy(ClusterAlertRuleEntity::getStackServiceName));
        // 根据服务生成对应的告警规则文件
        map.entrySet().stream().forEach(new Consumer<Map.Entry<String, List<ClusterAlertRuleEntity>>>() {
            @Override
            public void accept(Map.Entry<String, List<ClusterAlertRuleEntity>> stringListEntry) {
                String serviceName = stringListEntry.getKey();
                try {
                    Template template = freeMarkerConfigurer.getConfiguration().getTemplate("alert-rule-template.ftl");
                    // 处理模板
                    Map<String, Object> data = new HashMap<>();
                    data.put("serviceName", serviceName);
                    data.put("ruleList", stringListEntry.getValue());
                    StringWriter writer = new StringWriter();
                    template.process(data, writer);
                    String result = writer.toString();
                    System.out.println(result);
                } catch (IOException | TemplateException e) {
                    e.printStackTrace();
                }
            }
        });

        // 查找promethus服务所在节点，将本地目录里该集群的告警规则文件全部上传
    }
}
