package com.api.component;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomLogger {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CustomLogger.class);

    public CustomLogger() {
        LOGGER.info("logger is create");
    }

    public void logError(String text) {
        LOGGER.error(text);
    }

    public void logCachePut(String text) {
        LOGGER.info("Cache put : {}", text);
    }

    public void logInfo(String text) {
        LOGGER.info("{}", text);
    }

    public void logCacheRemove(String text) {
        LOGGER.info("Cache remove : {}", text);
    }
}
