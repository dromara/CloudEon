package com.data.udh.processor;

import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.dao.CommandTaskRepository;
import com.data.udh.entity.CommandTaskEntity;
import com.data.udh.utils.CommandState;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.smartboot.flow.core.EngineContext;
import org.smartboot.flow.core.executable.AbstractExecutable;

import java.util.Date;

@Data
@AllArgsConstructor
public abstract class BaseUdhTask implements Runnable {

    protected Integer commandTaskId;
    protected Integer commandId;
    protected Integer serviceInstanceId;


    @Override
    public void  run() {
        CommandTaskRepository commandTaskRepository = SpringUtil.getBean(CommandTaskRepository.class);
        CommandTaskEntity commandTaskEntity = commandTaskRepository.findById(commandTaskId).get();
        System.out.println("command task：" + commandTaskId + " 开始, 记录到数据库");
        commandTaskEntity.setStartTime(new Date());
        commandTaskEntity.setCommandState(CommandState.RUNNING);
        commandTaskRepository.saveAndFlush(commandTaskEntity);
        try {
            internalExecute();
        } catch (Exception e) {
            doWhenError();
        }
        doSleep(5000);
        System.out.println("command task：" + commandTaskId + " 结束, 记录到数据库");
        commandTaskEntity.setEndTime(new Date());
        commandTaskEntity.setCommandState(CommandState.SUCCESS);
        commandTaskRepository.saveAndFlush(commandTaskEntity);

    }

    private void doWhenError() {
        System.out.println(commandTaskId+":发生异常，处理异常。。。");
        throw new RuntimeException();
    }

    abstract public void internalExecute();


    private  void doSleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}