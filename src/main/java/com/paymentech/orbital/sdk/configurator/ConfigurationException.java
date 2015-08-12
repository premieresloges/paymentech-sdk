package com.paymentech.orbital.sdk.configurator;

import com.paymentech.orbital.sdk.util.exceptions.InitializationException;

/**
 * <p><b>Title:</b> ConfigurationException.java</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * OF Paymentech. The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Basava Parvataneni</p> <p><b>Description:</b><br><br> Indicates a configurator error </p>
 */
public class ConfigurationException extends InitializationException {
    /** Default Constructor */
    ConfigurationException() {
    }

    /**
     * Constructor including message
     * @param message
     */
    ConfigurationException(String message) {
        super(message);
    }
}
