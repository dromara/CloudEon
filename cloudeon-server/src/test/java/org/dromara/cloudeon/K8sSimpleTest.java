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

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.crd.helmchart.HelmChart;
import org.dromara.cloudeon.processor.TaskParam;
import org.dromara.cloudeon.utils.K8sUtil;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@Slf4j
public class K8sSimpleTest {

    private KubernetesClient getClient() {
        String kubeConfig = "" +
                "" +
                "apiVersion: v1\n" +
                "clusters:\n" +
                "- cluster:\n" +
                "    certificate-authority-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUMvakNDQWVhZ0F3SUJBZ0lCQURBTkJna3Foa2lHOXcwQkFRc0ZBREFWTVJNd0VRWURWUVFERXdwcmRXSmwKY201bGRHVnpNQjRYRFRJek1URXlPVEE1TXpNek1Gb1hEVE16TVRFeU5qQTVNek16TUZvd0ZURVRNQkVHQTFVRQpBeE1LYTNWaVpYSnVaWFJsY3pDQ0FTSXdEUVlKS29aSWh2Y05BUUVCQlFBRGdnRVBBRENDQVFvQ2dnRUJBTDY3CmdjRUQwM1ZnTWZyVzJvbkpMQkZjZVE1WlFDOVFTZjJscFZoRHNpa09OQXUvNGhseHgxNmRCSDJ2T2wvSnkycWEKNnF3NmpPeXBITnFKaTNxVURBY2x2Zm9EaXdHUVFNVUw2Y0FoVGVsWENyYnFyU1YwNGJzczB0QzRNMWJlWkJ2Uwpxb2NZY1VMZ1VEN1AxQmlWU2hnR09lY3ByUm14VGpZSFJ1Q2ZtRGJ2VWRPSmdrUFQ0V2tUNWozNlNjeUdLZ2tlCm9wZFozUUd5MTVDb2p0VDJjS0pDcVhINnZkZDFwNjFMTHlCLzlBZzZDQllvdExlYUdTZUFOdHNjZDFnNE5CMGIKN3g1dWN3VmQyUW9iNUt5QlZta3ladFAvZFBDWVlwcWhFZ3o4ZldJOU1RKzBiL0pGS0Yza2w1YUtpYVljQTBpWgpuK3lrSHZQMU8vYXRjWFQ1VGUwQ0F3RUFBYU5aTUZjd0RnWURWUjBQQVFIL0JBUURBZ0trTUE4R0ExVWRFd0VCCi93UUZNQU1CQWY4d0hRWURWUjBPQkJZRUZOV2xLWG1qMnh5VFdhSCtINDRjSXBIWHdSYVRNQlVHQTFVZEVRUU8KTUF5Q0NtdDFZbVZ5Ym1WMFpYTXdEUVlKS29aSWh2Y05BUUVMQlFBRGdnRUJBQU9SUVNvWVB3Mko4bm16eVduawpQRURnRjhnVDhXRTM1WkpZSDFEVTNUdk85RDNmK1p0dEFPYkpYYjhDV3p5T1psWUptTDY3SW1sWkVnUWNBSDQrCkRBdGs3NWhBejdwM3BOV0RseUl6cDVUR1NzR1hvYzRuWDFzN3NQWXhzTVIvOU82UkJ6bUtpYXJjOWl3Yno5NkgKaW5DTHJpb2R5ZjBGNDFoNkd3TjRaL1BXTForMUt4WkMrRTZhUForWHorSVUvTklYNGFqRlJJNnJ1Q1ZDVldGdgpoVmFNRFZNSEltT0E5azQyL2xvNVVNUXFpUXpZYTllNUpyRUVHdkNzWUxGMTBqR1p5ZlRUQjBtZitrOXVLSm1hCnNnd2ZyQVFMbWhnUyt6NWhwT25XeFNhVGgzSFBBaTg1cUJFalRrS3ZZVm1hb2l0RDBvUU1xay81S0ZrczhvOXYKaG8wPQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==\n" +
                "    server: https://k8sserver:6443\n" +
                "  name: cluster.local\n" +
                "contexts:\n" +
                "- context:\n" +
                "    cluster: cluster.local\n" +
                "    user: kubernetes-admin\n" +
                "  name: kubernetes-admin@cluster.local\n" +
                "current-context: kubernetes-admin@cluster.local\n" +
                "kind: Config\n" +
                "preferences: {}\n" +
                "users:\n" +
                "- name: kubernetes-admin\n" +
                "  user:\n" +
                "    client-certificate-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURJVENDQWdtZ0F3SUJBZ0lJQnQrM25WejFzS1F3RFFZSktvWklodmNOQVFFTEJRQXdGVEVUTUJFR0ExVUUKQXhNS2EzVmlaWEp1WlhSbGN6QWVGdzB5TXpFeE1qa3dPVE16TXpCYUZ3MHlOREV5TXpBeE9URXdNREphTURReApGekFWQmdOVkJBb1REbk41YzNSbGJUcHRZWE4wWlhKek1Sa3dGd1lEVlFRREV4QnJkV0psY201bGRHVnpMV0ZrCmJXbHVNSUlCSWpBTkJna3Foa2lHOXcwQkFRRUZBQU9DQVE4QU1JSUJDZ0tDQVFFQXhvYnVYa3BtVkd5blFVYksKT1MzMXV3OXhIVm11S0xrVnB6REUxaGtITHVkY201ejFiMThMV09YMTF5ZnVlOWpadmxMa3M4ekcxT2piM0gxNQpTWFJoSkM4eG52TG5lbklpOTUvc3YvZFRyNG1DQ2ZIakx1cWRlV1pDeTFMOTF3dXNrcHFZZ2UyNHdJNXQ5bjErCkc2eDhhdXVMZEhodkkyRzJ0QW9iTGJCQ2FjVGdsQWhnTjQ4NDNYd0hsemZxV2xya1hjNmJWK0hrQmVTZ09OQ2UKWVpqNGFJRDArRGtnTGVkWkVaY1NJREJYUEI2ZmZkRzRwL3VtSHJJWjFxQUhqbnJHOUxhMXBZL1Rja0Q0SkJsdgpWSEkzZHlPdEcvY0FtTklqRzlzWWlKVk1ybmhJYUJoWFZ0bFJTZVVhb00rUmltWmdJL01iblhqbE0rdVhuV3pjCittbDB5d0lEQVFBQm8xWXdWREFPQmdOVkhROEJBZjhFQkFNQ0JhQXdFd1lEVlIwbEJBd3dDZ1lJS3dZQkJRVUgKQXdJd0RBWURWUjBUQVFIL0JBSXdBREFmQmdOVkhTTUVHREFXZ0JUVnBTbDVvOXNjazFtaC9oK09IQ0tSMThFVwprekFOQmdrcWhraUc5dzBCQVFzRkFBT0NBUUVBSlBUaE04WE1hejAwTWlLbysvME50WkVUcEdrSmdhbndCRFF1CkhhTGxKN2FCcWJDekZoUUFYbEJQdVdPdWV1VkJncFlwaGRjYTUzRUQvMS9ReElCVFZmYjdpSWxJN3FrbGdrWWsKcFZtODczQ2pWV2lsRVlLeWdiUW5iTCtHMXVFRGxkMTRvdmtFcG92QjRLeXUwTTRNOHZmakxHSHd5UzVmKzlrRQpBcDdtNkczTTArTTN4aW8xcEFRbUR5c2F6QzdhaklSdmZrNHBnbXllbkVKRkhxUWwrcVNDVXM2Y2duQnRmVnZ5CnRkK0J0eStPRU9JK2N3eGROYVpnNHdRQk1EYVJwSkdiZHZqcklCT0E3SVBCckh2VWs4QWFlTEE2SHJVVjZZUmcKVjZZcXQxalJ2WVBYUUJMZno0eEc2OWU2OGdHRFlBaENNc01OdGxUOStVRE1GbkpHZVE9PQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==\n" +
                "    client-key-data: LS0tLS1CRUdJTiBSU0EgUFJJVkFURSBLRVktLS0tLQpNSUlFcEFJQkFBS0NBUUVBeG9idVhrcG1WR3luUVViS09TMzF1dzl4SFZtdUtMa1ZwekRFMWhrSEx1ZGNtNXoxCmIxOExXT1gxMXlmdWU5alp2bExrczh6RzFPamIzSDE1U1hSaEpDOHhudkxuZW5JaTk1L3N2L2RUcjRtQ0NmSGoKTHVxZGVXWkN5MUw5MXd1c2twcVlnZTI0d0k1dDluMStHNng4YXV1TGRIaHZJMkcydEFvYkxiQkNhY1RnbEFoZwpONDg0M1h3SGx6ZnFXbHJrWGM2YlYrSGtCZVNnT05DZVlaajRhSUQwK0RrZ0xlZFpFWmNTSURCWFBCNmZmZEc0CnAvdW1IcklaMXFBSGpuckc5TGExcFkvVGNrRDRKQmx2VkhJM2R5T3RHL2NBbU5Jakc5c1lpSlZNcm5oSWFCaFgKVnRsUlNlVWFvTStSaW1aZ0kvTWJuWGpsTSt1WG5XemMrbWwweXdJREFRQUJBb0lCQVFERVVtRGMrOFFRRHRhagpkY2E3SHRrWFlDRGkvbkY4c2ZOWXY2Q1hmRzZmRW9xQkZJbWQxaWFaaGVkRUdxZjY3eW44eldwKzU2MWtsTlgvClNGR3RzeG54TjM1aGlpSWc4MGZqQ3RLTHo2Q0JRWUZJSmwwY1kwVFE3YkIxOHg4MURzVmN3T2E1N2dTNjN0NmQKVitKaXFZTHNGUHgyZERhcmpaQ29vQ25hVjZpRmdENnNITXRjMVhQalNrOUhsVHROWkJKUmhEc0l3eTBzbVcyegoveWw2clFLcTA3M1ZGNDFoanQrSG5uYUl6ZFowTnl6VnVwQVIzeHpQcnF1Vi9VN2s3a2VsV08wNEpuQkJOQVBNCmtqcWgyamZCOTNxOEhxVE5sRXF3dnJXN0pnS1ZSUjJ2QnFVNUNTSjk3Znk3TVNvZ1NwT1NoQTUxVndzaWpVK3UKUDY4N3BQTzVBb0dCQU5mSTloTng2dHJVWGhxRWkxSkpJMUdiRXF0TURsREM0T1RkTFIrRzd3WXpmZlpLZ2VGaApYRy8ySW5VMHhkMHhodWxnYzlYaXBZbEJyeTg1M1N5NEdVckRYcFZSWDFEQ29tY29hRitiM25sNFRlUU9KeDk3CjA4b1VTRHMrL2ZMYWlsYVlqSmVwaWpLcm5LZGEvQU5VUXE0M2FRTzNzdXhneWRyb2h1ZFZNYVJ2QW9HQkFPdUcKbVdIcEN4WUUyNkkrUnVob3dNbTRPSWhUU0VuRmljdkcxeERVWDVlMkxtaE4xNWd4UzhlOU0vVW51VjdVUXNubApwRHpDZE1Bd0NETlpxbXVRSTlhTVQrTmQvYS96aGlLWm9wRmJwUHBVL3p3UDBUSHRSTGliOStHdk9EZnZaa0dGCnhlcWRrUTVXL1lMTHV1enl1SWk5ZmRzcjdMQk1reXViUnRWRThqdGxBb0dCQU1UZWhDT1plenZMSlUvc1BYQUYKYWtPNXgzNmhGUzU1bmRVd05VcmVRSlRYeGNRK2xlQ2FnMHRpdEcxYWlHc1dGSkEzZjNkVUlOTHBLbnRidjM1ZApPRFlOcU8xeUlCUCtmMHkrZ1BzNXFmQUk0b2Qrb2hNSFZtSzN2bTdQT2NHbndTN2dYdVMvdVdZaFMvc2o5MmpTCkowUHJLZFJLZE9OVUt0V2Q3L1orczV2aEFvR0FZeXVpcStwZmc1NzZCLytuQmJjTjdpSUdrOGhWZU5LWGFkbEkKdDBwbEVkRmhDd1F6MGw1M1pSd1NvNWhkWWtPSDk5RWM3WVNIZW1EL0l2Z1BYUWt0UGVxSXZOalh0OTJYVGp4WQppbElIVG15NXA4V1ZOU3VOc3huaEx3TURiZkg4b1h3OVVNT1Z6MjdyZ2NaYzUrWnZzd3Z1MFhsV1NRbUZNbWhJClZBYWE1RTBDZ1lBRlh5M3dlZWFYVG9pTWJ5d05td05ocHR0WVNZMXJYd1pXSldFdllvcHpLYUM0emVVRHNqeG4KVU1vWHFQcWNNaHdNRUYwS1pNU0k3bGRDOWhDeHVLUGxYQ0RUZnIrdHIxTktrcmtwTTNxSEpVREFVNHRaYjFZawpYMGFwVDZsbVB0YUlrdy9MUGZCdXBsMjE3RkI1V09ZaFFRSDZYS1IxZk9ER3hpQVY0dFZXVnc9PQotLS0tLUVORCBSU0EgUFJJVkFURSBLRVktLS0tLQo=" +
                "";
        return K8sUtil.getKubernetesClient(kubeConfig);
    }

