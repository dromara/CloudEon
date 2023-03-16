package com.data.udh.processor;

import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

public class ConfigProcessor implements BasicProcessor {
    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        OmsLogger logger = context.getOmsLogger();

        String jobParams = context.getJobParams();
        logger.info("当前 context:{}", context.getWorkflowContext());
        logger.info("当前 job params:{}", jobParams);

        String nodeName = jobParams.split(";")[0];
        String config = jobParams.split(";")[1];
        for (String s : config.split(",")) {
            logger.info(nodeName+" 使用模板引擎渲染文件: "+s);
        }
        // 休眠10s模拟工作内容
        Thread.sleep(20_000L);
        logger.info("完成ConfigProcessor了");

        return new ProcessResult(true);
    }
}
