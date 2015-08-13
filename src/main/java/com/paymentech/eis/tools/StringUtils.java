/* StringUtils - Contains static utilities needed by the PageFactories
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * $Header:   //nsmpfile1/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/StringUtils.java-arc   1.3   Apr 14 2014 13:51:12   rlincoln  $
 *
 * Written by:
 *		$Author:   rlincoln  $		$Revision:   1.3  $	$Date:   Apr 14 2014 13:51:12  $	$State$
 *
 * $Locker$
 * $Source$
 *
*/

// Package declaration

package com.paymentech.eis.tools;

// Standard Java imports

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

import java.io.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.StringTokenizer;

// Regexp import

/**
 * String utility methods.
 */
public class StringUtils {
  /**
   * PHONE_REGEXP is a regular expression representing a US telephone format
   */
  public static final String PHONE_REGEXP =
      "^.?([0-9]{3}).*([0-9]{3}).?([0-9]{4})";
  /**
   * SSN_REGEXP is a regular expression representing a social security number format
   */
  public static final String SSN_REGEXP =
      "^([0-9]{3}).?([0-9]{2}).?([0-9]{4})";
  /**
   * ZIP_REGEXP is a regular expression representing a 5 or 9 digit zip code format
   */
  public static final String ZIP_REGEXP = "^([0-9]{5}).?([0-9]{4})?";

  /**
   * PHONE_MASK is a mask representing a US telephone format
   */
  public static final String PHONE_MASK = "(###)###-####";
  /**
   * SSN_MASK is a mask representing a social security number format
   */
  public static final String SSN_MASK = "###-###-####";
  /**
   * ZIP_MASK is a mask representing a 5 or 9 digit zip code format
   */
  public static final String ZIP_MASK = "#####-####";


  /**
   * Constructor: StringUtils
   * Utility Classes cannot be instantiated.
   */
  private StringUtils() {
  }

  /**
   * getBooleanFromFlag If string equals 'Y', then return true. Otherwise,
   * return false.  This function is convenient for
   * Oracle databases that have no concept of boolean data types.
   *
   * @param flag the value to test for true or false.
   * @return if flag is "Y", return true, else return false.
   */
  public static boolean getBooleanFromFlag(String flag) {
    if (flag == null) {
      return false;
    }
    return (flag.equals("Y"));
  }

  /**
   * If the boolean is true, return "Y", else return "N". This function is
   * convenient for Oracle databases that have no
   * concept of boolean datatypes.
   *
   * @param b the boolean to test
   * @return if boolean is true, then return "Y", else return "N"
   */
  public static String getFlagFromBoolean(boolean b) {
    return (b ? "Y" : "N");
  }

  /**
   * Test to see if string is null or empty string.
   *
   * @param s the string to test
   * @return true if string is not null and is not an empty string.
   */
  public static boolean isEmpty(String s) {
    if ((s != null) && (s.length() > 0)) {
      return false;
    }
    return true;
  }

  /**
   * Removes the first occurance of html in the path
   *
   * @param path String to be parsed
   * @return the remainder of the string without the beginning of the path
   */
  public static String removeNTpath(String path) {
    // find the first occurance of html/ in path that was passed in
    String matchString = "html\\";
    int index = path.indexOf(matchString);
    return (path.substring(index + matchString.length()));
  }

  /**
   * Replaces a string of text with a new string of text.
   *
   * @param line      - the line to perform substitution in
   * @param oldString - the text to be replaced
   * @param newString - the new text to use
   * @return - the line with the substitution completed.
   */
  public static String replace(String line, String oldString,
                               String newString) {
    int index = 0;
    while ((index = line.indexOf(oldString, index)) >= 0) {
      line = line.substring(0, index) + newString +
          line.substring(index + oldString.length());
      index += newString.length();
    }
    return line;
  }

  /**
   * Removes the XML header from the given String
   *
   * @param - line: the String that contains the XML
   * @return - the String without the XML header
   */
  public static String removeXMLHeader(String line) {
    int start = 0, end = 0;
    if ((start = line.indexOf("<?xml")) >= 0) {
      if ((end = line.indexOf("?>")) >= 0) {
        String xmlHeader = line.substring(start, end + 2);
        System.out.println("The xml header is:" + xmlHeader);
        line = replace(line, xmlHeader, "");
      } else {
        System.out.println("Sorry, couldn't find end of tag");
      }
    }
    return line;
  }

