package com.data.udh;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import tech.powerjob.server.PowerJobServerApplication;

@ActiveProfiles(profiles = "daily")
@SpringBootTest(classes = PowerJobServerApplication.class)
public class MyTest {

    @Test
    public void st() {
        System.out.println("hekk");
    }
}