    @Test
    public void listHelm() {
        KubernetesClient client = getClient();
        List<HelmChart> helmChartList = client.resources(HelmChart.class).inNamespace("monitoring").list().getItems();
        for (HelmChart helmChart : helmChartList) {
            log.info(JSON.toJSONString(helmChart, SerializerFeature.PrettyFormat));
        }
    }

    private String helmChartYamlStr = "" +
            "apiVersion: helm.cattle.io/v1\n" +
            "kind: HelmChart\n" +
            "metadata:\n" +
            "  name: prometheus-stack \n" +
            "  namespace: monitoring\n" +
            "spec:\n" +
            "  #repo: https://charts.grapps.cn\n" +
            "  #repo: https://helm-charts.itboon.top/prometheus-community\n" +
            "  #repo: https://prometheus-community.github.io/helm-charts\n" +
            "  repo: https://files.linshenkx.cn:33443/charts\n" +
            "  chart: kube-prometheus-stack\n" +
            "  targetNamespace: monitoring\n" +
            "  set:\n" +
            "    namespaceOverride: \"monitoring\"\n" +
            "    alertmanager.service.type: \"NodePort\"\n" +
            "    grafana.defaultDashboardsTimezone: \"Asia/Shanghai\"\n" +
            "    grafana.adminPassword: \"1qaz@WSX\"\n" +
            "    grafana.sidecar.dashboards.folderAnnotation: \"folder\"\n" +
            "    grafana.sidecar.dashboards.provider.allowUiUpdates: \"true\"\n" +
            "    grafana.service.nodePort: \"30902\"\n" +
            "    grafana.service.type: \"NodePort\"\n" +
            "    prometheus.service.nodePort: \"30900\"\n" +
            "    prometheus.service.type: \"NodePort\"\n" +
            "    prometheus.prometheusSpec.additionalScrapeConfigsSecret.enabled: \"true\"\n" +
            "    prometheus.prometheusSpec.additionalScrapeConfigsSecret.name: \"additional-scrape-configs\"\n" +
            "    prometheus.prometheusSpec.additionalScrapeConfigsSecret.key: \"prometheus-additional.yaml\"\n" +
            "    kubelet.serviceMonitor.cAdvisorMetricRelabelings: \"\"\n" +
            "" +
            "";
    private String helmChartYamlStr2 = "" +
            "apiVersion: helm.cattle.io/v1\n" +
            "kind: HelmChart\n" +
            "metadata:\n" +
            "  name: traefik \n" +
            "  namespace: bdata \n" +
            "spec:\n" +
            "  chart: stable/traefik\n" +
            "  set:\n" +
            "    rbac.enabled: \"true\"\n" +
            "    ssl.enabled: \"true\"" +
            "";

