package org.dromara.cloudeon.service;


import cn.hutool.core.map.MapUtil;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.cloudeon.dao.*;
import org.dromara.cloudeon.dto.RoleNodeInfo;
import org.dromara.cloudeon.entity.*;
import org.dromara.cloudeon.enums.RoleType;
import org.dromara.cloudeon.utils.K8sUtil;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ServiceService {
    @Resource
    private ServiceRoleInstanceRepository serviceRoleInstanceRepository;

    @Resource
    private ClusterInfoRepository clusterInfoRepository;
    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;
    @Resource
    private ServiceInstanceConfigRepository serviceInstanceConfigRepository;
    @Resource
    private StackServiceRoleRepository stackServiceRoleRepository;
    @Resource
    private StackServiceRepository stackServiceRepository;
    @Resource
    private ServiceRoleInstanceRepository roleInstanceRepository;
    @Resource
    private ServiceInstanceConfigRepository configRepository;
    @Resource
    private ClusterNodeRepository clusterNodeRepository;
    @Resource
    private Environment environment;

    public String getNamespace(Integer clusterId) {
        String namespace = clusterInfoRepository.findById(clusterId).get().getNamespace();
        if (StringUtils.isBlank(namespace)) {
            namespace = "default";
        }
        return namespace;
    }

    public String getNamespace(ServiceInstanceEntity serviceInstanceEntity) {
        return getNamespace(serviceInstanceEntity.getClusterId());
    }

    public Map<String, String> getConfigMaps(Integer clusterId, String stackServiceName) {
        return serviceInstanceConfigRepository
                .findByServiceInstanceId(serviceInstanceRepository
                        .findByClusterIdAndStackServiceName(clusterId, stackServiceName))
                .stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue));
    }

    public RoleType getRoleType(ServiceRoleInstanceEntity roleInstance) {
        if (StringUtils.isNoneBlank(roleInstance.getRoleType())) {
            return RoleType.getRoleType(roleInstance.getRoleType());
        }
        return RoleType.getRoleType(stackServiceRoleRepository.findById(roleInstance.getStackServiceRoleId())
                .orElseThrow(() -> new RuntimeException("id:" + roleInstance.getStackServiceRoleId() + "找不到对应的stackServiceRole"))
                .getType());
    }

    public String getRoleServiceFullName(ServiceRoleInstanceEntity roleInstance) {
        StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findById(roleInstance.getStackServiceRoleId()).get();
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(roleInstance.getServiceInstanceId()).get();
        return getRoleServiceFullName(stackServiceRoleEntity, serviceInstanceEntity);
    }

    public String getRoleServiceFullName(StackServiceRoleEntity stackServiceRoleEntity, ServiceInstanceEntity serviceInstanceEntity) {
        return K8sUtil.formatK8sNameStr(stackServiceRoleEntity.getRoleFullName() + "-" + serviceInstanceEntity.getServiceName());
    }

    public String getRoleFullName(StackServiceRoleEntity stackServiceRoleEntity) {
        return K8sUtil.formatK8sNameStr(stackServiceRoleEntity.getRoleFullName());
    }

    public String getServiceFullName(ServiceInstanceEntity serviceInstanceEntity) {
        return K8sUtil.formatK8sNameStr(serviceInstanceEntity.getServiceName());
    }

    public Map<String, Object> getDataModel(Integer serviceInstanceId, String roleName) {
        ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
        StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();
        List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceId(serviceInstanceId);

        Map<String, Object> dataModel = new HashMap<>();

        // 查询服务实例所有配置项,包括全局
        List<ServiceInstanceConfigEntity> allConfigEntityList = configRepository.findByServiceInstanceId(serviceInstanceId);
        if ("HELM_CONTROLLER".equalsIgnoreCase(stackServiceEntity.getName())) {
            String kubeConfig = clusterInfoRepository.findById(serviceInstanceEntity.getClusterId()).get().getKubeConfig();
            dataModel.put("super", MapUtil.of("kube_config", kubeConfig));
        }
        if (!"GLOBAL".equalsIgnoreCase(stackServiceEntity.getName())) {
            Integer globalServiceInstanceId = serviceInstanceRepository.findByClusterIdAndStackServiceName(serviceInstanceEntity.getClusterId(), "GLOBAL");
            allConfigEntityList.addAll(configRepository.findByServiceInstanceId(globalServiceInstanceId));
        }
        if (StringUtils.isNotEmpty(roleName)) {
            StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findByServiceIdAndName(stackServiceEntity.getId(), roleName);
            String roleFullName = getRoleFullName(stackServiceRoleEntity);
            String roleServiceFullName = getRoleServiceFullName(stackServiceRoleEntity, serviceInstanceEntity);
            int roleNodeCnt = serviceRoleInstanceRepository.countByServiceInstanceIdAndServiceRoleName(serviceInstanceId, roleName);
            dataModel.put("roleFullName", roleFullName);
            dataModel.put("roleServiceFullName", roleServiceFullName);
            dataModel.put("roleNodeCnt", roleNodeCnt);
        }

        dataModel.put("namespace", getNamespace(serviceInstanceEntity));
        dataModel.put("serviceFullName", getServiceFullName(serviceInstanceEntity));
        dataModel.put("service", serviceInstanceEntity);
        dataModel.put("runAs", stackServiceEntity.getRunAs());
        dataModel.put("conf", allConfigEntityList.stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue)));
        dataModel.put("serviceRoles", getServiceRoles(roleInstanceEntities, clusterNodeRepository));
        try {
            dataModel.put("cloudeonURL", "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + environment.getProperty("server.port"));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        // 获取该服务支持的自定义配置文件名
        String customConfigFiles = stackServiceEntity.getCustomConfigFiles();
        Map<String, Map<String, String>> confFiles = new HashMap<>();

        if (StringUtils.isNoneBlank(customConfigFiles)) {
            for (String confFileName : customConfigFiles.split(",")) {
                List<ServiceInstanceConfigEntity> groupConfEntities = configRepository.findByServiceInstanceIdAndConfFile(serviceInstanceId, confFileName);
                HashMap<String, String> map = new HashMap<>();
                for (ServiceInstanceConfigEntity groupConf : groupConfEntities) {
                    map.put(groupConf.getName(), groupConf.getValue());
                }
                confFiles.put(confFileName, map);
            }
        }
        dataModel.put("confFiles", confFiles);

        String dependenceServiceInstanceIds = serviceInstanceEntity.getDependenceServiceInstanceIds();
        if (StringUtils.isNotBlank(dependenceServiceInstanceIds)) {
            String[] depServiceInstanceIds = dependenceServiceInstanceIds.split(",");
            buildDependenceServiceInModel(dataModel, depServiceInstanceIds);
        }

        return dataModel;
    }

    private Map<String, List<RoleNodeInfo>> getServiceRoles(List<ServiceRoleInstanceEntity> roleInstanceEntities, ClusterNodeRepository clusterNodeRepository) {
        Map<String, List<RoleNodeInfo>> serviceRoles = roleInstanceEntities.stream().map(serviceRoleInstanceEntity -> {
            ClusterNodeEntity nodeEntity = clusterNodeRepository.findById(serviceRoleInstanceEntity.getNodeId()).get();
            return new RoleNodeInfo(serviceRoleInstanceEntity.getId(), nodeEntity.getHostname(), serviceRoleInstanceEntity.getServiceRoleName());
        }).collect(Collectors.groupingBy(RoleNodeInfo::getRoleName));
        return serviceRoles;
    }

    /**
     * 构建依赖服务进入Model中
     */
    private void buildDependenceServiceInModel(Map<String, Object> dataModel, String[] depServiceInstanceIds) {
        Map<String, Object> services = new HashMap<>();
        Arrays.stream(depServiceInstanceIds).forEach(id -> {
            Integer serviceInstanceId = Integer.valueOf(id);
            ServiceInstanceEntity serviceInstanceEntity = serviceInstanceRepository.findById(serviceInstanceId).get();
            Integer stackServiceId = serviceInstanceEntity.getStackServiceId();
            String stackServiceName = stackServiceRepository.findById(stackServiceId).get().getName();

            // 查询服务实例所有配置项
            List<ServiceInstanceConfigEntity> allConfigEntityList = configRepository.findByServiceInstanceId(serviceInstanceId);
            // 查出所有角色
            List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceId(serviceInstanceId);
            Map<String, List<RoleNodeInfo>> serviceRoles = getServiceRoles(roleInstanceEntities, clusterNodeRepository);
            services.put(stackServiceName, ImmutableMap.of(
                    "conf", allConfigEntityList.stream().collect(Collectors.toMap(ServiceInstanceConfigEntity::getName, ServiceInstanceConfigEntity::getValue)),
                    "serviceRoles", serviceRoles,
                    "service", serviceInstanceEntity));
        });

        dataModel.put("dependencies", services);
    }

}
