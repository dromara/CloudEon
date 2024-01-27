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
package org.dromara.cloudeon.verticle;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import io.vertx.core.AbstractVerticle;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.tascalate.concurrent.CompletableTask;
import net.tascalate.concurrent.Promise;
import org.dromara.cloudeon.dao.CommandRepository;
import org.dromara.cloudeon.dao.CommandTaskRepository;
import org.dromara.cloudeon.entity.CommandEntity;
import org.dromara.cloudeon.entity.CommandTaskEntity;
import org.dromara.cloudeon.enums.CommandState;
import org.dromara.cloudeon.processor.BaseCloudeonTask;
import org.dromara.cloudeon.processor.TaskParam;
import org.dromara.cloudeon.utils.Constant;
import org.dromara.cloudeon.utils.FutureExceptionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class CommandExecuteVerticle extends AbstractVerticle {

    private Map<Integer, Map<Integer, Future<Void>>> commandFutrueMap = new ConcurrentHashMap<>();
    private CommandTaskRepository commandTaskRepository = SpringUtil.getBean(CommandTaskRepository.class);
    private CommandRepository commandRepository = SpringUtil.getBean(CommandRepository.class);
    private ThreadPoolExecutor threadPoolExecutor = ThreadUtil.newExecutor(5, 100);

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(Constant.VERTX_COMMAND_ADDRESS, message -> {
            Integer commandId = (Integer) message.body();
            // 1. 从message中获取command
            vertx.executeBlocking(future -> {
                onReceiveCommandId(commandId);
            }, false, res -> {
                log.info("CommandExecuteVerticle commandId:{} 执行完毕", commandId);
            });
        });
        vertx.eventBus().consumer(Constant.VERTX_STOP_COMMAND_ADDRESS, message -> {
            Integer commandId = (Integer) message.body();
            vertx.executeBlocking(future -> {
                onStopCommandId(commandId);
            }, false, res -> {
                log.info("CommandExecuteVerticle 停止任务,commandId:{} 执行完毕", commandId);
            });
        });
        vertx.eventBus().consumer(Constant.VERTX_RETRY_COMMAND_ADDRESS, message -> {
            Integer commandId = (Integer) message.body();
            vertx.executeBlocking(future -> {
                onRetryCommandId(commandId);
            }, false, res -> {
                log.info("CommandExecuteVerticle 重试任务,commandId:{} 执行完毕", commandId);
            });
        });
    }

    private void onRetryCommandId(Integer commandId) {
        log.info("CommandExecuteActor 重试指令id为" + commandId + "...........");

        CommandEntity commandEntity = commandRepository.findById(commandId).get();
        CommandState commandState = commandEntity.getCommandState();
        // 非 停止或错误 状态无法执行重试
        if (!(commandState.equals(CommandState.STOPPED) || commandState.equals(CommandState.ERROR))) {
            log.info("指令状态为" + commandState + "，重试无效");
            return;
        }

        List<CommandTaskEntity> taskEntityList = commandTaskRepository.findByCommandId(commandId);
        // 移除所有SUCCESS状态的子任务
        taskEntityList = taskEntityList.stream().filter(task ->
                !task.getCommandState().equals(CommandState.SUCCESS)).collect(Collectors.toList());
        if (taskEntityList.isEmpty()) {
            log.info("没有需要重试的子任务");
            return;
        }
        executeTaskEntityList(commandId, taskEntityList);
    }

    private void onStopCommandId(Integer commandId) {
        log.info("CommandExecuteActor 停止指令id为" + commandId);
        CommandTaskRepository commandTaskRepository = SpringUtil.getBean(CommandTaskRepository.class);
        CommandRepository commandRepository = SpringUtil.getBean(CommandRepository.class);
        CommandEntity commandEntity = commandRepository.findById(commandId).get();
        CommandState commandState = commandEntity.getCommandState();
        if (commandState.isEnd()) {
            log.info("指令已经结束");
            return;
        }
        commandEntity.setCommandState(CommandState.STOPPING);
        commandRepository.saveAndFlush(commandEntity);
        if (commandState.equals(CommandState.WAITING)) {
            log.info("指令状态为等待，正在执行停止");
            while (!commandRepository.findById(commandId).get().getCommandState().isEnd()) {
                ThreadUtil.sleep(1_000);
            }
            log.info("指令已经结束");
            return;
        }
        // 当状态是running
        List<CommandTaskEntity> taskEntityList = commandTaskRepository.findByCommandId(commandId);
        Optional<CommandTaskEntity> firstRunningCommandOpt = taskEntityList.stream().filter(command -> command.getCommandState().equals(CommandState.RUNNING)).findFirst();
        if (!firstRunningCommandOpt.isPresent()) {
            log.info("没有正在运行的指令，直接结束");
            return;
        }
        CommandTaskEntity runningCommandTaskEntity = firstRunningCommandOpt.get();
        Integer runningCommandTaskEntityId = runningCommandTaskEntity.getId();
        Future<Void> future = commandFutrueMap.get(commandId).get(runningCommandTaskEntityId);
        future.cancel(true);
        log.info("取消正在执行的命令");
    }

    /**
     * 处理请求
     */
    private void onReceiveCommandId(Integer commandId) {
        log.info("CommandExecuteActor 接收到指令id为" + commandId + "...........");
        CommandTaskRepository commandTaskRepository = SpringUtil.getBean(CommandTaskRepository.class);
        CommandRepository commandRepository = SpringUtil.getBean(CommandRepository.class);

        List<CommandTaskEntity> taskEntityList = null;
        // 解决扫描不到任务直接结束command的问题
        while (true) {
            taskEntityList = commandTaskRepository.findByCommandId(commandId);
            int size = taskEntityList.size();
            log.info("根据commandId {} 找出task数量：{}", commandId, size);
            if (size > 0) break;
            ThreadUtil.sleep(2_000);
        }
        executeTaskEntityList(commandId, taskEntityList);
    }

    private void executeTaskEntityList(Integer commandId, List<CommandTaskEntity> taskEntityList) {
        // 根据任务列表生成runnable
        Map<Integer, BaseCloudeonTask> taskMap = taskEntityList.stream().collect(
                Collectors.toMap(CommandTaskEntity::getId,
                        commandTaskEntity -> {
                            // 反射生成任务对象
                            BaseCloudeonTask o = ReflectUtil.newInstance(commandTaskEntity.getProcessorClassName());
                            // 填充任务参数
                            o.setTaskParam(JSONObject.parseObject(commandTaskEntity.getTaskParam(), TaskParam.class));
                            return o;
                        },
                        // 合并函数，用于处理重复的 key
                        (existing, replacement) -> existing,
                        // 构造最终的 Map 类型，LinkedHashMap 用于保持插入顺序
                        LinkedHashMap::new));
        // 根据command task生成flow
        // 使用 CompletableTask/Promise 而非CompletableFuture 以实现 cancel时中断线程
        Promise<Void> completableFuture = CompletableTask.runAsync(() -> {
            log.info("记录command开始执行时间。。。");
            // 更新command状态
            CommandEntity updateCommandEntity = commandRepository.findById(commandId).get();
            updateCommandEntity.setCommandState(CommandState.RUNNING);
            updateCommandEntity.setStartTime(new Date());
            commandRepository.saveAndFlush(updateCommandEntity);
        }, threadPoolExecutor);
        for (Map.Entry<Integer, BaseCloudeonTask> taskEntry : taskMap.entrySet()) {
            Promise<Void> taskCompletableFuture = completableFuture.thenRunAsync(taskEntry.getValue());
            commandFutrueMap.putIfAbsent(commandId, Maps.newConcurrentMap());
            commandFutrueMap.get(commandId).put(taskEntry.getKey(), taskCompletableFuture);
            completableFuture = taskCompletableFuture;
        }

        completableFuture.whenComplete((unused, throwable) -> {
            if (throwable == null) {
                log.info("记录command正常结束时间。。。");
                // 更新command状态
                CommandEntity updateCommandEntity = commandRepository.findById(commandId).get();
                updateCommandEntity.setCommandState(CommandState.SUCCESS);
                updateCommandEntity.setEndTime(new Date());
                commandRepository.saveAndFlush(updateCommandEntity);
                commandFutrueMap.remove(commandId);
            }
        });

        completableFuture.exceptionally(throwable -> {
            throwable = FutureExceptionUtils.extractRealException(throwable);
            log.info("调度程序发现异常：" + throwable.getMessage(), throwable);
            log.info("记录command异常结束时间。。。");
            // 更新command状态
            CommandEntity updateCommandEntity = commandRepository.findById(commandId).get();
            if (updateCommandEntity.getCommandState().equals(CommandState.STOPPING)) {
                updateCommandEntity.setCommandState(CommandState.STOPPED);
            } else {
                updateCommandEntity.setCommandState(CommandState.ERROR);
            }
            updateCommandEntity.setEndTime(new Date());
            commandRepository.saveAndFlush(updateCommandEntity);
            commandFutrueMap.remove(commandId);
            return null;
        });
    }

}
