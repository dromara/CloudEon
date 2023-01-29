package com.data.udh;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
class DaoTest {

    @Test
    void testAutoConfiguration() {
        ConfigurableApplicationContext run = SpringApplication.run(DaoTest.class);
    }

}
