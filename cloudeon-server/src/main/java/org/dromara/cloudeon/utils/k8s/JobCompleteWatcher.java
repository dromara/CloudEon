package org.dromara.cloudeon.utils.k8s;

import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobStatus;
import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import lombok.extern.slf4j.Slf4j;
import org.dromara.cloudeon.processor.TaskParam;
import org.dromara.cloudeon.utils.K8sUtil;
import org.dromara.cloudeon.utils.LogUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class JobCompleteWatcher implements Watcher<Job> {
    private final TaskParam taskParam;
    private final CountDownLatch completionLatch;
    private final AtomicReference<JobStatus> finalJobStatus;

    public JobCompleteWatcher(TaskParam taskParam, CountDownLatch completionLatch, AtomicReference<JobStatus> finalJobStatus) {
        super();
        this.taskParam = taskParam;
        this.completionLatch = completionLatch;
        this.finalJobStatus = finalJobStatus;
    }

    @Override
    public void eventReceived(Action action, Job job) {
        if (action != Action.MODIFIED) {
            return;
        }
        if (!K8sUtil.checkJobEnded(job)) {
            return;
        }
        finalJobStatus.set(job.getStatus());
        completionLatch.countDown();
    }

    @Override
    public void onClose() {
        doClose();
    }

    @Override
    public void onClose(WatcherException cause) {
        LogUtil.logWithTaskId(taskParam, () -> {
            log.info("JobCompleteWatcher closed with exception: " + cause.getMessage());
            log.error(cause.getMessage(), cause);
        });
        doClose();
    }

    private void doClose() {
        LogUtil.logWithTaskId(taskParam, () -> {
            log.info("JobCompleteWatcher closed");
        });
    }
}

