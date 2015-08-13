package com.paymentech.orbital.sdk.transactionProcessor;

import com.paymentech.eis.tools.StringUtils;
import com.paymentech.orbital.sdk.configurator.Configurator;
import com.paymentech.orbital.sdk.configurator.ConfiguratorIF;
import com.paymentech.orbital.sdk.engine.EngineIF;
import com.paymentech.orbital.sdk.engine.http.HttpEngineConstants;
import com.paymentech.orbital.sdk.engine.pool.EngineNotAvailableException;
import com.paymentech.orbital.sdk.engine.pool.EnginePool;
import com.paymentech.orbital.sdk.engine.pool.EnginePoolIF;
import com.paymentech.orbital.sdk.interfaces.RequestIF;
import com.paymentech.orbital.sdk.interfaces.ResponseIF;
import com.paymentech.orbital.sdk.interfaces.TransactionProcessorIF;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;

/**
 * <p><b>Title:</b> TransactionProcessor.java</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * OF Paymentech. The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br>
 * Manages the execution of transactions; acquires and releases resources, manages retries and handles errors
 * including failover state. </p>
 */
public class TransactionProcessor implements TransactionProcessorIF {

  public static final long TWENTY_MINUTES_MILLIS = 20 * 60 * 1000;
  // messages
  private static String failoverLogMessage = null;
  private static String returnToNormalLogMessage = null;
  private static String returnToNormalLogMessageFromTimeOut = null;
  private static String[] retryException = null;
  // variables used for convince
  private static ConfiguratorIF configurator;
  private int retryAttempts = 0;
  private int maxRetryAttempts;
  private Map configurations;
  private Logger eCommerceLogger;
  private Logger engineLogger;
  private EnginePoolIF enginePool;

  /**
   * Initialize the TransactionProcessor.
   *
   * @param configurator Global configurations including loggers.
   * @return A response object indicating whether there were any errors.
   */
  public TransactionProcessor() throws InitializationException {

    // set some convince variables
    configurator = Configurator.getInstance();
    this.configurations = configurator.getConfigurations();
    this.eCommerceLogger = configurator.getCommonEcommerceLogger();
    this.engineLogger = configurator.getCommonEngineLogger();

    //Get the maximum retry attempts
    try {

      maxRetryAttempts = Integer.parseInt(
          (String) configurations.get(TpConstants.NUMBER_OF_RETRIES_KEY));
    } catch (Throwable th) {
      engineLogger.error("Failed to get the maximum number of retry attempts " +
          " from the linehandler.properties file. Defaulting to 0.");
      maxRetryAttempts = 0;
    }
    // Get the additional retry exception list from the configurator
    String retryExceptionList = (String) configurations.get(TpConstants.RETRY_EXCEPTION);
    if (!StringUtils.isEmpty(retryExceptionList)) {
      // check is it already process
      if (retryException == null)
        retryException = retryExceptionList.split(",");
    }

    String sMaxRetryAttempts = "0";
    sMaxRetryAttempts = (String) configurations.get(TpConstants.NUMBER_OF_RETRIES_KEY);
    if (StringUtils.isEmpty(sMaxRetryAttempts)) {
      maxRetryAttempts = 0;
    } else {
      maxRetryAttempts = Integer.parseInt(sMaxRetryAttempts);
    }

    try {
      this.enginePool = EnginePool.getInstance();
    } catch (ClassNotFoundException cnfe) {
      engineLogger.error("Engine class not found.", cnfe);
      throw new InitializationException("Engine class not found.");
    }

    // construct messages used in this processor
    failoverLogMessage = "automatic URL failover from " +
        (String) configurations.get(HttpEngineConstants.HOSTNAME_KEY) + ":" +
        (String) configurations.get(HttpEngineConstants.PORT_KEY) + " to " +
        (String) configurations.get(HttpEngineConstants.HOSTNAME_FAILOVER_KEY) + ":" +
        (String) configurations.get(HttpEngineConstants.PORT_FAILOVER_KEY);

    returnToNormalLogMessage = "automatic URL failover returning from failover mode server, " +
        (String) configurations.get(HttpEngineConstants.HOSTNAME_FAILOVER_KEY) + ":" +
        (String) configurations.get(HttpEngineConstants.PORT_FAILOVER_KEY) + " to normal mode server, " +
        (String) configurations.get(HttpEngineConstants.HOSTNAME_KEY) + ":" +
        (String) configurations.get(HttpEngineConstants.PORT_KEY);

    returnToNormalLogMessageFromTimeOut = "automatic URL failover mode timer expired... returning from failover mode server, " +
        (String) configurations.get(HttpEngineConstants.HOSTNAME_FAILOVER_KEY) + " to normal mode server, " +
        (String) configurations.get(HttpEngineConstants.HOSTNAME_KEY);

  }

  public Map getConfigurations() {
    return configurations;
  }

