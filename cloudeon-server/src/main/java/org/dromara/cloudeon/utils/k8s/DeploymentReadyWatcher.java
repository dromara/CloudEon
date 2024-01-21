package org.dromara.cloudeon.utils.k8s;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.processor.TaskParam;
import org.dromara.cloudeon.utils.LogUtil;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class DeploymentReadyWatcher implements Watcher<Deployment> {
    private final CountDownLatch completionLatch;
    private final TaskParam taskParam;

    public DeploymentReadyWatcher(TaskParam taskParam, CountDownLatch completionLatch) {
        super();
        this.taskParam = taskParam;
        this.completionLatch = completionLatch;
    }

    @Override
    public void eventReceived(Action action, Deployment deployment) {
        DeploymentStatus status = deployment.getStatus();
        if (status == null) {
            return;
        }
        boolean deploymentReady = deployment.getStatus().getReadyReplicas() != null
                && Objects.equals(deployment.getStatus().getReadyReplicas(), deployment.getStatus().getReplicas());
        if (deploymentReady) {
            completionLatch.countDown();
        }
    }

    @Override
    public void onClose() {
        LogUtil.logWithTaskId(taskParam, () -> log.info("DeploymentReadyWatcher closed"));
    }

    @Override
    public void onClose(WatcherException cause) {
        LogUtil.logWithTaskId(taskParam, () -> {
            log.info("DeploymentReadyWatcher closed with exception: " + cause.getMessage());
            log.error(cause.getMessage(), cause);
        });
    }
}

