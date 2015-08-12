/* The IConfigurablesFactory defines methods for creating and 
 * properly initialize dynamically loaded classes that are
 * configurable. 
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:02:12  $
 *
 * $Archive:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/IConfigurablesFactory.java-arc  $
 *
 * Revision history:
 *
 * $History: IConfigurablesFactory.java $
 * 
 * *****************  Version 1  *****************
 * User: Sayers       Date: 12/09/03   Time: 8:23a
 * Created in $/gateway/active/OrbitalSDK/src/com/paymentech/eis/tools
 * 
 * *****************  Version 2  *****************
 * User: Jpalmiero    Date: 4/02/01    Time: 3:31p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools
 * Changed package from com.pt to com.paymentech
 * 
 * *****************  Version 1  *****************
 * User: Jpalmiero    Date: 10/18/00   Time: 11:34a
 * Created in $/Paymentech.com/development/com/pt/eis/tools
 * Set of core classes that allow an object to be configurable
*/

// Package declaration
package com.paymentech.eis.tools;

// Standard Java imports 
import java.util.*;

/**
 * The IConfigurablesFactory defines methods for creating and 
 * properly initialize dynamically loaded classes that are
 * configurable. 
 *
 * @author	Jeff Palmiero
 * @version $Revision:   1.1  $
*/
public interface IConfigurablesFactory
{
    /**
     * Create the instance of a class and, if configurable, use 
     * the given configurations to configure it.
	 *
	 * @params		name		-		name of the class
	 * @params		conf		-	 	configuration info	
    */
    public Object create(String name, Configurations conf);
    
    /**
     * Create a vector of instances with given configurations.
	 *
	 * @params		names		-		names of the class
	 * @params		conf		-	 	configuration info	
    */
    public Vector create(Vector names, Configurations conf);
}
