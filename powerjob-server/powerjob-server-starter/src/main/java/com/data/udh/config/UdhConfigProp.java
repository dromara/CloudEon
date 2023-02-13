package com.data.udh.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UdhConfigProp {
    @Value("${udh.stack.load.path}")
    private String stackLoadPath;
    @Value("${udh.remote.script.path}")
    private String remoteScriptPath;
    @Value("${udh.task.log}")
    private String taskLog;
    @Value("${udh.work.home}")
    private String workHome;

}

