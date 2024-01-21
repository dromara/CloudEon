package org.dromara.cloudeon.utils.k8s;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.func.Supplier2;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import io.fabric8.kubernetes.client.dsl.PodResource;
import lombok.extern.slf4j.Slf4j;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

@Slf4j
public class PodLogWatcher implements Watcher<Pod> {

    private final Supplier2<OutputStream, Pod, ContainerStatus> outputStreamSupplier;
    private final KubernetesClient client;
    private Map<String, LogWatch> logWatchMap = Maps.newHashMap();

    public PodLogWatcher(KubernetesClient client, Supplier2<OutputStream, Pod, ContainerStatus> outputStreamSupplier) {
        super();
        this.outputStreamSupplier = outputStreamSupplier;
        this.client = client;
    }

    @Override
    public void eventReceived(Action action, Pod pod) {
        if (action == Action.ADDED || action == Action.MODIFIED || action == Action.ERROR) {
            String podName = pod.getMetadata().getName();
            PodResource podResource = client.pods().resource(pod);

            List<ContainerStatus> containerStatusList = pod.getStatus().getContainerStatuses();
            for (ContainerStatus containerStatus : containerStatusList) {
                String containerName = containerStatus.getName();
                // 应对容器重启导致日志跟踪丢失的问题
                String key = StrUtil.format("{}-{}-{}", podName, containerName, containerStatus.getRestartCount());
                if (logWatchMap.containsKey(key)) {
                    continue;
                }
                // 如果有多个容器，则在日志前打印当前容器名
                LogWatch logWatch = podResource.inContainer(containerName)
                        .tailingLines(200).watchLog(
                                outputStreamSupplier.get(pod, containerStatus));
                logWatchMap.put(key, logWatch);
            }

        }
    }

    private void doClose() {
        logWatchMap.values().forEach(IoUtil::close);
        logWatchMap.clear();
    }

    @Override
    public void onClose() {
        doClose();
    }

    @Override
    public void onClose(WatcherException e) {
        doClose();
    }
}
