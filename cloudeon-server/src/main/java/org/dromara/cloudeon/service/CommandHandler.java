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

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.cloudeon.config.CloudeonConfigProp;
import org.dromara.cloudeon.dao.*;
import org.dromara.cloudeon.dto.NodeInfo;
import org.dromara.cloudeon.dto.ServiceTaskGroupType;
import org.dromara.cloudeon.dto.SpecRoleHost;
import org.dromara.cloudeon.dto.TaskModel;
import org.dromara.cloudeon.entity.*;
import org.dromara.cloudeon.enums.CommandState;
import org.dromara.cloudeon.enums.CommandType;
import org.dromara.cloudeon.enums.TaskGroupType;
import org.dromara.cloudeon.enums.TaskType;
import org.dromara.cloudeon.processor.TaskParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.dromara.cloudeon.utils.Constant.AdminUserId;

@Service
public class CommandHandler {
    @Resource
    private CloudeonConfigProp cloudeonConfigProp;
    @Resource
    private ClusterNodeRepository clusterNodeRepository;
    @Resource
    private ServiceRoleInstanceRepository roleInstanceRepository;
    @Resource
    private StackServiceRoleRepository stackServiceRoleRepository;
    @Resource
    private StackServiceRepository stackServiceRepository;
    @Resource
    private CommandRepository commandRepository;
    @Resource
    private CommandTaskRepository commandTaskRepository;

