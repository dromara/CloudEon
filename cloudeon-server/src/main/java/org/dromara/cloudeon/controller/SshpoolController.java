package org.dromara.cloudeon.controller;

import org.dromara.cloudeon.dto.HostSshPoolMetrics;
import org.dromara.cloudeon.dto.ResultDTO;
import org.dromara.cloudeon.service.SshPoolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/sshpool")
public class SshpoolController {

    @Resource
    private SshPoolService sshPoolService;

    @GetMapping("/metrics")
    public ResultDTO<List<HostSshPoolMetrics>> metrics() {
        List<HostSshPoolMetrics> metrics = sshPoolService.metrics();

        return ResultDTO.success(metrics);
    }
}
