/* CipherUtils - Utility class for dealing with ciphers supported
 * by SSLSockets and SSLServerSockets
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
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:02:38  $
 *
 * $Archive:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/cipher/CipherUtils.java-arc  $
 *
 * Revision history:
 *
 * $History: CipherUtils.java $
 * 
 * *****************  Version 1  *****************
 * User: Sayers       Date: 12/09/03   Time: 8:23a
 * Created in $/gateway/active/OrbitalSDK/src/com/paymentech/eis/tools/cipher
 * 
 * *****************  Version 2  *****************
 * User: Jpalmiero    Date: 4/02/01    Time: 3:31p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools/cipher
 * Changed package from com.pt to com.paymentech
 * 
 * *****************  Version 1  *****************
 * User: Jpalmiero    Date: 11/10/00   Time: 8:46a
 * Created in $/Paymentech.com/development/com/pt/eis/tools/cipher
 * Utility file for dealing with SSL ciphers
*/

// Package declaration
package com.paymentech.eis.tools.cipher;

// Standard Java imports
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;

// XML imports

// Servlet imports

// Paymentech imports
import com.paymentech.eis.tools.Debug;

/**
 * Set of static utilities for manipulating the ciphers (eg algorithms)
 * supported by SSLSockets and SSLServerSockets
 */
public class CipherUtils
{
	/**
	 * Dump the ciphers that the secure socket supports
	 *
	 * @param socket The SSL Socket in question
	 */
	public static void dumpCipherSuites (SSLSocket socket)
	{
		String[] supported = socket.getEnabledCipherSuites ();
		dumpSupported (supported);
	}

	
	/**
	 * Dump the ciphers that the secure server socket supports
	 *
	 * @param socket The SSL Socket in question
	 */
	public static void dumpCipherSuites (SSLServerSocket server)
	{
		String[] supported = server.getEnabledCipherSuites ();
		dumpSupported (supported);
	}

	/**
	 * Helper method for dumping the supported ciphers
	 *
	 * @param supported String array containing all of the supported ciphers
	 */
	private static void dumpSupported (String[] supported)
	{
		for (int i = 0; i < supported.length; i++)
			{
			Debug.trace_verbose ("SecureUpstreamServer::dumpSupported",
				supported[i]);
			}
	}

	/**
	 * helper to get a string array of anonymous ciphers from the list
	 * of supported.
	 *
	 * @param supported A String array of supported ciphers
	 * @returns String[] A String array of anonymous ciphers
	 */
	private static String[] getAnonCipher (String[] supported)
	{
		String[] anonCipherSuitesSupported = new String [supported.length];

		int numAnonCipherSuitesSupported = 0;

		for (int i = 0; i < supported.length; i++)
			{
			if (supported[i].indexOf ("_anon_") > 0)
				{
				anonCipherSuitesSupported[numAnonCipherSuitesSupported++] =
					supported[i];
				}
			}

		// Create an array that has exactly numAnonSupported elements in it
		String[] anonSupported = new String [numAnonCipherSuitesSupported];
		System.arraycopy (anonCipherSuitesSupported, 0, anonSupported,
			0, numAnonCipherSuitesSupported);

		return anonSupported;
	}

	/**
	 * Enable anonymous ciphers as well as those which were previously enabled
	 * 
	 * @param socket The secure socket to enable the ciphers on
	 */
	public static void enableAnon (SSLSocket socket)
	{
		String[] supported = socket.getSupportedCipherSuites ();
		String[] anonCipherSuitesSupported = getAnonCipher (supported);

		String[] oldEnabled = socket.getEnabledCipherSuites ();
		String[] newEnabled = new String[oldEnabled.length + 
			anonCipherSuitesSupported.length];

		System.arraycopy (oldEnabled, 0, newEnabled, 0, oldEnabled.length);
		System.arraycopy (anonCipherSuitesSupported, 0, newEnabled,
			oldEnabled.length, anonCipherSuitesSupported.length);

		socket.setEnabledCipherSuites (newEnabled);
	}

	/**
	 * Enable anonymous ciphers as well as those which were previously enabled
	 * 
	 * @param server The secure server socket to enable the ciphers on
	 */
	public static void enableAnon (SSLServerSocket server)
	{
		String[] supported = server.getSupportedCipherSuites ();
		String[] anonCipherSuitesSupported = getAnonCipher (supported);

		String[] oldEnabled = server.getEnabledCipherSuites ();
		String[] newEnabled = new String[oldEnabled.length + 
			anonCipherSuitesSupported.length];

		System.arraycopy (oldEnabled, 0, newEnabled, 0, oldEnabled.length);
		System.arraycopy (anonCipherSuitesSupported, 0, newEnabled,
			oldEnabled.length, anonCipherSuitesSupported.length);

		server.setEnabledCipherSuites (newEnabled);
	}

	/**
	 * Enable only anonymous ciphers 
	 * 
	 * @param server The secure server socket to enable the ciphers on
	 */
	public static void enableOnlyAnon (SSLServerSocket server)
	{
		String[] supported = server.getSupportedCipherSuites ();
		String[] anonCipherSuitesSupported = getAnonCipher (supported);

		String[] newEnabled = new String[anonCipherSuitesSupported.length];

		System.arraycopy (anonCipherSuitesSupported, 0, newEnabled,
			0, anonCipherSuitesSupported.length);

		server.setEnabledCipherSuites (newEnabled);
	}

	/**
	 * Enable only anonymous ciphers 
	 * 
	 * @param socket The secure socket to enable the ciphers on
	 */
	public static void enableOnlyAnon (SSLSocket socket)
	{
		String[] supported = socket.getSupportedCipherSuites ();
		String[] anonCipherSuitesSupported = getAnonCipher (supported);

		String[] newEnabled = new String[anonCipherSuitesSupported.length];

		System.arraycopy (anonCipherSuitesSupported, 0, newEnabled,
			0, anonCipherSuitesSupported.length);

		socket.setEnabledCipherSuites (newEnabled);
	}
};