    @Test
    public void deleteHelm() {
        KubernetesClient client = getClient();
        ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata> loadResource = client.load(IoUtil.toUtf8Stream(helmChartYamlStr));
        loadResource.delete();
        loadResource.waitUntilCondition(Objects::isNull, 3, TimeUnit.MINUTES);
//        MixedOperation<HelmChart, KubernetesResourceList<HelmChart>, Resource<HelmChart>> helmResourceClient = client.resources(HelmChart.class);
//        Resource<HelmChart> helmChartResource = helmResourceClient.load(IoUtil.toUtf8Stream(helmChartYamlStr));
//        String lastJobName = helmChartResource.get().getStatus().getJobName();
//        helmChartResource.delete();
//        helmChartResource.waitUntilCondition(Objects::isNull, 10, TimeUnit.MINUTES);

//        //等待状态发生改变，否则获取到的还是之前的任务，如install
//        helmChartResource.waitUntilCondition(r -> !lastJobName.equals(r.getStatus().getJobName()), 10, TimeUnit.MINUTES);
//        HelmChart helmChart = helmChartResource.get();
//        String resourceName = helmChart.getMetadata().getName();
//        String resourceNamespace = helmChart.getMetadata().getNamespace();
//        log.info("jobName: " + helmChart.getStatus().getJobName());
//        K8sUtil.waitForJobCompleted(resourceNamespace, helmChart.getStatus().getJobName(), client, log, 600);
//
//        log.info(resourceNamespace + " " + resourceName);
    }

    @Test
    public void applyHelm() {
        KubernetesClient client = getClient();

        MixedOperation<HelmChart, KubernetesResourceList<HelmChart>, Resource<HelmChart>> helmResourceClient = client.resources(HelmChart.class);
        Resource<HelmChart> helmChartResource = helmResourceClient.load(IoUtil.toUtf8Stream(helmChartYamlStr));
//        helmChartResource.create();
//        helmChartResource.waitUntilCondition(r -> r.getStatus() != null && r.getStatus().getJobName() != null, 10, TimeUnit.MINUTES);
//        HelmChart helmChart = helmChartResource.get();
        HelmChart helmChart = helmChartResource.item();
        String resourceName = helmChart.getMetadata().getName();
        String resourceNamespace = helmChart.getMetadata().getNamespace();
        String jobName = StrUtil.format("helm-{}-{}", "install", helmChart.getMetadata().getName());
        log.info("jobName: " + jobName);
//        K8sUtil.waitForJobCompleted(resourceNamespace, helmChart.getStatus().getJobName(), client, log, 600);
        TaskParam taskParam = new TaskParam();
//        K8sUtil.waitForJobCompleted(helmChartResource::create, taskParam, resourceNamespace, jobName, client, log, 600);

        log.info(resourceNamespace + " " + resourceName);
    }

