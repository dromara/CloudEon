package org.dromara.cloudeon.dto;

import lombok.Data;
@Data
public class HostSshPoolMetrics {
    private String hostname;
    private SshPoolMetrics sshPoolMetrics;
}
