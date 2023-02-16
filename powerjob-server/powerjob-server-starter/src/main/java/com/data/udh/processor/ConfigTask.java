package com.data.udh.processor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.config.UdhConfigProp;
import com.data.udh.dao.*;
import com.data.udh.dto.RoleNodeInfo;
import com.data.udh.entity.*;
import com.data.udh.utils.SshUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sshd.client.session.ClientSession;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
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

        // todo 加载依赖服务的配置到本地conf目录，例如spark依赖core-site.xml和hdfs-site.xml还有hive-site.xml文件
        // 创建工作目录  ${workHome}/zookeeper1/node001/conf
        String workHome = udhConfigProp.getWorkHome();
        String taskExecuteHostName = taskParam.getHostName();
        String outputConfPath = workHome + File.separator + serviceInstanceEntity.getServiceName() + File.separator + taskExecuteHostName + File.separator + CONF_DIR;
        // 先删除清空
        FileUtil.del(outputConfPath);
        FileUtil.mkdir(outputConfPath);

        // 用freemarker在本地生成服务实例的所有配置文件
        // 创建核心配置对象
        Configuration config = new Configuration(Configuration.getVersion());
        // 设置加载的目录
        try {
            String renderDir = udhConfigProp.getStackLoadPath() + File.separator + stackCode + File.separator + stackServiceName + File.separator + RENDER_DIR;
            log.info("加载配置文件模板目录："+renderDir);
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
            dataModel.put("localhostname", taskExecuteHostName);

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
                    String outPutFile = outputConfPath + File.separator + StringUtils.substringBeforeLast(templateName, ".ftl");
                    FileWriter out = new FileWriter(outPutFile);
                    template.process(dataModel, out);
                    log.info("完成配置文件生成："+outPutFile);
                    out.close();
                } else {
                    InputStream fileReader = new FileInputStream(renderDir + File.separator + templateName);
                    String outPutFile = outputConfPath + File.separator + templateName;
                    FileOutputStream out = new FileOutputStream(outPutFile);
                    IOUtils.copy(fileReader, out);
                    log.info("完成配置文件生成："+outPutFile);
                    IOUtils.close(fileReader);
                    IOUtils.close(out);
                }
            }

        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // ssh上传所有配置文件到指定目录
        ClusterNodeEntity nodeEntity = clusterNodeRepository.findByHostname(taskParam.getHostName());
        ClientSession clientSession = SshUtils.openConnectionByPassword(nodeEntity.getIp(), nodeEntity.getSshPort(), nodeEntity.getSshUser(), nodeEntity.getSshPassword());
        String remoteDirPath = "/opt/udh/" + serviceInstanceEntity.getServiceName() + File.separator + "conf";
        log.info("拷贝本地配置目录："+outputConfPath+" 到节点"+taskParam.getHostName()+"的："+remoteDirPath);
        SshUtils.uploadLocalDirToRemote(clientSession, remoteDirPath, outputConfPath);
        log.info("成功拷贝本地配置目录："+outputConfPath+" 到节点"+taskParam.getHostName()+"的："+remoteDirPath);
        SshUtils.closeConnection(clientSession);


    }

}
