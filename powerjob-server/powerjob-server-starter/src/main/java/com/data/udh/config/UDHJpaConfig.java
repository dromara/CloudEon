package com.data.udh.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Objects;

/**
 * 核心数据库 JPA 配置
 *
 * @author tjq
 * @since 2020/4/27
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        // repository包名
        basePackages = UDHJpaConfig.CORE_PACKAGES,
        // 实体管理bean名称
        entityManagerFactoryRef = "udhEntityManagerFactory",
        transactionManagerRef = "udhTransactionManager"
)
public class UDHJpaConfig {

    @Resource(name = "udhDatasource")
    private DataSource udhDatasource;


    public static final String CORE_PACKAGES = "com.data.udh";

    /**
     * 生成配置文件，包括 JPA配置文件和Hibernate配置文件，相当于以下三个配置
     * spring.jpa.show-sql=false
     * spring.jpa.open-in-view=false
     * spring.jpa.hibernate.ddl-auto=update
     *
     * @return 配置Map
     */
    private static Map<String, Object> genDatasourceProperties() {

        JpaProperties jpaProperties = new JpaProperties();
        jpaProperties.setOpenInView(false);
        jpaProperties.setShowSql(false);

        HibernateProperties hibernateProperties = new HibernateProperties();
        hibernateProperties.setDdlAuto("none");

        HibernateSettings hibernateSettings = new HibernateSettings();
        return hibernateProperties.determineHibernateProperties(jpaProperties.getProperties(), hibernateSettings);
    }

    @Bean(name = "udhEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean initRemoteEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> datasourceProperties = genDatasourceProperties();
        return builder
                .dataSource(udhDatasource)
                .properties(datasourceProperties)
                .packages(CORE_PACKAGES)
                .persistenceUnit("udhPersistenceUnit")
                .build();
    }


    @Bean(name = "udhTransactionManager")
    public JpaTransactionManager udhTransactionManager(EntityManagerFactoryBuilder builder) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(initRemoteEntityManagerFactory(builder).getObject());
        return transactionManager;
    }


}
