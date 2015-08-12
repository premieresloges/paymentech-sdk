/* Debug2.java - A second apporoach to creating a Debugging utilities.
 * This version is configurable. It also includes logging to a file.
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:02:02  $
 *
 * $Archive:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/Debug2.java-arc  $
 *
 * Revision history:
 *
 * $History: Debug2.java $
 * 
 * *****************  Version 1  *****************
 * User: Sayers       Date: 12/09/03   Time: 8:22a
 * Created in $/gateway/active/OrbitalSDK/src/com/paymentech/eis/tools
 * 
 * *****************  Version 2  *****************
 * User: Jpalmiero    Date: 4/02/01    Time: 3:31p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools
 * Changed package from com.pt to com.paymentech
 * 
 * *****************  Version 1  *****************
 * User: Jpalmiero    Date: 10/25/00   Time: 5:37p
 * Created in $/Paymentech.com/development/com/pt/eis/tools
 * Utility which allows user to log to a file
*/

// Package declaration
package com.paymentech.eis.tools;

// Standard imports
import java.io.*;					// for ByteArrayOutputStream
import java.lang.Thread;			// for Thread functions

/** 
 * Debug2.java - A second apporoach to creating a Debugging utilities.
 * This version is configurable. For example you could have this in
 * your config:
 *
 * <b>			something.debug = true 						</b>
 * <b>			something.verbose = true 					</b>
 * <b>			something.logfile = /var/spool/bung.out		</b>
*/
public class Debug2
{
	////////////////////////////////////////////////////////////////
	// Manifest constants 
	////////////////////////////////////////////////////////////////
	private final String DEBUG = "debug";  
	private final String VERBOSE = "verbose";  
	private final String LOGFILE = "logfile";  
	private final String SINGLE_LOG_FILE = "singleLogFile";

	////////////////////////////////////////////////////////////////
	// Private Members
	////////////////////////////////////////////////////////////////

	private boolean m_bDebug = false;			// debug status
	private boolean m_bVerbose = false;			// verbosity

	private ThreadLocal m_bSingleLogFile = null;	// Need to store wether
													// to use singleLogFile
													// or not on a thread
													// by thread basis

	private String m_name = "";					// output file name
	private static PrintStream m_globalOut = null;	// single log file for all
													// threads.
	private PrintStream m_out = null;				// thread specific output

    /**
     * Public Constructor. Don't let anyone instantiate this class.
    */
    public Debug2 () 
	{
	;
    }

    /**
     * Public Constructor given configuration information.
	 *
	 * @param	config		-		the configuration
	 *
	 * @see Configurations
    */
    public Debug2 (Configurations config)
	{
		try
			{
			if (config != null)
				{
				String verbose = (String) config.get (VERBOSE, "false");
				String debug = (String) config.get (DEBUG, "false");
				String singleLogFile = (String) config.get (SINGLE_LOG_FILE,
					"false");

				m_bVerbose = (verbose != null && verbose.equals ("true"))
					? true : false;
				m_bDebug = (debug != null && debug.equals ("true"))
					? true : false;

				boolean useSingleLogFile = (singleLogFile != null &&
					singleLogFile.equals ("true")) ? true : false;

				m_name = (String) config.get (LOGFILE, "");

				// Determine if we should use a single logfile or
				// a different log file for each thread
				m_bSingleLogFile = new ThreadLocal ();
				m_bSingleLogFile.set (new Boolean (useSingleLogFile));

				if (useSingleLogFile && !m_name.equals ("") && 
					m_globalOut == null)
					{
					synchronized (this)
						{
						if (m_globalOut == null)
							m_globalOut = new PrintStream (
								new FileOutputStream (m_name)); 
						}
					}
				else if (!useSingleLogFile && !m_name.equals (""))
					{ // If the thread does not want to use a single log file
					m_out = new PrintStream (
						new FileOutputStream (m_name));
					}
				
				}
			}

		catch (IOException ex)
			{
				m_out = null;	
			}
    }

