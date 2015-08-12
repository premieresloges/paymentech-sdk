package com.paymentech.orbital.sdk.util.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.log4j.Logger;

import com.paymentech.orbital.sdk.configurator.ConfiguratorIF;


/**
 * <p><b>Title:</b> StrictSSLProtocolSocketFactory</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b>
 * <br>
 * <br> EasySSLProtocolSocketFactory can be used to creats SSL {@link Socket}s that accept self-signed certificates.
 * </p>
 **/
public class EasySSLProtocolSocketFactory implements SecureProtocolSocketFactory {

    /** Log object for this class. */
    private static final Logger LOG = Logger.getLogger(EasySSLProtocolSocketFactory.class);
    
    private ConfiguratorIF configurator = null;    

    /**
     * Constructor for EasySSLProtocolSocketFactory.
     */
    public EasySSLProtocolSocketFactory() {
        super();
    }
    
    public EasySSLProtocolSocketFactory (ConfiguratorIF configurator) {
    	super();
    	this.configurator = configurator;
    }    

    /**
     * @see SecureProtocolSocketFactory#createSocket(String,int,InetAddress,int)
     */
    public Socket createSocket(
        String host,
        int port,
        InetAddress clientHost,
        int clientPort)
        throws IOException, UnknownHostException {
    	
    	SSLSocketFactory sf = SocketFactoryFactory.getSocketFactory(configurator);
    	return sf.createSocket(
            host,
            port,
            clientHost,
            clientPort
        );
    }

    /**
     * @see SecureProtocolSocketFactory#createSocket(String,int)
     */
    public Socket createSocket(String host, int port)
        throws IOException, UnknownHostException {
    	
    	SSLSocketFactory sf = SocketFactoryFactory.getSocketFactory(configurator);
    	
        return sf.createSocket(
            host,
            port
        );
    }

    /**
     * @see SecureProtocolSocketFactory#createSocket(Socket,String,int,boolean)
     */
    public Socket createSocket(
        Socket socket,
        String host,
        int port,
        boolean autoClose)
        throws IOException, UnknownHostException {
    	
    	SSLSocketFactory sf = SocketFactoryFactory.getSocketFactory(configurator);
    	
        return sf.createSocket(
            socket,
            host,
            port,
            autoClose
        );
    }
	

   public Socket createSocket(
            String host,
            int port,
            InetAddress clientHost,
            int clientPort,
            HttpConnectionParams params )
            throws IOException, UnknownHostException 
    {
        	SSLSocketFactory sf = SocketFactoryFactory.getSocketFactory( configurator );
        	Socket retval = sf.createSocket( host, port, clientHost, clientPort );
        	// TODO: set socket parameters here
        	return retval;
    }


    public boolean equals(Object obj) {
        return ((obj != null) && obj.getClass().equals(EasySSLProtocolSocketFactory.class));
    }

    public int hashCode() {
        return EasySSLProtocolSocketFactory.class.hashCode();
    }

}