  /**
   * Process a transaction. This method is called by the clients code. It should return an ResponseIF object
   * or throw a TransactionException
   *
   * @param request The request object
   * @return ResponseIF A response object
   * @throws TransactionException
   */
  public ResponseIF process(RequestIF request) throws TransactionException {
    EngineIF engine = null;
    ResponseIF response = null;
    long logTransactionID = 0;
    boolean done = false;

    try {

      retryAttempts = 0;

      logTransactionID = request.getLogTransactionID();

      if (logTransactionID == 0) {
        eCommerceLogger.debug("********* request/response counter has been reset, " +
            " due to application restart *********");
        engineLogger.debug("********* request/response counter has been reset, " +
            " due to application restart *********");
      }

      //Get an engine from the pool, will block until an engine is available
      engine = getEngine();

      //Reset failover (if we are in failover mode and if failover interval has expired)
      resetFailoverStateTimed();

      //Execute the transaction
      engineLogger.debug("executing transaction...");

      while (!done) {

        // try to connect and send
        // this method will throw an error for anything that
        // does not cause a retry or give a completed response
        response = connectAndSend(engine, request);

        if (response != null) {

          engineLogger.debug("transaction " + request.getLogTransactionID() + " complete.");

          // it was good and we have some type of response
          done = true;

        } else {


          // increment the retry counter

          // increment the retry counter
          retryAttempts++;

          if (retryAttempts > maxRetryAttempts) {

            String failoverHost = engine.retFailOverHost();
            if (failoverHost == null || failoverHost.trim().length() == 0) {
              engineLogger.info("Retry attempts have been exhausted");
              engineLogger.info("FailoverHost is not configured in linehandler.properties file");
              throw new TransactionException("Transaction failed - check engine log for details.");
            }
            engineLogger.debug("Retry attempts have been exhausted");

            // we have meet the limit for trying to connect
            // we need to check if we need to fail over or
            // we are already in a failed over state
            if (!getFailoverState()) {

              // since we are not in failover mode, we can swith over and try to
              // process in the secondary site
              engineLogger.info("Switching over to fail over site and resetting retry attempts");
              setFailoverState(true);
              retryAttempts = 0;

            } else {

              // write out the failure to the log files
              eCommerceLogger.error("Transaction " + request.getLogTransactionID() +
                  " has failed to connect to Paymentech gateway - check engine log for details. ");

              engineLogger.error("Transaction " + request.getLogTransactionID() +
                  " has failed to connect to Paymentech gateway - check engine log for details. ");

              // throw TransactionException
              throw new TransactionException("Transaction failed - check engine log for details.");

            }
          }
        }
      }
    } catch (TransactionException te) {
      throw te;
    } catch (Throwable th) {
      engineLogger.error("Caught Exception: " + th.getMessage(), th);
      throw new TransactionException(th.getMessage());
    } finally {
      // make sure that we can't leave this method without
      // trying to relase our engine
      if (engine != null) {

        engineLogger.debug("releasing engine back to pool...");

        // make sure the release is in a try catch so we will know
        // if something goes wrong
        try {
          enginePool.release();
        } catch (Throwable th) {
          engineLogger.error("Caught error trying to release engine resource: " + th.getMessage(), th);
        }

      }
    }

    return response;
  }

  public int getRetryAttempts() {
    return this.retryAttempts;
  }

  /**
   * Return the failover state.
   *
   * @return boolean, "true" implies "in failover state"
   */
  public boolean getFailoverState() {
    return EnginePool.isFailover();
  }

  /**
   * Set the failover state
   *
   * @param failoverState, "true" implies "go to failover state"
   */
  protected void setFailoverState(boolean failoverState) {

    if (failoverState) {

      EnginePool.setFailover(true);

      engineLogger.info(failoverLogMessage);

      //Record the time that failover began
      EnginePool.setFailoverStartMillis(System.currentTimeMillis());

      engineLogger.debug("failover occurred at failoverStartMillis = " + EnginePool.getFailoverStartMillis());

    } else {

      EnginePool.setFailover(false);

      engineLogger.info(returnToNormalLogMessage);
      //Report to the eCommerceLogger only if debug level logging
      engineLogger.info(returnToNormalLogMessage);
      EnginePool.setFailoverStartMillis(0);

    }

  }

  /**
   * Get a engine resource from the pool
   *
   * @return EngineIF
   * @throws TransactionException
   */
  protected EngineIF getEngine() throws TransactionException {
    EngineIF engine = null;

    try {

      engineLogger.debug("acquiring engine from pool...");

      // get an engine from the pool
      engine = enginePool.acquire();

    } catch (EngineNotAvailableException ena) {
      engineLogger.error("this thread has already acquired an engine and has not yet released it or Initialization error", ena);
      throw new TransactionException(ena.getMessage());
    } catch (Throwable th) {
      engineLogger.error("an exception occurred while attempting to acquire an engine from the pool or Initialization error", th);
      throw new TransactionException(th.getMessage());
    }

    engineLogger.debug("engine acquired...");

    return engine;
  }

