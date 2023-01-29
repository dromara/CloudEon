package com.data.udh.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import tech.powerjob.common.enums.*;
import tech.powerjob.common.model.LogConfig;
import tech.powerjob.common.model.PEWorkflowDAG;
import tech.powerjob.common.request.http.SaveJobInfoRequest;
import tech.powerjob.common.request.http.SaveWorkflowNodeRequest;
import tech.powerjob.common.request.http.SaveWorkflowRequest;
import tech.powerjob.common.response.ResultDTO;
import tech.powerjob.common.response.WorkflowNodeInfoDTO;
import tech.powerjob.common.serialize.JsonUtils;
import tech.powerjob.server.core.service.JobService;
import tech.powerjob.server.core.workflow.WorkflowService;
import tech.powerjob.server.persistence.remote.model.WorkflowNodeInfoDO;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HelloService {

    @Resource
    WorkflowService workflowService;

    @Resource
    JobService jobService;




    public void wf() {
        SaveJobInfoRequest base = new SaveJobInfoRequest();
        base.setJobName("DAG-Node-");
        base.setJobDescription("job描述........");
        // 定时信息设置：工作流
        base.setTimeExpressionType(TimeExpressionType.WORKFLOW);
        // 单机执行
        base.setExecuteType(ExecuteType.STANDALONE);
        // 内置
        base.setProcessorType(ProcessorType.BUILT_IN);
        base.setProcessorInfo("com.data.udh.processor.DemoProcessor");
        LogConfig logConfig = new LogConfig().setType(LogType.ONLINE.getV());
        base.setLogConfig(logConfig);

        List<Long> jobIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SaveJobInfoRequest request = JSONObject.parseObject(JSONObject.toJSONBytes(base), SaveJobInfoRequest.class);
            request.setJobName(request.getJobName() + i);
            request.setAppId(1L);
            Long jobID = jobService.saveJob(request);
            jobIds.add(jobID);
        }

        SaveWorkflowRequest req = new SaveWorkflowRequest();

        req.setWfName("workflow-by-client");
        req.setWfDescription("created by client");
        req.setEnable(true);
        req.setTimeExpressionType(TimeExpressionType.API);
        req.setAppId(1L);

        System.out.println("req ->" + JSONObject.toJSON(req));

        // 创建节点
        List<SaveWorkflowNodeRequest> saveWorkflowNodeRequests = jobIds.stream().map(new Function<Long, SaveWorkflowNodeRequest>() {
            @Override
            public SaveWorkflowNodeRequest apply(Long aLong) {
                SaveWorkflowNodeRequest saveWorkflowNodeRequest1 = new SaveWorkflowNodeRequest();
                saveWorkflowNodeRequest1.setJobId(aLong);
                saveWorkflowNodeRequest1.setAppId(1L);
                saveWorkflowNodeRequest1.setNodeName("DAG-Node-" + aLong);
                saveWorkflowNodeRequest1.setType(WorkflowNodeType.JOB.getCode());
                return saveWorkflowNodeRequest1;
            }
        }).collect(Collectors.toList());


        List<WorkflowNodeInfoDO> nodeList = workflowService.saveWorkflowNode(saveWorkflowNodeRequests);


        // DAG 图
        List<PEWorkflowDAG.Node> nodes = Lists.newLinkedList();
        List<PEWorkflowDAG.Edge> edges = Lists.newLinkedList();

        // 构建DAG的节点
        nodeList.forEach(e -> {
            nodes.add(new PEWorkflowDAG.Node(e.getId()));
        });

        // 构建DAG的边
        for (int i = 0; i < nodeList.size() - 1; i++) {
            edges.add(new PEWorkflowDAG.Edge(nodeList.get(i).getId(), nodeList.get(i + 1).getId()));
        }
        PEWorkflowDAG peWorkflowDAG = new PEWorkflowDAG(nodes, edges);

        // 保存完整信息
        req.setDag(peWorkflowDAG);
        workflowService.saveWorkflow(req);
    }
}
