package com.paymentech.orbital.sdk.engine.http;

import com.paymentech.eis.tools.StringUtils;
import com.paymentech.orbital.sdk.configurator.Configurator;
import com.paymentech.orbital.sdk.configurator.ConfiguratorIF;
import com.paymentech.orbital.sdk.engine.EngineIF;
import com.paymentech.orbital.sdk.engine.pool.EnginePool;
import com.paymentech.orbital.sdk.interfaces.RequestIF;
import com.paymentech.orbital.sdk.interfaces.ResponseIF;
import com.paymentech.orbital.sdk.response.Response;
import com.paymentech.orbital.sdk.transactionProcessor.TransactionException;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;
import com.paymentech.orbital.sdk.util.mimeHeader.MimeHeader;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.SocketException;
import java.util.Map;

/**
 * <p><b>Title:</b> HttpEngine</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p>
 * <p><b>Description:</b>
 * <br><br> Uses HTTP protocol to execute an Orbital Gateway transaction </p>
 */
public class HttpEngine implements EngineIF {
  protected ConfiguratorIF configurator;
  protected Logger engineLogger;
  protected Logger eCommerceLogger;
  protected Map configurations;
  protected String failoverHostName = "";
  protected int normalPort = 80;
  protected int failoverPort = 80;
  protected int connectionTimeoutSeconds = 90;
  protected int readTimeoutSeconds = 90;
  protected boolean verbose = false;
  // HTTPClient objects that should be able to hang around as long as this engine
  protected HttpClient httpClient = new HttpClient();
  protected HostConfiguration hostConfiguration = new HostConfiguration();
  private String normalHostName = "";

  /**
   * Initialize the engine
   *
   * @throws InitializationException if an error occurs during intialization
   */
  public void init() throws InitializationException {

    //Get the configurator (is a singleton)
    this.configurator = Configurator.getInstance();

    // make sure that we have one or we can't go on
    if (configurator == null) {
      engineLogger.error("engine requires a configurator");
      throw new NullPointerException("engine requires a configurator");
    }

    //Get the configurations Map from the Configurator
    this.configurations = configurator.getConfigurations();

    //Get the engine logger

    this.engineLogger = configurator.getCommonEngineLogger();
    this.eCommerceLogger = configurator.getCommonEcommerceLogger();
    engineLogger.debug("initializing engine...");

    //Get the verbosity
    String sVerbose = (String) configurations.get(HttpEngineConstants.VERBOSITY_KEY);
    if (!StringUtils.isEmpty(sVerbose)) {
      verbose = Boolean.getBoolean(sVerbose.toLowerCase());
    } else {
      verbose = false;
    }

    //Get the normal (non-failover) hostname
    normalHostName = (String) configurations.get(HttpEngineConstants.HOSTNAME_KEY);
    if (StringUtils.isEmpty(normalHostName)) {
      this.engineLogger.error("hostName is not configured.");
      throw new InitializationException("HostName is not configured in linehandler.properties file");
    }

    //Get the port
    try {
      normalPort = Integer.parseInt((String) configurations.get(HttpEngineConstants.PORT_KEY));
    } catch (Exception ex) {
      this.engineLogger.info("port is not configured... will default to port 80");
      normalPort = 80;
    }

    //Get the failover hostname
    failoverHostName = (String) configurations.get(HttpEngineConstants.HOSTNAME_FAILOVER_KEY);
    if (StringUtils.isEmpty(failoverHostName)) {
      this.engineLogger.info("failover hostName is not configured.");
    }

    //Get the failover port
    try {
      failoverPort = Integer.parseInt((String) configurations.get(HttpEngineConstants.PORT_FAILOVER_KEY));
    } catch (Exception ex) {
      this.engineLogger.info("failover port is not configured... will default to port 80");
      failoverPort = 80;
    }

    //Get the connection timeout
    try {
      connectionTimeoutSeconds = Integer.parseInt((String) configurations.get(HttpEngineConstants.CONNECTION_TIMEOUT_KEY));
    } catch (Exception ex) {
      this.engineLogger.info("Connection timeout is not configured... defaulting to 5 seconds.");
      connectionTimeoutSeconds = 90;
    }

    // read socket timeout here
    try {
      readTimeoutSeconds = Integer.parseInt((String) configurations.get(HttpEngineConstants.READ_TIMEOUT_KEY));
    } catch (Exception ex) {
      this.engineLogger.info("Read timeout is not configured... defaulting to 90 seconds.");
      readTimeoutSeconds = 90;
    }

  }

