package org.dromara.cloudeon.utils.k8s;

import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobStatus;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.processor.TaskParam;
import org.dromara.cloudeon.utils.LogUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class JobCompleteWatcher implements Watcher<Job> {
    private final TaskParam taskParam;
    private final CountDownLatch completionLatch;
    private final AtomicBoolean isJobEndSuccess;
    private final AtomicInteger retryCount;

    public JobCompleteWatcher(TaskParam taskParam, CountDownLatch completionLatch, AtomicBoolean isJobEndSuccess, AtomicInteger retryCount) {
        super();
        this.taskParam = taskParam;
        this.completionLatch = completionLatch;
        this.isJobEndSuccess = isJobEndSuccess;
        this.retryCount = retryCount;
    }

    @Override
    public void eventReceived(Action action, Job job) {
        if (action != Action.MODIFIED) {
            return;
        }
        JobStatus status = job.getStatus();
        if (status == null || status.getConditions().isEmpty()) {
            return;
        }
        isJobEndSuccess.set("Complete".equalsIgnoreCase(status.getConditions().get(0).getType()));
        if (status.getFailed() != null) {
            retryCount.set(status.getFailed());
        }
        completionLatch.countDown();
    }

    @Override
    public void onClose() {
        doClose();
    }

    @Override
    public void onClose(WatcherException cause) {
        LogUtil.logWithTaskId(taskParam, () -> {
            log.info("Watcher closed with exception: " + cause.getMessage());
            log.error(cause.getMessage(), cause);
        });
        doClose();
    }

    private void doClose() {
        LogUtil.logWithTaskId(taskParam, () -> {
            log.info("Watcher closed");
        });
    }
}

