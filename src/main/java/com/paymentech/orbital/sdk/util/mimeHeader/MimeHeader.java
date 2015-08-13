package com.paymentech.orbital.sdk.util.mimeHeader;

import com.paymentech.eis.tools.StringUtils;
import com.paymentech.orbital.sdk.configurator.ConfiguratorIF;
import com.paymentech.orbital.sdk.engine.http.HttpEngineConstants;
import com.paymentech.orbital.sdk.interfaces.RequestIF;
import com.paymentech.orbital.sdk.request.RequestConstructionException;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * <p><b>Title:</b> MimeHeader.java</p> <p>(C)opyright 2003 Paymentech THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Paymentech.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br>
 * Creates a mime header properly formatted for the Orbital Gateway </p>
 */
public class MimeHeader {

  /**
   * Key for Mime Version Header
   */
  public static final String MIME_VERSION_HEADER = "MIME-Version";

  /**
   * Key for Content Type Header
   */
  public static final String CONTENT_TYPE_HEADER = "Content-type";

  /**
   * Key for Content Length Header
   */
  public static final String CONTENT_LENGTH_HEADER = "Content-length";

  /**
   * Key for Content Encoding Header
   */
  public static final String CONTENT_ENCODING_HEADER = "Content-transfer-encoding";

  /**
   * Key for Document Type Header
   */
  public static final String DOCUMENT_TYPE_HEADER = "Document-type";

  /**
   * Key for Request Number Header
   */
  public static final String REQUEST_NUMBER_HEADER = "Request-number";

  /**
   * Key for Response Number Header
   */
  public static final String RESPONSE_NUMBER_HEADER = "Response-number";

  /**
   * Key for Merchant ID Header
   */
  public static final String MERCHANT_ID_HEADER = "Merchant-id";

  /**
   * Key for Trace Number Header
   */
  public static final String TRACE_NUMBER_HEADER = "Trace-number";

  /**
   * Key for SDK Version Number Header
   */
  public static final String SDK_VERSION_HEADER = "Interface-version";

  /**
   * Key for Document Type value
   */
  public static final String DOC_TYPE_REQUEST = "Request";

  /**
   * Key for Document Type value
   */
  public static final String DOC_TYPE_RESPONSE = "Response";

  /**
   * Key for Document Type value
   */
  public static final String DOC_TYPE_CONTROL_REQUEST = "controlrequest";

  /**
   * Key for Document Type value
   */
  public static final String DOC_TYPE_CONTROL_RESPONSE = "controlresponse";

  /**
   * Key for Mime Header default value
   */
  public static final String MIME_VERSION_DEFAULT = "1.0";

  /**
   * Key for Mime Header default value
   */
  public static final String SDK_VERSION_DEFAULT = "version not specified";

  /**
   * Key for Mime Header default value
   */
  public static final String CONTENT_TYPE_DIRECTORY_DEFAULT = "application";

  /**
   * Key for Mime Header default value
   */
  public static final String DTD_VERSION_DEFAULT = "PTI21";

  /**
   * Key for Mime Header default value
   */
  public static final String CONTENT_ENCODING_DEFAULT = "text";

  /* Mime Header Properties*/
  private ConfiguratorIF configurator = null;
  private Map configurations = null;
  private Logger engineLogger = null;

  /**
   * Constructor
   *
   * @param configurator the configurator
   */
  public MimeHeader(ConfiguratorIF configurator) {

    if (!(configurator == null)) {
      this.configurator = configurator;
    } else {
      throw new NullPointerException("mime header requires configurator");
    }

    //Get the configurations Map from the Configurator
    this.configurations = configurator.getConfigurations();
    //Get the engine logger
    this.engineLogger = configurator.getCommonEngineLogger();
  }

  public void populateMimeHeaders(PostMethod method, RequestIF request) throws InitializationException, RequestConstructionException {

    // MIME Version
    Header header = new Header(MIME_VERSION_HEADER, MIME_VERSION_DEFAULT);
    method.addRequestHeader(header);

    // SDK Version
    header = new Header(SDK_VERSION_HEADER,
        (String) configurations.get(HttpEngineConstants.SDK_VERSION_KEY));
    method.addRequestHeader(header);

    // Content Type
    header = new Header(CONTENT_TYPE_HEADER,
        CONTENT_TYPE_DIRECTORY_DEFAULT + "/" + (String) configurations.get(HttpEngineConstants.DTD_VERSION_KEY));
    method.addRequestHeader(header);

    // Content Encoding Header
    header = new Header(CONTENT_ENCODING_HEADER, CONTENT_ENCODING_DEFAULT);
    method.addRequestHeader(header);

    // Request Number
    header = new Header(REQUEST_NUMBER_HEADER, Long.toString(request.getLogTransactionID()));
    method.addRequestHeader(header);

    // Document Type
    header = new Header(DOCUMENT_TYPE_HEADER, DOC_TYPE_REQUEST);
    method.addRequestHeader(header);

    // check if we are going to send a trace
    if (!StringUtils.isEmpty(request.getField("MerchantID"))
        && (!StringUtils.isEmpty(request.getTraceNumber()))) {

      header = new Header(MERCHANT_ID_HEADER, request.getField("MerchantID"));
      method.addRequestHeader(header);

      header = new Header(TRACE_NUMBER_HEADER, request.getTraceNumber());
      method.addRequestHeader(header);

    }

    if (engineLogger.isDebugEnabled()) {

      //Record the mime header in the engine log file
      StringBuffer sbMimeHeaders = new StringBuffer();

      //Transform the mimeHeaders into a String so we can log it
      for (int index = 0; index < method.getRequestHeaders().length; index++) {

        header = (Header) method.getRequestHeaders()[index];

        sbMimeHeaders.append(header.getName());
        sbMimeHeaders.append(": ");
        sbMimeHeaders.append(header.getValue());
        sbMimeHeaders.append("\n");

      }

      sbMimeHeaders.append("Content-length: ");
      sbMimeHeaders.append(request.getXML().length());
      sbMimeHeaders.append("\n");

      engineLogger.debug("request " + request.getLogTransactionID() + " mime header ==>\n" + sbMimeHeaders.toString());
    }
  }
}
