package com.data.udh.processor;

import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

public class InstallProcessor implements BasicProcessor {
    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        OmsLogger logger = context.getOmsLogger();

        String jobParams = context.getJobParams();
        logger.info("当前 context:{}", context.getWorkflowContext());
        logger.info("当前 job params:{}", jobParams);

        String nodeName = jobParams.split(";")[0];
        String config = jobParams.split(";")[1];
        for (String s : config.split(",")) {
            logger.info(nodeName + "创建目录：" + s);
            logger.info(nodeName + "执行命令：mkdir -p " + s);
            logger.info(nodeName + "授予目录权限：" + s);
            logger.info(nodeName + "执行命令：chmod +x " + s);

        }
        for (int i = 0; i < 1000; i++) {
            logger.info(nodeName + "检查安装是否成功 "+i);
        }
        // 休眠10s模拟工作内容
        Thread.sleep(20_000L);
        logger.info("完成InstallProcessor了");

        return new ProcessResult(true);
    }
}