  /**
   * Executes a transaction; sends the request and returns the resulting response
   *
   * @param request the request
   * @return the response
   * @throws TransactionException
   */
  public ResponseIF execute(RequestIF request) throws InitializationException,
      IOException, TransactionException {
    PostMethod method = null;
    Response response = null;
    MimeHeader mimeHeader = null;
    HttpClient httpclient = null;

    try {

      engineLogger.debug("executing engine...");

      //Create the Mime Headers
      mimeHeader = new MimeHeader(this.configurator);

      //Create the PostMethod object
      httpclient = getHttpClient(determineHost(), determinePort(),
          connectionTimeoutSeconds, readTimeoutSeconds);

      setProxyHostAndPort(httpclient);

      engineLogger.debug("just before posting the request...");

      if (eCommerceLogger.isDebugEnabled()) {
        eCommerceLogger.debug("request " + request.getLogTransactionID() + " ==> "
            + request.getMaskedXML());
      }

      //authorizationURI is the path where the server will look for the xml dtd
      String authorizationURI = (String) configurations.get(HttpEngineConstants.AUTHORIZATION_URI_KEY);
      if (StringUtils.isEmpty(authorizationURI)) {
        authorizationURI = "/authorize";
      }

      method = getPostMethod(determineHost());

      method.setPath(authorizationURI);

      // populate the mime headers
      mimeHeader.populateMimeHeaders(method, request);

      // set the payload
      method.setRequestBody(request.getXML());

      httpclient.executeMethod(method);

      engineLogger.debug("just after posting the request...");

      engineLogger.debug("building the response...");

      // create the response object (since we have one)
      response = new Response();
      // add the mime header data to the payload
      StringBuffer xmlData = new StringBuffer(removeDoctype(method.getResponseBodyAsString()));
      Header resendCnt = method.getResponseHeader("retry-count");
      if (resendCnt != null) {
        int pos = xmlData.indexOf(">");
        if (pos > 0)
          xmlData = xmlData.insert(pos + 1, "<RetryCount>" + resendCnt.getValue() + "</RetryCount>");
      }
      Header lastResend = method.getResponseHeader("last-retry-attempt");
      if (lastResend != null) {
        int pos = xmlData.indexOf(">");
        if (pos > 0)
          xmlData = xmlData.insert(pos + 1, "<LastRetryAttempt>" + lastResend.getValue() + "</LastRetryAttempt>");
      }
      response.setRawMessage(xmlData.toString());

      if (eCommerceLogger.isDebugEnabled()) {
        eCommerceLogger.debug("response " + request.getLogTransactionID() + " ==> " + response.toMaskedXmlString());
      }

    } catch (InitializationException ie) {
      engineLogger.error("Unable to create a response object.", ie);
      throw ie;
    } catch (IOException ioe) {
      engineLogger.error("IOException occurred.", ioe);
      throw ioe;
    } catch (Throwable th) {
      engineLogger.error("Exception occurred.", th);
      throw new TransactionException(th.getMessage());
    } finally {
      if (method != null) {
        try {
          method.releaseConnection();
        } catch (Throwable th) {
          ;
        }
      }
    }

    return response;
  }

  public int getConnectionTimeout() {
    return this.connectionTimeoutSeconds;
  }

