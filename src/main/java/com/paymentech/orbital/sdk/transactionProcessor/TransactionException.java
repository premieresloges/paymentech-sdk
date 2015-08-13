package com.paymentech.orbital.sdk.transactionProcessor;

/**
 * <p><b>Title:</b> TransactionProcessorException.java</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * SOURCE CODE OF Paymentech. The copyright notice above does not evidence any actual or intended publication of such source
 * code.</p> <p><b>Author:</b> Sam Ayers</p> <p><b>Description:</b><br><br>
 * Indicates an unrecoverable error occurred while attempting to process a transaction.  The message
 * contains the specifics. </p>
 */
public class TransactionException extends Exception {
  /**
   * Default constructor
   */
  public TransactionException() {
    super();
  }

  /**
   * Constructor including the message
   *
   * @param message
   */
  public TransactionException(String message) {
    super(message);
  }
}