	/**
	 * PRECONDITION - Asserts a precondition. This is the same thing
	 * as ASSERT but is defined to provide richer semantic meaning. 
	*/
	public void PRECONDITION (String msg, boolean condition)
	{
		ASSERT (msg, condition);
	}

	/**
	 * POSTCONDITION - Asserts a postcondition. This is the same thing
	 * as ASSERT but is defined to provide richer semantic meaning. 
	*/
	public void POSTCONDITION (String msg, boolean condition)
	{
		ASSERT (msg, condition);
	}

	/**
	 * ASSERT - Asserts some condition. This will only be available
	 * in the debug mode.
	*/
	public void ASSERT (String msg, boolean condition)
	{
		if (m_bDebug == true && !condition) 
			{
			System.out.println ("ASSERT FAILED: [" + msg + "]");
			Thread.dumpStack ();
			}
	}

	/**
	 * trace_debug - Prints to the log file if we are in the
	 * debug mode.
	 *
	 * @param	msg1		- 		some message
	 * @param	msg2		- 		some other message
	*/
	public void trace_debug (String msg1, String msg2)
	{
		if (m_bDebug == true)
			trace_error (msg1, msg2);
	}

	/**
	 * trace_verbose - Prints to the log file if we are in the
	 * verbose mode.
	 *
	 * @param	msg1		- 		some message
	 * @param	msg2		- 		some other message
	*/
	public void trace_verbose (String msg1, String msg2)
	{
		if (m_bVerbose == true)
			trace_error (msg1, msg2);
	}

	/**
	 * trace_error - Prints to the log file at all times.
	 *
	 * @param	msg1		- 		some message
	 * @param	msg2		- 		some other message
	*/
	public void trace_error (String msg1, String msg2)
	{
		System.out.println ("[" + msg1 + "] [" + msg2 + "]");
	}

	/**
	 * mode - Sets the debugging mode. 
	 *
	 * @param	value		- 		true for debugging <b>ON</b>	
	*/
	public void mode (boolean mode)
	{
		m_bDebug = mode;
	}

	/**
	 * mode - Reports whether debugging it turned on or not.
	 *
	 * @param	none
	 *
	 * @returns	Indicate if debugging it turned on.
	 */
	 public boolean mode ()
	 {
		return m_bDebug;
	 }

	/**
	 * verbose - Sets the verbosity mode.
	 *
	 * @param	value		- 		true for vebose output
	*/
	public void verbose (boolean mode)
	{
		m_bVerbose = mode;
	}

	/**
	 * verbose - Reports whether verbosity it turned on or not.
	 *
	 * @param	none
	 *
	 * @returns	Indicate if debugging it turned on.
	 */
	 public boolean verbose ()
	 {
		return m_bVerbose;
	 }

	/**
	 * stackTraceAsString - Returns a stack trace as a String. 
	 *
	 * @param	exception		- 	 	some exception	
	 * @return	string			-		exception with stack trace 
	*/
	public String stackTraceAsString (Exception ex)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream ();
		PrintWriter writer = new PrintWriter (bytes, true);
		
		ex.printStackTrace (writer);

		return (bytes.toString ());
	}

	/**
	 * trace_log - Logs to the specified log file. This will only
	 * be done if we are in the verbose mode. This method is thread
	 * safe.
	 *
	 * @param 	msg			-		first message
	 * @param 	msg2		-		second message
	 *
	*/
	synchronized public void trace_log (String msg, String msg2)
	{
		PrintStream logFile = out ();

		if (logFile != null && m_bVerbose == true)
			{
			logFile.println ("[" + System.currentTimeMillis () + "] [" + 
				Thread.currentThread ().getName () + "] [" + msg + "] [" +
				msg2 +"]");
			logFile.flush ();

			trace_verbose (msg, msg2);
			}
	}

	/**
	 * out - Returns the output print writer.   
	 * 
	 * @returns		Either the log print-writer or stdout 
	*/
	public PrintStream out ()
	{
		boolean useSingleLogFile = 
			((Boolean)m_bSingleLogFile.get ()).booleanValue ();

		PrintStream logFile = ((useSingleLogFile) ? m_globalOut : m_out);
		return (logFile != null ? m_out : System.out);
	}
		
}
