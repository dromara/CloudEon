package com.data.udh.config;

import tech.powerjob.server.common.utils.OmsFileUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.io.File;

/**
 * 多重数据源配置
 *
 */
@Configuration
public class UDHDatasourceConfig {


    @Bean("udhDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.udh")
    public DataSource initUDHDatasource() {
        return DataSourceBuilder.create().build();
    }

}
