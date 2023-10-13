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

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobStatus;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.dsl.FilterWatchListDeletable;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import io.fabric8.kubernetes.client.dsl.PodResource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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


    public static String formatK8sDateStr(String dataStr) {
        if (StringUtils.isBlank(dataStr)) {
            return dataStr;
        }
        Instant utcInstant = Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(dataStr));
        ZonedDateTime beijingTime = ZonedDateTime.ofInstant(utcInstant, beijingZoneId);
        return chineseDateTimeFormatter.format(beijingTime);
    }

    public static int waitForJobCompleted(String namespace, String jobName, KubernetesClient client, Logger logger, long waitSeconds) {
        CountDownLatch jobCompletionLatch = new CountDownLatch(1);

        AtomicBoolean isJobEndSuccess = new AtomicBoolean(false);
        AtomicInteger retryCount = new AtomicInteger(0);
        Watcher<Job> watcher = new Watcher<Job>() {
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
                jobCompletionLatch.countDown();
            }

            @Override
            public void onClose(WatcherException cause) {
                logger.info("Watcher closed");
                if (cause != null) {
                    logger.error(cause.getMessage(), cause);
                }
            }
        };

        Watch watch = client.batch().v1().jobs()
                .inNamespace(namespace)
                .withName(jobName)
                .watch(watcher);

        FilterWatchListDeletable<Pod, PodList, PodResource> podListWatch = client.pods().inNamespace(namespace).withLabel("job-name", jobName);
        Watch podListLogWatch = podListWatch.watch(new Watcher<Pod>() {
            @Override
            public void eventReceived(Action action, Pod pod) {
                if (action.equals(Action.ADDED)) {
                    String podName = pod.getMetadata().getName();
                    try (LogWatch logWatch = client.pods()
                            .inNamespace(namespace)
                            .withName(podName)
                            .watchLog();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(logWatch.getOutput()))
                    ) {
                        String line = reader.readLine();
                        while (line != null) {
                            logger.info("Log of pod " + podName + "> " + line);
                            line = reader.readLine();
                        }
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }

            @Override
            public void onClose() {
                Watcher.super.onClose();
            }

            @Override
            public void onClose(WatcherException e) {
                logger.error(e.getMessage(), e);
            }
        });
        logger.info("Waiting  for job to complete...");
        try {
            if (!jobCompletionLatch.await(waitSeconds, TimeUnit.SECONDS)) {
                logger.error("Job is not completed within {} seconds", waitSeconds);
                throw new RuntimeException("Job is not completed within " + waitSeconds + " seconds");
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        watch.close();
        podListLogWatch.close();

        boolean flag = isJobEndSuccess.get();
        logger.info("Job completed with success status: " + flag);
        if (!flag) {
            throw new RuntimeException("Job failed,retryCount: " + retryCount.get());
        }
        return retryCount.get();
    }

}