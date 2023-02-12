package com.data.udh.processor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.data.udh.dao.CommandRepository;
import com.data.udh.dao.CommandTaskRepository;
import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.CommandTaskEntity;
import com.data.udh.utils.CommandState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class CommandExecuteActor extends AbstractActor {


    public static Props props() {
        return Props.create(CommandExecuteActor.class, () -> new CommandExecuteActor());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, this::onReceiveCommandId)
                .matchAny(obj -> log.warn("[ProcessorTrackerActor] receive unknown request: {}.", obj))
                .build();
    }


    /**
     * 处理请求
     */
    private void onReceiveCommandId(Integer commandId) {
        log.info("CommandExecuteActor 接收到指令id为"+commandId+"...........");
        CommandTaskRepository commandTaskRepository = SpringUtil.getBean(CommandTaskRepository.class);
        CommandRepository commandRepository = SpringUtil.getBean(CommandRepository.class);


        List<CommandTaskEntity> taskEntityList = commandTaskRepository.findByCommandId(commandId);
        // 根据任务列表生成runnable
        List<Runnable> runnableList = taskEntityList.stream().map(new Function<CommandTaskEntity, Runnable>() {
            @Override
            public Runnable apply(CommandTaskEntity commandTaskEntity) {
                // 反射生成任务对象
                BaseUdhTask o = ReflectUtil.newInstance(commandTaskEntity.getProcessorClassName());
                // 填充任务参数
                o.setTaskParam(JSONObject.parseObject(commandTaskEntity.getTaskParam(),TaskParam.class));
                return o;
            }
        }).collect(Collectors.toList());

        // 根据command task生成flow
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                log.info("记录command开始执行时间。。。");
                // 更新command状态
                CommandEntity updateCommandEntity = commandRepository.findById(commandId).get();
                updateCommandEntity.setCommandState(CommandState.RUNNING);
                updateCommandEntity.setStartTime(new Date());
                commandRepository.saveAndFlush(updateCommandEntity);
            }
        });

        for (Runnable runnable : runnableList) {
            completableFuture =completableFuture.thenRunAsync(runnable);
        }



        completableFuture.whenComplete(new BiConsumer<Void, Throwable>() {
            @Override
            public void accept(Void unused, Throwable throwable) {
                if (throwable == null) {
                    log.info("记录command正常结束时间。。。");
                    // 更新command状态
                    CommandEntity updateCommandEntity = commandRepository.findById(commandId).get();
                    updateCommandEntity.setCommandState(CommandState.SUCCESS);
                    updateCommandEntity.setEndTime(new Date());
                    commandRepository.saveAndFlush(updateCommandEntity);
                }
            }
        });

        completableFuture.exceptionally(new Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable throwable) {
                throwable.printStackTrace();
                log.info("调度程序发现异常：" + throwable.getMessage());
                log.info("记录command异常结束时间。。。");
                // 更新command状态
                CommandEntity updateCommandEntity = commandRepository.findById(commandId).get();
                updateCommandEntity.setCommandState(CommandState.ERROR);
                updateCommandEntity.setEndTime(new Date());
                commandRepository.saveAndFlush(updateCommandEntity);
                return null;
            }
        });
    }

}