    @Test
    public void waitForJobCompleted() throws InterruptedException {
        String modelYamlStr = "" +
                "" +
                "apiVersion: batch/v1\n" +
                "kind: Job\n" +
                "metadata:\n" +
                "  name: random-exit-job  \n" +
                "spec:\n" +
                "  completions : 2\n" +
//                "  backoffLimit: 2\n" +
                "  template:\n" +
                "    spec:\n" +
                "      restartPolicy: Never\n" +
                "      containers:\n" +
                "      - name: random-exit\n" +
                "        image: centos:7\n" +
                "        command: [\"/bin/bash\"]  \n" +
                "        args:\n" +
                "        - -c\n" +
                "        - |\n" +
                "          date && sleep 1\n" +
                "          if [[ $(($RANDOM % 2)) -eq 0 ]]; then  \n" +
                "            exit 0\n" +
                "          else\n" +
                "            exit 1\n" +
                "          fi\n" +
                "";
        KubernetesClient client = getClient();
        ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata> loadResource = client.load(IoUtil.toUtf8Stream(modelYamlStr));
        if (loadResource.get().get(0) != null) {
            loadResource.delete();
            }
        Job job = client.batch().v1().jobs().load(IoUtil.toUtf8Stream(modelYamlStr)).item();
        K8sUtil.waitForJobCompleted(() -> loadResource.forceConflicts().serverSideApply(), null, client, job, 600);
    }



