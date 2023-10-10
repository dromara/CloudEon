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

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class K8sFileBrowserTest {

    @Test
    public void uploadDirInPod() {
        try (KubernetesClient k8s = new KubernetesClientBuilder().build()) {
            File fileToUpload = new File("E:\\workspace\\CloudEon\\cloudeon-stack\\EDP-1.0.0\\monitor");
            k8s.pods().inNamespace("default") // <- Namespace of Pod
                    .withName("hadoop-hdfs-datanode-hdfs1-9f5f7c69f-6x2wg") // <- Name of Pod
                    .dir("/tmp/monitor-dir") // <- Path of directory inside Pod
                    .upload(fileToUpload.toPath()); // <- Local Path of directory
        }
    }

    @Test
    public void uploadFileInPod() throws FileNotFoundException {
        try (KubernetesClient client = new KubernetesClientBuilder().build()) {
            File fileToUpload = new File("E:\\workspace\\CloudEon\\cloudeon-server\\src\\main\\resources\\application.properties");
            client.pods().inNamespace("default") // <- Namespace of Pod
                    .withName("hadoop-hdfs-datanode-hdfs1-9f5f7c69f-6x2wg") // <- Name of Pod
                    .file("/tmp/application.properties") // <- Target location of copied file inside Pod
                    .upload(fileToUpload.toPath()); // <- Path of local file
        }
    }

    /**
     * Download directory from Pod
     */
    @Test
    public void downloadDirInPod() {
        try (KubernetesClient k8s = new KubernetesClientBuilder().build()) {
            File fileToDownload = new File("E:\\workspace\\CloudEon\\");
            k8s.pods().inNamespace("default") // <- Namespace of Pod
                    .withName("hadoop-hdfs-datanode-hdfs1-9f5f7c69f-6x2wg") // <- Name of Pod
                    .dir("/tmp/monitor-dir").copy(fileToDownload.toPath());
        }
    }

    @Test
    public void viewFileInPod() throws IOException {
        KubernetesClient k8s = new KubernetesClientBuilder().build();
        InputStream aDefault = k8s.pods().inNamespace("default") // <- Namespace of Pod
                .withName("hadoop-hdfs-datanode-hdfs1-9f5f7c69f-6x2wg") // <- Name of Pod
                .dir("/opt/edp/hdfs1/conf/core-site.xml") // <- Path of directory inside Pod
                .read();

        System.out.println(convertInputStreamToString(aDefault));
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();
        }
    }
}
