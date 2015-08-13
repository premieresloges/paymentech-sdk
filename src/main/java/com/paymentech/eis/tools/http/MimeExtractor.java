/* MimeExtractor - This is a utility to extract the mime header from
 * the stream.
 *
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:02:42  $
 *
 * $Archive:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/http/MimeExtractor.java-arc  $
 *
 * Revision history:
 *
 * $History: MimeExtractor.java $
 * 
 * *****************  Version 1  *****************
 * User: Sayers       Date: 12/09/03   Time: 8:24a
 * Created in $/gateway/active/OrbitalSDK/src/com/paymentech/eis/tools/http
 * 
 * *****************  Version 16  *****************
 * User: Mshah        Date: 9/27/02    Time: 1:35p
 * Updated in $/Paymentech.com/development/projects/eisSSL/src/com/paymentech/eis/tools/http
 * 
 * *****************  Version 1  *****************
 * User: Mshah        Date: 9/27/02    Time: 11:02a
 * Created in $/Paymentech.com/development/projects/PaymentechSDK/misc/oldCode/COM/paymentech/eis/tools/http
 * 
 * *****************  Version 15  *****************
 * User: Sayers       Date: 5/22/02    Time: 3:14p
 * Updated in $/Paymentech.com/development/projects/eisSSL/src/com/paymentech/eis/tools/http
 * Changed log levels from verbose to debug to reduce volume of logging in
 * SDK when running verbose mode
 * 
 * *****************  Version 14  *****************
 * User: Jpalmiero    Date: 2/18/02    Time: 9:14a
 * Updated in $/Paymentech.com/development/projects/eisSSL/src/com/paymentech/eis/tools/http
 * Changed so that these classes implement the Header and HeaderExtractor
 * interface.
 * 
 * *****************  Version 13  *****************
 * User: Smonahan     Date: 12/04/01   Time: 11:45a
 * Updated in $/Paymentech.com/development/projects/eisSSL/src/com/paymentech/eis/tools/http
 * Add JavaDoc
 * 
 * *****************  Version 11  *****************
 * User: Jpalmiero    Date: 4/25/01    Time: 8:21a
 * Updated in $/Paymentech.com/development/com/paymentech/eis/tools/http
 * Changed trace messages
 *
 * *****************  Version 10  *****************
 * User: Jpalmiero    Date: 4/02/01    Time: 4:27p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Changed package from com.pt to com.paymentech
 *
 * *****************  Version 9  *****************
 * User: Jpalmiero    Date: 3/28/01    Time: 12:01p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Fix for bad mac received message.
 *
 * *****************  Version 8  *****************
 * User: Mshah        Date: 3/08/01    Time: 5:40p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Change error reporting to Verbose
 *
 * *****************  Version 7  *****************
 * User: Mshah        Date: 1/30/01    Time: 9:40a
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Socket closed - verbiage change
 *
 * *****************  Version 6  *****************
 * User: Mshah        Date: 12/21/00   Time: 1:38p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Weblogic socket restart
 *
 * *****************  Version 5  *****************
 * User: Mshah        Date: 12/20/00   Time: 1:48p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Rethrow IO Exception
 *
 * *****************  Version 4  *****************
 * User: Jpalmiero    Date: 12/08/00   Time: 4:00p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * If I get an end of stream indicator, display it in an error message.
 *
 * *****************  Version 3  *****************
 * User: Jpalmiero    Date: 12/07/00   Time: 4:25p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Changed functionality to only look for a lineFeed in the mime header
 * from trintech
 *
 * *****************  Version 2  *****************
 * User: Jpalmiero    Date: 11/02/00   Time: 1:44p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/http
 * Changed MimeExtractor so that the read operation can be interrupted.
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

import javax.net.ssl.SSLProtocolException;
import java.io.IOException;
import java.io.Reader;
import java.net.SocketException;

// Paymentech imports
// SSL imports

/**
 * This utility class takes a reader and will return the entire
 * MIME header extracted from that reader.  This class is also
 * responsible for positing the reader at the first byte of the
 * payload.
 *
 * @author Jeff Palmiero
 * @version $Revision:   1.1  $
 */
public class MimeExtractor implements HeaderExtractor {
  private Reader m_reader = null;

  /**
   * Get a handle to the reader so that extract method can use it
   * to pluck out the MIME Header
   *
   * @param reader The reader which extract will work on (typically
   *               the underlying stream is a socket.)
   */
  public MimeExtractor(Reader reader) {
    Debug.PRECONDITION("reader != null", reader != null);
    m_reader = reader;
  }

  /**
   * Hide the default constructor because it leaves the extractor
   * in an inconsistent state.
   */
  private MimeExtractor() {
  }

  /**
   * Extracts the mime information into MimeHeader class
   *
   * @return Header
   * @throws IOException
   */
  public Header extract() throws IOException {

    MimeHeader retHeader = null;

    StringBuffer MIMEHeader = new StringBuffer();
    StringBuffer requestLine = new StringBuffer();

    int nextChar;
    boolean blankLineFound = false;

    try {
      while (!blankLineFound) {
        // smoke the requestLine
        requestLine.setLength(0);

        boolean lfFound = false;

        while ((nextChar = m_reader.read()) != -1) {
          // It appears that TT is not sending carriage returns
          // in their response message so just look for lineFeeds
          if (nextChar == '\n') lfFound = true;

          requestLine.append((char) nextChar);

          // If found a LF must be EOL
          if (lfFound) break;
        }


        if (nextChar == -1) {
          Debug.trace_debug("MimeExtractor::extract",
              " Got to the end of the file without "
                  + "detecting a blank line");

          // -1 imply's that either the stream is closed or the socket
          //was closed by the application or by the socket timeout.

          throw new SocketException("MimeExtractor reports that socket is closed");

        } else if (requestLine.charAt(0) == '\n') { // Found the blank line I was looking for
          blankLineFound = true;
        } else {
          Debug.trace_debug("MimeExtractor::extract",
              "Normal EOL (10) incountered -> " + nextChar + ".  Here is the line: "
                  + requestLine.toString());
          MIMEHeader.append(requestLine.toString());
        }
      }

      retHeader = new MimeHeader(MIMEHeader.toString());
    } catch (SSLProtocolException spe) {
      Debug.trace_error("MimeExtractor::extract",
          spe.getMessage());
      throw spe;
    } catch (SocketException soe) {
      //This could be a time out so do not report it as an error

      Debug.trace_debug("MimeExtractor::extract",
          soe.getMessage());

      // If the SocketException is due to an interrupt
      // request(code: 10004) from the factory
      // do not re-throw the exception, otherwise pass the
      // exception to the Reader Thread.

      if (soe.getMessage().indexOf("10004") == -1)
        throw soe;

    } catch (IOException ioe) {
      Debug.trace_error("MimeExtractor::extract",
          ioe.getMessage());
      throw ioe;
    }

    return retHeader;
  }
};
