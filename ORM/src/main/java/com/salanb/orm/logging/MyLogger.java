package com.salanb.orm.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class for maintaining 1 logger in our app
 */
public class MyLogger {

    /**
     * The logger used for logging
     */
    public static final Logger logger = LogManager.getLogger(MyLogger.class);

}
