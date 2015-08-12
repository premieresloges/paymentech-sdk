/* MimeUtils - Set of utilities to help deal with mime headers
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:02:54  $
 *
 * $Archive:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/http/MimeUtils.java-arc  $
 *
 * Revision history:
 *
 * $History: MimeUtils.java $
 * 
 * *****************  Version 1  *****************
 * User: Sayers       Date: 12/09/03   Time: 8:24a
 * Created in $/gateway/active/OrbitalSDK/src/com/paymentech/eis/tools/http
 * 
 * *****************  Version 10  *****************
 * User: Jpalmiero    Date: 2/27/02    Time: 9:02a
 * Updated in $/Paymentech.com/development/projects/eisSSL/src/com/paymentech/eis/tools/http
 * Added a condition to only write the outfile.xml if Debug Verbose mode
 * is turned on.
 * 
 * *****************  Version 9  *****************
 * User: Smonahan     Date: 12/04/01   Time: 11:45a
 * Updated in $/Paymentech.com/development/projects/eisSSL/src/com/paymentech/eis/tools/http
 * Add JavaDoc
 * 
 * *****************  Version 8  *****************
 * User: Jpalmiero    Date: 4/02/01    Time: 4:27p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Changed package from com.pt to com.paymentech
 * 
 * *****************  Version 7  *****************
 * User: Mshah        Date: 12/27/00   Time: 8:56a
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Externalize the DTD and Transaction types
 * 
 * *****************  Version 6  *****************
 * User: Jpalmiero    Date: 12/20/00   Time: 2:41p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * 
 * *****************  Version 5  *****************
 * User: Jpalmiero    Date: 12/13/00   Time: 4:03p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Fixed a bug where when we are generated MIME headers we should be
 * ending each line with a LF instead of a CRLF.
 * 
 * *****************  Version 4  *****************
 * User: Jpalmiero    Date: 12/13/00   Time: 2:18p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Change code so that it will dynamically replace the request number tag
 * with the current request number.
 * 
 * *****************  Version 3  *****************
 * User: Jpalmiero    Date: 12/08/00   Time: 4:05p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Added string constants at the top instead of string literals.
 * 
 * *****************  Version 2  *****************
 * User: Jpalmiero    Date: 12/07/00   Time: 4:26p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Changed the mime header to conform to trintech's mime header standard.
 * 
 * *****************  Version 1  *****************
 * User: Jpalmiero    Date: 10/25/00   Time: 5:38p
 * Created in $/Paymentech.com/development/com/pt/eis/tools/http
 * Utility classes for working with Mime headers
*/

// Package declaration
package com.paymentech.eis.tools.http;

// Paymentech imports
import com.paymentech.eis.tools.StringUtils;
import com.paymentech.eis.tools.Debug;

/** MimeUtils - Set of utilities to help deal with mime headers*/
public class MimeUtils
{
    /**Value of content length tag = '<CONTENT_LENGTH>'*/
    private static final String CONTENT_LENGTH_TAG = "<CONTENT_LENGTH>";
    /**Value of document type tag = '<DOCUMENT_TYPE>'*/
    private static final String DOCUMENT_TYPE_TAG = "<DOCUMENT_TYPE>";
    /**Value of transaction number tag = '<TRANSACTION_NUMBER>'*/
    private static final String TRANSACTION_NUMBER_TAG = "<TRANSACTION_NUMBER>";
    /**Value of request response tag = '<REQUEST_RESPONSE_TAG>'*/
	private static final String REQUEST_RESPONSE_TAG = "<REQUEST_RESPONSE_TAG>";
    /**Java value for CRLF = '\n'*/
	private static final String CRLF = "\n";
    /**Value of request key used when prepending mime content = 'Request'*/
    public  static final String TRANS_REQUEST_TYPE = "Request";
	/**Value of response key used when prepending mime content = 'Response'*/
    public  static final String TRANS_RESPONSE_TYPE = "Response";
	/**Value of control request key used when prepending mime content = 'controlrequest'*/
    public  static final String CONTROL_REQUEST_TYPE = "controlrequest";
	/**Value of control response key used when prepending mime content = 'controlresponse'*/
    public  static final String CONTROL_RESPONSE_TYPE = "controlresponse";
	//public  static final String DTD_VERSION = "PTI6";
    /**Value of the Paymentech DTD version is use = 'PTI8'*/
	public  static final String DTD_VERSION = "PTI8";

