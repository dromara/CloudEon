/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.dromara.cloudeon.utils;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.func.Supplier2;
import cn.hutool.core.lang.func.VoidFunc0;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.JobSpec;
import io.fabric8.kubernetes.api.model.batch.v1.JobStatus;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.Watch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.cloudeon.crd.helmchart.HelmChart;
import org.dromara.cloudeon.processor.TaskParam;
import org.dromara.cloudeon.utils.k8s.DeploymentReadyWatcher;
import org.dromara.cloudeon.utils.k8s.JobCompleteWatcher;
import org.dromara.cloudeon.utils.k8s.PodLogWatcher;

import java.io.OutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class K8sUtil {
    private static DateTimeFormatter chineseDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static ZoneId beijingZoneId = ZoneId.of("Asia/Shanghai");

    public static KubernetesClient getKubernetesClient(String kubeConfig, String namespace) {
        io.fabric8.kubernetes.client.Config config = io.fabric8.kubernetes.client.Config.fromKubeconfig(kubeConfig);
        config.setNamespace(StringUtils.isBlank(namespace) ? "default" : namespace);
        return new KubernetesClientBuilder().withConfig(config).build();
    }

    public static KubernetesClient getKubernetesClient(String kubeConfig) {
        return getKubernetesClient(kubeConfig, null);
    }

    public static String formatK8sNameStr(String nameStr) {
        return nameStr.toLowerCase().replace("_", "-");
    }

    public static String formatK8sDateStr(String dataStr) {
        if (StringUtils.isBlank(dataStr)) {
            return dataStr;
        }
        Instant utcInstant = Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(dataStr));
        ZonedDateTime beijingTime = ZonedDateTime.ofInstant(utcInstant, beijingZoneId);
        return chineseDateTimeFormatter.format(beijingTime);
    }


    public static String getNamespace(KubernetesClient client, HasMetadata resource) {
        if (StringUtils.isNotEmpty(resource.getMetadata().getNamespace())) {
            return resource.getMetadata().getNamespace();
        }
        if (StringUtils.isNotEmpty(client.getNamespace())) {
            return client.getNamespace();
        }
        return "default";
    }

    public static boolean checkDeploymentReady(Deployment deployment) {
        if (deployment == null) {
            return false;
        }
        DeploymentStatus status = deployment.getStatus();
        if (status == null || deployment.getStatus().getReadyReplicas() == null) {
            return false;
        }
        return Objects.equals(deployment.getStatus().getReadyReplicas(), deployment.getStatus().getReplicas());
    }

    public static void waitForDeploymentReady(VoidFunc0 resourceAction, TaskParam taskParam, KubernetesClient client, Deployment deployment, long waitSeconds) throws InterruptedException {
        // 如果已经在运行了则不再跟踪日志,当任务被中断重试可能进入此状态
        if (checkDeploymentReady(client.apps().deployments().resource(deployment).get())) {
            log.info("Deployment is ready");
            return;
        }
        CountDownLatch completionLatch = new CountDownLatch(1);
        try (Watch ignored = client.apps().deployments().resource(deployment)
                .watch(new DeploymentReadyWatcher(taskParam, completionLatch));
             Watch ignored1 = client.pods().inNamespace(getNamespace(client, deployment)).withLabelSelector(deployment.getSpec().getSelector())
                     .watch(new PodLogWatcher(client, getTaskParamOutputStreamSupplier(taskParam)))
        ) {
            resourceAction.callWithRuntimeException();
            log.info("Waiting for deployment to be ready ...");
            if (!completionLatch.await(waitSeconds, TimeUnit.SECONDS)) {
                log.error("Deployment is not ready within {} seconds", waitSeconds);
                throw new RuntimeException("Deployment is not ready within " + waitSeconds + " seconds");
            }
        }
        log.info("Deployment is ready");
    }

    public static Job getInstallJobByHelmChart(KubernetesClient client, HelmChart helmChart) {
        return getJobByHelmChart(client, helmChart, "install");
    }

    public static Job getDeleteJobByHelmChart(KubernetesClient client, HelmChart helmChart) {
        return getJobByHelmChart(client, helmChart, "delete");
    }

    private static Job getJobByHelmChart(KubernetesClient client, HelmChart helmChart, String type) {
        ObjectMeta jobMeta = new ObjectMeta();
        jobMeta.setNamespace(K8sUtil.getNamespace(client, helmChart));
        jobMeta.setName(StrUtil.format("helm-{}-{}", type, helmChart.getMetadata().getName()));
        return new JobBuilder().withMetadata(jobMeta).build();
    }

    public static boolean checkJobEnded(Job job) {
        JobStatus status = job.getStatus();
        return status != null && !status.getConditions().isEmpty();
    }

    public static boolean checkJobEndedSuccess(JobStatus status) {
        return "Complete".equalsIgnoreCase(status.getConditions().get(0).getType());
    }

    public static void waitForJobCompleted(VoidFunc0 resourceAction, TaskParam taskParam, KubernetesClient client, Job job, long waitSeconds) throws InterruptedException {
        Job runningJob = client.batch().v1().jobs().resource(job).get();
        AtomicReference<JobStatus> finalJobStatus = new AtomicReference<>();
        if (runningJob == null || !checkJobEnded(runningJob)) {
            // 如果job不存在或者job已存在但未结束，则跟踪日志
            CountDownLatch jobCompletionLatch = new CountDownLatch(1);
            if (job.getSpec() == null || job.getSpec().getSelector() == null || job.getSpec().getSelector().getMatchLabels().isEmpty()) {
                LabelSelector labelSelector = new LabelSelector();
                labelSelector.setMatchLabels(MapUtil.of("job-name", job.getMetadata().getName()));
                JobSpec jobSpec = new JobSpec();
                jobSpec.setSelector(labelSelector);
                job.setSpec(jobSpec);
            }
            try (Watch ignored = client.batch().v1().jobs()
                    .resource(job)
                    .watch(new JobCompleteWatcher(taskParam, jobCompletionLatch, finalJobStatus));
                 Watch ignored1 = client.pods().inNamespace(getNamespace(client, job))
                         .withLabelSelector(job.getSpec().getSelector())
                         .watch(new PodLogWatcher(client, getTaskParamOutputStreamSupplier(taskParam)))
            ) {
                resourceAction.callWithRuntimeException();
                log.info("Waiting  for job to complete...");
                if (!jobCompletionLatch.await(waitSeconds, TimeUnit.SECONDS)) {
                    log.error("Job is not completed within {} seconds", waitSeconds);
                    throw new RuntimeException("Job is not completed within " + waitSeconds + " seconds");
                }
            }
        } else {
            finalJobStatus.set(runningJob.getStatus());
        }
        JobStatus status = finalJobStatus.get();
        boolean isJobEndSuccess;
        int retryCount = 0;
        isJobEndSuccess = K8sUtil.checkJobEndedSuccess(status);
        if (status.getFailed() != null) {
            retryCount = status.getFailed();
        }
        log.info("Job ended with status: " + (isJobEndSuccess ? "success" : "failed") + ", retryCount: " + retryCount);
        if (!isJobEndSuccess) {
            throw new RuntimeException("Job failed,retryCount: " + retryCount);
        }
    }

    private static Supplier2<OutputStream, Pod, ContainerStatus> getTaskParamOutputStreamSupplier(TaskParam taskParam) {
        return (pod, containerStatus) -> {
            String podName = pod.getMetadata().getName();
            return new LogOutputStream(taskParam,
                    pod.getStatus().getContainerStatuses().size() > 1
                            ? s -> StrUtil.format("Log of pod {}({}) > {}", podName, containerStatus.getName(), s)
                            : s -> StrUtil.format("Log of pod {} > {}", podName, s));
        };
    }

    public static String getConfigMapStr(String configmapName, Map<String, String> labels, Map<String, String> fileStrMap) {
        Map<String, Object> dataModel = Maps.newHashMapWithExpectedSize(3);
        dataModel.put("configmapName", configmapName);
        dataModel.put("labels", labels);
        for (Map.Entry<String, String> dataEntry : fileStrMap.entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String line : dataEntry.getValue().split("\\R")) {
                sb.append("    ").append(line).append("\n");
            }
            fileStrMap.put(dataEntry.getKey(), sb.toString());
        }
        dataModel.put("fileStrMap", fileStrMap);
        return FreemarkerUtil.templateEval(ResourceUtil.readUtf8Str("templates/configmap.yaml.ftl"), dataModel);
    }

    private static final DateTimeFormatter K8S_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    public static LocalDateTime parseDateStrToDateTime(String dateStr) {
        return LocalDateTime.parse(dateStr, K8S_DATE_TIME_FORMATTER);
    }

    public static long parseDateStrToLong(String dateStr) {
        return parseDateStrToDateTime(dateStr).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

}