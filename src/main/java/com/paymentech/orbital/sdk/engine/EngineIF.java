package com.paymentech.orbital.sdk.engine;

import com.paymentech.orbital.sdk.interfaces.RequestIF;
import com.paymentech.orbital.sdk.interfaces.ResponseIF;
import com.paymentech.orbital.sdk.transactionProcessor.TransactionException;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;

import java.io.IOException;

/**
 * <p><b>Title:</b> EngineIF</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <b>Description:</b><br><br> Published interface for the Engine classes. </p>
 */
public interface EngineIF {
  /**
   * Key that locates the engine class in the configurations.
   */
  String ENGINE_CLASS_KEY = "engine.class";

  /**
   * Initialize the engine.
   *
   * @param configurator Global configurations
   * @throws InitializationException if there is an error during initialization
   */
  void init() throws InitializationException;

  /**
   * Execute an Orbital Gateway transaction.
   *
   * @param request The xml request object
   * @return The xml response object
   */
  ResponseIF execute(RequestIF request) throws InitializationException,
      IOException, TransactionException;

  /**
   * Get the connection timeout (in seconds)
   *
   * @return int
   */
  int getConnectionTimeout();

  /**
   * Signal the engine that it is about to be returned to the pool.  Give the engine a chance to cleanup resources and do
   * some logging before being returned to the pool.
   */

  int getReadTimeout();

  void release();

  String retFailOverHost();
}