  public int getReadTimeout() {
    return this.readTimeoutSeconds;
  }

  /**
   * Logs the release of the engine back to the pool.
   */
  public void release() {
    engineLogger.debug("releasing engine...");
  }

  /**
   * Configures a connection object (Note: the TCP/IP connection is not actually established by this method)
   *
   * @param hostname       the hostname
   * @param port           the port
   * @param timeoutSeconds the number of seconds to wait for a connection
   * @return the connection object
   * @throws IllegalStateException
   * @throws SocketException
   * @throws ProtocolNotSuppException if the requested protocol (http or https) is not supported by the underlying
   *                                  http connection classes
   */
  protected HttpClient getHttpClient(String hostname, int port, int connectionTimeoutSeconds,
                                     int readTimeoutSeconds) throws SocketException, IllegalStateException {

    HttpClient httpClient = new HttpClient();

    // set the configuration information
    HostConfiguration config = new HostConfiguration();
    config.setHost(hostname, port);

    httpClient.setHostConfiguration(config);

    // set the connection time out
    httpClient.setConnectionTimeout(connectionTimeoutSeconds * 1000);

    // set the read time out
    httpClient.setTimeout(readTimeoutSeconds * 1000);

    return httpClient;
  }

  protected void setProxyHostAndPort(HttpClient httpClient) {

    String proxyHost = (String) configurations.get(HttpEngineConstants.PROXY_HOSTNAME);
    int proxyPort = 0;
    if (proxyHost != null) {
      try {
        proxyPort = Integer.parseInt((String) configurations.get(HttpEngineConstants.PROXY_PORT));
      } catch (Exception ex) {

        proxyPort = 0;
        engineLogger.warn("Missing or non numeric proxy port no is specified");
        engineLogger.warn("Disabling the proxy functionality from the linehandler.properties file");

      }
    }

    if ((proxyHost == null)) {
      proxyHost = System.getProperty("http.proxyHost");
      try {
        proxyPort = Integer.parseInt(System.getProperty("http.proxyPort", "80"));
      } catch (Exception ex) {
        engineLogger.warn("Missing or non numeric proxy port no is specified");
        engineLogger.warn("Disabling the proxy functionality from the JVM property");
        proxyPort = 0;
      }
    }

    if ((proxyHost != null) && (proxyHost.length() > 0)
        && (proxyPort > 0)) {
      engineLogger.debug("Setting proxy host to [" + proxyHost + "] " +
          " and proxy port to [" + Integer.toString(proxyPort) + "]");
      httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
    }
  }

  protected PostMethod getPostMethod(String hostname) {
    return new PostMethod();
  }

  /**
   * Helper method that removes the Document Type from the response message (the Document Type causes
   * some xml viewers to fail)
   *
   * @param input the xml message with doc type
   * @return the xml message with doc type stripped away
   */
  protected String removeDoctype(String input) {
    String pattern = "<!DOCTYPE Response";

    // locate where the doc type starts
    int beginIndex = input.indexOf(pattern);

    // this is needed in the case where the response
    // doesn't have a doctype
    if (beginIndex < 0) {
      return input;
    }

    int endIndex = input.indexOf(">", beginIndex);

    // grab the first part
    String filteredInput = input.substring(0, beginIndex - 1);

    // add the second part
    filteredInput += input.substring(endIndex);

    // return the input stripped of its doctype
    return filteredInput;
  }

  /**
   * Helper method to determine the correct host to use
   */
  protected String determineHost() {
    if (EnginePool.isFailover()) {
      return this.failoverHostName;
    } else {
      return this.normalHostName;
    }
  }

  /**
   * Helper method to determine the correct port to use
   */
  protected int determinePort() {
    if (EnginePool.isFailover()) {
      return this.failoverPort;
    } else {
      return this.normalPort;
    }
  }

  public String retFailOverHost() {
    return this.failoverHostName;
  }
}
