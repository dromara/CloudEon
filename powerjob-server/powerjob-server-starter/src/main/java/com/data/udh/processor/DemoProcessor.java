package com.data.udh.processor;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.core.processor.ProcessResult;
import tech.powerjob.worker.core.processor.TaskContext;
import tech.powerjob.worker.core.processor.sdk.BasicProcessor;
import tech.powerjob.worker.log.OmsLogger;

import java.util.Map;

@Component
public class DemoProcessor implements BasicProcessor {

    @Override
    public ProcessResult process(TaskContext context) throws Exception {
        OmsLogger logger = context.getOmsLogger();
        logger.info("current:" + context.getJobParams());
        System.out.println("jobParams: " + context.getJobParams());
        System.out.println("currentContext:"+ JSON.toJSONString(context));

        // 尝试获取上游任务
        Map<String, String> workflowContext = context.getWorkflowContext().fetchWorkflowContext();
        System.out.println("工作流上下文数据：");
        System.out.println(workflowContext);

        return new ProcessResult(true, context.getJobId() + " process successfully.");
    }
}