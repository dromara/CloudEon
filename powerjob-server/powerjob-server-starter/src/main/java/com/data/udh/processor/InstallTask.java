package com.data.udh.processor;

public class InstallTask extends  BaseUdhTask{
    public InstallTask(Integer commandTaskId, Integer commandId, Integer serviceInstanceId) {
        super(commandTaskId, commandId, serviceInstanceId);
    }

    @Override
    public void internalExecute() {
        System.out.println(commandTaskId+":模拟执行。。。。");
        if (commandTaskId == 10) {
            int a = 1 / 0;
        }

    }
}
