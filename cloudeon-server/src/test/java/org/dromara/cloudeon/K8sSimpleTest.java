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

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.dto.VolumeMountDTO;
import org.dromara.cloudeon.utils.K8sUtil;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Predicate;

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
        K8sUtil.runJob("default","init-work",client, volumeMounts,"openjdk:8-jdk-alpine","/opt/edp/monitor/conf/test.sh",log,"iZhp3ivd08e0qfgauq0gmbZ".toLowerCase());


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
