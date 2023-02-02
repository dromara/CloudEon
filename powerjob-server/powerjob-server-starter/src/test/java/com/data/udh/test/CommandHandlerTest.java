package com.data.udh.test;

import com.data.udh.dto.ServiceRoleInstance;
import com.data.udh.service.CommandHandler;
import com.data.udh.utils.CommandType;
import com.data.udh.utils.TaskGroupType;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
public class CommandHandlerTest {

    @Test
    public void getAllTaskTypes(){
        CommandHandler commandBootstrapHandler = new CommandHandler();

        commandBootstrapHandler.buildTaskGroupTypes(CommandType.INSTALL_SERVICE, Lists.newArrayList("HDFS","ZOOKEEPER"))
                .stream().flatMap(new Function<TaskGroupType, Stream<?>>() {
                    @Override
                    public Stream<?> apply(TaskGroupType taskGroupType) {
                        return taskGroupType.getTaskTypes().stream().map(taskType -> taskType.getName());
                    }
                }).forEach(System.out::println);



    }

    @Test
    public void getTaskModels() {
        CommandHandler commandBootstrapHandler = new CommandHandler();
        LinkedHashMap<String, List<ServiceRoleInstance.NodeInfo>> roles = new LinkedHashMap<>();
        // 按角色启动顺序放入map
        roles.put("Journal Node", Lists.newArrayList(ServiceRoleInstance.NodeInfo.builder().hostName("node001").build(),ServiceRoleInstance.NodeInfo.builder().hostName("node002").build(),ServiceRoleInstance.NodeInfo.builder().hostName("node003").build()));
        roles.put("Name Node", Lists.newArrayList(ServiceRoleInstance.NodeInfo.builder().hostName("node004").build(),ServiceRoleInstance.NodeInfo.builder().hostName("node002").build()));
        roles.put("Data Node", Lists.newArrayList(ServiceRoleInstance.NodeInfo.builder().hostName("node2").build(),ServiceRoleInstance.NodeInfo.builder().hostName("node004").build(),ServiceRoleInstance.NodeInfo.builder().hostName("node006").build()));
        ServiceRoleInstance serviceRoleInstance = ServiceRoleInstance.builder().serviceName("HDFS1").stackServiceName("HDFS").roleHostMaps(roles).build();
        List<TaskGroupType> taskGroupTypes = commandBootstrapHandler.buildTaskGroupTypes(CommandType.INSTALL_SERVICE, Lists.newArrayList("HDFS"));
        commandBootstrapHandler.buildTaskModels(taskGroupTypes, serviceRoleInstance).stream().map(e->e.getTaskName()).forEach(System.out::println);
    }
}
