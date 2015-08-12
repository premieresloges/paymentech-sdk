package com.paymentech.eis.tools.logging;

import java.util.Enumeration;
import java.util.Properties;
import java.io.IOException;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.paymentech.eis.tools.InitializationException;

public class LoggingSetup {

    public LoggingSetup() {}

    public static void configureLogging(Properties config) throws InitializationException {
		configureLogging(config, true);
    }

    /**
     * Configure the logging objects
     */
    public static void configureLogging(java.net.URL url)
    {
    	if (url != null)
    		DOMConfigurator.configure(url);
        else
        	BasicConfigurator.configure();
    }
    
    /**
     * Configure the logging objects
     * @param configFilename
     * @param forceReload
     */
   public static void configureLogging(String configFilename, boolean live)
   {
	   if (configFilename != null)
	   {
		   if ( live )
			   DOMConfigurator.configureAndWatch(configFilename);
		   else
			   DOMConfigurator.configure(configFilename);
		  
	   }
       else
    	   BasicConfigurator.configure();
    }
    /**
     * 
     * @param config
     * @param forceReload
     * @throws InitializationException
     */
    public static void configureLogging(Properties config, boolean forceReload) throws InitializationException {
        Element configElement = null;
        if ((forceReload)) {
			if (config != null) {
				PropertyConfigurator.configure(config);
			}
			if ((config == null) || (config.size() == 0)) {
				BasicConfigurator.configure();
			}
        }
    }

    public static boolean isConfigured(String loggerName) 
    {
    	try
    	{
         Enumeration  logs = LogManager.getCurrentLoggers();
         while (logs.hasMoreElements()) 
         {
             	Category c = (Category)logs.nextElement();
             	if ( c.getName().equals(loggerName))
             	{
             		if (!(c.getAllAppenders() instanceof org.apache.log4j.helpers.NullEnumeration))
                     return true;
             	}
          }
          return false;
    	}
    	catch(Exception e)
    	{
    		return false;
    	}
    }

}
