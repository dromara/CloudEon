package com.data.udh.processor;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExampleTask extends BaseUdhTask{

    public ExampleTask(UdhTaskContext taskContext) {
        super(taskContext);
    }

    @Override
    public void internalExecute() {

        // todo 日志采集的相关执行
        System.out.println(taskContext.commandTaskId + ":模拟执行。。。。");
        if (taskContext.commandTaskId == 10) {
            int a = 1 / 0;
        }

    }
}
