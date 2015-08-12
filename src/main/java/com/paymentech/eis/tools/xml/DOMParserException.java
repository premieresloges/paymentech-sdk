/* DOMParserException.java - Defines the exception class which indicates 
 * that something went wrong trying to parse an xml document.
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * $Header:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/xml/DOMParserException.java-arc   1.1   Feb 07 2007 08:03:16   byounie  $
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:03:16  $	$State$
 *
 * $Locker$
 * $Source$
 *
 * Revision history:
 *
 * $Log:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/xml/DOMParserException.java-arc  $
// 
//    Rev 1.1   Feb 07 2007 08:03:16   byounie
// Changed copyright
// 
//    Rev 1.0   Sep 05 2006 17:35:40   bkisiel
// Initial revision.
 * 
 * 1     12/09/03 8:24a Sayers
 * 
 * 2     4/02/01 4:28p Jpalmiero
 * Changed package from com.pt to com.paymentech
*/

// Package declaration
package com.paymentech.eis.tools.xml;

/**
 * DOMParserException - Indicates that something went wrong trying to parse
 * an xml document.
 *
 * @author		jpalmiero
 * @version		$Revision:   1.1  $
*/
public class DOMParserException extends Exception
{
	/**
	 * Constructs the exception with the human-readable error message. 
	 *
	 * @params	msg		The human-readable error message.
	*/
	DOMParserException (String msg)
	{
		super (msg);
	}
};
