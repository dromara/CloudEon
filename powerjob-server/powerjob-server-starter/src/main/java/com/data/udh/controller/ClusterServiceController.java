package com.data.udh.controller;

import cn.hutool.core.bean.BeanUtil;
import com.data.udh.controller.request.InitServiceRequest;
import com.data.udh.controller.request.ModifyClusterInfoRequest;
import com.data.udh.dao.*;
import com.data.udh.entity.*;
import com.data.udh.utils.ServiceRoleState;
import com.data.udh.utils.ServiceState;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.powerjob.common.response.ResultDTO;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.data.udh.utils.Constant.AdminUserId;
import static com.data.udh.utils.Constant.AdminUserName;
import static com.data.udh.utils.ServiceState.OPERATING;

/**
 * 集群服务相关接口
 */
@RestController
@RequestMapping("/service")
public class ClusterServiceController {

    @Resource
    private ServiceInstanceRepository serviceInstanceRepository;

    @Resource
    private ServiceRoleInstanceRepository roleInstanceRepository;

    @Resource
    private StackServiceRoleRepository stackServiceRoleRepository;

    @Resource
    private ServiceRoleInstanceWebuisRepository roleInstanceWebuisRepository;

    @Resource
    private ServiceInstanceConfigRepository serviceInstanceConfigRepository;


    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/initService")
    public ResultDTO<Void> initService(@RequestBody InitServiceRequest req) {
        Integer clusterId = req.getClusterId();
        Integer stackId = req.getStackId();
        List<InitServiceRequest.ServiceInfo> serviceInfos = req.getServiceInfos();

        for (InitServiceRequest.ServiceInfo serviceInfo : serviceInfos) {
            // 查询实例表获取新增的实例序号
            int maxInstanceSeq = serviceInstanceRepository.maxByStackServiceId(serviceInfo.getStackServiceId());
            int newInstanceSeq = maxInstanceSeq + 1;

            ServiceInstanceEntity serviceInstanceEntity = new ServiceInstanceEntity();
            serviceInstanceEntity.setInstanceSequence(newInstanceSeq);
            serviceInstanceEntity.setSid(serviceInfo.getStackServiceName() + newInstanceSeq);
            serviceInstanceEntity.setServiceName(serviceInfo.getStackServiceName() + newInstanceSeq);
            serviceInstanceEntity.setLabel(serviceInfo.getStackServiceLabel());
            serviceInstanceEntity.setClusterId(clusterId);
            serviceInstanceEntity.setCreateTime(new Date());
            serviceInstanceEntity.setUpdateTime(new Date());
            serviceInstanceEntity.setEnableKerberos(serviceInfo.getEnableKerberos());
            serviceInstanceEntity.setStackServiceId(serviceInfo.getStackServiceId());
            serviceInstanceEntity.setServiceState(ServiceState.OPERATING);

            // 持久化service信息
            serviceInstanceRepository.save(serviceInstanceEntity);
            // 获取持久化后的service 实例id
            Integer serviceInstanceEntityId = serviceInstanceEntity.getId();

            // 获取service 所有角色
            List<InitServiceRequest.InitServiceRole> roles = serviceInfo.getRoles();
            for (InitServiceRequest.InitServiceRole role : roles) {
                Integer stackRoleId = role.getStackRoleId();
                StackServiceRoleEntity stackServiceRoleEntity = stackServiceRoleRepository.findById(stackRoleId).orElseThrow(() -> new IllegalArgumentException("can't find stack service role by id:" + stackRoleId));

                // 遍历该角色分布的节点，生成serviceRoleInstanceEntities
                List<ServiceRoleInstanceEntity> serviceRoleInstanceEntities = role.getNodeIds().stream().map(new Function<Integer, ServiceRoleInstanceEntity>() {
                    @Override
                    public ServiceRoleInstanceEntity apply(Integer nodeId) {
                        ServiceRoleInstanceEntity roleInstanceEntity = new ServiceRoleInstanceEntity();
                        roleInstanceEntity.setClusterId(clusterId);
                        roleInstanceEntity.setCreateTime(new Date());
                        roleInstanceEntity.setUpdateTime(new Date());
                        roleInstanceEntity.setServiceInstanceId(serviceInstanceEntityId);
                        roleInstanceEntity.setStackServiceRoleId(role.getStackRoleId());
                        roleInstanceEntity.setServiceRoleName(role.getStackRoleName());
                        roleInstanceEntity.setServiceRoleState(ServiceRoleState.OPERATING);
                        roleInstanceEntity.setNodeId(nodeId);
                        return roleInstanceEntity;
                    }
                }).collect(Collectors.toList());

                // 批量持久化role实例
                List<ServiceRoleInstanceEntity> serviceRoleInstanceEntitiesAfter = roleInstanceRepository.saveAllAndFlush(serviceRoleInstanceEntities);

                // 为每个角色分布的节点，都生成service RoleUi地址
                List<ServiceRoleInstanceWebuisEntity> roleInstanceWebuisEntities = serviceRoleInstanceEntitiesAfter.stream().map(new Function<ServiceRoleInstanceEntity, ServiceRoleInstanceWebuisEntity>() {
                    @Override
                    public ServiceRoleInstanceWebuisEntity apply(ServiceRoleInstanceEntity serviceRoleInstanceEntity) {
                        String roleLinkExpression = stackServiceRoleEntity.getLinkExpression();
                        // 持久化service Role UI信息
                        ServiceRoleInstanceWebuisEntity serviceRoleInstanceWebuisEntity = new ServiceRoleInstanceWebuisEntity();
                        serviceRoleInstanceWebuisEntity.setName("UI地址");
                        serviceRoleInstanceWebuisEntity.setServiceInstanceId(serviceInstanceEntityId);
                        serviceRoleInstanceWebuisEntity.setServiceRoleInstanceId(serviceRoleInstanceEntity.getServiceInstanceId());
                        serviceRoleInstanceWebuisEntity.setWebHostUrl(roleLinkExpression);
                        serviceRoleInstanceWebuisEntity.setWebIpUrl(roleLinkExpression);
                        return serviceRoleInstanceWebuisEntity;
                    }
                }).collect(Collectors.toList());

                // 批量持久化role web ui
                roleInstanceWebuisRepository.saveAll(roleInstanceWebuisEntities);


            }

            List<InitServiceRequest.InitServicePresetConf> presetConfList = serviceInfo.getPresetConfList();
            List<ServiceInstanceConfigEntity> serviceInstanceConfigEntities = presetConfList.stream().map(new Function<InitServiceRequest.InitServicePresetConf, ServiceInstanceConfigEntity>() {
                @Override
                public ServiceInstanceConfigEntity apply(InitServiceRequest.InitServicePresetConf initServicePresetConf) {
                    ServiceInstanceConfigEntity serviceInstanceConfigEntity = new ServiceInstanceConfigEntity();
                    BeanUtil.copyProperties(initServicePresetConf,serviceInstanceConfigEntity);
                    serviceInstanceConfigEntity.setUpdateTime(new Date());
                    serviceInstanceConfigEntity.setCreateTime(new Date());
                    serviceInstanceConfigEntity.setServiceId(serviceInstanceEntityId);
                    serviceInstanceConfigEntity.setUserId(AdminUserId);
                    return serviceInstanceConfigEntity;
                }
            }).collect(Collectors.toList());

            // 批量持久化service Conf信息
            serviceInstanceConfigRepository.saveAll(serviceInstanceConfigEntities);

            // todo 根据需要安装的服务在实例表中找到依赖的服务id，并更新service信息

            // todo 生成新增服务command和调用workflow

        }


        return ResultDTO.success(null);
    }
}
