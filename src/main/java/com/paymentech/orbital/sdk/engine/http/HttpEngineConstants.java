package com.paymentech.orbital.sdk.engine.http;

/**
 * <p><b>Title:</b> HttpEngineConstants</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * Paymentech. The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p>
 * <p><b>Description:</b><br><br> Constants used by the HTTP engine </p>
 */
public interface HttpEngineConstants {
    /** "true" means verbose */
    String VERBOSITY_KEY = "engine.verbose";

    /** Hostname of the Orbital Gateway that will process our transactions */
    String HOSTNAME_KEY = "engine.hostname";

    /** Port of the Orbital Gateway that will process our transactions */
    String PORT_KEY = "engine.port";

    /** Hostname of the Orbital Gateway that will process our transactions while in failover mode */
    String HOSTNAME_FAILOVER_KEY = "engine.hostname.failover";

    /** Port of the Orbital Gateway that will process our transactions while in failover mode */
    String PORT_FAILOVER_KEY = "engine.port.failover";

    /** Timeout value in seconds for the HTTP connection */
    String CONNECTION_TIMEOUT_KEY = "engine.connection_timeout_seconds";
    
    /** Timeout value in seconds for the HTTP connection */
    String READ_TIMEOUT_KEY = "engine.read_timeout_seconds";    

    /** The DTD Version that will be used by the Gateway to validate our request */
    String DTD_VERSION_KEY = "DTDVersion";

    /** The DTD Version that will be used by the Gateway to validate our request */
    String SDK_VERSION_KEY = "engine.sdk_version";

    /** The URI to which the request will be posted */
    String AUTHORIZATION_URI_KEY = "engine.authorizationURI";
    
    /** Type of SSL Socket Factory implementation to use */
    String SSL_SOCKET_FACTORY_KEY = "engine.ssl.socketfactory"; 
    
    String PROXY_HOSTNAME = "engine.proxyname";
    
    String PROXY_PORT = "engine.proxyport";
    
    
}