  /**
   * Reads the contents of a file and stores
   * it into a String
   *
   * @param path the path to the file to be read
   * @returns the String containing the contents of the file
   */
  public static String readFile(String path) {
    String result = new String();
    String htmlLine = new String();
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader(path));
      while ((htmlLine = in.readLine()) != null)
        result += htmlLine + "\n";
    } catch (FileNotFoundException fnf) {
      System.out.println("In readTemplate, caught a FileNotFoundException  " +
          "trying to open the file: " + path);
    } catch (IOException ioe) {
      System.out.println("In readTemplate, caught an IOException " +
          "trying to open the file: " + path);
    } finally {
      try {
        in.close();
      } catch (Exception e) {
      }
    }
    return (result);
  }

  /**
   * Writes a message to the specified file
   *
   * @param path    the path to the file to be written to
   * @param message text to write to the file
   */
  public static void writeFile(String path, String message) {
    try {
      PrintWriter out = new PrintWriter(new FileWriter(path));
      out.print(message);
      out.close();
    } catch (IOException ioe) {
      Debug.trace_debug("StringUtils::writeFile",
          "Error writting file to path: " + path + ". " +
              ioe.getMessage());
    }
  }

  /**
   * Retrieves all paren values in a regular expression and concatenates
   * them into a single string.
   *
   * @param value the string to parse
   * @param expr  the regular expression
   * @return a string containing the concatenation of all paren-ed values
   * based on the regular expression.
   */
  public static String getParenContents(String value, String expr) {
    StringBuffer sb = new StringBuffer();
    RE re = null;
    if ((isEmpty(value)) || (expr == null)) {
      return value;
    }
    try {
      re = new RE(expr);
    } catch (RESyntaxException e) {
      e.printStackTrace();
      return null;
    }
    if (re.match(value)) {
      for (int i = 1; i < re.getParenCount(); i++) {
        sb.append(re.getParen(i));
      }
    }
    return new String(sb);
  }

  /**
   * Retrieves a paren values in a regualr expression
   *
   * @param value the string to parse
   * @param expr  the regular expression
   * @return a string containing the concatenation of all paren-ed values
   * based on the regular expression.
   */
  public static String getParenContents(String value,
                                        String expr, int index) {
    RE re = null;
    System.out.println("value : " + value + " expr: " + expr +
        " index: " + index);
    // if value is null or expression is empty, just return expression
    if ((isEmpty(value)) || (expr == null)) {
      return value;
    }
    // get regular expression object
    try {
      re = new RE(expr);
    } catch (RESyntaxException e) {
      e.printStackTrace();
      return null;
    }
    // if value doesn't match, return null
    if (!re.match(value)) {
      return null;
    }
    // if index is greater than the number of parens, return null.
    if (index > re.getParenCount()) {
      return null;
    }
    // return paren value
    return re.getParen(index);
  }

  /**
   * Creates a String by applying a mask to some string value. The mask
   * expression is defined as follows: # - replacement character all others
   * - literals <BR> ex: (###)###-#### will convert "1234567890" into
   * "(123)456-7890" <BR> There is no way to escape a # currently.
   *
   * @param value the string to mask
   * @param mask  the mask to apply
   * @return the masked string.
   */
  public static String maskedFormat(String maskvalues[], String mask) {
    StringBuffer sb = new StringBuffer();
    int maskIndex = 0;
    int valueIndex = 0;
    StringBuffer valuebuff = new StringBuffer();
    for (int i = 0; i < maskvalues.length; i++) {
      if (!(maskvalues[i] == null)) {
        valuebuff.append(maskvalues[i]);
      }
    }
    return maskedFormat(valuebuff.toString(), mask);
  }

  /**
   * Creates a String by applying a mask to some string value. The mask
   * expression is defined as follows: # - replacement character all others
   * - literals <BR> ex: (###)###-#### will convert "1234567890" into
   * "(123)456-7890" <BR> There is no way to escape a # currently.
   *
   * @param value the string to mask
   * @param mask  the mask to apply
   * @return the masked string.
   */
  public static String maskedFormat(String value, String mask) {
    StringBuffer sb = new StringBuffer();
    int maskIndex = 0;
    int valueIndex = 0;
    if ((value == null) || (mask == null)) {
      return null;
    }
    while ((maskIndex < mask.length()) && (valueIndex < value.length())) {
      if (mask.charAt(maskIndex) != '#') {
        sb.append(mask.charAt(maskIndex++));
        continue;
      }
      sb.append(value.charAt(valueIndex++));
      maskIndex++;
    }
    return new String(sb);
  }

  /**
   * Formats a number from a string format
   *
   * @param number    Number to format
   * @param decPlaces number of places past the decimal to display
   */
  public static String decimalFormat(String number, int decPlaces) {
    NumberFormat dollarsFormat = NumberFormat.getNumberInstance();
    dollarsFormat.setMinimumFractionDigits(decPlaces);
    dollarsFormat.setMaximumFractionDigits(decPlaces);
    return dollarsFormat.format(Double.parseDouble(number));
  }

  /**
   * Removes the decimal formatting
   *
   * @param number to format
   * @return String value of the unformatted decimal
   */
  public static String decimalUnformat(String number) {
    String value;
    try {
      value = NumberFormat.getNumberInstance().parse(number).toString();
    } catch (java.text.ParseException e) {
      System.out.println("decimalUnformat::Unable to parse amount: " + e);
      value = null;
    }
    return value;
  }

  /**
   * Strips out all characters except digits and decimal points
   *
   * @param stringToConvert
   * @return BigDecimal value of the input string
   */
  public static BigDecimal toBigDecimal(String stringToConvert) {
    // Strip out all characters except Digits and Decimal Points
    String strippedString = new String();
    char[] arrayToConvert = stringToConvert.toCharArray();
    for (int i = 0; i < arrayToConvert.length; i++) {
      if ((Character.isDigit(arrayToConvert[i])) ||
          (arrayToConvert[i] == '.')) {
        strippedString += arrayToConvert[i];
      }
    }
    // Return a new Big Decimal
    return new BigDecimal(strippedString);
  }

  /**
   * Strips out all spaces from the string
   *
   * @param stringToConvert
   * @return string with no spaces
   */
  public static String removeSpaces(String stringToConvert) {
    StringTokenizer st = new StringTokenizer(stringToConvert, " ", false);
    String t = "";
    while (st.hasMoreElements()) t += st.nextElement();
    return t;
  }
};
