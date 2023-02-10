package com.data.udh.processor;

import ch.qos.logback.classic.ClassicConstants;
import cn.hutool.core.math.MathUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.data.udh.dao.CommandRepository;
import com.data.udh.dao.CommandTaskRepository;
import com.data.udh.entity.CommandEntity;
import com.data.udh.entity.CommandTaskEntity;
import com.data.udh.utils.CommandState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Date;
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseUdhTask implements Runnable {
    public static final String TASKID = "taskId";
    public static final String TASK_LOG_HOME = "TASK_LOG_HOME";

    protected UdhTaskContext taskContext;
    protected CommandTaskRepository commandTaskRepository;
    protected CommandRepository commandRepository;

    public BaseUdhTask(UdhTaskContext taskContext) {
        this.taskContext = taskContext;
    }

    private void init() {
        MDC.put(TASKID, (taskContext.commandId +"-"+ taskContext.getCommandTaskId()));

        commandTaskRepository = SpringUtil.getBean(CommandTaskRepository.class);
        log.info("command task：" + taskContext.commandTaskId + " 开始, 记录到数据库");
        CommandTaskEntity commandTaskEntity = commandTaskRepository.findById(taskContext.commandTaskId).get();
        commandTaskEntity.setStartTime(new Date());
        commandTaskEntity.setCommandState(CommandState.RUNNING);
        commandTaskRepository.saveAndFlush(commandTaskEntity);

         commandRepository = SpringUtil.getBean(CommandRepository.class);
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
        log.info("command task：" + taskContext.commandTaskId + " 结束, 记录到数据库");
        CommandTaskEntity commandTaskEntity = commandTaskRepository.findById(taskContext.commandTaskId).get();
        commandTaskEntity.setEndTime(new Date());
        commandTaskEntity.setCommandState(CommandState.SUCCESS);
        commandTaskRepository.saveAndFlush(commandTaskEntity);

        //主动让SiftingAppender结束文件.
        log.info(ClassicConstants.FINALIZE_SESSION_MARKER, "close sifting Appender of `{}`", MDC.get(TASKID));

        //清理MDC.
        MDC.clear();

        // 更新command进度
        CommandEntity updateCommandEntity = commandRepository.findById(getTaskContext().commandId).get();
        // 计算进度
        Integer successTaskCnt = commandTaskRepository.countByCommandStateAndCommandId(CommandState.SUCCESS, getTaskContext().commandId);
        Integer totalTaskCnt = commandTaskRepository.countByCommandId( getTaskContext().commandId);
        Double progress = Math.floor(successTaskCnt.doubleValue() / totalTaskCnt.doubleValue() * 100);
        updateCommandEntity.setTotalProgress(progress.intValue());
        commandRepository.saveAndFlush(updateCommandEntity);

    }

    private void doWhenError(Exception e) {
        log.info(taskContext.commandTaskId + ":发生异常，处理异常。。。"+e.getMessage());
        CommandTaskEntity commandTaskEntity = commandTaskRepository.findById(taskContext.commandTaskId).get();
        commandTaskEntity.setEndTime(new Date());
        commandTaskEntity.setCommandState(CommandState.ERROR);
        commandTaskRepository.saveAndFlush(commandTaskEntity);

        // 更新command状态
        CommandEntity updateCommandEntity = commandRepository.findById(getTaskContext().commandId).get();
        updateCommandEntity.setCommandState(CommandState.ERROR);
        commandRepository.saveAndFlush(updateCommandEntity);
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