    @Test
    public void getNS() {
        Config config = Config.fromKubeconfig("apiVersion: v1\n" +
                "clusters:\n" +
                "- cluster:\n" +
                "    certificate-authority-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUM1ekNDQWMrZ0F3SUJBZ0lCQURBTkJna3Foa2lHOXcwQkFRc0ZBREFWTVJNd0VRWURWUVFERXdwcmRXSmwKY201bGRHVnpNQjRYRFRJek1ETXlNVEUwTkRRek9Gb1hEVE16TURNeE9ERTBORFF6T0Zvd0ZURVRNQkVHQTFVRQpBeE1LYTNWaVpYSnVaWFJsY3pDQ0FTSXdEUVlKS29aSWh2Y05BUUVCQlFBRGdnRVBBRENDQVFvQ2dnRUJBSzFMCno5YmpndlBtbFlMeDFHU2ZiWFZzZ29GODRDRWxSSHptWWg2UjdIRTBGUHlSSHFsd2dQQ0tvMHp5dm5rQ1Q5b2oKQm4rYmprTUdVUDdsWVI5dzBGZEF4bHg1ZTYzZndKbDJ4azRuZk9ub3kxOHdWekpWYytQRXRoRC90MnhZREYxTQpYQkxGaXB6eHo1NFBFNlhIK0w1R0dhYWg3U3RIVUhBakdUdXRJSU5ER2diSCtEMzcwZklsRTEzWDVmK0N1K24wCjBxa21KVXhyRTBSVUpXeDlLYXRJZGY0YXhUZ0xYQnpWZy9XMnh6WFAxejBnOWcrUXFNUnlubmhuNTRYcE1BR1cKa1BSUjJPb0toS1NtSXh5K055b2ViR2EzcGpuaVpBc1RPU0FYY2NNMTBhZE9iU2RLbnEzUG1VRk9CVWpnT2o3awpoUjQvM1V1T1VEZUdlUDFYQUZNQ0F3RUFBYU5DTUVBd0RnWURWUjBQQVFIL0JBUURBZ0trTUE4R0ExVWRFd0VCCi93UUZNQU1CQWY4d0hRWURWUjBPQkJZRUZKaGYrM2cvREtaN0xpM0VFd1FkMXR1Y1RUNjRNQTBHQ1NxR1NJYjMKRFFFQkN3VUFBNElCQVFCU3FZaUtlanYwVjJnb1hqRTloUjQreUdiWm8yaHlIMVNhcVNFVURhZG1pQzhQUURregpHRHFMajl5ZkplclQ2djBGQlNpSkJLVlBPakFxUE5xcC9ZdElyYU5OVTljbFI3QXZ3T3NldWtLNlBiSmhjQWErCjhpRWV4UzdSa3ROSjNONnl2Nkc5Q0ZxMzFpNzNqenNxM1hwM0xoTzBSTk9sWkVUdG01Zm5xZENFVVp0Zm5uYmsKanU2Y1RGandrM0pSMm9XbXZVSHVEU3FUMW1JQkZqS1JQV21IYTBNNWZvMkxtck8zaXhHOWF6c1FMVzBmVlRsNApFWnBPMnJGTnd3Y01YbHJXK3hQVFZNS1dyWmRBNGlOSnp6c1A1ZlJmVG5SWkFseElHMm15QWh2OFR2L0xyVXg3CnozbXAzSUNXVGJaUHRMdi9PNWcrMVhVZURUNTZyejRLQkdQVQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==\n" +
                "    server: https://192.168.197.149:6443\n" +
                "  name: cluster.local\n" +
                "contexts:\n" +
                "- context:\n" +
                "    cluster: cluster.local\n" +
                "    user: kubernetes-admin\n" +
                "  name: kubernetes-admin@cluster.local\n" +
                "current-context: kubernetes-admin@cluster.local\n" +
                "kind: Config\n" +
                "preferences: {}\n" +
                "users:\n" +
                "- name: kubernetes-admin\n" +
                "  user:\n" +
                "    client-certificate-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURJVENDQWdtZ0F3SUJBZ0lJSEVrNTZPTURucmd3RFFZSktvWklodmNOQVFFTEJRQXdGVEVUTUJFR0ExVUUKQXhNS2EzVmlaWEp1WlhSbGN6QWVGdzB5TXpBek1qRXhORFEwTXpoYUZ3MHlOREF6TWpBeE5EUTBNemxhTURReApGekFWQmdOVkJBb1REbk41YzNSbGJUcHRZWE4wWlhKek1Sa3dGd1lEVlFRREV4QnJkV0psY201bGRHVnpMV0ZrCmJXbHVNSUlCSWpBTkJna3Foa2lHOXcwQkFRRUZBQU9DQVE4QU1JSUJDZ0tDQVFFQXZhcjlCSHpKT3E5ejJhWkMKdVE1SE1McEJpMG54MTBPeUJHOEpsYUVNRlZLMlJmd0NVY2h3ak84dDk3Q2VTdGRFQVlKUFlJZGp6L0hmT3RRbwpQVlAzVGc5MHN0UkQ1aU9Kc29WNEwyL2l4aUNxOHJYalFPamhjeEFNcEJ6Rk95bkd1TWJ6dHRLeVlURElaMG5yCldvSWNPUGJ3dzkrVm9oWGFVb1B1ZjhvbFZmazVMT1QreWYraW5FdnlhWlBmeU5ES0VQQTJVeXFvVEVheWNjZFIKNmROZ2VZQ0VPQ2grZ2lacVVrQUMyOFk0RVVWakhuQXpHdU56eHRnUFdpbUxwamxKY2ltck00T2VJeENRc2QwYQpzZFFKdUZYWURoUEhrVzM1U0lsOTQ4a254MnRNaEdadGg1Q043V0wxSy9wbUF6bVpNZlhqaEgxT3VYbGtuQjJLClVWdmJqUUlEQVFBQm8xWXdWREFPQmdOVkhROEJBZjhFQkFNQ0JhQXdFd1lEVlIwbEJBd3dDZ1lJS3dZQkJRVUgKQXdJd0RBWURWUjBUQVFIL0JBSXdBREFmQmdOVkhTTUVHREFXZ0JTWVgvdDRQd3ltZXk0dHhCTUVIZGJibkUwKwp1REFOQmdrcWhraUc5dzBCQVFzRkFBT0NBUUVBTC9kRUtBUEdQK29kSENZYTlxMDVWRk9pNzZKa3FGbzB1cU9WCklXR1RxeUoreU1WUFhMaU4yZGRhRmVqK2xqSUg0SkpqeHo5RXlxUU1ieWJYSmRPTHN0V2E4K3RMNVo5ZXQyQkoKVjl2eXQ0UjhVakRrSzZlVk5oZXB4WVovSWp0V0lJTk1rV2d3aktyOUIxc2NiK0ZuOXlZbGZXWHk4d0s1bzg5RApKRDBtZlcvL1B2WGxFc0VkVmVkaThmTzE0SEVyNlZIZmJ3dG1XZVZLdVhLWnEyeEFPWXNROHNLUXRGcFRoQTNuCkIvWkRrV280Qkd5a2ptQ3Q5UFRWVDN4REo1RjdhbzRjTmpPZDUwMEtac0szRHhRZXRvbUR3V3ZJTGtxbXRJczcKajcxSWd6Z0JsUzZDQklEREZRQVc1UENpNUlJRUhzTFAwdWNROFNwZUpzaEVGa0hMNnc9PQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==\n" +
                "    client-key-data: LS0tLS1CRUdJTiBSU0EgUFJJVkFURSBLRVktLS0tLQpNSUlFb3dJQkFBS0NBUUVBdmFyOUJIekpPcTl6MmFaQ3VRNUhNTHBCaTBueDEwT3lCRzhKbGFFTUZWSzJSZndDClVjaHdqTzh0OTdDZVN0ZEVBWUpQWUlkanovSGZPdFFvUFZQM1RnOTBzdFJENWlPSnNvVjRMMi9peGlDcThyWGoKUU9qaGN4QU1wQnpGT3luR3VNYnp0dEt5WVRESVowbnJXb0ljT1Bid3c5K1ZvaFhhVW9QdWY4b2xWZms1TE9UKwp5ZitpbkV2eWFaUGZ5TkRLRVBBMlV5cW9URWF5Y2NkUjZkTmdlWUNFT0NoK2dpWnFVa0FDMjhZNEVVVmpIbkF6Ckd1Tnp4dGdQV2ltTHBqbEpjaW1yTTRPZUl4Q1FzZDBhc2RRSnVGWFlEaFBIa1czNVNJbDk0OGtueDJ0TWhHWnQKaDVDTjdXTDFLL3BtQXptWk1mWGpoSDFPdVhsa25CMktVVnZialFJREFRQUJBb0lCQUNRUUYrMjdnRk45T3N6ZQpkUDlVdktxQ0w2WTVXQmR6RXEwUEk4WmtpYlNnTm5JV0dhYk5Nc0ZKVlBjc2lOeDRFOEVwc3NnSzFpcWF0YlFzCjFMM2Nja0JRWmdMK295NW1BVytGT3pYaDB6K1N4STVEa1VNdFJIaXBTNDRFdm1laWFOdUhVSjJwY0N0VXFEWWoKY3ZHUm5hWWpKZUpJWjk0YXc1ays1cUU3b1YrNElHcjdETUdUM2dETk9MamZMS3E5Zjl1ZFREUkJwZkltbFdPNQpLU0d5b0xFSlJXSkd0SDUxdkV4aWN6TUMzdzhKNnFWSkUvQ09jamdSV3VpMEMrVGZtMUdUcjUwSk1VYUtiTGhyClpFYUMwUEhETVVRMmxzUUVFbmMwRmJvZmRxcytvMTFndDh6QjlEUDNHVzdCSjd3QkNwNlRNRm5DWk5DUlZZNWQKeUlyWnIwRUNnWUVBelZuWktHVThGOXZROFRrTmdHOHQxaDIvRURYODZ5UlZDeVhmQTBuaVZOUFpJVEQrRGM1QQozbkp4M052eWNnT1l4U0ttVkgzYUU0UDJpSUtWMnR2bEdFM1h3VjBxbUM5ditaVFBZdjZBVnQ5bmxjcllGWUV0CkRnb2xrTnUvQzZydUtDbG1zeGtheXcyR3drSTlLTmtBREhEWHhyM0FoRlltMEpGRHQ2NDRwOTBDZ1lFQTdITGoKNTdoTnlyWmpneEZDUnp1dUNOSUgxcU5WckNaUFlwYzdPZURONHZURkd3SjYyc1M4OU1wY3hpd1Q2UytJRGh2cwoyZitlckxIK0FGYTBaN0l6SnpIYndxcFlwYVY3MU45THRKUnRQd0lFSGY1MFcwc1lZQ0pMbFNPMVhxT1ozdVZDClR0cFNVczdkTFVRMERsSTFUZy9JOENmVmlNL2Y0TWZld1ZpK0gzRUNnWUJQbFkyeXVTRkVBZDRGVHQ0cnMycnAKTzVnTHVWQ3U5T0s4c2sydTRaaUUxYUdsMm0zcmZjN29KeVIzdXdwSUk1cTJkQXBRWG9JQTVEak1pUWQ0elpZSgpDRW9nMTNHbGoyVHZManY5bXJLMGVGcVYxQXBRczBKNTJYYmJvRDUzVUNTQ2ppRU9NaUdQSmt2ZXgzc2FkSmN2Ck95QjFGcDhnNnA2YVlHSUZNdEVrUlFLQmdRQ3ZvMkJiL25INnhLVUM5VTBRY09xRUx0QVh4bGliZWhHNklMQ2oKKzdPMGhUSHRNRmhtTFlKWExBTGlTbGUzL2RESStrRmtaaGRPSFNHYXlzMVR3ZkZ4aWYyK2lwOHkzTXd4Z25WUAovSGx5Tm1Nc2pKbU9QeWdxTVErSUIzQndqb0o4S2p5cEtrL0FwMTF3aEp0T2tBNThvQWtaSzkzWXRPR09yYWx3CllpVklZUUtCZ0ZsMEN0RlhRZmlHN2VubzdsRWVlV3dSRXFxL01vZGVPcjRtRUZJWk84VEZwUHlpK1NuRklRUVEKQWdQRW5SdnZtb29IM1VDTzNRaUU3UHA5VzdrTTd6ZG1IZ09RTWNZK1NLMkdqU21BYzJXQWlGWkM0aXduTkV6QQp1bE5wLzh4d1BtbS9ZTGwyQTR3ckh4ZE5CQkhmRUlhMDZha21TbmRVTS9Uck9vQjVCTG1KCi0tLS0tRU5EIFJTQSBQUklWQVRFIEtFWS0tLS0tCg==");
        KubernetesClient client = new KubernetesClientBuilder().withConfig(config).build();

        // 检查namespace是否存在
        Namespace namespace = client.namespaces().withName("default2").get();
        System.out.printf("Namespace %s exists: %s%n", namespace.getMetadata().getName(), namespace != null);

    }

