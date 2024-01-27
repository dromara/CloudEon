package org.dromara.cloudeon.utils.k8s;

import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.processor.TaskParam;
import org.dromara.cloudeon.utils.K8sUtil;
import org.dromara.cloudeon.utils.LogUtil;

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
        if (K8sUtil.checkDeploymentReady(deployment)) {
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

