package com.data.udh.processor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.config.UdhConfigProp;
import com.data.udh.dao.*;
import com.data.udh.dto.RoleNodeInfo;
import com.data.udh.entity.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@Slf4j
public class ConfigTask extends BaseUdhTask {


    private static final String CONF_DIR = "conf";
    private static final String RENDER_DIR = "render";

    @Override
    public void internalExecute() {
        StackServiceRepository stackServiceRepository = SpringUtil.getBean(StackServiceRepository.class);
        ServiceInstanceRepository serviceInstanceRepository = SpringUtil.getBean(ServiceInstanceRepository.class);
        ClusterNodeRepository clusterNodeRepository = SpringUtil.getBean(ClusterNodeRepository.class);
        ServiceRoleInstanceRepository roleInstanceRepository = SpringUtil.getBean(ServiceRoleInstanceRepository.class);
        ServiceInstanceConfigRepository configRepository = SpringUtil.getBean(ServiceInstanceConfigRepository.class);

        UdhConfigProp udhConfigProp = SpringUtil.getBean(UdhConfigProp.class);

        TaskParam taskParam = getTaskParam();
        Integer serviceInstanceId = taskParam.getServiceInstanceId();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();
        List<ServiceInstanceConfigEntity> configEntityList = configRepository.findByServiceInstanceIdAndCustomConfFile(serviceInstanceId,null);
        List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceId(serviceInstanceId);

        String stackCode = stackServiceEntity.getStackCode();
        String stackServiceName = stackServiceEntity.getName().toLowerCase();


        // 创建工作目录  ${workHome}/zookeeper1/node001/conf
        String workHome = udhConfigProp.getWorkHome();
        String confPath = workHome + File.separator + serviceInstanceEntity.getServiceName() + File.separator + taskParam.getHostName() + File.separator + CONF_DIR;
        // 先删除清空
        FileUtil.del(confPath);
        FileUtil.mkdir(confPath);

        // 用freemarker在本地生成服务实例的所有配置文件
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        try {
            String renderDir = udhConfigProp.getStackLoadPath() + File.separator + stackCode + File.separator + stackServiceName + File.separator + RENDER_DIR;
            File renderDirFile = new File(renderDir);
            config.setDirectoryForTemplateLoading(renderDirFile);
            // 构建数据模型
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("service", serviceInstanceEntity);

            dataModel.put("conf", configEntityList.stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue)));

            Map<String, List<RoleNodeInfo>> serviceRoles = roleInstanceEntities.stream().map(new Function<ServiceRoleInstanceEntity, RoleNodeInfo>() {
                @Override
                public RoleNodeInfo apply(ServiceRoleInstanceEntity serviceRoleInstanceEntity) {
                    ClusterNodeEntity nodeEntity = clusterNodeRepository.findById(serviceRoleInstanceEntity.getNodeId()).get();
                    return new RoleNodeInfo(nodeEntity.getId(), nodeEntity.getHostname(), serviceRoleInstanceEntity.getServiceRoleName());
                }
            }).collect(Collectors.groupingBy(RoleNodeInfo::getRoleName));

            dataModel.put("serviceRoles", serviceRoles);
            dataModel.put("localhostname", "node001");

            // 获取该服务支持的自定义配置文件名
            String customConfigFiles = stackServiceEntity.getCustomConfigFiles();
            Map<String, Map<String, String>> customConfs = new HashMap<>();

            if (StringUtils.isNoneBlank(customConfigFiles)) {
                for (String customConfigFileName : customConfigFiles.split(",")) {
                    List<ServiceInstanceConfigEntity> customConfEntities = configRepository.findByServiceInstanceIdAndCustomConfFile(serviceInstanceId, customConfigFileName);
                    HashMap<String, String> map = new HashMap<>();
                    for (ServiceInstanceConfigEntity customConfEntity : customConfEntities) {
                        map.put(customConfEntity.getName(), customConfEntity.getValue());
                    }
                    customConfs.put(customConfigFileName, map);
                }
            }
            dataModel.put("customConfs", customConfs);

            // 执行渲染
            for (String templateName : renderDirFile.list()) {
                if (templateName.endsWith(".ftl")) {
                    Template template = config.getTemplate(templateName);
                    FileWriter out = new FileWriter(confPath + File.separator + StringUtils.substringBeforeLast(templateName, ".ftl"));
                    template.process(dataModel, out);
                    out.close();
                } else {
                    InputStream fileReader = new FileInputStream(renderDir + File.separator + templateName);
                    FileOutputStream out = new FileOutputStream(confPath + File.separator + templateName);
                    IOUtils.copy(fileReader, out);
                    IOUtils.close(fileReader);
                    IOUtils.close(out);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        // ssh上传所有配置文件到指定目录


    }

}
