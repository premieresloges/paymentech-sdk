/* ResourceNotAvailable.java	Exception class which is thrown when a 
 * resource cannot be acquired from a ResourceManager for some reason.
 *
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * $Header:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/resource/ResourceNotAvailable.java-arc   1.1   Feb 07 2007 08:03:10   byounie  $
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:03:10  $	$State$
 *
 * $Locker$
 * $Source$
 *
 * Revision history:
 *
 * $Log:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/resource/ResourceNotAvailable.java-arc  $
// 
//    Rev 1.1   Feb 07 2007 08:03:10   byounie
// Changed copyright
// 
//    Rev 1.0   Sep 05 2006 17:35:38   bkisiel
// Initial revision.
 * 
 * 1     12/09/03 8:24a Sayers
 * 
 * 2     4/02/01 4:27p Jpalmiero
 * Changed package from com.pt to com.paymentech
*/

// Package declaration
package com.paymentech.eis.tools.resource;

/**
 * ResourceNotAvailable - An exception class indicating that a resource 
 * cannot be obtained from a ResourceManager for some reason.
 *
 * @author		jpalmiero
 * @version		$Revision:   1.1  $	
*/
public class ResourceNotAvailable extends Throwable  
{
	/**
	 * Constructor initializes exception with a human-readable error message.
	 *
	 * @params	message		The human-readable error message
	*/
	ResourceNotAvailable (String message)
	{
		super (message);
	}
};

