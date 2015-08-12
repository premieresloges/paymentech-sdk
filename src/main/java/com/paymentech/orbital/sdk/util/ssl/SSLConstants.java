package com.paymentech.orbital.sdk.util.ssl;

/**
 * <p><b>Title:</b> SSLConstants.java</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * Paymentech. The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Constants used by the SSL classes. </p>
 */
public interface SSLConstants {
    /** Locates the passphrase in the configurations. */
    String KEYSTORE_PASSPHRASE_KEY = "engine.ssl.keystore.passphrase";

    /** Locates the keystore file name in the configurations. */
    String KEYSTORE_FILENAME_KEY = "engine.ssl.keystore.filename";

    /** Locates the truststore passphrase in the configurations */
    String TRUSTORE_PASSPHRASE_KEY = "engine.ssl.trustore.passphrase";

    /** Locates the truststore filename in the configurations */
    String TRUSTORE_FILENAME_KEY = "engine.ssl.trustore.filename";
    
}
