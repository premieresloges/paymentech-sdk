/* IConfigurable - This interface must be implemented by all those classes 
 * that need parameters to specify their global behavior during their 
 * initialization. The idea of this class wass taken from the cocoon servlet
 * engine.
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:02:06  $
 *
 * $Archive:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/IConfigurable.java-arc  $
 *
 * Revision history:
 *
 * $History: IConfigurable.java $
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

/**
 * This interface must be implemented by all those classes that need 
 * parameters to specify their global behavior during their initialization.
 * 
 * <p>Every class must implement this interface and have empty contructor 
 * methods instead of relying on Reflection for configuration.
 *
 * @author	Jeff Palmiero
 * @version $Revision:   1.1  $
*/
public interface IConfigurable 
{
    /**
     * Initialize the class by passing its configurations.
    */
    public void init(Configurations conf) throws InitializationException;
}

