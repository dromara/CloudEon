package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.dao.CommandTaskRepository;
import com.data.udh.entity.CommandTaskEntity;
import com.data.udh.utils.CommandState;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public abstract class BaseUdhTask implements Runnable {

    protected UdhTaskContext taskContext;
    protected CommandTaskRepository commandTaskRepository;

    private void init() {
        commandTaskRepository = SpringUtil.getBean(CommandTaskRepository.class);
        CommandTaskEntity commandTaskEntity = commandTaskRepository.findById(taskContext.commandTaskId).get();
        System.out.println("command task：" + taskContext.commandTaskId + " 开始, 记录到数据库");
        commandTaskEntity.setStartTime(new Date());
        commandTaskEntity.setCommandState(CommandState.RUNNING);
        commandTaskRepository.saveAndFlush(commandTaskEntity);
    }

    @Override
    public void run() {
        init();

        try {
            internalExecute();
        } catch (Exception e) {
            doWhenError(e);
        }
        doSleep(5000);
        after();

    }

    private void after() {
        System.out.println("command task：" + taskContext.commandTaskId + " 结束, 记录到数据库");
        CommandTaskEntity commandTaskEntity = commandTaskRepository.findById(taskContext.commandTaskId).get();
        commandTaskEntity.setEndTime(new Date());
        commandTaskEntity.setCommandState(CommandState.SUCCESS);
        commandTaskRepository.saveAndFlush(commandTaskEntity);
    }

    private void doWhenError(Exception e) {
        System.out.println(taskContext.commandTaskId + ":发生异常，处理异常。。。"+e.getMessage());
        CommandTaskEntity commandTaskEntity = commandTaskRepository.findById(taskContext.commandTaskId).get();
        commandTaskEntity.setEndTime(new Date());
        commandTaskEntity.setCommandState(CommandState.ERROR);
        commandTaskRepository.saveAndFlush(commandTaskEntity);
        throw new RuntimeException();
    }

    abstract public void internalExecute();


    private void doSleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}