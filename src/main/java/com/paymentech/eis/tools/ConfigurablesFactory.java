/* ConfigurablesFactory - Implements the IConfigurablesFactory which 
 * defines me methods for creating and properly initialize dynamically 
 * loaded classes that are configurable. 
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 06 2007 17:02:54  $
 *
 * $Archive:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/ConfigurablesFactory.java-arc  $
 *
 * Revision history:
 *
 * $History: ConfigurablesFactory.java $
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
 * User: Jpalmiero    Date: 10/18/00   Time: 11:34a
 * Created in $/Paymentech.com/development/com/pt/eis/tools
 * Set of core classes that allow an object to be configurable
*/

// Package declaration
package com.paymentech.eis.tools;

// Standard Java imports 

import java.util.Enumeration;
import java.util.Vector;

/**
 * The ConfigurablesFactory implements IConfigurablesFactory for creating and
 * properly initialize dynamically loaded classes that are
 * configurable.
 *
 * @version $Revision:   1.1  $
 * @author Jeff Palmiero
 */
public class ConfigurablesFactory {
  /**
   * CT - Default construction of a ConfigurablesFactory.
   */
  public ConfigurablesFactory() {
    ;
  }

  /**
   * create - create the instance of a class and, if configurable, use
   * the given configurations to configure it.
   *
   * @params name    -		name of the class
   * @params conf    -		configuration information
   */
  public static Object create(String name, Configurations conf)
      throws RuntimeException {
    Object theObject = null;

    try {
      theObject = Class.forName(name).newInstance();

      if ((theObject instanceof IConfigurable) && (conf != null))
        ((IConfigurable) theObject).init(conf);
      else
        throw new RuntimeException("Error creating [" +
            name + "] : Class may not be an IConfigurable" +
            " or configuration is null");
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException("Error creating [" +
          name + "] : class is not found");
    } catch (IllegalAccessException ex) {
      throw new RuntimeException("Error creating [" +
          name + "] : does not have access");
    } catch (InstantiationException ex) {
      throw new RuntimeException("Error creating [" +
          name + "] : could not instantiate " + ex.getMessage());
    } catch (RuntimeException ex) {
      throw ex;    // just throw it !!
    } catch (NoClassDefFoundError ex) {
      throw new RuntimeException("Error creating [" + name +
          "]: make sure class is in the classpath");
    } catch (Throwable ex) {
      throw new RuntimeException("ConfigurablesFactory Error: " +
          "unknown exception creating [" + name + "] : " + ex);
    }

    return (theObject);
  }

  /**
   * create - create the instance of a class.
   *
   * @params name    -		name of the class
   */
  public static Object create(String name) throws RuntimeException {
    Object theObject = null;

    try {
      theObject = Class.forName(name).newInstance();
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException("Error creating [" +
          name + "] : class is not found");
    } catch (IllegalAccessException ex) {
      throw new RuntimeException("Error creating [" +
          name + "] : does not have access");
    } catch (InstantiationException ex) {
      throw new RuntimeException("Error creating [" +
          name + "] : could not instantiate " + ex.getMessage());
    } catch (RuntimeException ex) {
      throw ex;    // just throw it !!
    } catch (NoClassDefFoundError ex) {
      throw new RuntimeException("Error creating [" + name +
          "]: make sure class is in the classpath");
    } catch (Throwable ex) {
      throw new RuntimeException("ConfigurablesFactory Error: " +
          "unknown exception creating [" + name + "] : " + ex);
    }

    return (theObject);
  }

  /**
   * Create a vector of instances with given configurations.
   *
   * @params names    -		a vector of names
   * @params conf    -		configuration information
   */
  public static Vector create(Vector names, Configurations conf) {
    Vector objList = new Vector(names.size());
    Enumeration e = names.elements();

    while (e.hasMoreElements())
      objList.addElement(create((String) e.nextElement(), conf));

    return (objList);
  }
}
