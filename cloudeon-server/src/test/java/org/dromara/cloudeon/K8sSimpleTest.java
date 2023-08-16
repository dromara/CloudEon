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

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.dto.VolumeMountDTO;
import org.dromara.cloudeon.utils.K8sUtil;
import org.junit.Test;

import java.io.FileNotFoundException;
@Slf4j
public class K8sSimpleTest {

    /**
     * test.sh
     * <p>
     * <p>

     for i in $(seq 1 20); do
     echo "Loop $i"
     sleep 1
     if [ $i -eq 13 ]; then
     echo "Error occurred"
     exit 1
     fi
     done

     * <p>
     * echo "Finished"
     */
    @Test
    public void job() throws FileNotFoundException, InterruptedException {

        Config config = Config.fromKubeconfig("apiVersion: v1\n" +
                "clusters:\n" +
                "- cluster:\n" +
                "    certificate-authority-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUM2VENDQWRHZ0F3SUJBZ0lCQURBTkJna3Foa2lHOXcwQkFRc0ZBREFWTVJNd0VRWURWUVFERXdwcmRXSmwKY201bGRHVnpNQ0FYRFRJek1EZ3hOakEyTWpFMU1Gb1lEekl4TWpNd056SXpNRFl5TVRVd1dqQVZNUk13RVFZRApWUVFERXdwcmRXSmxjbTVsZEdWek1JSUJJakFOQmdrcWhraUc5dzBCQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBCjFuMWc0b2kvS0UvOEVOcXR0dStmQ29UNVNMRFZsZ09YVVNjN0lIOEVrR0dKOGI2R2JiU2EvdHdRWndZUFdmQTIKcDhEc0EzMkpEbVFHSVEvYStsSG96Ym5QbE9KWkxSN1lBNldjdEM4YTV2TW5MaTVJcHozRnlXVVNQSjcxRlhuWApaVXd4ZDc2aHN5WDVzTkZtc0dKdDRvMGs3dkNmWVRCTmNWTlRyMlVGNG9zT1dMbTBIN3VhU0FYWlFsRHlSZHpHCkp3bmMwZ3dTL0h2aTY0a1RnalVIUWJkcm92dUtQN2tjY043NktqZFpWa0FUS0JndkJuN21tVWI2RzNiTkF1VjYKeXRucFdnQVhwcUJDckdpbXpMQU1RQ3VORDFTWGtpTnJKUStPRW5kU0FUTThpb1A3TTF3YUVsdlk3cFQ3WjczQwppMGhoU0c2MThEMnBnSEtpb09za25RSURBUUFCbzBJd1FEQU9CZ05WSFE4QkFmOEVCQU1DQXFRd0R3WURWUjBUCkFRSC9CQVV3QXdFQi96QWRCZ05WSFE0RUZnUVV5SDFIZjlyYVR1RFZNNk5GZEh2dnJxMFRkZ0F3RFFZSktvWkkKaHZjTkFRRUxCUUFEZ2dFQkFCN0lXRi9MMm1zdjJlRGFxdWRIQzVvUkkxR2VHalVGdWZVMCsyTTRGZ2dOWUR4ZgpiN0ozR2J4R3AyT3BpNE5QbG5UQU0rVlQxUFI2bW1keXQ1M013dUJLTTlTMG93WEZzanE4L3p3K0s1ejkxRkoyCmIvS2dMMmJtNUdJQmNUSjJ0cHNwakVqbVlpSzBFU0Z5SXZkNnJuVm5WeDgzK2xERnZQOHNYYTF1ajF1K0ZnRWUKTGlOOXZRTGQySVRBakNWSlo3aFB0elhkUUVjL1phQW55VWo1amltSU9uOTEvblJtT0xMR3F1ejE3OE4wTzBlWAorNHJzS05ZTGVmdlN4dTR3ZjBuZ05tNGZidDE5c0J3Y0FNK3g0R2swcGZvTlUvcUQ3V3NLRldNZ05VUXJ0c01VCk4xMC9NdVIrMkVoQXg0cXlVaEJ2VjVHWEpNbjc2c2VoSC9vMVROdz0KLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=\n" +
                "    server: http://39.104.203.111:8001\n" +
                "  name: kubernetes\n" +
                "contexts:\n" +
                "- context:\n" +
                "    cluster: kubernetes\n" +
                "    user: kubernetes-admin\n" +
                "  name: kubernetes-admin@kubernetes\n" +
                "current-context: kubernetes-admin@kubernetes\n" +
                "kind: Config\n" +
                "preferences: {}\n" +
                "users:\n" +
                "- name: kubernetes-admin\n" +
                "  user:\n" +
                "    client-certificate-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURGVENDQWYyZ0F3SUJBZ0lJZERJZ2QwOXdFQXN3RFFZSktvWklodmNOQVFFTEJRQXdGVEVUTUJFR0ExVUUKQXhNS2EzVmlaWEp1WlhSbGN6QWdGdzB5TXpBNE1UWXdOakl4TlRCYUdBOHlNVEl6TURjeU16QTJNakUxTjFvdwpOREVYTUJVR0ExVUVDaE1PYzNsemRHVnRPbTFoYzNSbGNuTXhHVEFYQmdOVkJBTVRFR3QxWW1WeWJtVjBaWE10CllXUnRhVzR3Z2dFaU1BMEdDU3FHU0liM0RRRUJBUVVBQTRJQkR3QXdnZ0VLQW9JQkFRRG5mMjZoUHFXNGRCQTIKQi9RaHFFWWROb0FjRWg3M2REOTlxajhna2xOSzhOaEJLMVFUditxeEttbXIzK2tiWjF3Vm0zaFpVdTE3c3RoeQozbXpIMjVXN3g4ZlJNYkY4Q2NJNERjRHg2ZGtFbVRRbVc3MjFGdDFhMExWNDNLaU0yVlV4a2M4ODNxbUpaSlFFCnhCbVhtU1NDSjhTODJ6Y2RBWDBQdXNLclk0S1k4cEJGVW5SeDd5NFVkOWpTY01nUVNkaEdQVjBFd1dWRE9Eb3MKTVRkQVBka1ZxeUdpRmRtcklmOGN1ZWdsYzlKY3Z0ODg0MzcvNWRMY1VNdzlwOXVENVo0MHJtMjBQSFpRWCtRNQptMTk2NGk0ZW9JRXBtSEFCc0ZGditiWXkxOHhJUzV2Q29vRmNXR1hobUxHK1pVcUMzZWtLMHZJaGNRTi8yeXh5CmdjeW0vNks3QWdNQkFBR2pTREJHTUE0R0ExVWREd0VCL3dRRUF3SUZvREFUQmdOVkhTVUVEREFLQmdnckJnRUYKQlFjREFqQWZCZ05WSFNNRUdEQVdnQlRJZlVkLzJ0cE80TlV6bzBWMGUrK3VyUk4yQURBTkJna3Foa2lHOXcwQgpBUXNGQUFPQ0FRRUFENkI5YUQ0UTdTcXNJOTVPSzZwL1hwTE9YMnQyZks4MXZraEZlVmxMR1BkbjZkTUxObWNiCnBNYlJQY2ExZUNnSWI1UFJyQ2hVaFQ5ZnFoN25aaXNoSjFiYWlGRUprQXVIR0R3ZUdsd3hkL2hEOUd4eDF6aXkKeGcrUkZhQVNMV29yWFJYT0NoZHVQVHRwaGxldGFjRE5tZkJ3NkFpZjRxRWFNMWxEZ0ZENnRhMGJZaHlXeERacwpNMWJhRjlrV1ZzREpEUWZVUllaSDFsbmQ1eUQ2SEl2cndTQUs2eW5tNUFNZ2h6SlBTZjVpbTBGU3QwVEpKVkZxClphTzA5aDhsSTEwWVNRYlVUYkZMd1Rhem5vQk93NnZpaTc3S0xzdHRoL09yUXRtanlZb1lTbFJJNk51WE40c2QKZVdaajVHQlRLODZCd0I0UHFHN1dRWVpndFpJb2l4MHU1UT09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K\n" +
                "    client-key-data: LS0tLS1CRUdJTiBSU0EgUFJJVkFURSBLRVktLS0tLQpNSUlFb2dJQkFBS0NBUUVBNTM5dW9UNmx1SFFRTmdmMElhaEdIVGFBSEJJZTkzUS9mYW8vSUpKVFN2RFlRU3RVCkU3L3FzU3BwcTkvcEcyZGNGWnQ0V1ZMdGU3TFljdDVzeDl1VnU4ZkgwVEd4ZkFuQ09BM0E4ZW5aQkprMEpsdTkKdFJiZFd0QzFlTnlvak5sVk1aSFBQTjZwaVdTVUJNUVpsNWtrZ2lmRXZOczNIUUY5RDdyQ3EyT0NtUEtRUlZKMApjZTh1RkhmWTBuRElFRW5ZUmoxZEJNRmxRemc2TERFM1FEM1pGYXNob2hYWnF5SC9ITG5vSlhQU1hMN2ZQT04rCi8rWFMzRkRNUGFmYmcrV2VOSzV0dER4MlVGL2tPWnRmZXVJdUhxQ0JLWmh3QWJCUmIvbTJNdGZNU0V1YndxS0IKWEZobDRaaXh2bVZLZ3QzcEN0THlJWEVEZjlzc2NvSE1wditpdXdJREFRQUJBb0lCQUI4VXRlYkNRWCs5WFh2VApuNHh4U2xDYnc2R0JNVlVwNzJoUTFqN3FSbktXV1lLT2JiQmxJUDUrWWtlb1BXVG5nSzZKL0NPS0JjYUk0WlN5CmxrcFRDZ3gzS3pYRUJUOVJGQmx3aDBvemltYWFweXpMUG5JMHlxMXB2aU9uQko0OGc3cXZCL0wxTmkvektBaG4KeDhQWjFOUUF3T0pXVUZUOW9TdmVlRXFJaHIwLytuMENMTE82eHhlMm92MElJN29GM3E1b0tpK2R3L29EdFU2bQpwT3FRbzA3Z1dKZm5BUTljK25hZ3N0ZllsREg4V3E5UFFLdTUvZ1BmVWNBazVJUVlNV3gxbXkzSmpvbUpFRkpiCjVMem82cUpMVHdJcW13d0QyVjVnK3NHcDQ2ajMyaUdsRDYyTS96MjAzSGVRWXQ4NVg3ZU80aFRJZmlPcjQxZ3oKN1p0eHUra0NnWUVBL3JMaHFyeHlqUUFNSTFKNTlHT1VOenErYkNQc0hBbjU3UmRmallObDA3VEkzN3FmLzN6VgpLaVhja1RhNGY4d2ZOZHN3ampzNkVRNHk3UXNhOGVFTmxURnJkTFF0b0JRNWhOazNhY1RUbGFNdTdaRGtHcWhTCkZ6NFZiVjZBUkpLcE02N0xpU2V2NkQyRnp5bXV0NWJjNDFOWkJyekhHMFNYbUt0cnB1emRBbzhDZ1lFQTZLNDAKemRmVlNOS2VGR295cmtFMzFaUzgzWUlFT2hKcGFNd3poUHhQc0cvK2FDOVZ0RW9jOHFiaFFPdmx0aXJISnU3VwpPM3VZbDJzM09WRmZJTlFGaGdMNFJPeTN6bjVEZUk5UUxFUyt2c3NNRHRyWXlFRFF5Z2V4NnlDYnNEM3RCOExkCmEwamI2QVNhKzVidHVVNzVzVHFsQkw1MStlcC9aUDNWZWZrZFF4VUNnWUEyU2NpaUROTFp6UVhKVFo0akFrcW8KVHdRaHBySi84M3hyRmMxUEs0Kzd4VS8vcUJiTWJUNCtZcDJWOGpUM1FIbnlqOHJVdGprVlE1S0ZSaFd6TXNZagpZOHBFc05iOHhQTFJwejhSYzF1cURJTkhMZGdBK3Btc3pKWGludjcySHRDajdJRUR0Z3JmbEtWOTE2T2ZEQy8rCjRGZ2NnSVpzQUgzVGs4NDVZVWxtYndLQmdEQzhaR1VGSXpCb3BTSERpTEFGQ3d2YVpxREMzZDNJQTNvbTQxZWsKZlpDSU5MSmZ6OFMralVlcCtwNWRpclRZU3ZSMXJEdXpUS2ZTbGpPVVBxZWlvVEdMcDdMUUhrUExJSmk4ODk4QgoyeTRkVzM0MUNwa1BNbXhPcGs2SWV2TzBWTlIrVldCbVYyRkdyYXVxMWtvdEo1R2VwZmZUYU9TYTRHb24zTEg2Cm9zMXRBb0dBSzlRdHBJRTNLRVhuSFlZNldGQ0VMaUpTd3JvTWsvYkQ1dURQWUdmUDNlb3FVb3pBcUlvMnFsZmEKWW5YMGFjcXZYMTlBVzhXMTZidHQ3Z3I1UWsrb29yeHoydkJhVk9lZDNxT0pOZ3NVVHd5VTBaR2cwYk94NDZxTgo3Q2JFb0NLZ0ZtV2kvcGM1WGdwQlZiY0lCOUwzQWJRblRXY1czUGsvYlRnSFlhYmNDRWs9Ci0tLS0tRU5EIFJTQSBQUklWQVRFIEtFWS0tLS0tCg==");
        KubernetesClient client = new KubernetesClientBuilder().withConfig(config).build();

        VolumeMountDTO[] volumeMounts = {new VolumeMountDTO("config-volume", "/opt/edp/monitor/conf", "/opt/edp/monitor/conf")};
        K8sUtil.runJob("init-work",client, volumeMounts,"openjdk:8-jdk-alpine","/opt/edp/monitor/conf/test.sh",log,"iZhp3ivd08e0qfgauq0gmbZ".toLowerCase());


    }

}
