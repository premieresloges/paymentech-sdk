package com.paymentech.orbital.sdk.request;

/**
 * <p><b>Title:</b> XMLTemplateNotFoundException</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * SOURCE CODE OF Paymentech. The copyright notice above does not evidence any actual or intended publication of such source
 * code.</p> <p><b>Author:</b> Basava Parvataneni</p> <p><b>Description:</b><br><br>
 * Indicates a required xml template was not found. </p>
 */
public class XMLTemplateNotFoundException extends Exception {
  /**
   * Default constructor
   */
  public XMLTemplateNotFoundException() {
  }

  /**
   * Constructor including message
   *
   * @param message
   */
  public XMLTemplateNotFoundException(String message) {
    super(message);
  }
}
