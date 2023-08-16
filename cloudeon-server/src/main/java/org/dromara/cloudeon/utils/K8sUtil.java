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

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.JobStatus;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class K8sUtil {
    public static void runJob(String name, KubernetesClient client, String volumePath, String image, String cmd, Logger logger,String hostname) {
        // delete job
        List<StatusDetails> statusDetailsList = client.batch().v1().jobs()
                .inNamespace("default")
                .withName(name)
                .delete();
        statusDetailsList.forEach(System.out::println);

        // 1. 定义 HostPathVolumeSource
        HostPathVolumeSource hostPathVolume = new HostPathVolumeSourceBuilder()
                .withPath(volumePath)
                .build();

        Volume volume = new VolumeBuilder()
                .withName("config-volume")
                .withHostPath(hostPathVolume) //本地目录
                .build();

        VolumeMount mount = new VolumeMountBuilder()
                .withName("config-volume")
                .withMountPath(volumePath)
                .build();


        Container container = new ContainerBuilder()
                .withName("init")
                .withImage(image)
                .withCommand("sh", "-c", cmd)
                .withVolumeMounts(mount) // 挂载
                .build();

        Job job = new JobBuilder()
                .withNewMetadata()
                .withName(name)
                .endMetadata()
                .withNewSpec()
                .withBackoffLimit(0)
                .withNewTemplate()
                .withNewSpec()
                .withVolumes(volume)
                .withNodeSelector(Collections.singletonMap("kubernetes.io/hostname", hostname))
                .withContainers(container).withHostNetwork(true)
                .withRestartPolicy("Never")
                .endSpec()
                .endTemplate()
                .endSpec()
                .build();

        client.batch().v1().jobs()
                .inNamespace("default")
                .resource(job).create();


        // Get the pod name associated with the job
        List<Pod> pods = client.pods()
                .inNamespace("default")
                .withLabel("job-name", name)
                .list().getItems();

        String podName = "";
        if (!pods.isEmpty()) {
            podName = pods.get(0).getMetadata().getName();
            logger.info("Pod name: " + podName);
        }

        CountDownLatch jobCompletionLatch = new CountDownLatch(1);

        Watcher<Job> watcher = new Watcher<Job>() {
            @Override
            public void eventReceived(Action action, Job job) {
                logger.info("job watch action: " + action);

                if (action == Action.ADDED || action == Action.MODIFIED) {
                    JobStatus status = job.getStatus();
                    if (status != null) {
                        boolean isJobSuccessful = status.getSucceeded() != null && status.getSucceeded() > 0;
                        boolean isJobFailed = status.getFailed() != null && status.getFailed() > 0;

                        if (isJobSuccessful) {
                            logger.info("Job ran successfully!");
                            jobCompletionLatch.countDown(); // Decrement the latch count
                        } else if (isJobFailed) {
                            logger.info("Job failed.");
                            jobCompletionLatch.countDown(); // Decrement the latch count
                        }
                    }
                }
            }

            @Override
            public void onClose(WatcherException cause) {
                logger.info("Watcher closed");
                if (cause != null) {
                    cause.printStackTrace();
                }
            }


        };

        Watch watch = client.batch().v1().jobs()
                .inNamespace("default")
                .withName(name)
                .watch(watcher);


        // Print pod logs
        try (LogWatch logWatch = client.pods()
                .inNamespace("default")
                .withName(podName)
                .watchLog()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(logWatch.getOutput()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info(line);  // You can replace this with your desired logging mechanism
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                logWatch.close();
            }

            // Wait for the job to complete
            logger.info("Waiting  for job to complete...");
            jobCompletionLatch.await();
            logger.info("Job completed!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            watch.close();
        }


        logger.info("Done...");
    }
}
