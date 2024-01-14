package org.dromara.cloudeon.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.dromara.cloudeon.config.CloudeonConfigProp;
import org.dromara.cloudeon.dao.StackInfoRepository;
import org.dromara.cloudeon.entity.StackInfoEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/charts")
public class ChartsController {

    @Resource
    private CloudeonConfigProp cloudeonConfigProp;
    @Resource
    private StackInfoRepository stackInfoRepository;

    @GetMapping(value = "/{stackId}/index.yaml", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public StreamingResponseBody getIndexYaml(HttpServletRequest request, @PathVariable Integer stackId) {
        int serverPort = request.getServerPort();
        String serverBaseUrl = String.format("%s://%s%s", request.getScheme(), request.getServerName(), (serverPort == 80 || serverPort == 443) ? "" : ":" + serverPort);

        StackInfoEntity stackInfo = stackInfoRepository.findById(stackId).orElseThrow(() -> new RuntimeException("找不到对应的stack信息"));
        String stackBaseDir = cloudeonConfigProp.getStackLoadPath() + File.separator + stackInfo.getStackCode();
        String cloudEonHelmChartsReleasesDownloadUrl = serverBaseUrl + "/apiPre/charts/" + stackId + "/releases/download";
        return outputStream -> {
            String yamlContent = FileUtil.readUtf8String(stackBaseDir + File.separator + "charts" + File.separator + "index.yaml");
            yamlContent = yamlContent.replaceAll("CloudEonHelmChartsReleasesDownloadUrl", cloudEonHelmChartsReleasesDownloadUrl);
            outputStream.write(yamlContent.getBytes(StandardCharsets.UTF_8));
        };
    }

    @GetMapping(value = "/{stackId}/releases/download/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public StreamingResponseBody releaseDownload(@PathVariable Integer stackId, @PathVariable String fileName) {
        StackInfoEntity stackInfo = stackInfoRepository.findById(stackId).orElseThrow(() -> new RuntimeException("找不到对应的stack信息"));
        String stackBaseDir = cloudeonConfigProp.getStackLoadPath() + File.separator + stackInfo.getStackCode();
        return outputStream -> IoUtil.copy(FileUtil.getInputStream(stackBaseDir + "/charts/download/" + fileName), outputStream);
    }

}
