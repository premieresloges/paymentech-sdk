/*
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:02:50  $
 *
 * $Archive:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/http/MimeHeader.java-arc  $
 *
 * Revision history:
 *
 * $History: MimeHeader.java $
 * 
 * *****************  Version 1  *****************
 * User: Sayers       Date: 12/09/03   Time: 8:24a
 * Created in $/gateway/active/OrbitalSDK/src/com/paymentech/eis/tools/http
 * 
 * *****************  Version 10  *****************
 * User: Mshah        Date: 9/27/02    Time: 1:36p
 * Updated in $/Paymentech.com/development/projects/eisSSL/src/com/paymentech/eis/tools/http
 * 
 * *****************  Version 9  *****************
 * User: Jpalmiero    Date: 2/18/02    Time: 9:15a
 * Updated in $/Paymentech.com/development/projects/eisSSL/src/com/paymentech/eis/tools/http
 * Changed so that these classes implement the Header and HeaderExtractor
 * interface.
 * 
 * *****************  Version 8  *****************
 * User: Smonahan     Date: 12/04/01   Time: 11:45a
 * Updated in $/Paymentech.com/development/projects/eisSSL/src/com/paymentech/eis/tools/http
 * Add JavaDoc
 * 
 * *****************  Version 7  *****************
 * User: Jpalmiero    Date: 4/02/01    Time: 4:27p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Changed package from com.pt to com.paymentech
 * 
 * *****************  Version 6  *****************
 * User: Jpalmiero    Date: 12/30/00   Time: 4:28p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Remove mail imports
 *
 * *****************  Version 5  *****************
 * User: Mshah        Date: 12/22/00   Time: 9:21a
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Get the XML Document type from the Mime Header
 *
 * *****************  Version 4  *****************
 * User: Jpalmiero    Date: 12/13/00   Time: 4:01p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Added method to determine the request sequence number from the MIME
 * header (used by the secure upstream linehandler)
 *
 * *****************  Version 3  *****************
 * User: Jpalmiero    Date: 12/13/00   Time: 2:17p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Moved common code for finding headers to a private method
 *
 * *****************  Version 2  *****************
 * User: Jpalmiero    Date: 12/08/00   Time: 4:01p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Added some class constrants instead of using string literals.
 *
 * *****************  Version 1  *****************
 * User: Jpalmiero    Date: 10/25/00   Time: 5:38p
 * Created in $/Paymentech.com/development/com/pt/eis/tools/http
 * Utility classes for working with Mime headers
*/

// Package declaration
package com.paymentech.eis.tools.http;

// Standard Java imports

import com.paymentech.eis.tools.Debug;

import java.util.StringTokenizer;

// Paymentech imports

/**
 * MimeHeader - This class represents an encapsulation of the mime
 * header information.  It hides the complexity of parsing through
 * a mime header.
 */
public class MimeHeader implements Header {
  /**
   * Mime header key used to retrieve Content-type value
   */
  public static final String CONTENT_TYPE_HEADER = "Content-type";
  /**
   * Mime header key used to retrieve Content-length
   */
  public static final String CONTENT_LENGTH_HEADER = "Content-length";
  /**
   * Mime header key used to retrieve Document-type
   */
  public static final String DOCUMENT_TYPE_HEADER = "Document-type";
  /**
   * Mime header key used to retrieve application
   */
  public static final String CONTENT_TYPE_VALUE = "application";
  /**
   * Mime header key used to retrieve Response-number
   */
  public static final String RESPONSE_NUMBER_HEADER = "Response-number";
  /**
   * Mime header key used to retrieve Request-number
   */
  public static final String REQUEST_NUMBER_HEADER = "Request-number";
  /**
   * Mime header key used to retrieve Request-number
   */
  public static final String RETRY_BYPASS_WARNING = "Warning-Msg";

  private String m_header = null;

  /**
   * Constructor
   *
   * @param header Raw header information
   */
  public MimeHeader(String header) {
    Debug.PRECONDITION("header != null", header != null);
    m_header = header;
  }

  /**
   * Get the length of the document payload
   *
   * @return int
   */
  public int getContentLength() {
    int contentLength = 0;
    String clString = "";

    if ((clString = getValue(CONTENT_LENGTH_HEADER)) != null) {
      contentLength = Integer.parseInt(clString);
    } else {
      Debug.trace_error("MimeHeader::getContentLength",
          "Could not find |" + CONTENT_LENGTH_HEADER + "| in |"
              + m_header + "|");
    }

    return contentLength;
  }

  /**
   * Get response number
   */
  public long getResponseNumber() {
    long responseNumber = 0;
    String rnString = "";

    if ((rnString = getValue(RESPONSE_NUMBER_HEADER)) != null) {
      responseNumber = Long.parseLong(rnString);
    } else {
      Debug.trace_error("MimeHeader::getResponseNumber",
          "Could not find |" + RESPONSE_NUMBER_HEADER + "| in |"
              + m_header + "|");
    }

    return responseNumber;
  }

  /**
   * Get request number
   */
  public long getRequestNumber() {
    long requestNumber = 0;
    String rnString = "";

    if ((rnString = getValue(REQUEST_NUMBER_HEADER)) != null) {
      requestNumber = Long.parseLong(rnString);
    } else {
      Debug.trace_error("MimeHeader::getRequest",
          "Could not find |" + REQUEST_NUMBER_HEADER + "| in |"
              + m_header + "|");
    }

    return requestNumber;
  }

  /**
   * Get retry bypass warning
   */
  public String getRetryBypassWarning() {
    return getValue(RETRY_BYPASS_WARNING);
  }

  /**
   * Get docuemnt type
   */
  public String getDocumentType() {

    return getValue(DOCUMENT_TYPE_HEADER);

  }

  private String getValue(String key) {
    int index = 0;
    String matchString = "";

    String matchingHeader = key + ":";

    if ((index = m_header.indexOf(matchingHeader)) != -1) {
      String remainder = m_header.substring(
          matchingHeader.length() + index);

      StringTokenizer tokenizer = new StringTokenizer(remainder);

      matchString = tokenizer.nextToken();
    }

    return matchString;
  }

  /**
   * Return raw header passed in through contructor
   */
  public String toString() {
    return m_header;
  }
};
