package com.data.udh.controller;

import com.data.udh.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.powerjob.common.response.ResultDTO;
import tech.powerjob.server.core.service.AppInfoService;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private AppInfoService appInfoService;

    @Resource
    private HelloService helloService;



    @GetMapping("demo")
    public ResultDTO<Long> demo() {
        Long assertApp = appInfoService.assertApp("powerjob-agent-test", "123456");

        return ResultDTO.success(assertApp);
    }

    @GetMapping("wf")
    public ResultDTO<Long> wf() {
        helloService.wf();
        return ResultDTO.success(1L);
    }

}
