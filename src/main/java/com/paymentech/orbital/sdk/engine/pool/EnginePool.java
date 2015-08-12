package com.paymentech.orbital.sdk.engine.pool;

import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

import com.paymentech.eis.tools.StringUtils;
import com.paymentech.orbital.sdk.configurator.Configurator;
import com.paymentech.orbital.sdk.configurator.ConfiguratorIF;
import com.paymentech.orbital.sdk.engine.EngineIF;
import com.paymentech.orbital.sdk.transactionProcessor.TpConstants;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;

/**
 * <p><b>Title:</b> EnginePool</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Resource manager for the engine resource.  Governs access to
 * the Orbital Gateway.</p>
 */
public class EnginePool
    implements EnginePoolIF {
  /** EnginePool is a Singleton - private constructor */
  private EnginePool() {}

  /** Initialize the engine pool * */
  public static EnginePoolIF getInstance() throws ClassNotFoundException,
      InitializationException {
  	
    if (instance == null) {
    	
      instance = new EnginePool();
      configurator = Configurator.getInstance();
      if (configurator == null) {
        EnginePool.engineLogger.error("configurator is null");
        throw new InitializationException("configurator is null");
      }
      
      EnginePool.configurations = configurator.getConfigurations();
      EnginePool.engineLogger = configurator.getCommonEngineLogger();
      
      //EnginePool should be initialized only once each time the java vm is started
      EnginePool.engineLogger.debug(
          "********** Begin EnginePool Initialization **********");
      
      //Determine the engine pool size      
      try {
      	 EnginePool.poolSize = Integer.parseInt((String) EnginePool.configurations.get(TpConstants.
                POOL_SIZE_KEY));
      	 if ( EnginePool.poolSize > 100 )
      	 {
      		 EnginePool.engineLogger.warn("Engine pool size value[ " + EnginePool.poolSize + "] from linehandler.properties is greater than allowed limit of 100");
      	     EnginePool.engineLogger.warn("Resetting the value to 100");
      	     EnginePool.poolSize=100;
      	 }
      		 
      } catch (Throwable th) {
      	EnginePool.engineLogger.info(
                "engine pool size not configured... using default value of " +
                TpConstants.DEFAULT_POOL_SIZE);      	
      }
       
      //Determine which engine to use
      String engineClass = (String) EnginePool.configurations.get(EngineIF.
          ENGINE_CLASS_KEY);
      
      if (StringUtils.isEmpty(engineClass)) {
        //Default to the HttpsEngine
        EnginePool.engineLogger.info("engine class not configured... defaulting to HttpsEngine");
        engineClass = "com.paymentech.orbital.sdk.engine.https.HttpsEngine";
      }
      
      fillEnginePool (engineClass);

      //Container for available engines
      EnginePool.enginesAvailable = new Stack();
      
      // Container for mapping engine users to the engines
      EnginePool.engineLogger.debug("creating engines-in-use hash table for " +
      		EnginePool.poolSize + " engines...");
      
      EnginePool.enginesInUse = new Hashtable(EnginePool.poolSize);
      
      // All of the engines are initially available
      EnginePool.engineLogger.debug("creating engines available stack for " +
      		EnginePool.poolSize + " engines...");
      
      for (short i = 0; i < EnginePool.poolSize; i++) {
        EnginePool.enginesAvailable.push(new Short(i));
      }
      
      EnginePool.engineLogger.debug(
          "********** End Engine Pool Initialization **********");
    }
    
    return instance;
  }

  /** Acquire an engine from the pool * */
  public EngineIF acquire() throws EngineNotAvailableException {
  	EngineIF engine = null;
  	String threadId = Thread.currentThread().getName();  	
    String message = "";  	
  	boolean reuseEngine = false;  	
  	
    EnginePool.engineLogger.debug("inside EnginePool.acquire");

    EnginePool.engineLogger.debug("threadId = " + threadId);
    
    synchronized (this) { // Begin Synchronized block
    	
      EnginePool.engineLogger.debug("inside synchronized code");
      EnginePool.engineLogger.debug("checking if this thread already has an engine acquired...");
      
      if (EnginePool.enginesInUse.containsKey(threadId)) {
        reuseEngine = true;
        EnginePool.engineLogger.debug("Thread [" + threadId + "] has already " +
        		"acquired an engine from the enginePool.  Will reuse previously allocated thread.");
      } else {
        EnginePool.engineLogger.debug("Thread [" + threadId +
        		"] has no engine acquired... will now attempt to acquire one");
      }
      
      if (reuseEngine) {
        //Return the engine that this thread previously acquired
        Short reuseEngineIndex = (Short)(EnginePool.enginesInUse.get(threadId));
        engine = (EngineIF) engines[reuseEngineIndex.intValue()];
      } else {
      	
        // Get an engine; block until one is available
        EnginePool.engineLogger.debug("checking if there are any engines available...");
        
        while (enginesAvailable.empty()) {
          try {
            EnginePool.engineLogger.debug("blocking - awaiting an available engine");
            wait();
          }
          catch (InterruptedException ie) {
            EnginePool.engineLogger.error("Thread [" + threadId +
            	"] could not obtain an engine.");
            throw new EngineNotAvailableException("Thread [" + threadId +
                "] could not obtain an engine.");
          }
        }
        
        // now we should be able to an engine
        Short availableIndex = (Short) enginesAvailable.pop();
        EnginePool.engineLogger.debug("getting engine for availableIndex = " + availableIndex);
        
        engine = (EngineIF) engines[availableIndex.intValue()];
        if (engine == null) {
          EnginePool.engineLogger.error("available engine is null");
          throw new EngineNotAvailableException("available engine is null");
        }
        
        
        EnginePool.engineLogger.debug("marking availableIndex " + availableIndex +
        		" as in-use by thread [" + threadId + "]");
        
        // Associate the threadId to the engine index.
        EnginePool.enginesInUse.put(threadId, availableIndex);
      }
      
    } // End Synchronized block

    try {
      //The engine is acquired... initialize it before returning it
      EnginePool.engineLogger.debug("initializing engine...");
      engine.init();
    } catch (InitializationException ie) {
      EnginePool.engineLogger.error("engine failed to initialize", ie);
      throw new EngineNotAvailableException(ie.getMessage());
    }
    
    return engine;
  }

  /** Release an engine back to the pool */
  public void release() {
  	String threadId = Thread.currentThread().getName();
  	
    EnginePool.engineLogger.debug("inside release...");

    synchronized (this) { // Begin Synchronized block
    	
      if (EnginePool.enginesInUse.containsKey(threadId)) {
      	
        EnginePool.engineLogger.debug("releasing engine allocated to thread [" +
        		threadId + "] ...");
        
        //Remove engine from the "in use" table
        Short engineIndex = (Short) EnginePool.enginesInUse.remove(threadId);
        
        //Make it available
        enginesAvailable.push(engineIndex);
        
        //Give the engine an opportunity to clean up before being returned to the pool
        ( (EngineIF) engines[engineIndex.intValue()]).release();
        
        // Tell the waiting threads an engine is available
        EnginePool.engineLogger.debug("notifying waiting threads that an engine is available...");
        
        notifyAll();
        
      } else {
        EnginePool.engineLogger.error("this thread released an engine that it did not previously acquire.");
      }
      
    } // End Synchronized block
    
  }

  public static boolean isFailover() {
    return failover;
  }

  public static void setFailover(boolean failover) {
    EnginePool.failover = failover;
  }

  public static long getFailoverStartMillis() {
    return EnginePool.failoverStartMillis;
  }

  public static void setFailoverStartMillis(long failoverStartMillis) {
    EnginePool.failoverStartMillis = failoverStartMillis;
  }
  
  // for testing only
  public static void resetPool () {
	  instance = null;
  }
  
  // for testing only
  public static int getAvailableEngineCount () {
	  return enginesAvailable.size();
  }
  
  
  private static void fillEnginePool (String engineClass) throws InitializationException {
    EngineIF engine = null;
    EnginePool.engineLogger.debug("creating pool of " + EnginePool.poolSize + " engines...");
    
    try {
      //Create an array of engines
      for (int i = 0; i < EnginePool.poolSize; i++) {
        engine = (EngineIF) ( ( (Class.forName(engineClass)).newInstance()));
        EnginePool.engines[i] = engine;
      }
    } catch (ClassNotFoundException cnfe) {
      EnginePool.engineLogger.error("could not find engine class " + engineClass, cnfe);
      throw new InitializationException(cnfe.getMessage());
    } catch (InstantiationException ie) {
      EnginePool.engineLogger.error("could not instantiate engine class" + engineClass, ie);
      throw new InitializationException(ie.getMessage());
    }
    catch (IllegalAccessException iae) {
      EnginePool.engineLogger.error("could not instantiate engine class" + engineClass, iae);
      throw new InitializationException(iae.getMessage());
    }  	
  }

  private static EnginePool instance = null;
  private static ConfiguratorIF configurator;
  private static Map configurations;
  private static Logger engineLogger;
  private static int poolSize = 10;
  private static Object[] engines = new Object[100];
  private static Hashtable enginesInUse = new Hashtable();
  private static Stack enginesAvailable = new Stack();
  private static boolean failover = false;
  private static long failoverStartMillis = 0;
}
