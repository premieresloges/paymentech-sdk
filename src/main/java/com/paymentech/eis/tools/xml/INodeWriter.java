/* INodeWriter.java		--	Defines the com.paymentech.eis.tools.xml.INodeWriter 
 * interface which a client uses to print an org.w3c.dom.Node instance to
 * a java output stream.   
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * $Header:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/xml/INodeWriter.java-arc   1.1   Feb 07 2007 08:03:26   byounie  $
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:03:26  $	$State$
 *
 * $Locker$
 * $Source$
 *
 * Revision history:
 *
 * $Log:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/xml/INodeWriter.java-arc  $
// 
//    Rev 1.1   Feb 07 2007 08:03:26   byounie
// Changed copyright
// 
//    Rev 1.0   Sep 05 2006 17:35:42   bkisiel
// Initial revision.
 * 
 * 1     12/09/03 8:24a Sayers
 * 
 * 2     4/02/01 4:28p Jpalmiero
 * Changed package from com.pt to com.paymentech
*/

// Package declaration
package com.paymentech.eis.tools.xml;

// Standard Java imports
import java.io.OutputStream;
import java.io.IOException;
import java.io.Writer;

// XML imports
import org.w3c.dom.Node;
import org.w3c.dom.DOMException;

/**
 * INodeWriter -  Abstract class for printing org.w3c.dom.Node elements with
 * the UTF-8 encoding scheme (plain-old ascii).  Support for other encoding 
 * schemes could be added here.
 *
 * @author		jpalmiero
 * @version		$Revision:   1.1  $
*/
public abstract class INodeWriter
{
	/**
	 * printNode 	Prints 'theNode' to the output stream 'os' with the
	 * UTF-8 encoding.
	 *
	 * @params	theNode		The node to print
	 * @params	os			The output stream 
	 *
	*/
	public abstract void printNode (Node theNode, OutputStream os)
		throws DOMException, IOException;

	public abstract void printNode (Node theNode, Writer writer)
		throws DOMException, IOException;

	/**
	*  nodeToString			Returns a string buffer containing 
	*  the printed node with encoding UTF-8.	
	*
	*  @params	theNode		The node to print
	*/
	public abstract StringBuffer nodeToString (Node theNode)
		throws DOMException; 	

	/**
	* instance		Returns an INodeWriter instance.  Actually, a 
	* NodeWriterImpl instance is returned, but this is immaterial to the
	* client. This enforces the singleton pattern.
	*/
	public static INodeWriter instance ()
	{
		return m_nodeWriter;	
	}

	// Instance variables 
	private static INodeWriter m_nodeWriter = new NodeWriterImpl ();  
};

