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

package org.dromara.cloudeon.fabric8;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.ExecListener;
import io.fabric8.kubernetes.client.dsl.ExecWatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("java:S106")
public class ExecuteCommandOnPodExample implements AutoCloseable {

  private final KubernetesClient client;

  public ExecuteCommandOnPodExample() {
    Config config = new ConfigBuilder().build();
    this.client = new KubernetesClientBuilder().withConfig(config).build();
  }

  @Override
  public void close() {
    client.close();
  }

  @SneakyThrows
  public String execCommandOnPod(String podName, String namespace, String... cmd) {
    Pod pod = client.pods().inNamespace(namespace).withName(podName).get();
    System.out.printf("Running command: [%s] on pod [%s] in namespace [%s]%n",
        Arrays.toString(cmd), pod.getMetadata().getName(), namespace);

    CompletableFuture<String> data = new CompletableFuture<>();
    try (ExecWatch execWatch = execCmd(pod, data, cmd)) {
      return data.get(10, TimeUnit.SECONDS);
    }

  }

  private ExecWatch execCmd(Pod pod, CompletableFuture<String> data, String... command) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    return client.pods()
        .inNamespace(pod.getMetadata().getNamespace())
        .withName(pod.getMetadata().getName())
        .writingOutput(baos)
        .writingError(baos)
        .usingListener(new SimpleListener(data, baos))
        .exec(command);
  }

  static class SimpleListener implements ExecListener {

    private CompletableFuture<String> data;
    private ByteArrayOutputStream baos;

    public SimpleListener(CompletableFuture<String> data, ByteArrayOutputStream baos) {
      this.data = data;
      this.baos = baos;
    }

    @Override
    public void onOpen() {
      System.out.println("Reading data... ");
    }

    @Override
    public void onFailure(Throwable t, Response failureResponse) {
      System.err.println(t.getMessage());
      data.completeExceptionally(t);
    }

    @Override
    public void onClose(int code, String reason) {
      System.out.println("Exit with: " + code + " and with reason: " + reason);
      data.complete(baos.toString());
    }
  }

  public static void main(String[] args) {

    final String pod = "nginx";
    final String namespace = "default";
    String basePath = "/etc";
    final String command = "ls -lQ --color=never --full-time " + basePath;
//    final String command = "cat /opt/edp/hdfs1/conf/core-site.xml";

    try (ExecuteCommandOnPodExample example = new ExecuteCommandOnPodExample()) {
      String cmdOutput = example.execCommandOnPod(pod, namespace, command.split(" "));
      System.out.println(cmdOutput);

      // 解析命令执行结果并封装到 JavaBean 中
      String[] lines = cmdOutput.split("\\r?\\n");
      for (String line : lines) {
        if (!line.isEmpty() && !line.startsWith("total")) {
          FileEntry fileEntry = parseLineToBean(line, basePath);
          // 处理每个文件条目
          System.out.println(fileEntry);
        }
      }
    }

  }

  private static FileEntry parseLineToBean(String line, String basePath) {
    String[] parts = line.split("\\s+");
    String permissions = parts[0];
    String owner = parts[2];
    String group = parts[3];
    String size = parts[4];
    String date = parts[5] + " " + parts[6] + " " + parts[7];
    String name = parts[8].replace("\"", "");
    // 判断是否目录
    boolean isDir = false;
    if (line.startsWith("d")) {
      isDir = true;
    }

    String fullPath = basePath + "/" + name;

    // 创建 FileEntry 对象并设置属性
    FileEntry fileEntry = new FileEntry(isDir, fullPath, permissions, owner, group, size, date, name);
    return fileEntry;
  }

  @Data
  @AllArgsConstructor
  static class FileEntry {

    boolean isDir;
    String fullPath;
    String permissions;
    String owner;
    String group;
    String size;
    String date;
    String name;

  }
}