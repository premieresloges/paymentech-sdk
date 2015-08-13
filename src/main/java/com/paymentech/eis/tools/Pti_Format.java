/*
 * This is a generic utiltiy class to format ISO Messages.
 *
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:02:24  $
 *
 */

// Package declaration

package com.paymentech.eis.tools;

//Standard Java Imports

import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * This is a generic utiltiy class to format ISO Messages.
 *
 * @author Manjul Shah
 * @version $Revision:   1.1  $
 */
public class Pti_Format {

  /**
   * This will either truncate or pad the string with spaces based on the length
   *
   * @param str to be formatted
   * @return formatted_string set to the specified length
   * @len the exact length the string needs to be
   */
  public static String FormatString(String str, int len) {
    String formatted_string = new String();
    if (str.length() > len)
      formatted_string = str.substring(0, len);
    else {
      String base = "                                                                                                             ";
      formatted_string = str + base.substring(0, len - str.length());
    }
    return formatted_string;
  }

  /**
   * Formats an ISO number format to specified length padding with 0's
   *
   * @param str    the String containing the number to format
   * @param length the length it needs to be
   * @returns the formatted number (as a String)
   */
  public static String FormatNumber(String str, int length)
      throws ParseException {
    String pattern = "0000000000000000000000000000000000000000000000000000000000000000";
    pattern = pattern.substring(0, length);
    DecimalFormat myFormatter = new DecimalFormat(pattern);
    String r_str = new String();
    r_str = myFormatter.format(myFormatter.parse(str));
    return r_str;
  }

  /**
   * @param mbittext the text to return
   * @return the Length + the mbittext
   * @pattern Decimal format pattern
   */
  public static String FormatXVar(String mbittext, String pattern) {
    DecimalFormat myFormatter = new DecimalFormat(pattern);
    String BitTextLen = myFormatter.format(mbittext.length());
    return (BitTextLen + mbittext);
  }

  /**
   * @param str The string to add filler to
   * @param len The number of blank characters to add
   * @return the string with the specified number of blank characters at the end.
   */
  public static String AddFiller(String str, int len) {
    String Filler = "                                                                       ";
    return (str + Filler.substring(0, len));
  }
}