    /** MIME Header (eventually we want to externalize this)*/
    private static final String MIME_HEADER =
        "MIME-Version: 1.0" + CRLF +
		MimeHeader.CONTENT_TYPE_HEADER + ": " +
		MimeHeader.CONTENT_TYPE_VALUE + "/" + DTD_VERSION + CRLF +
		MimeHeader.CONTENT_LENGTH_HEADER + ": " + CONTENT_LENGTH_TAG + CRLF +
        "Content-transfer-encoding: text" + CRLF +
		REQUEST_RESPONSE_TAG + ": " + TRANSACTION_NUMBER_TAG +
		CRLF +
		MimeHeader.DOCUMENT_TYPE_HEADER + ": " + DOCUMENT_TYPE_TAG + CRLF +
		CRLF; // Dont forget the blank line at the end

    /**
     * Helper method to tack a mimeHeader on the front of this xml request
     * @param rawMsg xml message that needs mime header
     * @param documentType Document type to be set in mime header
     * @param transType Transaction type to be set in mime header
     * @param reqNo Request number to be set in mime header
     */
	public static String prependMimeHeader (String rawMsg,
		String documentType, String transType, long reqNo)
    {
        StringBuffer request = new StringBuffer ();

        Integer length = new Integer (rawMsg.length ());

        // substitute the length for <CONTENT_LENGTH>
        String header = StringUtils.replace (MIME_HEADER,
            CONTENT_LENGTH_TAG, length.toString ());

        header = StringUtils.replace (header,
            DOCUMENT_TYPE_TAG, TRANS_REQUEST_TYPE);

		header = StringUtils.replace (header,
			REQUEST_RESPONSE_TAG, transType);

		String requestNumStr = (new Long (reqNo).toString ());
		
		header = StringUtils.replace (header,
			TRANSACTION_NUMBER_TAG, requestNumStr);

        request.append (header);
        request.append (rawMsg);

		if (Debug.getVerboseMode ()) {
			StringUtils.writeFile ("outbound" + requestNumStr + ".xml",
				request.toString ());
		}

        return request.toString ();
    }

  	/**
     * Helper method to tack a mimeHeader on the front of this xml request
     * @param rawMsg xml message that needs mime header
     * @param transType Transaction type to be set in mime header
     * @param transType_no Transaction type number to be set in mime header
     * @param dtd_ver DTD version to be set in mime header
     * @param reqNo Request number to be set in mime header
     */
	public static String prependMimeHeader (String rawMsg,
											String transType, 
											String transType_no,
											String dtd_ver,
											long reqNo)
    {
        StringBuffer request = new StringBuffer ();

        Integer length = new Integer (rawMsg.length ());

        // substitute the length for <CONTENT_LENGTH>
        String header = StringUtils.replace (MIME_HEADER,
            CONTENT_LENGTH_TAG, length.toString ());

        header = StringUtils.replace (header,
            DOCUMENT_TYPE_TAG, transType);

		header = StringUtils.replace (header,
			REQUEST_RESPONSE_TAG, transType_no);

		String requestNumStr = (new Long (reqNo).toString ());
		
		header = StringUtils.replace (header,
			TRANSACTION_NUMBER_TAG, requestNumStr);

		header = StringUtils.replace (header,
			DTD_VERSION, dtd_ver);

        request.append (header);
        request.append (rawMsg);

		if (Debug.getVerboseMode ()) {
			StringUtils.writeFile ("outbound" + requestNumStr + ".xml",
				request.toString ());
		}

        return request.toString ();
    }
	
};
