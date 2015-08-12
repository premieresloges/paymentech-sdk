package com.paymentech.orbital.sdk.util.filehandling;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.paymentech.orbital.sdk.configurator.Configurator;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;

/**
 * <p><b>Title:</b> ClassPathTemplateLoader</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br>
 * Implementation of the TemplateLoaderIF class. Read the contents of a XML template file from the classpath. </p>
 */
public class ClassPathTemplateLoader implements TemplateLoaderIF {

	public String loadTemplate(String value) throws InitializationException {
		// the value passed in should be the file name available in the classpath
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
	
	private String buildXMLAsString(String fileFromPath) throws InitializationException {
		String str = null;
		StringBuffer buffer = new StringBuffer();
		InputStream stream = null;
		Reader reader = null;
		BufferedReader bis = null;
		String path = fileFromPath;
		
		try {
			
			// get the file name only
			if (fileFromPath.indexOf("/") > -1) 
			{
				fileFromPath = fileFromPath.substring(fileFromPath.lastIndexOf("/") + 1);
			}
			stream = getClass().getResourceAsStream("/" + fileFromPath);
			if ( stream == null )
			{
				if (path.indexOf("/") > -1) 
				{
					path =path.substring(path.indexOf("/") + 1);
					stream = getFileAsInputStream(this, path);
				}
			}
			if ( stream == null )
			{
				Configurator.getInstance().getCommonEngineLogger().error("Exception - couldn't locate " + fileFromPath );
            throw new InitializationException("Exception - couldn't locate " + fileFromPath );
			}
			reader = new InputStreamReader (stream);
			bis = new BufferedReader(reader);
		
	        while ((str = bis.readLine()) != null) {
	            buffer.append(str.trim());
	        }
	        
	        Configurator.getInstance().getCommonEngineLogger().debug
				("XML File Path: -> " + fileFromPath);
        
        } catch (IOException e) {
        	Configurator.getInstance().getCommonEngineLogger().error
				("IOException - Check the file - " + fileFromPath, e);
            throw new InitializationException
				("IOException - Check the file - " + fileFromPath + e.getMessage());
        } catch (Exception e) {
        	Configurator.getInstance().getCommonEngineLogger().error
				("Exception - Check the file - " + fileFromPath, e);
            throw new InitializationException
				("Exception - Check the file - " + fileFromPath + e.getMessage());
        } finally {
            if (reader != null) { try { reader.close(); } catch (Exception ex) {;} }
            if (bis != null) { try { bis.close(); } catch (Exception ex) {;}  }
        }
        
        return buffer.toString();
        
	}

}
