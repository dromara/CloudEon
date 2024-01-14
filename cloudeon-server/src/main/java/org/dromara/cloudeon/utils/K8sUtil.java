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

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.JobSpec;
import io.fabric8.kubernetes.api.model.batch.v1.JobStatus;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.dromara.cloudeon.crd.helmchart.HelmChart;
import org.dromara.cloudeon.processor.TaskParam;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Slf4j
    public static class DeploymentReadyWatcher implements Watcher<Deployment> {
        private final CountDownLatch completionLatch;
        private final TaskParam taskParam;

        public DeploymentReadyWatcher(TaskParam taskParam, CountDownLatch completionLatch) {
            super();
            this.taskParam = taskParam;
            this.completionLatch = completionLatch;
        }

        @Override
        public void eventReceived(Action action, Deployment deployment) {
            DeploymentStatus status = deployment.getStatus();
            if (status == null) {
                return;
            }
            boolean deploymentReady = deployment.getStatus().getReadyReplicas() != null
                    && Objects.equals(deployment.getStatus().getReadyReplicas(), deployment.getStatus().getReplicas());
            if (deploymentReady) {
                completionLatch.countDown();
            }
        }

        @Override
        public void onClose() {
            LogUtil.logWithTaskId(taskParam, () -> log.info("DeploymentReadyWatcher closed"));
        }

        @Override
        public void onClose(WatcherException cause) {
            LogUtil.logWithTaskId(taskParam, () -> {
                log.info("DeploymentReadyWatcher closed with exception: " + cause.getMessage());
                log.error(cause.getMessage(), cause);
            });
        }
    }

    @Slf4j
    public static class JobCompleteWatcher implements Watcher<Job> {
        private final TaskParam taskParam;
        private final CountDownLatch completionLatch;
        private final AtomicBoolean isJobEndSuccess;
        private final AtomicInteger retryCount;

        public JobCompleteWatcher(TaskParam taskParam, CountDownLatch completionLatch, AtomicBoolean isJobEndSuccess, AtomicInteger retryCount) {
            super();
            this.taskParam = taskParam;
            this.completionLatch = completionLatch;
            this.isJobEndSuccess = isJobEndSuccess;
            this.retryCount = retryCount;
        }

        @Override
        public void eventReceived(Action action, Job job) {
            if (action != Action.MODIFIED) {
                return;
            }
            JobStatus status = job.getStatus();
            if (status == null || status.getConditions().isEmpty()) {
                return;
            }
            isJobEndSuccess.set("Complete".equalsIgnoreCase(status.getConditions().get(0).getType()));
            if (status.getFailed() != null) {
                retryCount.set(status.getFailed());
            }
            completionLatch.countDown();
        }

        @Override
        public void onClose() {
            doClose();
        }

        @Override
        public void onClose(WatcherException cause) {
            LogUtil.logWithTaskId(taskParam, () -> {
                log.info("Watcher closed with exception: " + cause.getMessage());
                log.error(cause.getMessage(), cause);
            });
            doClose();
        }

        private void doClose() {
            LogUtil.logWithTaskId(taskParam, () -> {
                log.info("Watcher closed");
            });
        }
    }

    @Slf4j
    public static class PodLogWatcher implements Watcher<Pod> {

        private final TaskParam taskParam;
        private final KubernetesClient client;
        private List<LogWatch> logWatchelist = Lists.newArrayList();

        public PodLogWatcher(TaskParam taskParam, KubernetesClient client) {
            super();
            this.taskParam = taskParam;
            this.client = client;
        }

        @Override
        public void eventReceived(Action action, Pod pod) {
            if (action == Action.ADDED) {
                String podName = pod.getMetadata().getName();
                LogWatch logWatch = client.pods()
                        .inNamespace(pod.getMetadata().getNamespace())
                        .withName(podName)
                        .watchLog(new LogOutputStream(taskParam,
                                s -> "Log of pod " + podName + "> " + s));
                logWatchelist.add(logWatch);
            }
        }

        private void doClose() {
            logWatchelist.forEach(IoUtil::close);
            logWatchelist.clear();
            LogUtil.logWithTaskId(taskParam, () -> log.info("PodLogWatcher closed"));
        }

        @Override
        public void onClose() {
            doClose();
        }

        @Override
        public void onClose(WatcherException e) {
            LogUtil.logWithTaskId(taskParam, () -> log.error(e.getMessage(), e));
            doClose();
        }
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

    public static void waitForDeploymentReady(ResourceAction resourceAction, TaskParam taskParam, KubernetesClient client, Deployment deployment, long waitSeconds) {
        CountDownLatch completionLatch = new CountDownLatch(1);
        try (Watch ignored = client.apps().deployments().resource(deployment)
                .watch(new DeploymentReadyWatcher(taskParam, completionLatch));
             Watch ignored1 = client.pods().inNamespace(getNamespace(client, deployment)).withLabelSelector(deployment.getSpec().getSelector()).watch(new PodLogWatcher(taskParam, client))
        ) {
            resourceAction.action();
            log.info("Waiting for deployment to be ready ...");
            try {
                if (!completionLatch.await(waitSeconds, TimeUnit.SECONDS)) {
                    log.error("Deployment is not ready within {} seconds", waitSeconds);
                    throw new RuntimeException("Deployment is not ready within " + waitSeconds + " seconds");
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
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

    public static int waitForJobCompleted(ResourceAction resourceAction, TaskParam taskParam, KubernetesClient client, Job job, long waitSeconds) {
        CountDownLatch jobCompletionLatch = new CountDownLatch(1);

        AtomicBoolean isJobEndSuccess = new AtomicBoolean(false);
        AtomicInteger retryCount = new AtomicInteger(0);
        if (job.getSpec() == null || job.getSpec().getSelector() == null || job.getSpec().getSelector().getMatchLabels().isEmpty()) {
            LabelSelector labelSelector = new LabelSelector();
            labelSelector.setMatchLabels(MapUtil.of("job-name", job.getMetadata().getName()));
            JobSpec jobSpec = new JobSpec();
            jobSpec.setSelector(labelSelector);
            job.setSpec(jobSpec);
        }
        try (Watch ignored = client.batch().v1().jobs()
                .resource(job)
                .watch(new JobCompleteWatcher(taskParam, jobCompletionLatch, isJobEndSuccess, retryCount));
             Watch ignored1 = client.pods().inNamespace(getNamespace(client, job)).withLabelSelector(job.getSpec().getSelector()).watch(new PodLogWatcher(taskParam, client))
        ) {
            resourceAction.action();
            log.info("Waiting  for job to complete...");
            try {
                if (!jobCompletionLatch.await(waitSeconds, TimeUnit.SECONDS)) {
                    log.error("Job is not completed within {} seconds", waitSeconds);
                    throw new RuntimeException("Job is not completed within " + waitSeconds + " seconds");
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
        boolean flag = isJobEndSuccess.get();
        log.info("Job completed with success status: " + flag);
        if (!flag) {
            throw new RuntimeException("Job failed,retryCount: " + retryCount.get());
        }
        return retryCount.get();
    }


    public interface ResourceAction {
        void action();
    }
}