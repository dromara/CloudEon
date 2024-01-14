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

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.cloudeon.dto.NodeInfo;
import org.dromara.cloudeon.dto.ServiceTaskGroupType;
import org.dromara.cloudeon.dto.SpecRoleHost;
import org.dromara.cloudeon.dto.TaskModel;
import org.dromara.cloudeon.enums.CommandType;
import org.dromara.cloudeon.enums.TaskGroupType;
import org.dromara.cloudeon.enums.TaskType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CommandHandler {


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
