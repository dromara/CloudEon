package com.data.udh.processor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.config.UdhConfigProp;
import com.data.udh.dao.*;
import com.data.udh.entity.ServiceInstanceEntity;
import com.data.udh.entity.StackServiceEntity;
import com.data.udh.entity.StackServiceRoleEntity;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@NoArgsConstructor
public class StartRoleK8sServiceTask extends BaseUdhTask{
    private static final String K8S_DIR = "k8s";
    private static final String K8S_RESOURCE_DIR = "k8s-resource";
    @Override
    public void internalExecute() {
        StackServiceRepository stackServiceRepository = SpringUtil.getBean(StackServiceRepository.class);
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);
        ServiceRoleInstanceRepository roleInstanceRepository = SpringUtil.getBean(ServiceRoleInstanceRepository.class);
        ServiceInstanceConfigRepository configRepository = SpringUtil.getBean(ServiceInstanceConfigRepository.class);
        StackServiceRoleRepository stackServiceRoleRepository = SpringUtil.getBean(StackServiceRoleRepository.class);

        UdhConfigProp udhConfigProp = SpringUtil.getBean(UdhConfigProp.class);
        String workHome = udhConfigProp.getWorkHome();

        // 查询框架服务角色名获取模板名
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), taskParam.getRoleName());
        String roleFullName = stackServiceRoleEntity.getRoleFullName();

        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();

        String stackCode = stackServiceEntity.getStackCode();
        String stackServiceName = stackServiceEntity.getName().toLowerCase();


        // 创建本地k8s资源工作目录  ${workHome}/k8s-resource/ZOOKEEPER1/
        String k8sResourceOutputPath = workHome + File.separator + K8S_RESOURCE_DIR+File.separator+serviceInstanceEntity.getServiceName() ;
        log.info("开始角色k8s资源文件生成："+k8sResourceOutputPath);
        // 先删除清空
        log.info("重置目录："+k8sResourceOutputPath);
        FileUtil.del(k8sResourceOutputPath);
        FileUtil.mkdir(k8sResourceOutputPath);

        // 渲染生成k8s资源
        String k8sTemplateFileName = roleFullName + ".yaml.ftl";
        String k8sTemplateDir = udhConfigProp.getStackLoadPath() + File.separator + stackCode + File.separator + stackServiceName + File.separator + K8S_DIR;;
        log.info("加载服务实例角色k8s资源模板目录："+k8sTemplateDir);


        Template template = null;
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 构建数据模型
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("dockerImage", stackServiceEntity.getDockerImage());
        dataModel.put("service", serviceInstanceEntity);

        try {
            config.setDirectoryForTemplateLoading(new File(k8sTemplateDir));
            template = config.getTemplate(k8sTemplateFileName);
            String outPutFile = k8sResourceOutputPath + File.separator + StringUtils.substringBeforeLast(k8sTemplateFileName, ".ftl");
            FileWriter out = new FileWriter(outPutFile);
            template.process(dataModel, out);
            log.info("完成角色k8s资源文件生成："+outPutFile);
            out.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
