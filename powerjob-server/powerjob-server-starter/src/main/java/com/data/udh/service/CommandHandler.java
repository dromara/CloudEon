package com.data.udh.service;

import com.data.udh.dto.NodeInfo;
import com.data.udh.dto.ServiceTaskGroupType;
import com.data.udh.dto.TaskModel;
import com.data.udh.utils.CommandType;
import com.data.udh.utils.TaskGroupType;
import com.data.udh.utils.TaskType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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
                taskGroupTypes.add(TaskGroupType.PULL_IMAGE_FROM_REGISTRY);
                taskGroupTypes.add(TaskGroupType.INSTALL_SERVICE);
                taskGroupTypes.add(TaskGroupType.CONFIG_SERVICE);
                if (stackServiceName.equals("HDFS")) {
                    taskGroupTypes.add(TaskGroupType.INIT_HDFS);
                }
                if (stackServiceName.equals("YARN")) {
                    taskGroupTypes.add(TaskGroupType.INIT_YARN);
                }

                taskGroupTypes.add(TaskGroupType.TAG_AND_START_K8S_SERVICE);

                if (stackServiceName.equals("DORIS")) {
                    taskGroupTypes.add(TaskGroupType.INIT_DORIS);
                }
                return taskGroupTypes;
            case START_SERVICE:
                return Lists.newArrayList(TaskGroupType.TAG_AND_START_K8S_SERVICE);
            case RESTART_SERVICE:
                return Lists.newArrayList(TaskGroupType.CANCEL_TAG_AND_STOP_K8S_SERVICE, TaskGroupType.TAG_AND_START_K8S_SERVICE);

            case STOP_SERVICE:
                return Lists.newArrayList(TaskGroupType.CANCEL_TAG_AND_STOP_K8S_SERVICE);
            default:
                return null;

        }

    }

    /**
     * 生成单个服务的所有TaskModel
     */
    public List<TaskModel> buildTaskModels(ServiceTaskGroupType serviceTaskGroupType) {

        List<TaskModel> taskModelList = serviceTaskGroupType.getTaskGroupTypes().stream().flatMap(new Function<TaskGroupType, Stream<TaskModel>>() {
            @Override
            public Stream<TaskModel> apply(TaskGroupType taskGroupType) {
                // 判断taskGroup是否需要按角色迭代：如JN、NN
                Stream<TaskModel> taskModelStream;
                if (taskGroupType.isRoleLoop()) {
                    taskModelStream = serviceTaskGroupType.getRoleHostMaps().keySet().stream().flatMap(roleName -> {
                        return taskGroupType.getTaskTypes().stream().flatMap(new Function<TaskType, Stream<TaskModel>>() {
                            @Override
                            public Stream<TaskModel> apply(TaskType taskType) {
                                if (taskType.isHostLoop()) {
                                    Stream<TaskModel> taskModelStream = serviceTaskGroupType.getRoleHostMaps().get(roleName).stream().map(new Function<NodeInfo, TaskModel>() {
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
