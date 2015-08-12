/* Configuration.java - Encalupsulation of all configuration needed by
 * something that is "Confugurable". The idea for this interface was taken
 * form the Cocoon project.
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:00:14  $
 *
 * $Archive:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/Configurations.java-arc  $
 *
 * Revision history:
 *
 * $History: Configurations.java $
 * 
 * *****************  Version 1  *****************
 * User: Sayers       Date: 12/09/03   Time: 8:22a
 * Created in $/gateway/active/OrbitalSDK/src/com/paymentech/eis/tools
 * 
 * *****************  Version 7  *****************
 * User: Jpalmiero    Date: 5/23/01    Time: 5:30p
 * Updated in $/Paymentech.com/development/projects/eisCommon/src/com/paymentech/eis/tools
 * Changed Debug method call to getVerboseMode
 *
 * *****************  Version 6  *****************
 * User: Mshah        Date: 4/30/01    Time: 2:32p
 * Updated in $/Paymentech.com/development/com/paymentech/eis/tools
 * Use Standard Name
 *
 * *****************  Version 5  *****************
 * User: Jpalmiero    Date: 4/02/01    Time: 3:31p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools
 * Changed package from com.pt to com.paymentech
 *
 * *****************  Version 4  *****************
 * User: Jpalmiero    Date: 1/24/01    Time: 6:36a
 * Updated in $/Paymentech.com/development/com/pt/eis/tools
 * Changed one of the methods in the constructor to use hashTable.put
 * instead of Properties.setProperty.  This makes this class backward
 * compatiable with jvm 1.1.
 *
 * *****************  Version 3  *****************
 * User: Mshah        Date: 1/17/01    Time: 4:30p
 * Updated in $/Paymentech.com/development/com/pt/eis/tools
 * Added code to only list the configuration file if we are running in
 * Verbose mode.
 *
 * *****************  Version 2  *****************
 * User: Mshah        Date: 12/12/00   Time: 10:02a
 * Updated in $/Paymentech.com/development/com/pt/eis/tools
 * Use propertiesResourceBundle to locate and load the properties file
 * from the class path.
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
import java.io.*;
import com.paymentech.eis.tools.Debug;

/**
 * This class encapsulates all the configurations needed by a Configurable
 * class to work.
 *
 * @author 	Jeff Palmiero
 * @version $Revision:   1.1  $
*/
public class Configurations extends Properties
{
	///////////////////////////////////////////////////////////////////////
	// Member variables
	///////////////////////////////////////////////////////////////////////

    private String baseName;


	///////////////////////////////////////////////////////////////////////
	// Constructors
	///////////////////////////////////////////////////////////////////////

    /**
     * CT - Default constructor. Initialize the base clas.
    */
    public Configurations ()
	{
        super ();
    }

    /**
     * Create the class from a the file.
	 *
	 * @params		file 		-		file name to load
	 * @throws		Exception
    */
    public Configurations (String file) throws Exception
	{
        this (file, null);
    }

    /**
     * Create the class with given defaults and from the property file located in the class path.
	 * As long as the properties file is in the class path, the getBundle method of propertyResourceBundle
	 * will locate the file.
     *
	 * @params		file 		-		file name to load
	 * @params		config 		-		default configurartion
	 * @throws		Exception
    */
    public Configurations (String file, Configurations defaults)
		throws Exception
	{
        this (defaults);

		String base_name = file.substring(0,file.lastIndexOf('.'));
		PropertyResourceBundle bundle =
                (PropertyResourceBundle) PropertyResourceBundle.getBundle (base_name);

		String next_key;

		for (Enumeration e = bundle.getKeys();  e.hasMoreElements() ;)
 	    {
			next_key = (String)e.nextElement();

			String next_value = bundle.getString (next_key);

			// setProperty not supported in jdk 1.1, so use put method out
			// of the hashTable.
			//setProperty(next_key,next_value);
			put (next_key, next_value);
		}

		//Print out the properties
		if (Debug.getVerboseMode())
                  list(System.out);

    }

    /**
     * Create the class with given defaults.
	 *
	 * @params		config 		-		default configurartion
    */
    public Configurations (Configurations config)
	{
        super (config);
    }

    /**
     * Set the configuration
	 *
	 * @params		key 		-		key to set
	 * @params		value 		-		the value
    */
    public void set (String key, Object value)
	{
        super.put (key, value);
    }

    /**
     * Get the configuration.
	 *
	 * @params		key 		-		key to set
	 * @returns		value		-		value as an Object
    */
    public Object get (String key)
	{
        return super.get (key);
    }

    /**
     * Get the configuration and use the given default value if not found.
	 *
	 * @params		key 		-		key to set
	 * @params		def 		-		the default value
    */
    public Object get (String key, Object def)
	{
        Object o = super.get (key);

        return ((o == null) ? def : o);
    }

    /**
     * Get the configuration, throw an exception if not present.
	 *
	 * @params		key 		-		key to set
	 * @returns		Object		-		value as an Object
    */
    public Object getNotNull (String key)
	{
        Object o = super.get (key);

        if (o == null)
			{
			RuntimeException ex =
				new RuntimeException ("Configuration item '" + ((baseName ==
					null) ? "" : baseName + "." + key) + "' is not set");

			throw (ex);		// let someone know !!!
        	}

		return (o);
    }

    /**
     * Get a vector of configurations when the syntax is incremental
	 *
	 * @params		key 			-		key to set
	 * @returns		configurations	-		vector of configurations
    */
    public Vector getVector (String key)
	{
        Vector v = new Vector ();

        for (int i = 0; ; i++)
			{
            Object n = get (key + "." + i);

            if (n == null)
				break;

            v.addElement (n);
        	}

        return (v);
    }

    /**
     * Create a subconfiguration starting from the base node.
	 *
	 * @params	base		-		name of level to strip off
    */
    public Configurations getConfigurations (String base)
	{
        Configurations c = new Configurations ();
        c.basename ((baseName == null) ? base : baseName + "." + base);

    	String prefix = base + ".";

        Enumeration keys = this.propertyNames ();

        while (keys.hasMoreElements())
			{
            String key = (String) keys.nextElement ();

			if (key.startsWith (prefix))
				{
				c.set (key.substring (prefix.length ()), this.get (key));
				}
			else if (key.equals (base))
				{
				c.set ("", this.get (key));
				}
        }

    	return (c);
    }

	/**
	 * sets the basename
	 *
	 * @params	baseName	-	the new basename
	*/
    public void basename (String baseName)
	{
        this.baseName = baseName;
    }
}
