package org.dromara.cloudeon.utils;

import cn.hutool.core.io.LineHandler;

import org.slf4j.Logger;

public class RemoteSshTaskLineHandler implements LineHandler {

    private Logger logger ;

    public RemoteSshTaskLineHandler(Logger logger) {
        this.logger = logger;
    }
    @Override
    public void handle(String line) {
        logger.info(line);
    }
}
