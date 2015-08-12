package com.paymentech.orbital.sdk.engine.https;

import java.net.SocketException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

import com.paymentech.eis.tools.StringUtils;
import com.paymentech.orbital.sdk.engine.http.HttpEngine;
import com.paymentech.orbital.sdk.engine.http.HttpEngineConstants;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;
import com.paymentech.orbital.sdk.util.ssl.EasySSLProtocolSocketFactory;
import com.paymentech.orbital.sdk.util.ssl.StrictSSLProtocolSocketFactory;

/**
 * <p><b>Title:</b> HttpsEngine</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Provides a secure ssl (https) transaction </p>
 */
public class HttpsEngine extends HttpEngine {
    /**
     * The SSLSocketFactory is used by the HTTPConnection class to create secure connections.
     * There should only be one SSLSocketFactory, and therefore this class implements the singleton pattern.
     */
    protected static SecureProtocolSocketFactory socketFactory = null;

    /**
     * Initialize the socketFactory
     * @param configurator contains global configurations
     * @throws InitializationException if there are any errors initializing
     */
    public void init() throws InitializationException {
        super.init();
        //Get a socket factory to be used by HTTPConnection for creating a secure connection
        //Note: the socket factory is created once and is static(i.e. is a singleton)
        if (socketFactory == null) {
        	
        	// check what kind of ssl socket factory to create (default to strict, if none definied)
        	String sslSockectFactoryType = 
        			(String)this.configurator.getConfigurations().get(HttpEngineConstants.SSL_SOCKET_FACTORY_KEY);
			
			if (!StringUtils.isEmpty(sslSockectFactoryType)
					&& (sslSockectFactoryType.equalsIgnoreCase("default"))) {
	            socketFactory = (EasySSLProtocolSocketFactory) 
				(new EasySSLProtocolSocketFactory (this.configurator));						
			} else {
	            socketFactory = (SecureProtocolSocketFactory) 
				(new StrictSSLProtocolSocketFactory (this.configurator));			
			}
	
        }
    }

    /**
     * Return a secure (SSL) connection
     * @param hostname the Orbital Gateway hostname
     * @param port the Orbital Gateway SSL port
     * @param timeoutSeconds the number of seconds to timeout waiting for a connection
     * @return the HTTPConnection object
     * @throws IllegalStateException
     * @throws SocketException
     * @throws ProtocolNotSuppException if the underlying http connection classes do not support https
     */
    protected HttpClient getHttpClient(String hostname, int port, int connectionTimeoutSeconds, 
    	int readTimeoutSeconds) throws SocketException, IllegalStateException {

    	Protocol https = new Protocol("https", socketFactory, port);
    	
    	Protocol.registerProtocol("https", https);
        
    	HttpClient httpClient = new HttpClient();
		
        // set the connection time out
		httpClient.setConnectionTimeout(connectionTimeoutSeconds * 1000);		
        
        // set the read time out
		httpClient.setTimeout(readTimeoutSeconds * 1000);

	// turn off the internal retry handler
        HttpClientParams clientParams = httpClient.getParams();

        clientParams.setParameter( HttpMethodParams.RETRY_HANDLER, 
		new DefaultHttpMethodRetryHandler( 0, false ) );

        httpClient.setParams( clientParams );
		
        return httpClient;        
    }
    
    protected PostMethod getPostMethod (String hostname) {
    	return new PostMethod ("https://" + hostname);
    }    
}
