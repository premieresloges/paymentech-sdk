package com.paymentech.orbital.sdk.logger;

import com.paymentech.orbital.sdk.util.exceptions.InitializationException;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * <p><b>Title:</b> LoggerIF</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><b>Description:</b><br><br>Wrapper for Apache-Commons Log class. Only around to provide backward-compatibility during phase out. </p>
 *
 * @deprecated
 */
public class LoggerIF {

  private static Logger logger;
  int DEBUG = 1;
  int INFO = 2;
  int WARN = 3;
  int ERROR = 4;
  int FATAL = 5;
  private String logFileName = null;

  public LoggerIF(Logger logger) {

    if (LoggerIF.logger == null) {
      LoggerIF.logger = logger;
    }

  }

  /**
   * Get the log file name
   *
   * @return for future use
   */
  public String getLogFile() {
    return logFileName;
  }

  /**
   * Set the log file name
   *
   * @param logFileName for future use
   */
  public void setLogFile(String logFileName) {
    this.logFileName = logFileName;
  }

  /**
   * Get the log level as DEBUG as 0 , INFO as 1...etc
   *
   * @return
   */
  public int getLogLevel() {
    int logLevel = 0;

    /*	if (logger.isFatalEnabled()) {
    		logLevel = FATAL;
    	}

    	if (logger.isErrorEnabled()) {
    		logLevel = ERROR;
    	}

    	if (logger.isWarnEnabled()) {
    		logLevel = WARN;
    	}*/

    if (logger.isInfoEnabled()) {
      logLevel = INFO;
    }

    if (logger.isDebugEnabled()) {
      logLevel = DEBUG;
    }

    return logLevel;
  }

  /**
   * Set the log level as DEBUG, INFO, WARN, ERROR or FATAL
   *
   * @return
   */
  public void setLogLevel(int logLevel) {
    ;
  }

  /**
   * Get the log level as DEBUG, INFO ...etc
   *
   * @return
   */
  public String getLogLevelString() {
    String logLevel = "";

    /*	if (logger.isFatalEnabled()) {
    		logLevel = "FATAL";
    	}

    	if (logger.isErrorEnabled()) {
    		logLevel = "ERROR";
    	}

    	if (logger.isWarnEnabled()) {
    		logLevel = "WARN";
    	}
    	*/
    if (logger.isInfoEnabled()) {
      logLevel = "INFO";
    }

    if (logger.isDebugEnabled()) {
      logLevel = "DEBUG";
    }

    return logLevel;
  }

  /**
   * Initialize the loggers
   *
   * @throws <{InitializationException}>
   */
  public void init() throws InitializationException {
    ;
  }

  /**
   * Initialize the loggers by the passed in log configuration file
   *
   * @param logConfigFile
   * @throws <{InitializationException}>
   */
  public void init(Map log4jPropertiesMap) throws InitializationException {
    ;
  }

  /**
   * A message logged in the log file if the log level is set to DEBUG and ALL level
   *
   * @param message
   */
  public void debug(Object message) {
    logger.debug(message);
  }

  /**
   * A message logged in the log file if the log level is set to DEBUG and ALL level
   *
   * @param message - a message string
   * @param t       - throwable
   */
  public void debug(Object message, Throwable t) {
    logger.debug(message, t);
  }

  /**
   * A message logged in the log file if the log level is set to INFO and lower level
   *
   * @param message - a message string
   */
  public void info(Object message) {
    logger.info(message);
  }

  /**
   * A message logged in the log file if the log level is set to INFO and lower level
   *
   * @param message - a message string
   * @param t       - throwable
   */
  public void info(Object message, Throwable t) {
    logger.info(message, t);
  }

  /**
   * A message logged in the log file if the log level is set to WARN and lower level
   *
   * @param message - a message string
   */
  public void warn(Object message) {
    logger.warn(message);
  }

  /**
   * A message logged in the log file if the log level is set to WARN and lower level
   *
   * @param message - a message string
   * @param t       - throwable
   */
  public void warn(Object message, Throwable t) {
    logger.warn(message, t);
  }

  /**
   * A message logged in the log file if the log level is set to ERROR and lower level
   *
   * @param message - a message string
   */
  public void error(Object message) {
    logger.error(message);
  }

  /**
   * A message logged in the log file if the log level is set to ERROR and lower level
   *
   * @param message - a message string
   * @param t       - throwable
   */
  public void error(Object message, Throwable t) {
    logger.error(message, t);
  }

  /**
   * A message logged in the log file if the log level is set to FATAL and lower level
   * And the Log Level 'FATAL' is the highest level
   *
   * @param message - a message string
   */
  public void fatal(Object message) {
    logger.fatal(message);
  }

  /**
   * A message logged in the log file if the log level is set to FATAL and lower level
   *
   * @param message - a message string
   * @param t       - throwable
   */
  public void fatal(Object message, Throwable t) {
    logger.fatal(message, t);
  }
}
