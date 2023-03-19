package com.data.udh;
 
import com.data.udh.dao.AlertQuotaRepository;
import com.data.udh.dao.ClusterInfoRepository;
import com.data.udh.dto.AlertItem;
import com.data.udh.entity.ClusterAlertQuotaEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClusterRepositoryTest {

    @Resource
    private ClusterInfoRepository clusterInfoRepository;

    @Resource
    private AlertQuotaRepository alertQuotaRepository;
 
    @Test
    public void dod() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>"+clusterInfoRepository.findAll());
    }

    @Test
    public void alert() throws IOException, TemplateException {
        List<ClusterAlertQuotaEntity> alerts = alertQuotaRepository.findAll();
        List<AlertItem> alertItems = alerts.stream().map(new Function<ClusterAlertQuotaEntity, AlertItem>() {
            @Override
            public AlertItem apply(ClusterAlertQuotaEntity clusterAlertQuota) {
                AlertItem alertItem = new AlertItem();
                alertItem.setAlertName(clusterAlertQuota.getAlertQuotaName());
                alertItem.setAlertExpr(clusterAlertQuota.getAlertExpr() + " " + clusterAlertQuota.getCompareMethod() + " " + clusterAlertQuota.getAlertThreshold());
                alertItem.setClusterId(1);
                alertItem.setServiceRoleName(clusterAlertQuota.getServiceRoleName());
                alertItem.setAlertLevel(clusterAlertQuota.getAlertLevel().getDesc());
                alertItem.setAlertAdvice(clusterAlertQuota.getAlertAdvice());
                alertItem.setTriggerDuration(clusterAlertQuota.getTriggerDuration());
                return alertItem;
            }
        }).collect(Collectors.toList());

        // 1.加载模板
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        config.setDirectoryForTemplateLoading(new File("/Volumes/Samsung_T5/opensource/e-mapreduce/cloudeon-stack/UDH-1.0.0/monitor/render/rule"));

        // 数据对象
        Map<String, Object> data = new HashMap<>();
        data.put("itemList", alertItems);
        data.put("serviceName", "internal-service");

        // 得到模板对象
        Template template = config.getTemplate("rules-internal.yml.ftl");
        FileWriter out = new FileWriter(new File("/Volumes/Samsung_T5/opensource/e-mapreduce/" + File.separator + "rules-alert.yml"));
        template.process(data, out);
        out.close();
    }
}