/* IResource.java	Interface representing a resource which can be managed
 * by a ResourceManager.
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * $Header:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/resource/IResource.java-arc   1.1   Feb 07 2007 08:03:00   byounie  $
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:03:00  $	$State$
 *
 * $Locker$
 * $Source$
 *
 * Revision history:
 *
 * $Log:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/resource/IResource.java-arc  $
// 
//    Rev 1.1   Feb 07 2007 08:03:00   byounie
// Changed copyright
// 
//    Rev 1.0   Sep 05 2006 17:35:36   bkisiel
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
 * IResource - The IResource is an interface representing an entity which can
 * be managed by a ResourceManager which can throttle access to it.
 *
 * @author		jpalmiero
 * @version		$Revision:   1.1  $
*/
public interface IResource 
{
	/**
	 * resourceAcquired			This method is invoked when the resource
	 * is acquired.
	*/
	public void resourceAcquired();

	/**
	 * resourceReleased			This method is invoked when the resource
	 * is released.
	*/
	public void resourceReleased();
};

