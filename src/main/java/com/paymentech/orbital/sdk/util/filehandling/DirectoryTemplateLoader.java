package com.paymentech.orbital.sdk.util.filehandling;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.paymentech.orbital.sdk.configurator.Configurator;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;

/**
 * <p><b>Title:</b> DirectoryTemplateLoader</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br>
 * Default implementation of the TemplateLoaderIF class. Read the contents of a XML template file from a given path. </p>
 */
public class DirectoryTemplateLoader implements TemplateLoaderIF {

	public String loadTemplate(String value) throws InitializationException {
		// the valule passed in should be the path (either with or without the PAYMENTECH_HOME
		// environment variable
		return buildXMLAsString (value);
	}
	public InputStream getFileAsInputStream(Object owner,String filename)
    {
    	InputStream is = null;
    	String altFileName = filename.startsWith("/") ? filename : ("/" + filename );
    	is = Thread.currentThread().getContextClassLoader().getResourceAsStream(altFileName);
    	if ( is == null )
    		is = Thread.currentThread().getContextClassLoader().getResourceAsStream(altFileName);
    	if ( is == null)
    		is = owner.getClass().getResourceAsStream( filename );
    	if ( is == null)
    		is = owner.getClass().getResourceAsStream( altFileName );
        return is;
    }
	
	 private String getenv( String variable )
		{
		 String retval = null;
	    	
	    	if ( System.getProperty( "java.version" ).startsWith( "1.4" ) )
	    	{
	    		retval = System.getProperty( variable );
	    	}
	    	else
	    	{
	    		retval = System.getenv( variable );
	    		
	    		if ( retval == null )
	    		{
	    			retval = System.getProperty( variable );
	    		}
	    	}
	    	return retval;
		}
	 
	private String buildXMLAsString(String xmlFilePath) throws InitializationException {
        StringBuffer buffer = new StringBuffer();
        String str = null;
        FileReader fr = null;
        BufferedReader bis = null;
        
        try {
	        // check whether file location as an environment variable embedded
	        ArrayList env = getFileLocation(xmlFilePath);
	        
	        // check whether an environment variable and its rest of the location are present
	        if (env.size() == 2) {
	            String path = getenv((String)env.get(0));
	            if (path == null) 
	            {
	            	// Ramesh
	            	// Check to see the file is in class path
	            	if (((String)env.get(1)).length() > 0 ) 
	            	{
	            		String name = (String)env.get(1);
	            		if ( name.startsWith("/") || name.startsWith("\\") )
	            		{
	            			name = name.substring(1);
	            		}
	            		InputStream is = this.getFileAsInputStream(this, name);
	            		if (is != null)
	            		{
	            			buffer = new StringBuffer();
	            			InputStream stream = null;
	            			Reader reader = null;
	            			bis = null;
	            			reader = new InputStreamReader(is);
	            			bis = new BufferedReader(reader);
	            	        while ((str = bis.readLine()) != null) 
	            	        {
	            	            buffer.append(str.trim());
	            	        }
	            	        if (reader != null) { try { reader.close(); } catch (Exception ex) {;} }
	                        if (bis != null) { try { bis.close(); } catch (Exception ex) {;}  }
	                        return buffer.toString();
	            		}
	            	}
	                Configurator.getInstance().getCommonEngineLogger().error
						("The Environment pattern '" + (String)env.get(0) +
	                    "' setting in the Configurations is not set up in the CLASSPATH!");
	                throw new InitializationException("Environment Variable is not set - " + (String)env.get(0));
	            }
	            
	            String file = (String)env.get(1);
	            path = path + file;
	            xmlFilePath = path;
	        }

        	Configurator.getInstance().getCommonEngineLogger().debug
				("XML File Path: -> " + xmlFilePath);
        	
           fr = new FileReader(xmlFilePath);
           bis = new BufferedReader(fr);
            
            while ((str = bis.readLine()) != null) {
                buffer.append(str);
            }
           
        } catch (FileNotFoundException e) {
        	Configurator.getInstance().getCommonEngineLogger().error
				("FileNotFoundException - Check the file - " + xmlFilePath, e);
            throw new InitializationException
				("FileNotFoundException - Check the file - " + xmlFilePath + e.getMessage());
        } catch (IOException e) {
        	Configurator.getInstance().getCommonEngineLogger().error
				("IOException - Check the file - " + xmlFilePath, e);
            throw new InitializationException
				("IOException - Check the file - " + xmlFilePath + e.getMessage());
        } catch (Exception e) {
        	Configurator.getInstance().getCommonEngineLogger().error
				("Exception - Check the file - " + xmlFilePath, e);
            throw new InitializationException
				("Exception - Check the file - " + xmlFilePath + e.getMessage());
        } finally {
            if (fr != null) { try { fr.close(); } catch (Exception ex) {;} }
            if (bis != null) { try { bis.close(); } catch (Exception ex) {;}  }
        }
        
        return buffer.toString();
    }	
	   
    private ArrayList getFileLocation(String xmlFilePath) {
        // search for "="
        StringTokenizer token = new StringTokenizer(xmlFilePath, "%");
        String path = null;
        String file = null;
        ArrayList arrayList = new ArrayList();
        // check whether the file location string as an embedded environment variable
        if (xmlFilePath.startsWith("%")) {
            while (token.hasMoreTokens()) {
                path = token.nextToken().toString();
                file = token.nextElement().toString();
                break;
            }
        }
        // add the environment variable and the rest of the file location into the list
        if ((path != null) && (file != null)) {
            arrayList.add(path);
            arrayList.add(file);
        }
        return arrayList;
    } 	   

}
