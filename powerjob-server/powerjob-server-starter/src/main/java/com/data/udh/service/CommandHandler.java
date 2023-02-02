package com.data.udh.service;

import com.data.udh.dto.ServiceRoleInstance;
import com.data.udh.utils.CommandType;
import com.data.udh.utils.TaskGroupType;
import com.data.udh.utils.TaskType;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandHandler {



    public List<TaskGroupType> buildTaskGroupTypes(CommandType commandType, List<String> stackServiceNames) {
        List<TaskGroupType> allTaskGroupTypes = stackServiceNames.stream().flatMap(name -> {
            switch (commandType) {
                case INSTALL_SERVICE:
                    List<TaskGroupType> taskGroupTypes = Lists.newArrayList();
                    taskGroupTypes.add(TaskGroupType.PULL_IMAGE_FROM_REGISTRY);
                    taskGroupTypes.add(TaskGroupType.INSTALL_SERVICE);
                    taskGroupTypes.add(TaskGroupType.CONFIG_SERVICE);
                    if (name.equals("HDFS")) {
                        taskGroupTypes.add(TaskGroupType.INIT_HDFS);
                    }
                    taskGroupTypes.add(TaskGroupType.TAG_AND_START_K8S_SERVICE);
                    return taskGroupTypes.stream();
                case START_SERVICE:
                    return Lists.newArrayList(TaskGroupType.TAG_AND_START_K8S_SERVICE).stream();

                case STOP_SERVICE:
                    return Lists.newArrayList(TaskGroupType.CANCEL_TAG_AND_STOP_K8S_SERVICE).stream();
                default:
                    return null;

            }
        }).collect(Collectors.toList());
        return allTaskGroupTypes;
    }

    public List<TaskModel> buildTaskModels(List<TaskGroupType> taskGroupTypes, ServiceRoleInstance serviceRoleInstance) {

        List<TaskModel> taskModelList = taskGroupTypes.stream().flatMap(new Function<TaskGroupType, Stream<TaskModel>>() {
            @Override
            public Stream<TaskModel> apply(TaskGroupType taskGroupType) {
                // 判断taskGroup是否需要按角色迭代：如JN、NN
                Stream<TaskModel> taskModelStream;
                if (taskGroupType.isRoleLoop()) {
                    taskModelStream = serviceRoleInstance.getRoleHostMaps().keySet().stream().flatMap(roleName -> {
                        return taskGroupType.getTaskTypes().stream().flatMap(new Function<TaskType, Stream<TaskModel>>() {
                            @Override
                            public Stream<TaskModel> apply(TaskType taskType) {
                                if (taskType.isHostLoop()) {
                                    Stream<TaskModel> taskModelStream = serviceRoleInstance.getRoleHostMaps().get(roleName).stream().map(new Function<ServiceRoleInstance.NodeInfo, TaskModel>() {
                                        @Override
                                        public TaskModel apply(ServiceRoleInstance.NodeInfo nodeInfo) {
                                            return TaskModel.builder().taskName(roleName+" :"+taskType.getName()+" ("+nodeInfo.getHostName()+")").build();
                                        }
                                    });
                                    return taskModelStream;
                                } else {
                                    return Stream.of(TaskModel.builder().taskName(roleName+" :"+taskType.getName()).build());
                                }
                            }
                        });
                    });
                } else {
                    taskModelStream = taskGroupType.getTaskTypes().stream().map(new Function<TaskType, TaskModel>() {
                        @Override
                        public TaskModel apply(TaskType taskType) {
                            return TaskModel.builder().taskName(taskType.getName()).build();
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
    public static class TaskModel {
        private String taskName;


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
