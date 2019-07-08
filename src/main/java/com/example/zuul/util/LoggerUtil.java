package com.example.zuul.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private Logger logger;

    public LoggerUtil(Class<?> clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    public LoggerUtil(String name) {
        logger = LoggerFactory.getLogger(name);
    }

    public void info(String var1) {
        logger.info(var1);
    }

    public void info(String var1, Throwable var2) {
        logger.info(var1, var2);
    }

    public void warn(String var1) {
        logger.warn(var1);
    }

    public void warn(String var1, Throwable var2) {
        logger.warn(var1, var2);
    }

    public void error(String var1) {
        logger.error(var1);
    }

    public void error(String var1, Throwable var2) {
        logger.error(var1, var2);
    }
}
