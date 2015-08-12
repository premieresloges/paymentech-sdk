package com.paymentech.orbital.sdk.interfaces;

import com.paymentech.orbital.sdk.transactionProcessor.TransactionException;

/**
 * <p><b>Title:</b> TransactionProcessorIF</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * OF Paymentech. The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Sam Ayers</p> <p><b>Description:</b><br><br>
 * Published interface for the transaction processor class. </p>
 */
public interface TransactionProcessorIF {
    /**
     * Process an Orbital Gateway Transaction
     * @param myRequest
     * @return ResponseIF The response object
     */
    ResponseIF process(RequestIF myRequest) throws TransactionException;
}
