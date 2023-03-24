package com.data.udh.processor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.config.UdhConfigProp;
import com.data.udh.dao.*;
import com.data.udh.entity.ServiceInstanceConfigEntity;
import com.data.udh.entity.ServiceInstanceEntity;
import com.data.udh.entity.StackServiceEntity;
import com.data.udh.entity.StackServiceRoleEntity;
import com.data.udh.utils.Constant;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@NoArgsConstructor
public class StartK8sServiceTask extends BaseUdhTask{
    @Override
    public void internalExecute() {
        StackServiceRepository stackServiceRepository = SpringUtil.getBean(StackServiceRepository.class);
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        StackServiceRoleRepository stackServiceRoleRepository = SpringUtil.getBean(StackServiceRoleRepository.class);
        ServiceRoleInstanceRepository serviceRoleInstanceRepository = SpringUtil.getBean(ServiceRoleInstanceRepository.class);
        ServiceInstanceConfigRepository configRepository = SpringUtil.getBean(ServiceInstanceConfigRepository.class);

        UdhConfigProp udhConfigProp = SpringUtil.getBean(UdhConfigProp.class);
        String workHome = udhConfigProp.getWorkHome();

        // 查询框架服务角色名获取模板名
        String roleName = taskParam.getRoleName();
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(taskParam.getStackServiceId(), roleName);
        String roleFullName = stackServiceRoleEntity.getRoleFullName();

        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();

        String stackCode = stackServiceEntity.getStackCode();
        String stackServiceName = stackServiceEntity.getName().toLowerCase();

        // 查询服务实例所有配置项
        List<ServiceInstanceConfigEntity> allConfigEntityList = configRepository.findByServiceInstanceId(serviceInstanceId);
        // 创建本地k8s资源工作目录  ${workHome}/k8s-resource/ZOOKEEPER1/
        String k8sResourceOutputPath = workHome + File.separator + Constant.K8S_RESOURCE_DIR+File.separator+serviceInstanceEntity.getServiceName() ;
        log.info("开始角色k8s资源文件生成："+k8sResourceOutputPath);

        if (!FileUtil.exist(k8sResourceOutputPath)) {
            log.info("目录{}不存在，创建目录...",k8sResourceOutputPath);
            FileUtil.mkdir(k8sResourceOutputPath);
        }

        // 渲染生成k8s资源
        String k8sTemplateFileName = roleFullName + ".yaml.ftl";
        String k8sTemplateDir = udhConfigProp.getStackLoadPath() + File.separator + stackCode + File.separator + stackServiceName + File.separator + Constant.K8S_DIR;;
        log.info("加载服务实例角色k8s资源模板目录："+k8sTemplateDir);

        // 查询本服务实例拥有的指定角色节点数
        int roleNodeCnt = serviceRoleInstanceRepository.countByServiceInstanceIdAndServiceRoleName(serviceInstanceId,roleName);

        Template template = null;
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 构建数据模型
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("dockerImage", stackServiceEntity.getDockerImage());
        String roleServiceFullName = roleFullName + "-" + serviceInstanceEntity.getServiceName().toLowerCase();
        dataModel.put("roleServiceFullName", roleServiceFullName);
        dataModel.put("service", serviceInstanceEntity);
        dataModel.put("roleNodeCnt", roleNodeCnt);
        dataModel.put("conf", allConfigEntityList.stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue)));

        String outputFileName = null;
        outputFileName=StringUtils.substringBeforeLast(k8sTemplateFileName, ".ftl");
        String outPutFile = k8sResourceOutputPath + File.separator + outputFileName;
        try {
            config.setDirectoryForTemplateLoading(new File(k8sTemplateDir));
            template = config.getTemplate(k8sTemplateFileName);
            FileWriter out = new FileWriter(outPutFile);
            template.process(dataModel, out);
            log.info("完成角色k8s资源文件生成："+outPutFile);
            out.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // 调用k8s命令启动资源
        try (  KubernetesClient client = new KubernetesClientBuilder().build();){
            List<HasMetadata> metadata = client.load(new FileInputStream(outPutFile))
                    .inNamespace("default")
                    .create();
            String deploymentName = ((Deployment) metadata.get(0)).getMetadata().getName();
            final Deployment deployment = client.apps().deployments().inNamespace("default").withName(deploymentName).get();
            Resource<Deployment> resource = client.resource(deployment).inNamespace("default");
            resource.watch(new Watcher<Deployment>() {
                @Override
                public void eventReceived(Action action, Deployment resource) {
                    log.info("{} {}", action.name(), resource.getMetadata().getName());
                    switch (action) {
                        case ADDED:
                            log.info("{} got added", resource.getMetadata().getName());
                            break;
                        case DELETED:
                            log.info("{} got deleted", resource.getMetadata().getName());
                            break;
                        case MODIFIED:
                            log.info("{} got modified", resource.getMetadata().getName());
                            break;
                        default:
                            log.error("Unrecognized event: {}", action.name());
                    }
                }

                @Override
                public void onClose(WatcherException cause) {
                    System.out.println(cause.getMessage());
                }
            });
            resource.waitUntilReady(10, TimeUnit.MINUTES);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
}