  /**
   * Sends transaction and returns boolean that relates to the success of the send
   * Meanings of boolean return values
   * True - Send was good and we can take a look at the response to see what we got back
   * False - Could not connection and its OK to retry the connect attempt
   *
   * @return boolean
   * @throws TransactionException
   */
  protected ResponseIF connectAndSend(EngineIF engine, RequestIF request) throws TransactionException, SocketTimeoutException {
    ResponseIF response = null;

    // nothing gets out of this method that we don't log and anything that is not a ConnectException is
    // considered bad enough that we need to throw a TransactionException
    try {

      response = engine.execute(request);

      // indicate that the response message was successful created by sending
      // the response object back (don't know if
      // it altogether good at this point, but we do know we got something back
    } catch (SocketTimeoutException e) {
      if (StringUtils.isEmpty(request.getTraceNumber())) {
        engineLogger.error("Caught SocketTimeoutException: " + e.getMessage(), e);
        throw new TransactionException(e.getMessage());
      }
      // this means that we failed to connect, so we
      // may need to retry. of course if the retry trace is
      // not set then we are not even going to chance a
      // retry of any kind (even through the only way
      // we can get here is from a ConnectionException)
      //if (StringUtils.isEmpty(request.getTraceNumber())) {
      //	throw new TransactionException ("Connecting to Paymentech gateway has " +
      //			"failed and retry trace number is not set. In this condition, the SDK will " +
      //			"never retry to connect. Please supply a retry trace number to use SDK retry functionality");
      //}

      // if the connection timeout is less then 90 seconds, then
      // we are not going to allow retry
      if (engine.getReadTimeout() < 90) {
        throw new TransactionException("Reading from Paymentech gateway has " +
            "failed and read timeout is set to less then " +
            "90 seconds. Please increase the read timeout in the linehandler.properties " +
            "to 90 seconds or greater to enable auto retry");
      }
      return response;
    } catch (InitializationException ie) {
      engineLogger.error("Caught InitializationException: " + ie.getMessage(), ie);
      throw new TransactionException(ie.getMessage());
    } catch (IOException ioe) {
      // lets check exact type of IO exception
      // if this is a ConnectException, we can safely retry the transaction
      // if not, then we need to throw the error because we can not be
      // sure if the transaction was received by the host
      boolean exceptionMatch = false;
      if (retryException != null) {
        for (int ctr = 0; ctr < retryException.length; ctr++) {
          if (ioe.getClass().getName().equals(retryException[ctr]))
            exceptionMatch = true;
        }
      }
      if (ioe instanceof java.net.ConnectException || ioe instanceof java.net.UnknownHostException || exceptionMatch || ioe instanceof javax.net.ssl.SSLPeerUnverifiedException || ioe instanceof javax.net.ssl.SSLHandshakeException) {
        engineLogger.error("Caught java.net.ConnectException which means that we can retry");
      } else {
        throw new TransactionException(ioe.getMessage());
      }
    } catch (Throwable th) {
      engineLogger.error("Caught Exception: " + th.getMessage(), th);
      throw new TransactionException(th.getMessage());
    }

    return response;
  }

  /**
   * Reset the failover state back to normal, only if 20 minutes has passed since we entered failover
   */
  protected void resetFailoverStateTimed() {

    if (EnginePool.isFailover()) {

      //If we have been in failover for 20 minutes, go back to normal mode
      long currentTimeMillis = System.currentTimeMillis();

      if (engineLogger.isDebugEnabled()) {

        long milliSecondsRemaining = TWENTY_MINUTES_MILLIS -
            (currentTimeMillis - EnginePool.getFailoverStartMillis());

        if (milliSecondsRemaining > 0) {
          engineLogger.debug("milliseconds remaining in failover mode = " + milliSecondsRemaining);
        } else {
          engineLogger.debug("milliseconds remaining in failover mode = 0");
        }

      }

      if (currentTimeMillis > (EnginePool.getFailoverStartMillis() + TWENTY_MINUTES_MILLIS)) {

        //Go back to normal mode
        EnginePool.setFailover(false);

        engineLogger.info(returnToNormalLogMessageFromTimeOut);

        //Reset the failover start time (just for cleanup)
        EnginePool.setFailoverStartMillis(0);

      } else {

        //Just go back to normal mode if a rollover has occurred
        if (currentTimeMillis < EnginePool.getFailoverStartMillis()) {
          EnginePool.setFailover(false);
          EnginePool.setFailoverStartMillis(0);
          engineLogger.info("system time rolled over... returning to normal (non-failover) mode");
        }

      }

    }

  }

  /**
   * Get the eCommerce logger
   *
   * @return Log eCommerce Logger
   */
  protected Logger getECommerceLog() {
    return this.eCommerceLogger;
  }

  /**
   * Set the eCommerce logger
   *
   * @param logger The eCommerce Logger
   */
  protected void setECommerceLog(Logger logger) {
    this.eCommerceLogger = logger;
  }

  /**
   * Get the Engine Logger
   *
   * @return Log The Engine Logger
   */
  protected Logger getEngineLog() {
    return this.engineLogger;
  }

  /**
   * Set the Engine Logger
   *
   * @param logger The Engine Logger
   */
  protected void setEngineLog(Logger logger) {
    this.engineLogger = logger;
  }

}