    @Test
    public void logDeployment() {
        KubernetesClient client = getClient();
        Deployment deployment = client.apps().deployments().inNamespace("bdata").withName("helm-controller-helm-controller").get();
        LogWatch logWatch = client.apps().deployments().inNamespace("bdata").withName("helm-controller-helm-controller").watchLog(System.out);
//        try (LogWatch logWatch = client.apps().deployments().inNamespace("bdata").withName("helm-controller-helm-controller").watchLog(System.out);
//             BufferedReader reader = new BufferedReader(new InputStreamReader(logWatch.getOutput()))
//        ) {
//            String line = reader.readLine();
//            while (line != null) {
//                log.info("Log of deployment " + deployment.getMetadata().getName() + "> " + line);
//                line = reader.readLine();
//            }
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//        }
        ThreadUtil.safeSleep(10000);
    }

    @Test
    public void getPodsEvent() throws ParseException {
        Config config = Config.fromKubeconfig("apiVersion: v1\n" +
            "clusters:\n" +
            "- cluster:\n" +
            "    certificate-authority-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUM1ekNDQWMrZ0F3SUJBZ0lCQURBTkJna3Foa2lHOXcwQkFRc0ZBREFWTVJNd0VRWURWUVFERXdwcmRXSmwKY201bGRHVnpNQjRYRFRJek1ETXlNVEUwTkRRek9Gb1hEVE16TURNeE9ERTBORFF6T0Zvd0ZURVRNQkVHQTFVRQpBeE1LYTNWaVpYSnVaWFJsY3pDQ0FTSXdEUVlKS29aSWh2Y05BUUVCQlFBRGdnRVBBRENDQVFvQ2dnRUJBSzFMCno5YmpndlBtbFlMeDFHU2ZiWFZzZ29GODRDRWxSSHptWWg2UjdIRTBGUHlSSHFsd2dQQ0tvMHp5dm5rQ1Q5b2oKQm4rYmprTUdVUDdsWVI5dzBGZEF4bHg1ZTYzZndKbDJ4azRuZk9ub3kxOHdWekpWYytQRXRoRC90MnhZREYxTQpYQkxGaXB6eHo1NFBFNlhIK0w1R0dhYWg3U3RIVUhBakdUdXRJSU5ER2diSCtEMzcwZklsRTEzWDVmK0N1K24wCjBxa21KVXhyRTBSVUpXeDlLYXRJZGY0YXhUZ0xYQnpWZy9XMnh6WFAxejBnOWcrUXFNUnlubmhuNTRYcE1BR1cKa1BSUjJPb0toS1NtSXh5K055b2ViR2EzcGpuaVpBc1RPU0FYY2NNMTBhZE9iU2RLbnEzUG1VRk9CVWpnT2o3awpoUjQvM1V1T1VEZUdlUDFYQUZNQ0F3RUFBYU5DTUVBd0RnWURWUjBQQVFIL0JBUURBZ0trTUE4R0ExVWRFd0VCCi93UUZNQU1CQWY4d0hRWURWUjBPQkJZRUZKaGYrM2cvREtaN0xpM0VFd1FkMXR1Y1RUNjRNQTBHQ1NxR1NJYjMKRFFFQkN3VUFBNElCQVFCU3FZaUtlanYwVjJnb1hqRTloUjQreUdiWm8yaHlIMVNhcVNFVURhZG1pQzhQUURregpHRHFMajl5ZkplclQ2djBGQlNpSkJLVlBPakFxUE5xcC9ZdElyYU5OVTljbFI3QXZ3T3NldWtLNlBiSmhjQWErCjhpRWV4UzdSa3ROSjNONnl2Nkc5Q0ZxMzFpNzNqenNxM1hwM0xoTzBSTk9sWkVUdG01Zm5xZENFVVp0Zm5uYmsKanU2Y1RGandrM0pSMm9XbXZVSHVEU3FUMW1JQkZqS1JQV21IYTBNNWZvMkxtck8zaXhHOWF6c1FMVzBmVlRsNApFWnBPMnJGTnd3Y01YbHJXK3hQVFZNS1dyWmRBNGlOSnp6c1A1ZlJmVG5SWkFseElHMm15QWh2OFR2L0xyVXg3CnozbXAzSUNXVGJaUHRMdi9PNWcrMVhVZURUNTZyejRLQkdQVQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==\n" +
            "    server: https://192.168.197.149:6443\n" +
            "  name: cluster.local\n" +
            "contexts:\n" +
            "- context:\n" +
            "    cluster: cluster.local\n" +
            "    user: kubernetes-admin\n" +
            "  name: kubernetes-admin@cluster.local\n" +
            "current-context: kubernetes-admin@cluster.local\n" +
            "kind: Config\n" +
            "preferences: {}\n" +
            "users:\n" +
            "- name: kubernetes-admin\n" +
            "  user:\n" +
            "    client-certificate-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURJVENDQWdtZ0F3SUJBZ0lJSEVrNTZPTURucmd3RFFZSktvWklodmNOQVFFTEJRQXdGVEVUTUJFR0ExVUUKQXhNS2EzVmlaWEp1WlhSbGN6QWVGdzB5TXpBek1qRXhORFEwTXpoYUZ3MHlOREF6TWpBeE5EUTBNemxhTURReApGekFWQmdOVkJBb1REbk41YzNSbGJUcHRZWE4wWlhKek1Sa3dGd1lEVlFRREV4QnJkV0psY201bGRHVnpMV0ZrCmJXbHVNSUlCSWpBTkJna3Foa2lHOXcwQkFRRUZBQU9DQVE4QU1JSUJDZ0tDQVFFQXZhcjlCSHpKT3E5ejJhWkMKdVE1SE1McEJpMG54MTBPeUJHOEpsYUVNRlZLMlJmd0NVY2h3ak84dDk3Q2VTdGRFQVlKUFlJZGp6L0hmT3RRbwpQVlAzVGc5MHN0UkQ1aU9Kc29WNEwyL2l4aUNxOHJYalFPamhjeEFNcEJ6Rk95bkd1TWJ6dHRLeVlURElaMG5yCldvSWNPUGJ3dzkrVm9oWGFVb1B1ZjhvbFZmazVMT1QreWYraW5FdnlhWlBmeU5ES0VQQTJVeXFvVEVheWNjZFIKNmROZ2VZQ0VPQ2grZ2lacVVrQUMyOFk0RVVWakhuQXpHdU56eHRnUFdpbUxwamxKY2ltck00T2VJeENRc2QwYQpzZFFKdUZYWURoUEhrVzM1U0lsOTQ4a254MnRNaEdadGg1Q043V0wxSy9wbUF6bVpNZlhqaEgxT3VYbGtuQjJLClVWdmJqUUlEQVFBQm8xWXdWREFPQmdOVkhROEJBZjhFQkFNQ0JhQXdFd1lEVlIwbEJBd3dDZ1lJS3dZQkJRVUgKQXdJd0RBWURWUjBUQVFIL0JBSXdBREFmQmdOVkhTTUVHREFXZ0JTWVgvdDRQd3ltZXk0dHhCTUVIZGJibkUwKwp1REFOQmdrcWhraUc5dzBCQVFzRkFBT0NBUUVBTC9kRUtBUEdQK29kSENZYTlxMDVWRk9pNzZKa3FGbzB1cU9WCklXR1RxeUoreU1WUFhMaU4yZGRhRmVqK2xqSUg0SkpqeHo5RXlxUU1ieWJYSmRPTHN0V2E4K3RMNVo5ZXQyQkoKVjl2eXQ0UjhVakRrSzZlVk5oZXB4WVovSWp0V0lJTk1rV2d3aktyOUIxc2NiK0ZuOXlZbGZXWHk4d0s1bzg5RApKRDBtZlcvL1B2WGxFc0VkVmVkaThmTzE0SEVyNlZIZmJ3dG1XZVZLdVhLWnEyeEFPWXNROHNLUXRGcFRoQTNuCkIvWkRrV280Qkd5a2ptQ3Q5UFRWVDN4REo1RjdhbzRjTmpPZDUwMEtac0szRHhRZXRvbUR3V3ZJTGtxbXRJczcKajcxSWd6Z0JsUzZDQklEREZRQVc1UENpNUlJRUhzTFAwdWNROFNwZUpzaEVGa0hMNnc9PQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==\n" +
            "    client-key-data: LS0tLS1CRUdJTiBSU0EgUFJJVkFURSBLRVktLS0tLQpNSUlFb3dJQkFBS0NBUUVBdmFyOUJIekpPcTl6MmFaQ3VRNUhNTHBCaTBueDEwT3lCRzhKbGFFTUZWSzJSZndDClVjaHdqTzh0OTdDZVN0ZEVBWUpQWUlkanovSGZPdFFvUFZQM1RnOTBzdFJENWlPSnNvVjRMMi9peGlDcThyWGoKUU9qaGN4QU1wQnpGT3luR3VNYnp0dEt5WVRESVowbnJXb0ljT1Bid3c5K1ZvaFhhVW9QdWY4b2xWZms1TE9UKwp5ZitpbkV2eWFaUGZ5TkRLRVBBMlV5cW9URWF5Y2NkUjZkTmdlWUNFT0NoK2dpWnFVa0FDMjhZNEVVVmpIbkF6Ckd1Tnp4dGdQV2ltTHBqbEpjaW1yTTRPZUl4Q1FzZDBhc2RRSnVGWFlEaFBIa1czNVNJbDk0OGtueDJ0TWhHWnQKaDVDTjdXTDFLL3BtQXptWk1mWGpoSDFPdVhsa25CMktVVnZialFJREFRQUJBb0lCQUNRUUYrMjdnRk45T3N6ZQpkUDlVdktxQ0w2WTVXQmR6RXEwUEk4WmtpYlNnTm5JV0dhYk5Nc0ZKVlBjc2lOeDRFOEVwc3NnSzFpcWF0YlFzCjFMM2Nja0JRWmdMK295NW1BVytGT3pYaDB6K1N4STVEa1VNdFJIaXBTNDRFdm1laWFOdUhVSjJwY0N0VXFEWWoKY3ZHUm5hWWpKZUpJWjk0YXc1ays1cUU3b1YrNElHcjdETUdUM2dETk9MamZMS3E5Zjl1ZFREUkJwZkltbFdPNQpLU0d5b0xFSlJXSkd0SDUxdkV4aWN6TUMzdzhKNnFWSkUvQ09jamdSV3VpMEMrVGZtMUdUcjUwSk1VYUtiTGhyClpFYUMwUEhETVVRMmxzUUVFbmMwRmJvZmRxcytvMTFndDh6QjlEUDNHVzdCSjd3QkNwNlRNRm5DWk5DUlZZNWQKeUlyWnIwRUNnWUVBelZuWktHVThGOXZROFRrTmdHOHQxaDIvRURYODZ5UlZDeVhmQTBuaVZOUFpJVEQrRGM1QQozbkp4M052eWNnT1l4U0ttVkgzYUU0UDJpSUtWMnR2bEdFM1h3VjBxbUM5ditaVFBZdjZBVnQ5bmxjcllGWUV0CkRnb2xrTnUvQzZydUtDbG1zeGtheXcyR3drSTlLTmtBREhEWHhyM0FoRlltMEpGRHQ2NDRwOTBDZ1lFQTdITGoKNTdoTnlyWmpneEZDUnp1dUNOSUgxcU5WckNaUFlwYzdPZURONHZURkd3SjYyc1M4OU1wY3hpd1Q2UytJRGh2cwoyZitlckxIK0FGYTBaN0l6SnpIYndxcFlwYVY3MU45THRKUnRQd0lFSGY1MFcwc1lZQ0pMbFNPMVhxT1ozdVZDClR0cFNVczdkTFVRMERsSTFUZy9JOENmVmlNL2Y0TWZld1ZpK0gzRUNnWUJQbFkyeXVTRkVBZDRGVHQ0cnMycnAKTzVnTHVWQ3U5T0s4c2sydTRaaUUxYUdsMm0zcmZjN29KeVIzdXdwSUk1cTJkQXBRWG9JQTVEak1pUWQ0elpZSgpDRW9nMTNHbGoyVHZManY5bXJLMGVGcVYxQXBRczBKNTJYYmJvRDUzVUNTQ2ppRU9NaUdQSmt2ZXgzc2FkSmN2Ck95QjFGcDhnNnA2YVlHSUZNdEVrUlFLQmdRQ3ZvMkJiL25INnhLVUM5VTBRY09xRUx0QVh4bGliZWhHNklMQ2oKKzdPMGhUSHRNRmhtTFlKWExBTGlTbGUzL2RESStrRmtaaGRPSFNHYXlzMVR3ZkZ4aWYyK2lwOHkzTXd4Z25WUAovSGx5Tm1Nc2pKbU9QeWdxTVErSUIzQndqb0o4S2p5cEtrL0FwMTF3aEp0T2tBNThvQWtaSzkzWXRPR09yYWx3CllpVklZUUtCZ0ZsMEN0RlhRZmlHN2VubzdsRWVlV3dSRXFxL01vZGVPcjRtRUZJWk84VEZwUHlpK1NuRklRUVEKQWdQRW5SdnZtb29IM1VDTzNRaUU3UHA5VzdrTTd6ZG1IZ09RTWNZK1NLMkdqU21BYzJXQWlGWkM0aXduTkV6QQp1bE5wLzh4d1BtbS9ZTGwyQTR3ckh4ZE5CQkhmRUlhMDZha21TbmRVTS9Uck9vQjVCTG1KCi0tLS0tRU5EIFJTQSBQUklWQVRFIEtFWS0tLS0tCg==");
        KubernetesClient client = new KubernetesClientBuilder().withConfig(config).build();

        // 查找标签为app=zookeeper-server-zookeeper的pod 而且node为host3
        String namespace = "bigdata";
        List<Pod> podList = client.pods().inNamespace(namespace).withLabel("app", "filebeat-filebeat").list().getItems();
        Pod pod = podList.stream().filter(new Predicate<Pod>() {
            @Override
            public boolean test(Pod pod) {
                return pod.getStatus().getHostIP().equals("192.168.197.150");
            }
        }).findFirst().get();


        EventList eventList = client.v1().events()
            .inNamespace(namespace)
            .withField("involvedObject.name",pod.getMetadata().getName())
            .list();

        for (Event event : eventList.getItems()) {
            System.out.println("Event Type: " + event.getType());
            System.out.println("Event Count: " + event.getCount());
            System.out.println("Event Last Timestamp: "+event.getLastTimestamp());
            System.out.println("Event Reason: " + event.getReason());
            System.out.println("Event Message: " + event.getMessage());

            String utcTimeStr=event.getLastTimestamp();
            // 创建 SimpleDateFormat 对象，指定输入的日期格式
            SimpleDateFormat inputSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            // 设置时区为 UTC
            inputSdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            // 解析 UTC 时间字符串为 Date 对象
            Date utcDate = inputSdf.parse(utcTimeStr);

            // 创建 SimpleDateFormat 对象，指定输出的日期格式
            SimpleDateFormat outputSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // 设置时区为北京时间（东八区）
            outputSdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));

            // 格式化为北京时间字符串
            String beijingTimeStr = outputSdf.format(utcDate);

            System.out.println("UTC 时间：" + utcTimeStr);
            System.out.println("北京时间：" + beijingTimeStr);
        }
    }


}