    @Transactional(rollbackFor = Exception.class)
    public Integer buildServiceCommand(List<ServiceInstanceEntity> serviceInstanceEntities,
                                       Integer ClusterId, CommandType commandType) {
        return buildInternalCommand(serviceInstanceEntities, Lists.newArrayList(), ClusterId, commandType);
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer buildRoleCommand(List<ServiceInstanceEntity> serviceInstanceEntities, List<ServiceRoleInstanceEntity> spceRoleInstanceEntities,
                                    Integer ClusterId, CommandType commandType) {
        return buildInternalCommand(serviceInstanceEntities, spceRoleInstanceEntities, ClusterId, commandType);
    }

    private Integer buildInternalCommand(List<ServiceInstanceEntity> serviceInstanceEntities, List<ServiceRoleInstanceEntity> spceRoleInstanceEntities,
                                         Integer ClusterId, CommandType commandType) {

        // 创建 command
        CommandEntity commandEntity = new CommandEntity();
        commandEntity.setCommandState(CommandState.RUNNING);
        commandEntity.setCurrentProgress(0);
        commandEntity.setClusterId(ClusterId);
        commandEntity.setName(commandType.getDesc());
        commandEntity.setSubmitTime(new Date());
        commandEntity.setOperateUserId(AdminUserId);
        commandEntity.setType(commandType);
        // 持久化 command
        commandRepository.saveAndFlush(commandEntity);

        // todo 根据服务依赖进行调整顺序
        //  遍历command 涉及的服务实例
        AtomicInteger taskModelId = new AtomicInteger(1);
        for (ServiceInstanceEntity serviceInstanceEntity : serviceInstanceEntities) {
            StackServiceEntity stackServiceEntity = stackServiceRepository.findById(serviceInstanceEntity.getStackServiceId()).get();
            // 生成TaskGroupTypes
            List<TaskGroupType> taskGroupTypes = buildTaskGroupTypes(commandType, stackServiceEntity.getName());

            LinkedHashMap<String, List<NodeInfo>> roleHostMaps = new LinkedHashMap<>();
            // 查出该服务有的角色
            List<StackServiceRoleEntity> stackServiceRoleEntities = stackServiceRoleRepository.findByServiceIdOrderBySortNum(serviceInstanceEntity.getStackServiceId());
            // 遍历每个角色
            for (StackServiceRoleEntity stackServiceRoleEntity : stackServiceRoleEntities) {
                // 查出该角色的各个节点实例
                List<ServiceRoleInstanceEntity> roleInstanceEntities = roleInstanceRepository.findByServiceInstanceIdAndStackServiceRoleId(serviceInstanceEntity.getId(), stackServiceRoleEntity.getId());

                List<NodeInfo> nodeInfos = roleInstanceEntities.stream().map(new Function<ServiceRoleInstanceEntity, NodeInfo>() {
                    @Override
                    public NodeInfo apply(ServiceRoleInstanceEntity serviceRoleInstanceEntity) {
                        ClusterNodeEntity clusterNodeEntity = clusterNodeRepository.findById(serviceRoleInstanceEntity.getNodeId()).get();
                        return NodeInfo.builder().hostName(clusterNodeEntity.getHostname()).ip(clusterNodeEntity.getIp()).build();
                    }
                }).collect(Collectors.toList());
                roleHostMaps.put(stackServiceRoleEntity.getName(), nodeInfos);
            }


            ServiceTaskGroupType serviceTaskGroupType = ServiceTaskGroupType.builder()
                    .serviceName(serviceInstanceEntity.getServiceName())
                    .stackServiceName(stackServiceEntity.getName())
                    .taskGroupTypes(taskGroupTypes)
                    .roleHostMaps(roleHostMaps).build();

            List<TaskModel> taskModels;
            // 确定是角色相关指令还是服务指令
            if (!spceRoleInstanceEntities.isEmpty()) {
                List<SpecRoleHost> specRoleHosts = spceRoleInstanceEntities.stream().map(new Function<ServiceRoleInstanceEntity, SpecRoleHost>() {
                    @Override
                    public SpecRoleHost apply(ServiceRoleInstanceEntity serviceRoleInstanceEntity) {
                        ClusterNodeEntity clusterNodeEntity = clusterNodeRepository.findById(serviceRoleInstanceEntity.getNodeId()).get();
                        SpecRoleHost specRoleHost = SpecRoleHost.builder()
                                .roleName(serviceRoleInstanceEntity.getServiceRoleName())
                                .hostName(clusterNodeEntity.getHostname())
                                .build();
                        return specRoleHost;
                    }
                }).collect(Collectors.toList());
                taskModels = buildTaskModels(serviceTaskGroupType, specRoleHosts);
            } else {
                taskModels = buildTaskModels(serviceTaskGroupType);
            }
            List<TaskModel> models = taskModels.stream().map(e -> {
                e.setTaskSortNum(taskModelId.getAndIncrement());
                return e;
            }).collect(Collectors.toList());

            // 根据taskModels生成command task，并持久化数据库
            saveCommandTask2DB(commandEntity, serviceInstanceEntity, models);
        }

        return commandEntity.getId();
    }

    private void saveCommandTask2DB(CommandEntity commandEntity, ServiceInstanceEntity serviceInstanceEntity, List<TaskModel> models) {
        for (TaskModel taskModel : models) {
            CommandTaskEntity commandTaskEntity = new CommandTaskEntity();
            commandTaskEntity.setCommandId(commandEntity.getId());
            commandTaskEntity.setProgress(0);
            commandTaskEntity.setProcessorClassName(taskModel.getProcessorClassName());
            commandTaskEntity.setTaskName(taskModel.getTaskName());
            commandTaskEntity.setTaskShowSortNum(taskModel.getTaskSortNum());
            commandTaskEntity.setCommandState(CommandState.WAITING);
            commandTaskEntity.setServiceInstanceId(serviceInstanceEntity.getId());
            commandTaskEntity.setServiceInstanceName(serviceInstanceEntity.getServiceName());
            commandTaskRepository.saveAndFlush(commandTaskEntity);
            // 更新日志路径
            commandTaskEntity.setTaskLogPath(cloudeonConfigProp.getTaskLog() + File.separator + commandEntity.getId() + "-" + commandTaskEntity.getId() + ".log");
            // 更新任务参数
            TaskParam taskParam = buildTaskParam(taskModel, commandEntity, serviceInstanceEntity, commandTaskEntity);
            commandTaskEntity.setTaskParam(JSONObject.toJSONString(taskParam));
            commandTaskRepository.saveAndFlush(commandTaskEntity);
        }
    }

    private TaskParam buildTaskParam(TaskModel taskModel, CommandEntity commandEntity,
                                     ServiceInstanceEntity serviceInstanceEntity, CommandTaskEntity commandTaskEntity) {
        TaskParam taskParam = new TaskParam();
        BeanUtil.copyProperties(taskModel, taskParam);
        taskParam.setCommandTaskId(commandTaskEntity.getId());
        taskParam.setCommandId(commandEntity.getId());
        taskParam.setServiceInstanceId(serviceInstanceEntity.getId());
        taskParam.setServiceInstanceName(serviceInstanceEntity.getServiceName());
        taskParam.setStackServiceId(serviceInstanceEntity.getStackServiceId());
        return taskParam;
    }

    /**
     * 根据框架服务和指令，生成对应服务的TaskGroupType集合
     *
     * @param commandType
     * @param stackServiceName
     * @return
     */
    public List<TaskGroupType> buildTaskGroupTypes(CommandType commandType, String stackServiceName) {
        switch (commandType) {
            case INSTALL_SERVICE:
                List<TaskGroupType> taskGroupTypes = Lists.newArrayList();
                taskGroupTypes.add(TaskGroupType.CONFIG_SERVICE);
                taskGroupTypes.add(TaskGroupType.TAG_AND_START_K8S_SERVICE);
                taskGroupTypes.add(TaskGroupType.UPDATE_SERVICE_STATE);
                return taskGroupTypes;
            case START_SERVICE:
                return Lists.newArrayList(TaskGroupType.TAG_AND_START_K8S_SERVICE, TaskGroupType.UPDATE_SERVICE_STATE);
            case RESTART_SERVICE:
                return Lists.newArrayList(
                        TaskGroupType.CANCEL_TAG_AND_STOP_K8S_SERVICE,
                        TaskGroupType.DELETE_SERVICE,
                        TaskGroupType.CONFIG_SERVICE,
                        TaskGroupType.TAG_AND_START_K8S_SERVICE,
                        TaskGroupType.UPDATE_SERVICE_STATE);
            case STOP_SERVICE:
                return Lists.newArrayList(TaskGroupType.CANCEL_TAG_AND_STOP_K8S_SERVICE,TaskGroupType.UPDATE_SERVICE_STATE);
            case DELETE_SERVICE:
                return Lists.newArrayList(TaskGroupType.CANCEL_TAG_AND_STOP_K8S_SERVICE, TaskGroupType.DELETE_SERVICE, TaskGroupType.DELETE_DB_DATA);
            case UPGRADE_SERVICE_CONFIG:
                return Lists.newArrayList(TaskGroupType.CONFIG_SERVICE);
            case STOP_ROLE:
                return Lists.newArrayList(TaskGroupType.STOP_ROLE);
            case START_ROLE:
                return Lists.newArrayList(TaskGroupType.START_ROLE);
            default:
                return null;

        }

    }

    public List<TaskModel> buildTaskModels(ServiceTaskGroupType serviceTaskGroupType) {
        return buildTaskModels(serviceTaskGroupType, null);
    }

    /**
     * 生成单个服务的所有TaskModel
     */
    public List<TaskModel> buildTaskModels(ServiceTaskGroupType serviceTaskGroupType, List<SpecRoleHost> specRoleHosts) {

        List<TaskModel> taskModelList = serviceTaskGroupType.getTaskGroupTypes().stream().flatMap(new Function<TaskGroupType, Stream<TaskModel>>() {
            @Override
            public Stream<TaskModel> apply(TaskGroupType taskGroupType) {
                // 判断taskGroup是否需要按角色迭代：如JN、NN
                Stream<TaskModel> taskModelStream;
                if (taskGroupType.isRoleLoop()) {
                    taskModelStream = serviceTaskGroupType.getRoleHostMaps().keySet().stream().filter(e -> {
                        if (specRoleHosts == null || specRoleHosts.isEmpty()) {
                            return true;
                        } else {
                            return specRoleHosts.stream().map(SpecRoleHost::getRoleName).collect(Collectors.toList()).contains(e);
                        }

                    }).flatMap(roleName -> {
                        return taskGroupType.getTaskTypes().stream().flatMap(new Function<TaskType, Stream<TaskModel>>() {
                            @Override
                            public Stream<TaskModel> apply(TaskType taskType) {
                                if (taskType.isHostLoop()) {
                                    Stream<TaskModel> taskModelStream = serviceTaskGroupType.getRoleHostMaps().get(roleName).stream().filter(nodeInfo -> {
                                        if (specRoleHosts == null || specRoleHosts.isEmpty()) {
                                            return true;
                                        } else {
                                            return specRoleHosts.stream().map(SpecRoleHost::getHostName).collect(Collectors.toList()).contains(nodeInfo.getHostName());
                                        }
                                    }).map(new Function<NodeInfo, TaskModel>() {
                                        @Override
                                        public TaskModel apply(NodeInfo nodeInfo) {
                                            return TaskModel.builder()
                                                    .processorClassName(taskType.getProcessorClass())
                                                    .taskName(roleName + " :" + taskType.getName() + " (" + nodeInfo.getHostName() + ")")
                                                    .hostName(nodeInfo.getHostName())
                                                    .ip(nodeInfo.getIp())
                                                    .roleName(roleName)
                                                    .build();
                                        }
                                    });
                                    return taskModelStream;
                                } else {
                                    return Stream.of(TaskModel.builder()
                                            .processorClassName(taskType.getProcessorClass())
                                            .roleName(roleName)
                                            .taskName(roleName + " :" + taskType.getName())
                                            .build());
                                }
                            }
                        });
                    });
                } else {
                    taskModelStream = taskGroupType.getTaskTypes().stream().map(new Function<TaskType, TaskModel>() {
                        @Override
                        public TaskModel apply(TaskType taskType) {
                            return TaskModel.builder().processorClassName(taskType.getProcessorClass()).taskName(taskType.getName()).build();
                        }
                    });
                }
                return taskModelStream;

            }
        }).collect(Collectors.toList());

        return taskModelList;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskSample {
        private String taskNameTemplate;
        private Boolean isRoleSpec;


    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StepSample {
        private String stepNameTemplate;

        private List<TaskSample> taskSamples;


    }

}
