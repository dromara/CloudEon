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
package org.dromara.cloudeon;

import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.JobCondition;
import io.fabric8.kubernetes.api.model.batch.v1.JobStatus;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.dromara.cloudeon.dao.ClusterInfoRepository;
import org.dromara.cloudeon.dto.VolumeMountDTO;
import org.dromara.cloudeon.utils.ByteConverter;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.dsl.Resource;
import io.fabric8.kubernetes.client.dsl.RollableScalableResource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.utils.K8sUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class K8sTest {

    @javax.annotation.Resource
    private ClusterInfoRepository clusterInfoRepository;

    @Test
    public void listNode() {
        KubernetesClient client = new KubernetesClientBuilder().build();
        NodeList nodeList = client.nodes().list();
        List<Node> items = nodeList.getItems();
        items.forEach(e -> {
            String cpu = e.getStatus().getCapacity().get("cpu").getAmount();
            long memory = e.getStatus().getCapacity().get("memory").getNumericalAmount().longValue();
            long storage = e.getStatus().getCapacity().get("ephemeral-storage").getNumericalAmount().longValue();
            String ip = e.getStatus().getAddresses().get(0).getAddress();
            String hostname = e.getStatus().getAddresses().get(1).getAddress();
            String architecture = e.getStatus().getNodeInfo().getArchitecture();
            String containerRuntimeVersion = e.getStatus().getNodeInfo().getContainerRuntimeVersion();
            String kubeletVersion = e.getStatus().getNodeInfo().getKubeletVersion();
            String kernelVersion = e.getStatus().getNodeInfo().getKernelVersion();
            String osImage = e.getStatus().getNodeInfo().getOsImage();

            System.out.println("cpu: " + cpu);
            System.out.println("memory: " + ByteConverter.convertKBToGB(memory) + "GB");
            System.out.println("storage: " + ByteConverter.convertKBToGB(storage) + "GB");
            System.out.println("ip: " + ip);
            System.out.println("hostname: " + hostname);
            System.out.println("architecture: " + architecture);
            System.out.println("containerRuntimeVersion: " + containerRuntimeVersion);
            System.out.println("kubeletVersion: " + kubeletVersion);
            System.out.println("kernelVersion: " + kernelVersion);
            System.out.println("osImage: " + osImage);

            System.out.println("===============");

        });
    }

    @Test
    public void deployDetele() throws FileNotFoundException {
        try (KubernetesClient client = new KubernetesClientBuilder().build();) {
            client.load(new FileInputStream("/Volumes/Samsung_T5/opensource/e-mapreduce/work/k8s-resource/zookeeper13/zookeeper-server.yaml"))
                    .inNamespace("default")
                    .delete();
        }


    }

    @Test
    public void startDeploy() throws FileNotFoundException {
        KubernetesClient client = new KubernetesClientBuilder().build();
        List<HasMetadata> metadata = client.load(new FileInputStream("/Volumes/Samsung_T5/opensource/e-mapreduce/work/k8s-resource/zookeeper13/zookeeper-server.yaml"))
                .inNamespace("default")
                .create();
        String deploymentName = ((Deployment) metadata.get(0)).getMetadata().getName();
        final Deployment deployment = client.apps().deployments().inNamespace("default").withName(deploymentName).get();
        Resource<Deployment> resource = client.resource(deployment).inNamespace("default");
        resource.watch(new Watcher<Deployment>() {
            @Override
            public void eventReceived(Action action, Deployment resource) {
                log.info("{} {}", action.name(), resource.getMetadata().getName());
                switch (action) {
                    case ADDED:
                        log.info("{} got added", resource.getMetadata().getName());
                        break;
                    case DELETED:
                        log.info("{} got deleted", resource.getMetadata().getName());
                        break;
                    case MODIFIED:
                        log.info("{} got modified", resource.getMetadata().getName());
                        break;
                    default:
                        log.error("Unrecognized event: {}", action.name());
                }
            }

            @Override
            public void onClose(WatcherException cause) {
                System.out.println(cause.getMessage());
            }
        });
        resource.waitUntilReady(1200, TimeUnit.SECONDS);
    }

    @Test
    public void addNodeLabel() {
        KubernetesClient client = new KubernetesClientBuilder().build();

        // 添加label
        client.nodes().withName("fl001")
                .edit(r -> new NodeBuilder(r)
                        .editMetadata()
                        .addToLabels("my-hdfs-dn", "true")
                        .endMetadata()
                        .build());

        // 检查label
        System.out.println(client.nodes().withName("fl001").get().getMetadata().getLabels().get("my-hdfs-dn").equals("true"));
        // 移除lable
        client.nodes().withName("fl001")
                .edit(r -> new NodeBuilder(r)
                        .editMetadata()
                        .removeFromLabels("my-hdfs-dn")
                        .endMetadata()
                        .build());


    }

    @Test
    public void findPod() {
        KubernetesClient client = new KubernetesClientBuilder().build();
        List<Pod> pods = client.pods().inNamespace("default").withLabel("app=zookeeper-server-zookeeper13").list().getItems();
        for (Pod pod : pods) {
            String nodeName = pod.getSpec().getNodeName();
            if (nodeName != null && nodeName.equals("fl001")) {
                // do something with the pod
                String podName = pod.getMetadata().getName();
                System.out.println(podName);
//                client.pods().withName(podName).delete();
            }
        }
    }

    @Test
    public void scale() {
        KubernetesClient client = new KubernetesClientBuilder().build();
        RollableScalableResource<Deployment> resource = client.apps().deployments().inNamespace("default").withName("zookeeper-server-zookeeper13");
        Integer readyReplicas = resource.get().getStatus().getReadyReplicas();
        System.out.println("当前deployment可用Replicas: " + readyReplicas);
        System.out.println(resource.scale(readyReplicas - 1));

    }

    @Test
    public void ip() throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }

    @Test
    public void kubeconfig() throws FileNotFoundException {
        String kubeConfig = clusterInfoRepository.findById(1).get().getKubeConfig();

        Config config = Config.fromKubeconfig(kubeConfig);
        KubernetesClient client = new KubernetesClientBuilder().withConfig(config).build();
        NodeList nodeList = client.nodes().list();
        List<Node> items = nodeList.getItems();
        System.out.println(items.size());
    }


    @Test
    public void getPodEvent() {
        Config config = Config.fromKubeconfig("apiVersion: v1\n" +
                "clusters:\n" +
                "- cluster:\n" +
                "    certificate-authority-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUJlRENDQVIyZ0F3SUJBZ0lCQURBS0JnZ3Foa2pPUFFRREFqQWpNU0V3SHdZRFZRUUREQmhyTTNNdGMyVnkKZG1WeUxXTmhRREUyT1RJd01EVTFPRFF3SGhjTk1qTXdPREUwTURrek16QTBXaGNOTXpNd09ERXhNRGt6TXpBMApXakFqTVNFd0h3WURWUVFEREJock0zTXRjMlZ5ZG1WeUxXTmhRREUyT1RJd01EVTFPRFF3V1RBVEJnY3Foa2pPClBRSUJCZ2dxaGtqT1BRTUJCd05DQUFSd1BxcUYwMVFVaDJwenlpekFmSzNqNGh4dDNUMXF0ZXdZYjBkeDFxRDcKVURlUlI5QzR1RXZxY3dwVDRDSTViTytGZjlHM08xOUZockJ0aDNUQUpVblhvMEl3UURBT0JnTlZIUThCQWY4RQpCQU1DQXFRd0R3WURWUjBUQVFIL0JBVXdBd0VCL3pBZEJnTlZIUTRFRmdRVU1oaU95T0ZZUUw0bWd5SzRNeFJ1CjIxc25qTzB3Q2dZSUtvWkl6ajBFQXdJRFNRQXdSZ0loQU5Oais2Q0ZaSUhmZVFvS3ZuRUZVSlo1UlY3RFFWblcKc1FQRTZwYjJjWGxrQWlFQW1oV25FWVRTS3JMYTA0YmVjZkc0aTBoNjFBTUFBcWl3VmlQa2ZIZ2Q1anc9Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K\n" +
                "    server: https://192.168.65.2:6443\n" +
                "  name: default\n" +
                "contexts:\n" +
                "- context:\n" +
                "    cluster: default\n" +
                "    user: default\n" +
                "  name: default\n" +
                "current-context: default\n" +
                "kind: Config\n" +
                "preferences: {}\n" +
                "users:\n" +
                "- name: default\n" +
                "  user:\n" +
                "    client-certificate-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUJrakNDQVRlZ0F3SUJBZ0lJS2RjZXJ3SUdMdVV3Q2dZSUtvWkl6ajBFQXdJd0l6RWhNQjhHQTFVRUF3d1kKYXpOekxXTnNhV1Z1ZEMxallVQXhOamt5TURBMU5UZzBNQjRYRFRJek1EZ3hOREE1TXpNd05Gb1hEVEkwTURneApNekE1TXpNd05Gb3dNREVYTUJVR0ExVUVDaE1PYzNsemRHVnRPbTFoYzNSbGNuTXhGVEFUQmdOVkJBTVRESE41CmMzUmxiVHBoWkcxcGJqQlpNQk1HQnlxR1NNNDlBZ0VHQ0NxR1NNNDlBd0VIQTBJQUJEUUVJb3lCajl6ZmNiNFIKdDZrdnlwMkVRQTlkQVZYNlE0dkgyUElWblphM0pNd3dXcVpFZlVxTURFSzIwRWJ3M1kvZmdHQldNeUh4d3MrRQoxVUMrc2ttalNEQkdNQTRHQTFVZER3RUIvd1FFQXdJRm9EQVRCZ05WSFNVRUREQUtCZ2dyQmdFRkJRY0RBakFmCkJnTlZIU01FR0RBV2dCUmhSRnZ0eTlUdExvYzNkYzNjSUYyUWh3bEp0akFLQmdncWhrak9QUVFEQWdOSkFEQkcKQWlFQXJ1aGRxMm93NDFwVjErRFdkTUwyemZ3YXlJL25ZOGVaWnpNSG5seERpL2NDSVFEQ0JMcFJVdGUwVUprNgp3QS80RGRpOWEyd2VONVZaVFZsUVJHRGpFTGt3RWc9PQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCi0tLS0tQkVHSU4gQ0VSVElGSUNBVEUtLS0tLQpNSUlCZGpDQ0FSMmdBd0lCQWdJQkFEQUtCZ2dxaGtqT1BRUURBakFqTVNFd0h3WURWUVFEREJock0zTXRZMnhwClpXNTBMV05oUURFMk9USXdNRFUxT0RRd0hoY05Nak13T0RFME1Ea3pNekEwV2hjTk16TXdPREV4TURrek16QTAKV2pBak1TRXdId1lEVlFRRERCaHJNM010WTJ4cFpXNTBMV05oUURFMk9USXdNRFUxT0RRd1dUQVRCZ2NxaGtqTwpQUUlCQmdncWhrak9QUU1CQndOQ0FBUXBHRlhKZFhaU3o4T3FxZjd3U3M5aG1lMmtOSzN0VkF6VTVubGxmUUV5CnhFSFc0WUs4UzNtWlVDZUMrOXR1c0VrVSt5d2V6R09lMFVQWXBRNmp4V1ZWbzBJd1FEQU9CZ05WSFE4QkFmOEUKQkFNQ0FxUXdEd1lEVlIwVEFRSC9CQVV3QXdFQi96QWRCZ05WSFE0RUZnUVVZVVJiN2N2VTdTNkhOM1hOM0NCZAprSWNKU2JZd0NnWUlLb1pJemowRUF3SURSd0F3UkFJZ0tMMHpUMDBhczRZZmoySmErMTVlbHdkbEFGVmZWTjVxCkI5OVNyNW1UMTVBQ0lIKzIwTlBMNnJ2S0lIOWJ0T0J1WjhCYWtRb1I1cUlaTDhraDd2WXI3eXlnCi0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K\n" +
                "    client-key-data: LS0tLS1CRUdJTiBFQyBQUklWQVRFIEtFWS0tLS0tCk1IY0NBUUVFSUp0V0VJZzNFN3lIV09QbEcyajdqemNiRWRZN1Z5RUhxU3NOZEo4ald6U1hvQW9HQ0NxR1NNNDkKQXdFSG9VUURRZ0FFTkFRaWpJR1AzTjl4dmhHM3FTL0tuWVJBRDEwQlZmcERpOGZZOGhXZGxyY2t6REJhcGtSOQpTb3dNUXJiUVJ2RGRqOStBWUZZeklmSEN6NFRWUUw2eVNRPT0KLS0tLS1FTkQgRUMgUFJJVkFURSBLRVktLS0tLQo=\n");
        KubernetesClient client = new KubernetesClientBuilder().withConfig(config).build();

        EventList eventList = client.v1().events()
                .inNamespace("default")
                .withField("involvedObject.name", "prometheus-monitor-5fbb5d7cb8-4g5zm")
                .list();

        for (Event event : eventList.getItems()) {
            System.out.println("Event Type: " + event.getType());
            System.out.println("Event Reason: " + event.getReason());
            System.out.println("Event Message: " + event.getMessage());
            // 可以根据需要打印其他事件属性
        }

    }

}
