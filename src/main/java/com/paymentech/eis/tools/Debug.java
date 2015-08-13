/* Debug.java - Debugging utilities.
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * $Header:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/Debug.java-arc   1.1   Feb 07 2007 08:01:56   byounie  $
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:01:56  $	$State$
 *
 * $Locker$
 * $Source$
 *
 * Revision history:
 *
 * $Log:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/Debug.java-arc  $
// 
//    Rev 1.1   Feb 07 2007 08:01:56   byounie
// Changed copyright
// 
//    Rev 1.0   Sep 05 2006 17:35:20   bkisiel
// Initial revision.
 * 
 * 1     12/09/03 8:22a Sayers
 * 
 * 12    5/14/01 9:06a Jpalmiero
 * Change the name of method from isVerboseMode to getVerboseMode
 *
 * 11    4/30/01 2:33p Mshah
 * Use Standard Name
 *
 * 10    4/16/01 3:27p Mshah
 * Remove Display
 *
 * 9     4/02/01 3:31p Jpalmiero
 * Changed package from com.pt to com.paymentech
 *
 * 8     3/12/01 4:47p Mshah
 * Do not Dump Stack
 *
 * 7     3/08/01 5:40p Mshah
 * Log Trace Error Messages to a Error FIle
 *
 * 6     1/24/01 5:11p Mshah
 *
 * 5     12/30/00 4:28p Jpalmiero
 * Removed support for GenericServlet so that I do not need Servlet SDK
 * for JBuilder
 *
 * 4     11/30/00 9:43a Jpalmiero
 * Commented out system.exit until needed again.
 *
 * 3     11/02/00 1:38p Jpalmiero
 * Temporarily changed ASSERT to do an exit.
 *
 * 2     10/25/00 5:37p Jpalmiero
 * Added code to trace so that it shows the current thread
*/

// Package declaration
package com.paymentech.eis.tools;

// Standard Java imports

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;

// Comment out the support for servlet logging for the moment so JBuilder
// can compile without having to bring in the Servlet SDK jar
// import javax.servlet.GenericServlet;

/**
 * Debug.java - Utilities class for doing PRE,POST, ASSERT.
 */
public class Debug {
  ////////////////////////////////////////////////////////////////
  // Private Members
  ////////////////////////////////////////////////////////////////

  private static boolean m_bDebug = false;
  private static boolean m_bVerbose = false;
  private static String m_errFile = null;

  /**
   * Private Constructor. Don't let anyone instantiate this class.
   */
  protected Debug() {
    ;
  }

  /**
   * PRECONDITION - Asserts a precondition. This is the same thing
   * as ASSERT but is defined to provide richer semantic meaning.
   */
  public static void PRECONDITION(String msg, boolean condition) {
    ASSERT(msg, condition);
  }

  /**
   * POSTCONDITION - Asserts a postcondition. This is the same thing
   * as ASSERT but is defined to provide richer semantic meaning.
   */
  public static void POSTCONDITION(String msg, boolean condition) {
    ASSERT(msg, condition);
  }

  /**
   * ASSERT - Asserts some condition. This will only be available
   * in the debug mode.
   */
  public static void ASSERT(String msg, boolean condition) {
    if (m_bDebug == true && !condition) {
      System.out.println("ASSERT FAILED: [" + msg + "]");
      Thread.dumpStack();

      // Temporary to stop JVM in its tracks
      // System.exit (0);
    }
  }

  /**
   * trace_debug - Prints to the log file if we are in the
   * debug mode.
   *
   * @params msg1    - some message
   * @params msg2    - some other message
   */
  public static void trace_debug(String msg1, String msg2) {
    if (m_bDebug == true) {
      System.out.println(Thread.currentThread().getName()
          + ":[" + msg1 + "] [" + msg2 + "]");
    }
  }

  /**
   * trace_verbose - Prints to the log file if we are in the
   * verbose mode.
   *
   * @params msg1    - some message
   * @params msg2    - some other message
   */
  public static void trace_verbose(String msg1, String msg2) {
    if (m_bVerbose == true) {
      System.out.println(Thread.currentThread().getName()
          + ":[" + msg1 + "] [" + msg2 + "]");
    }
  }

  /**
   * trace_error - Prints to the log file at all times.
   *
   * @params msg1    - some message
   * @params msg2    - some other message
   */
  public static void trace_error(String msg1, String msg2) {
    String errMsg = Thread.currentThread().getName()
        + ":[" + msg1 + "] [" + msg2 + "]";

    System.out.println(errMsg);

    if (m_errFile != null) {
      logErrorMessage(errMsg);
    }
  }

  /**
   * trace_debug - Prints debug messages to the servlet's log
   *
   * @params servlet      -       A logging servlet
   */

  // Comment out the support for servlet logging for the moment so JBuilder
  // can compile without having to bring in the Servlet SDK jar
    /*

    public static void trace_debug (GenericServlet servlet, String msg1,
        String msg2)
    {
        if (m_bDebug)
            servlet.getServletContext ().log ("[" + msg1 + "] [" + msg2 + "]");
    }
    */

  /**
   * trace_error - Prints to the servlet log
   *
   * @params servlet      -       A logging servlet
   */

  // Comment out the support for servlet logging for the moment so JBuilder
  // can compile without having to bring in the Servlet SDK jar
    /*
    public static void trace_error (GenericServlet servlet, String msg1,
        String msg2)
    {
        servlet.getServletContext ().log ("[" + msg1 + "] [" + msg2 + "]");
    }
    */

  /**
   * mode - Sets the debugging mode.
   *
   * @params value    - true for debugging <b>ON</b>
   */
  public static void mode(boolean mode) {
    m_bDebug = mode;
  }

  /**
   * verbose - Sets the verbosity mode.
   *
   * @params value    - true for vebose output
   */
  public static void verbose(boolean mode) {
    m_bVerbose = mode;
    //Thread.dumpStack();
  }

  /**
   * stackTraceAsString - Returns a stack trace as a String.
   *
   * @params exception    - some exception
   * @returns string      -		exception with stack trace
   */
  public static String stackTraceAsString(Exception ex) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintWriter writer = new PrintWriter(bytes, true);

    ex.printStackTrace(writer);

    return (bytes.toString());
  }

  /**
   * getVerboseMode
   */
  public static boolean getVerboseMode() {
    return m_bVerbose;
  }

  /**
   * Set the error file
   */
  public static void setErrorFile(String errFile) {
    m_errFile = errFile;
  }

  /**
   * Log a message
   */
  public static void logErrorMessage(String aErrorMsg) {
    try {
      FileWriter fw = new FileWriter(m_errFile, true);

      Date newDate = new Date();
      DateFormat formatDate =
          DateFormat.getDateInstance(DateFormat.LONG);
      DateFormat formatTime =
          DateFormat.getTimeInstance(DateFormat.LONG);

      fw.write("\n" + aErrorMsg + " Date: " + formatDate.format(newDate) +
          " Time: " + formatTime.format(newDate) + "\n");

      fw.flush();
      fw.close();
      //Thread.dumpStack();
    } catch (Exception exp) {
      Debug.trace_verbose("logMessage", "Error: " + exp.toString());
    }
  }

}
