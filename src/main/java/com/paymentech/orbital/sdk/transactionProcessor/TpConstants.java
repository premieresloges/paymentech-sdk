package com.paymentech.orbital.sdk.transactionProcessor;

/**
 * <p><b>Title:</b> TpConstants.java</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Sam Ayers</p> <p><b>Description:</b><br><br> Constants used by the Transaction Processor. </p>
 */
public interface TpConstants {
    /** The key for locating the verbosity mode in the configurations. */
    String VERBOSITY_KEY = "TransactionProcessor.verbose";

    /** The key for locating the pool size in the configurations. */
    String POOL_SIZE_KEY = "TransactionProcessor.poolSize";

    /** The key for locating the number of retries in the configurations. */
    String NUMBER_OF_RETRIES_KEY = "TransactionProcessor.retries";

    /** The Default Pool Size (the maximum number of concurrent transactions) */
    String DEFAULT_POOL_SIZE = "5";
    
    String RETRY_EXCEPTION = "TransactionProcessor.retryExceptionList";
}
