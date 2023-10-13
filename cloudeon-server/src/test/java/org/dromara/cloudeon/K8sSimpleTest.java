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
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.dsl.ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.utils.K8sUtil;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.function.Predicate;

@Slf4j
public class K8sSimpleTest {

    private KubernetesClient getClient() {
        String kubeConfig = "" +
                "" +
                "apiVersion: v1\n" +
                "clusters:\n" +
                "- cluster:\n" +
                "    certificate-authority-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUMvRENDQWVTZ0F3SUJBZ0lVTGpWWFk5Y0lSMkhNdnhEWjJUU1VpYTdHWWhzd0RRWUpLb1pJaHZjTkFRRUwKQlFBd0ZURVRNQkVHQTFVRUF3d0thM1ZpWlhKdVpYUmxjekFnRncweU16QTFNekF3TmpRd01qVmFHQTh5TVRJegpNRFV3TmpBMk5EQXlOVm93RlRFVE1CRUdBMVVFQXd3S2EzVmlaWEp1WlhSbGN6Q0NBU0l3RFFZSktvWklodmNOCkFRRUJCUUFEZ2dFUEFEQ0NBUW9DZ2dFQkFPT2pJZTZqTjMwNC9jdC9VZmQ1VjB0WGF2OTBDWE1HaXd4NW0ycnEKNE5ZWGE0ay9ldEVnQndLV2ZHUStzbGhGWng3UjI5ajZvQ0N6SGwxNXlSUzhteEhSdG5LWWMxMWlYbGdQZlVlVQpCUHFxTzFkT1RjcTIxbW5UME80TFZ5Z1NaU3ZWSjZubGovK0R3YnZkSStXeXVHajN5c1lCQmcyQ3dVNFM0VVViCnRHdk1oVWpab3VvT2F6ZXEwVENOcHpqYUc3TU5VK24xTkNmTzV2dDY4Y0tVcGlIVmMxeGpiS3JoSjFiL0p3bXQKcHF4WGJyMWZVRWpSSVhtc1BrdExNSExVOXlGbnUxS2VyMEVJN0haUUx4ZURaL1RCa0djb2kveXM1Y2MrRGZ3VgpwYXFHTkx3czVrZkRVeXltRGlRRHNyYy9EcmJtUjdLbzIrNmQrMjFhMUNSYkZWY0NBd0VBQWFOQ01FQXdEd1lEClZSMFRBUUgvQkFVd0F3RUIvekFPQmdOVkhROEJBZjhFQkFNQ0FxUXdIUVlEVlIwT0JCWUVGSFA3MDVnNUNsS3UKRjRhUzZlMnFMUUVJampDN01BMEdDU3FHU0liM0RRRUJDd1VBQTRJQkFRQjVQMHJDMEVmMG9GRysvWFNva3VFLwpHWHJrNDA3NGxtb1JkcFpjYkZ4YytTaUI3bS9WMzMrcHcwOHcwbTMwY0tITk9aZ1J1bGJKL1p3azcxNU1mLzlBCkEzakF5RlZXS3VFR1JTbUNVZEJGem92ZjNNREZuRUExUWlMb0VyVndhTVNWNEs1bUg5UzdhZzNTZnFHR2xqYkIKVkhxVmdMdm9RbkM0M0lRcmlZUVp0bmY5NWJZNWlYdDhacElVVDZleHF6dFpvMGhyUXMzVVVJbmx4Wkltdis4eAp6Z1J0dC9zMEJSMTdMU0ZQUjFIVWt2dmV6VEFzamp1WExaOVltODl5bldhYWUzS3EvOFJEK01vSG5jWXc1ZUJUCkU2ekY3L3M0S1Jna3NsRW9PbnR5YnpDMVpNeEcrZFpnVWgybzF0ekdoSUZOTC9rSTBhZkd4bDlMVzVNdG1XWHgKLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo=\n" +
                "    server: https://192.168.100.192:8443\n" +
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
                "    client-certificate-data: LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURTekNDQWpPZ0F3SUJBZ0lVZmdLeGwrV2xvWHB0UFk4YVkrbnd1R1FKdlU4d0RRWUpLb1pJaHZjTkFRRUwKQlFBd0ZURVRNQkVHQTFVRUF3d0thM1ZpWlhKdVpYUmxjekFnRncweU16QTFNekF3TmpRd05EbGFHQTh5TVRJegpNRFV3TmpBMk5EQTBPVm93TkRFWk1CY0dBMVVFQXd3UWEzVmlaWEp1WlhSbGN5MWhaRzFwYmpFWE1CVUdBMVVFCkNnd09jM2x6ZEdWdE9tMWhjM1JsY25Nd2dnRWlNQTBHQ1NxR1NJYjNEUUVCQVFVQUE0SUJEd0F3Z2dFS0FvSUIKQVFETXhCWnVweGpIRVkvaVF2NHhyOW1KdDhTaVJySGNtdUpvV2VCVDJNd1VLT0pTK1pFaFRtKzZkbDdBb0Iydgp1WXpWbHlkZmhoTWVtSUFQZnl5Y3dWbHRYZnhCcWFCOElkcThCM2FubnNDQ3JRMGUrQXBHM0V2dm12TUoxU3lQCkxTdWVvUWlKWWQ3UWZVM3BYdEdzYlorclpXT2J3Tm1RYTFWS3UvalFONlJlbytmZjRpcmE4ZEROdHU3SU1iVGgKT1pmYmVmMmdiR01nYXhJTStZSUI1bXpMODJ3NmJjeVAxUHkwY0d0ZVhydHJCSnZOT1NWQXlKcS9Oa2dNNno0RApzNGVxclNTME81MFB1LzlPTUNtVmtSSnp6MXFaYTdtdnc2emlwMC9nSEhtYkJqY2dndDNYNFpuOWR6NnhGQnppCjlqR1V4SDgvdW5MVFRFcnNNMW9OTlNoVEFnTUJBQUdqY2pCd01Ba0dBMVVkRXdRQ01BQXdEZ1lEVlIwUEFRSC8KQkFRREFnV2dNQk1HQTFVZEpRUU1NQW9HQ0NzR0FRVUZCd01DTUIwR0ExVWREZ1FXQkJSVzFNWWZFajlZOFBFRwoyVFRHTU41emhZdDB2REFmQmdOVkhTTUVHREFXZ0JSeis5T1lPUXBTcmhlR2t1bnRxaTBCQ0k0d3V6QU5CZ2txCmhraUc5dzBCQVFzRkFBT0NBUUVBR2FRdDJQdWRmTUlSUFNJamdzbWRUOUlVK3B6ZU9GblZvajdHZWVSNjcxL1QKZ1VmdXRSeTZsQ3FFMzFPaXA0LzlCelNNd1lPZDdMaThXUVFCYlNkZjdtdEo2SDFuVUpGcDdZZnRKaGlqN3hXdwpIeU4zbTVwYlBIcWVHMFAyM1pyeGVCbEhTN0ZhNXFtRzFRR0JId0kzblVLS1hXaDZpdUVZZE5mMm9GY1ByK2hoCk5CbjFweFY0aS9na1BoZHoxZ2o1YjVLd25kdVJTRDl4WDVtRW1SRGJvbHpRV3ZjNEU3UXc3bllNL0pwdlo3MlcKaUJ0NytHTGYzZ0E4aXowTHJZcnlXRnpUbUFvQ24ySHdYa0VyWGdDS1ZHTTBzczk1WThSOGhhN1VBYkp1ZGZlUwpZeUt3SGhtNHYwbEF2MlNnSU9vdzd5SzJnMVlOb0dtVWpoTmU1U3ZZcUE9PQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==\n" +
                "    client-key-data: LS0tLS1CRUdJTiBQUklWQVRFIEtFWS0tLS0tCk1JSUV2d0lCQURBTkJna3Foa2lHOXcwQkFRRUZBQVNDQktrd2dnU2xBZ0VBQW9JQkFRRE14Qlp1cHhqSEVZL2kKUXY0eHI5bUp0OFNpUnJIY211Sm9XZUJUMk13VUtPSlMrWkVoVG0rNmRsN0FvQjJ2dVl6Vmx5ZGZoaE1lbUlBUApmeXljd1ZsdFhmeEJxYUI4SWRxOEIzYW5uc0NDclEwZStBcEczRXZ2bXZNSjFTeVBMU3Vlb1FpSllkN1FmVTNwClh0R3NiWityWldPYndObVFhMVZLdS9qUU42UmVvK2ZmNGlyYThkRE50dTdJTWJUaE9aZmJlZjJnYkdNZ2F4SU0KK1lJQjVtekw4Mnc2YmN5UDFQeTBjR3RlWHJ0ckJKdk5PU1ZBeUpxL05rZ002ejREczRlcXJTUzBPNTBQdS85TwpNQ21Wa1JKenoxcVphN212dzZ6aXAwL2dISG1iQmpjZ2d0M1g0Wm45ZHo2eEZCemk5akdVeEg4L3VuTFRURXJzCk0xb05OU2hUQWdNQkFBRUNnZ0VBVk9GQnhUdVo3MGNONUVaTjlZM0YrS3NISlJkMStoTHdJRDZGV1d4b2FFRHMKVkdYa2JiQ0Vhd0JQVmJ6cG9XS2lpUlYvdWo2ckpVY2s2b2VXbUNJajdreUQyVG8xN3M3Znk0cXllbGc1eDlGeApPM0dwWE9kTHlQWnJvWnRPdmNrRktGdnJYSHVINzlmSldLQTMvU2h5QkF2aXh2a2hscGFQaEF1NFg2TjVETXRZCjNsa3c0NExHbXdYTWpGbk9oQnpMNjQxNDNQU0VRejUrYzRnL216RU9QTGgxSENqelJEVnNZdTlJQW90MzZJaUYKZWVFYVhYSEllNkoxRXhiMEE4V2lFdzAvWUp1cm1Lc09zVGNHNHpVeUxLMFhqNys1TysvVjcxdno2Qy9ZZUUwcwpFeXdDR1VoUUZuUjFUNHFaa3hPQndLaHVnSy9ZMjFZWlZ1OXlxbDNoalFLQmdRRE9BY0pSWGZjVWpac1UxZzd1CmNKRnNaQ3JiR05rckphVkp0VG52STBkdFMwZGNpaDRTQzExaFN1S2hHNkVjOXJqYnJhYmZiYktJc2VnVjA3cWIKU2UxYlhncCtkUlhZZGJTOTFGQ2pQOUNwMWV0MmxIeWtmV2VacWtQYTJtZmdqMmNSSEN0aWR4UXh4VUs2ajNRTQpCaFh4aDlWcy9YMmpvazRwbU1jcDRZeDFod0tCZ1FEK2RUeXRtZ2FDUGZkRHNyM1JzbkYzZ1RESFBjcU05MnpFCmdLT2duVG5nWkxyZ1hRcVB6ZlZTeUhSK1dqVzErWmw5RGF6M2hCSGg3RUlkQ3o3KzhnUWZyTTF5UVN4UXR1N2UKa05oaXRTdi9FNEtjYXROOXg4M2pGWGR2MW83V1A3R2lORTVQZWdQelo2a3FZT0tibi83YXJWUEwxUUlKRGNtVApDc1U2aUlWcDFRS0JnUUN5dUNQMG95aHYxRW51VWFheWhVWWtXdUl6SWVPRjR5cjZQeGI3dUFlSGNmOSs4UFFWCmczYUhxWWZqYlN6aEM4cGtDc3J5bXlDQUpwZktGOTJVU3haNFphV0UvOTdyNDNIaUhnZTNHTzNWNlpoVlQ0eXkKeDNqUmZ6MU82SnVsM2NMMHZST0dZUGhNRlc1R201MTVzTzNvbEljNy9zNjQzMTRnQ0VNQXVvUTRrUUtCZ1FEVQp4aEc4Rkl5V3dkd09KdHRsQ3JLb3ZFV2VoVVBuQmtwVU1rRWczL2Z5ZENoenpqa3pzSVFQK2dDM1d4V0ltak5ICmgzVDM3OTdJTExmSDg0eDB3TWpEOThvL1hOSUNtRVU3cEtEY1FTU09BYkY0dkRjbStUbG5ScDc5ek1yWnlwN3QKeEpFckVodFZvSHVyTFNLd0FXU3BWTUE2TkY2a1ZYd1YwYTdFV0Q0L0ZRS0JnUUNvV3JXSVB2MzdkUHFrTlhuaAp5NWNORmhJS1JJYURpSWt1bUxuMkZha1Z1VEJwRnlNR2M1cFdvZXBKcWN4eHU1ZnJGaGowWEtPOXZCODNETjNGCmRxclVma3d2YXpSdVdoVHRYdFlNbGV0aUtIY1RZcXpodjNQRWd5MFBvVEwwTHhPZmxmaUhOR2t2RS9PdWkwTzEKY2pyVkRWdVloeHBraGd6blVZc3pzWnJlalE9PQotLS0tLUVORCBQUklWQVRFIEtFWS0tLS0tCg==" +
                "";
        return K8sUtil.getKubernetesClient(kubeConfig);
    }

    @Test
    public void waitForJobCompleted() {
        String jobYamlStr = "" +
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
        while (true) {
            ParameterNamespaceListVisitFromServerGetDeleteRecreateWaitApplicable<HasMetadata> loaded = client.load(IoUtil.toUtf8Stream(jobYamlStr));
            if (loaded.get().get(0) != null) {
                loaded.delete();
            }
            List<HasMetadata> metadata = loaded.forceConflicts().serverSideApply();
            String resourceName = metadata.get(0).getMetadata().getName();
            int retryCount = K8sUtil.waitForJobCompleted("default", resourceName, client, log, 60);
            log.info("retryCount: " + retryCount);
        }
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
