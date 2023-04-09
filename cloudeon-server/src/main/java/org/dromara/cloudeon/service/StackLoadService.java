package org.dromara.cloudeon.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import org.dromara.cloudeon.config.CloudeonConfigProp;
import org.dromara.cloudeon.dao.StackInfoRepository;
import org.dromara.cloudeon.dao.StackServiceConfRepository;
import org.dromara.cloudeon.dao.StackServiceRepository;
import org.dromara.cloudeon.dao.StackServiceRoleRepository;
import org.dromara.cloudeon.dto.StackConfiguration;
import org.dromara.cloudeon.dto.StackServiceInfo;
import org.dromara.cloudeon.dto.StackServiceRole;
import org.dromara.cloudeon.entity.StackInfoEntity;
import org.dromara.cloudeon.entity.StackServiceConfEntity;
import org.dromara.cloudeon.entity.StackServiceEntity;
import org.dromara.cloudeon.entity.StackServiceRoleEntity;
import org.dromara.cloudeon.enums.ConfValueType;
import org.dromara.cloudeon.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.dromara.cloudeon.utils.Constant.*;

@Component
@Slf4j
public class StackLoadService implements ApplicationRunner {

    @Resource
    private CloudeonConfigProp cloudeonConfigProp;

    @Resource
    private StackInfoRepository stackInfoRepository;

    @Resource
    private StackServiceRepository stackServiceRepository;

    @Resource
    private StackServiceRoleRepository stackServiceRoleRepository;

    @Resource
    StackServiceConfRepository stackServiceConfRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Yaml yaml = new Yaml();
        File[] stackPath = FileUtil.ls(cloudeonConfigProp.getStackLoadPath());
        for (File file : stackPath) {
            // 获取框架名
            String stackName = file.getName();
            // 遍历框架子目录获取所有服务的目录
            File[] servicePaths = FileUtil.ls(file.getAbsolutePath());

            // 查找数据库中是否已有该stack
            StackInfoEntity stackInfoEntity = null;
            stackInfoEntity = stackInfoRepository.findByStackCode(stackName);
            if (stackInfoEntity == null) {
                // 持久化stack
                stackInfoEntity = new StackInfoEntity();
                stackInfoEntity.setStackCode(stackName);
                stackInfoRepository.save(stackInfoEntity);
            }

            Integer stackInfoEntityId = stackInfoEntity.getId();


            // 遍历每一个service并加载文件信息到数据库中
            for (File servicePath : servicePaths) {
                String serviceInfoYamlFilePath = servicePath + FileUtil.FILE_SEPARATOR + StackPackageInfoYAML;
                String iconAppFilePath = servicePath + FileUtil.FILE_SEPARATOR + DIR_ICON + FileUtil.FILE_SEPARATOR + ICON_APP;
                String iconDefaultFilePath = servicePath + FileUtil.FILE_SEPARATOR + DIR_ICON + FileUtil.FILE_SEPARATOR + ICON_DEFAULT;
                String iconDangerFilePath = servicePath + FileUtil.FILE_SEPARATOR + DIR_ICON + FileUtil.FILE_SEPARATOR + ICON_DANGER;
                if (FileUtil.exist(serviceInfoYamlFilePath)) {
                    log.info("找到" + servicePath.getName() + "的" + StackPackageInfoYAML);

                    // 读取service-info文件
                    InputStream infoInputStream = new FileInputStream(serviceInfoYamlFilePath);
                    StackServiceInfo serviceInfo = yaml.loadAs(infoInputStream, StackServiceInfo.class);

                    // 读取图标
                    String iconAppStr = ImageUtil.GetImageStr(iconAppFilePath);
                    String iconDefaultStr = ImageUtil.GetImageStr(iconDefaultFilePath);
                    String iconDangerStr = ImageUtil.GetImageStr(iconDangerFilePath);

                    // 查找数据库中是否含有该service
                    StackServiceEntity stackServiceEntity = null;
                    stackServiceEntity = stackServiceRepository.findByStackIdAndName(stackInfoEntityId, serviceInfo.getName());
                    if (stackServiceEntity == null) {
                        stackServiceEntity = new StackServiceEntity();
                    }

                    // 持久化service
                    BeanUtil.copyProperties(serviceInfo, stackServiceEntity);
                    stackServiceEntity.setStackId(stackInfoEntityId);
                    if (serviceInfo.getDashboard() != null && StrUtil.isNotBlank(serviceInfo.getDashboard().getUid())) {
                        stackServiceEntity.setDashboardUid(serviceInfo.getDashboard().getUid());
                    }
                    stackServiceEntity.setStackCode(stackInfoEntity.getStackCode());
                    stackServiceEntity.setDependencies(StrUtil.join(",", serviceInfo.getDependencies()));
                    stackServiceEntity.setCustomConfigFiles(StrUtil.join(",", serviceInfo.getCustomConfigFiles()));
                    stackServiceEntity.setServiceConfigurationYaml(yaml.dump(serviceInfo.getConfigurations()));
                    stackServiceEntity.setPersistencePaths(StrUtil.join(",", serviceInfo.getPersistencePaths()));
                    // 持久化图标base64
                    stackServiceEntity.setIconDanger(iconDangerStr);
                    stackServiceEntity.setIconDefault(iconDefaultStr);
                    stackServiceEntity.setIconApp(iconAppStr);
                    stackServiceRepository.save(stackServiceEntity);
                    Integer stackServiceEntityId = stackServiceEntity.getId();


                    // 持久化role
                    for (StackServiceRole serviceInfoRole : serviceInfo.getRoles()) {
                        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByStackIdAndNameAndServiceId(stackInfoEntityId, serviceInfoRole.getName(), stackServiceEntityId);
                        if (stackServiceRoleEntity == null) {
                            stackServiceRoleEntity = new StackServiceRoleEntity();
                        }
                        BeanUtil.copyProperties(serviceInfoRole, stackServiceRoleEntity);
                        stackServiceRoleEntity.setStackId(stackInfoEntityId);
                        stackServiceRoleEntity.setServiceId(stackServiceEntityId);
                        stackServiceRoleRepository.save(stackServiceRoleEntity);
                    }

                    // 持久化service conf
                    for (StackConfiguration configuration : serviceInfo.getConfigurations()) {
                        ConfValueType confValueType = ConfValueType.valueOf(configuration.getValueType());
                        StackServiceConfEntity stackServiceConfEntity = stackServiceConfRepository.findByStackIdAndNameAndServiceId(stackInfoEntityId, configuration.getName(), stackServiceEntityId);
                        if (stackServiceConfEntity == null) {
                            stackServiceConfEntity = new StackServiceConfEntity();
                        }
                        BeanUtil.copyProperties(configuration, stackServiceConfEntity);
                        stackServiceConfEntity.setStackId(stackInfoEntityId);
                        stackServiceConfEntity.setServiceId(stackServiceEntityId);
                        stackServiceConfEntity.setConfFile(configuration.getConfFile());
                        stackServiceConfEntity.setValueType(confValueType);
                        if (configuration.getOptions() != null) {
                            stackServiceConfEntity.setOptions(JSONObject.toJSONString(configuration.getOptions()));
                        }
                        stackServiceConfRepository.save(stackServiceConfEntity);
                    }


                    // close file stream
                    IoUtil.close(infoInputStream);


                }
            }

        }
    }
}
