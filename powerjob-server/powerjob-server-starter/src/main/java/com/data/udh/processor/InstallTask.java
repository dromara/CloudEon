package com.data.udh.processor;

public class InstallTask extends BaseUdhTask {


    public InstallTask(UdhTaskContext taskContext) {
        super(taskContext, null);
    }

    @Override
    public void internalExecute() {
        System.out.println(taskContext.commandTaskId + ":模拟执行。。。。");
        if (taskContext.commandTaskId == 10) {
            int a = 1 / 0;
        }

    }
}
