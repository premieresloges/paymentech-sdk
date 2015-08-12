package com.paymentech.orbital.sdk.request;

/**
 * <p><b>Title:</b> FieldNotFoundException</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * OF Paymentech. The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Basava Parvataneni</p> <p><b>Description:</b><br><br> Indicates the field to be set was not found </p>
 */
public class FieldNotFoundException extends Exception {
    /** Default constructor */
    public FieldNotFoundException() {
    }

    /**
     * Constructor with message
     * @param message
     */
    public FieldNotFoundException(String message) {
        super(message);
    }
}
