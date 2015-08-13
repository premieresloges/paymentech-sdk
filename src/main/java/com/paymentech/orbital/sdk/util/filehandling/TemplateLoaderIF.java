package com.paymentech.orbital.sdk.util.filehandling;

import com.paymentech.orbital.sdk.util.exceptions.InitializationException;

/**
 * <p><b>Title:</b> TemplateLoaderIF</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br>
 * Interface used for reading the XML templates from the file system or classpath </p>
 */
public interface TemplateLoaderIF {

  public String loadTemplate(String value) throws InitializationException;

}
