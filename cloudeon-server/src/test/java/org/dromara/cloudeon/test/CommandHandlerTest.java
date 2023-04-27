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
package org.dromara.cloudeon.test;

import org.dromara.cloudeon.dto.NodeInfo;
import org.dromara.cloudeon.dto.ServiceTaskGroupType;
import org.dromara.cloudeon.dto.TaskModel;
import org.dromara.cloudeon.service.CommandHandler;
import org.dromara.cloudeon.enums.CommandType;
import org.dromara.cloudeon.enums.TaskGroupType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
public class CommandHandlerTest {

    @Test
    public void getAllTaskTypes(){
        CommandHandler commandBootstrapHandler = new CommandHandler();

        List<String> installServiceNames = Lists.newArrayList("HDFS", "ZOOKEEPER", "YARN");
        for (String installServiceName : installServiceNames) {
            System.out.println("========= "+installServiceName+" =============");

            commandBootstrapHandler.buildTaskGroupTypes(CommandType.INSTALL_SERVICE, installServiceName)
                    .stream().flatMap(new Function<TaskGroupType, Stream<?>>() {
                        @Override
                        public Stream<?> apply(TaskGroupType taskGroupType) {
                            return taskGroupType.getTaskTypes().stream().map(taskType -> taskType.getName());
                        }
                    }).forEach(System.out::println);


        }



    }

    @Test
    public void testTaskModelGenerate() {
        CommandHandler commandBootstrapHandler = new CommandHandler();
//        CommandType commandType = CommandType.START_SERVICE;
        CommandType commandType = CommandType.INSTALL_SERVICE;
//        CommandType commandType = CommandType.STOP_SERVICE;

        // 按角色启动顺序放入map
        LinkedHashMap<String, List<NodeInfo>> roles = new LinkedHashMap<>();
        roles.put("Journal Node", Lists.newArrayList(NodeInfo.builder().hostName("node001").build(),NodeInfo.builder().hostName("node002").build(),NodeInfo.builder().hostName("node003").build()));
        roles.put("Name Node", Lists.newArrayList(NodeInfo.builder().hostName("node004").build(),NodeInfo.builder().hostName("node002").build()));
        roles.put("Data Node", Lists.newArrayList(NodeInfo.builder().hostName("node002").build(),NodeInfo.builder().hostName("node004").build(),NodeInfo.builder().hostName("node006").build()));
        roles.put("HttpFs", Lists.newArrayList(NodeInfo.builder().hostName("node004").build()));
        String hdfsStackServiceName = "HDFS";
        ServiceTaskGroupType hdfsServiceTaskGroupType = ServiceTaskGroupType.builder().serviceName("HDFS1").stackServiceName(hdfsStackServiceName).roleHostMaps(roles)
                .taskGroupTypes(commandBootstrapHandler.buildTaskGroupTypes(commandType, hdfsStackServiceName)).build();

        LinkedHashMap<String, List<NodeInfo>> yanRoles = new LinkedHashMap<>();
        yanRoles.put("Resource Manager", Lists.newArrayList(NodeInfo.builder().hostName("node001").build(),NodeInfo.builder().hostName("node002").build()));
        yanRoles.put("Node Manager", Lists.newArrayList(NodeInfo.builder().hostName("node002").build(),NodeInfo.builder().hostName("node003").build(),NodeInfo.builder().hostName("node006").build()));
        yanRoles.put("Timeline Server", Lists.newArrayList(NodeInfo.builder().hostName("node002").build()));
        String yarnStackServiceName = "YARN";
        ServiceTaskGroupType yarnServiceTaskGroupType = ServiceTaskGroupType.builder().serviceName("YARN1").stackServiceName(yarnStackServiceName).roleHostMaps(yanRoles)
                .taskGroupTypes(commandBootstrapHandler.buildTaskGroupTypes(commandType, yarnStackServiceName)).build();

        LinkedHashMap<String, List<NodeInfo>> zkRoles = new LinkedHashMap<>();
        zkRoles.put("Zookeeper Server", Lists.newArrayList(NodeInfo.builder().hostName("node002").build(),NodeInfo.builder().hostName("node004").build(),NodeInfo.builder().hostName("node006").build()));
        String zookeeperStackServiceName = "ZOOKEEPER";
        ServiceTaskGroupType zkServiceTaskGroupType = ServiceTaskGroupType.builder().serviceName("ZOOKEEPER1").stackServiceName(zookeeperStackServiceName).roleHostMaps(zkRoles)
                .taskGroupTypes(commandBootstrapHandler.buildTaskGroupTypes(commandType, zookeeperStackServiceName)).build();

        List<TaskModel> zkTaskModels = commandBootstrapHandler.buildTaskModels(zkServiceTaskGroupType);
        List<TaskModel> yarnTaskModels = commandBootstrapHandler.buildTaskModels(yarnServiceTaskGroupType);
        List<TaskModel> hdfsTaskModels = commandBootstrapHandler.buildTaskModels(hdfsServiceTaskGroupType);

        for (Map.Entry<String, List<TaskModel>> stringListEntry : ImmutableMap.of(hdfsStackServiceName, hdfsTaskModels, zookeeperStackServiceName, zkTaskModels, yarnStackServiceName, yarnTaskModels).entrySet()) {
            System.out.println("========= "+stringListEntry.getKey()+" =============");
            stringListEntry.getValue().forEach(e-> System.out.println(e.getTaskSortNum()+" : "+e.getTaskName()));

        }
    }
}
