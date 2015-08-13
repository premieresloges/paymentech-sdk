package com.paymentech.orbital.sdk.util.filehandling;

import com.paymentech.orbital.sdk.configurator.Configurator;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;

import java.io.*;

/**
 * <p><b>Title:</b> ClassPathTemplateLoader</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br>
 * Implementation of the TemplateLoaderIF class. Read the contents of a XML template file from the classpath. </p>
 */
public class ClassPathTemplateLoader implements TemplateLoaderIF {

  public String loadTemplate(String value) throws InitializationException {
    // the value passed in should be the file name available in the classpath
    return buildXMLAsString(value);
  }

  public InputStream getFileAsInputStream(Object owner, String filename) {
    InputStream is = null;
    String altFileName = filename.startsWith("/") ? filename : ("/" + filename);
    is = Thread.currentThread().getContextClassLoader().getResourceAsStream(altFileName);
    if (is == null)
      is = Thread.currentThread().getContextClassLoader().getResourceAsStream(altFileName);
    if (is == null)
      is = owner.getClass().getClassLoader().getResourceAsStream(filename);
    if (is == null)
      is = owner.getClass().getClassLoader().getResourceAsStream(altFileName);
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

      stream = getFileAsInputStream(this, path);
      if (stream == null) {
        Configurator.getInstance().getCommonEngineLogger().error("Exception - couldn't locate " + fileFromPath);
        throw new InitializationException("Exception - couldn't locate " + fileFromPath);
      }
      reader = new InputStreamReader(stream);
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
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception ex) {
          ;
        }
      }
      if (bis != null) {
        try {
          bis.close();
        } catch (Exception ex) {
          ;
        }
      }
    }

    return buffer.toString();

  }

}
