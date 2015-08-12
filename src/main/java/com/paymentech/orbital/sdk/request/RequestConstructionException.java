package com.paymentech.orbital.sdk.request;

/**
 * <p><b>Title:</b> RequestConstructionException</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Indicates an error occurred during building a complex request object.  The message contains the specifics. </p>
 */
public class RequestConstructionException extends Exception {
    /** Default constructor */
    public RequestConstructionException() {
        super();
    }

    /**
     * Constructor including the message
     * @param message
     */
    public RequestConstructionException(String message) {
        super(message);
    }
}
