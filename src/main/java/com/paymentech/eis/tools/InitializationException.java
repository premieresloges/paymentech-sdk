/* InitializationException.java - 
 * This exception is thrown when a Configurable object is initialized
 * with illegal parameters and cannot complete its initialization. 
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:02:18  $
 *
 * $Archive:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/InitializationException.java-arc  $
 *
 * Revision history:
 *
 * $History: InitializationException.java $
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
 * This exception is thrown when a Configurable object is initialized
 * with illegal parameters and cannot complete its initialization.
 * <p/>
 * <p>When such exception is thrown, the object is not guaranteed
 * to be usable and the factory should behave accordingly.
 *
 * @author Jeff Palmiero
 * @version $Revision:   1.1  $
 */
public class InitializationException extends InstantiationException {
  /**
   * Default constructions
   */
  public InitializationException() {
    super();
  }

  /**
   * Construction with a message
   *
   * @params some message
   */
  public InitializationException(String message) {
    super(message);
  }
}
