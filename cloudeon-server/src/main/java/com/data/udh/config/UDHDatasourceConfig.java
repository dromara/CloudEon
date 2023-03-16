//package com.data.udh.config;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
///**
// * 多重数据源配置
// *
// */
//@Configuration
//public class UDHDatasourceConfig {
//
//
//    @Bean("udhDatasource")
//    @ConfigurationProperties(prefix = "spring.datasource.udh")
//    public DataSource initUDHDatasource() {
//        return DataSourceBuilder.create().build();
//    }
//
//}
