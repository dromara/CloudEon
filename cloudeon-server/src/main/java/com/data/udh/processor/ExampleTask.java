package com.data.udh.processor;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExampleTask extends BaseUdhTask{

    @Override
    public void internalExecute() {

        // todo 日志采集的相关执行
        log.info(taskParam.getCommandTaskId() + ":模拟执行。。。。");
        if (taskParam.getCommandTaskId() == 10) {
//            int a = 1 / 0;
        }

    }
}
