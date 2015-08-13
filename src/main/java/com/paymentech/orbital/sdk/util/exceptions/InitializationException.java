package com.paymentech.orbital.sdk.util.exceptions;

/**
 * <p><b>Title:</b> InitializationException.java</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * <p><b>Author:</b> Sam Ayers</p> <p><b>Description:</b><br><br>
 * Indicates an error occurred during initialization.  The message contains the specifics. </p>
 */
public class InitializationException extends InstantiationException {
  /**
   * Default constructor
   */
  public InitializationException() {
    super();
  }

  /**
   * Constructor including the message
   *
   * @param message
   */
  public InitializationException(String message) {
    super(message);
  }
}
