package com.data.udh;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tech.powerjob.worker.PowerJobWorker;
import tech.powerjob.worker.common.PowerJobWorkerConfig;
import tech.powerjob.worker.common.constants.StoreStrategy;

@Component // 注意 这里必须有
//@Order(2) 如果有多个类需要启动后执行 order注解中的值为启动的顺序
public class StartPowerJobWorker implements CommandLineRunner {

    protected Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Value("${server.port}")
    private String powerJobServerPort;

    @Override
    public void run(String... args) throws Exception {
        // 1. 创建配置文件
        PowerJobWorkerConfig config = new PowerJobWorkerConfig();
        config.setPort(28888);
        // 固定appName
        config.setAppName("udh-agent");
        config.setServerAddress(Lists.newArrayList("127.0.0.1:" + powerJobServerPort));
        // 如果没有大型 Map/MapReduce 的需求，建议使用内存来加速计算
        config.setStoreStrategy(StoreStrategy.DISK);


        // 2. 创建 Worker 对象，设置配置文件
        PowerJobWorker worker = new PowerJobWorker();
        worker.setConfig(config);

        worker.init();
    }
}