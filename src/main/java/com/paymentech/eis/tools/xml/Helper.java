/* Helper.java		Class containing static helper methods for manipulating
 * a DOM document.
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * $Header:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/xml/Helper.java-arc   1.1   Feb 07 2007 08:03:22   byounie  $
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:03:22  $	$State$
 *
 * $Locker$
 * $Source$
 *
 * Revision history:
 *
 * $Log:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/xml/Helper.java-arc  $
// 
//    Rev 1.1   Feb 07 2007 08:03:22   byounie
// Changed copyright
// 
//    Rev 1.0   Sep 05 2006 17:35:40   bkisiel
// Initial revision.
 * 
 * 1     12/09/03 8:24a Sayers
 * 
 * 6     6/11/02 11:02a Sayers
 * javadoc cleanup
 * 
 * 5     12/03/01 1:15p Jpalmiero
 * Added a convenience method to get the value of an element within a
 * tree.
 *
 * 4     12/03/01 7:10a Jpalmiero
 * Added 3 helper methods:  replaceValue, replaceAttribute,
 * getAttributeValue.
 *
 * 3     11/26/01 11:18a Jpalmiero
 * Added a method to find an element recursively and to get the text value
 * of a node.
 *
 * 2     4/02/01 4:28p Jpalmiero
 * Changed package from com.pt to com.paymentech
*/

// Package declaration

package com.paymentech.eis.tools.xml;

// XML imports

import org.w3c.dom.*;

/**
 * Helper	A set of static helper routines for manipulating a DOM document.
 *
 * @author jpalmiero
 * @version $Revision: 1.1  $
 */
public class Helper {
  /**
   * Throws an exception if there is not exactly 'numExpected' elements
   * in the given node list for the specified name. The first Node in the
   * node list named 'name' is returned. If
   * 'numExpected' is 0, then "null" is returned.
   *
   * @params element      The element to check in
   * @params name      The tag-name of the desired Node.
   * @params numExpected    The number of expected elements
   */
  public static Node checkList(Element element, String name, int numExpected)
      throws DOMParserException {
    int numElements = 0;
    NodeList list = element.getChildNodes();
    Node n = null;
    Node retVal = null;

    // Debug

    // Iterate through the children of this node
    for (int i = 0; i < list.getLength(); i++) {
      n = list.item(i);
      if (n.getNodeType() == Node.ELEMENT_NODE &&
          ((Element) n).getTagName().equals(name)) {
        numElements++;
        if (retVal == null)
          retVal = n;
      }
    }

    if (numElements != numExpected)
      throw new DOMParserException("Wrong # of elements");

    return (numExpected > 0) ? retVal : null;
  }

  /**
   * Find element (tag) in the document and return its value.
   * If an attribute is specified, then return its.
   *
   * @param tag The element that we are searching for
   */
  public static String getValue
  (Element document, String tag, String attribute)
      throws TagNotFound, AttributeNotFound {
    String value = null;
    Element element = firstElementRecursive(document, tag);

    if (element == null)
      throw new TagNotFound
          ("Could not find " + tag + " in xml template.");
    else {
      // System.out.println("Element before: " + element);
      if (attribute == null || attribute.length() == 0)
        value = textValue(element);
      else {
        value = getAttributeValue(element, attribute);
      }
    }

    return value;
  }

  /**
   * Returns the text value of the given Node.  This assumes that
   * the Node is of form <Foo>Text Value</Foo>.  If there is no
   * child node for the given node, then null is returned.
   *
   * @params node    The Node whose text value to extract
   */
  public static String textValue(Node node) {
    NodeList list = node.getChildNodes();
    Node n = null;
    String value = null;

    for (int i = 0; i < list.getLength(); i++) {
      n = list.item(i);
      if (n.getNodeType() == Node.TEXT_NODE) {
        value = n.getNodeValue();
        break;
      }
    }
    return value;
  }

  /**
   * Determines if 1 or more nodes with the given tag-name exist as
   * a child of the given Element.
   *
   * @params element    The element to check
   * @params tagName    The tag name of the desired node
   */
  public static boolean nodeExists(Element element, String tagName) {
    return (element.getElementsByTagName(tagName).getLength() > 0);
  }

  /**
   * Helper function which creates an Element with a specified tag
   * name and text value and appends it to the end of the given node.
   *
   * @params doc      The owning document
   * @params node    The node to which to add the new element
   * @params tagName    The tag name of the new element
   * @params value    The text value of the new element
   * @returns The newly created element
   */
  public static Element addElementWithText(Document doc, Node node,
                                           String tagName, String value) {
    Element elem = doc.createElement(tagName);
    elem.appendChild(doc.createTextNode(value));
    node.appendChild(elem);

    return elem;
  }

  /**
   * Returns the first element with the specified tag name.  Null is
   * returned if there is no matching element.
   *
   * @params element      The element to check in
   * @params name      The tag-name of the desired Node.
   */
  public static Element firstElement(Element element, String name) {
    boolean foundElement = false;
    NodeList list = element.getChildNodes();
    Node n = null;
    Node retVal = null;
    Element theElement = null;

    // Iterate through the children of this node until we find our match
    for (int i = 0; i < list.getLength() && !foundElement; i++) {
      n = list.item(i);
      if (n.getNodeType() == Node.ELEMENT_NODE &&
          ((Element) n).getTagName().equals(name)) {
        foundElement = true;
        theElement = (Element) n;
      }
    }

    return theElement;
  }

  /**
   * Returns the first element with the specified tag name. Perform the
   * search recursively.
   *
   * @params element      The element to check in
   * @params name      The tag-name of the desired Node.
   */
  public static Element firstElementRecursive(Node element, String name) {
    boolean foundElement = false;
    NodeList list = element.getChildNodes();
    Node n = null;
    Node retVal = null;
    Element theElement = null;

    // Iterate through the children of this node until we find our match
    for (int i = 0; i < list.getLength() && !foundElement; i++) {
      n = list.item(i);
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        String tagName = ((Element) n).getTagName();
        // System.out.println("Evalating tag: " + tagName);
        if (tagName.compareToIgnoreCase(name) == 0) {
          theElement = (Element) n;
          break;
        }
      }

      // OK.  It wasn't him.  Let's check his children.
      if ((theElement = firstElementRecursive(n, name)) != null)
        break; // Found it!
    }
    return theElement;
  }

  /**
   * Helper function to replace the text value of an element.
   */
  public static void replaceTextValue(Element element, String value) {
    NodeList list = element.getChildNodes();
    Node n = null;
    for (int i = 0; i < list.getLength(); i++) {
      n = list.item(i);
      if (n.getNodeType() == Node.TEXT_NODE) {
        n.setNodeValue(value);
        break;
      }
    }

  }

  /**
   * Helper function which creates an Element with a specified tag
   * and appends it to the end of the given node.
   *
   * @params doc      The owning document
   * @params node    The node to which to add the new element
   * @params tagName    The tag name of the new element
   * @returns The newly created element
   */
  public static Element addElement(Document doc, Node node, String tagName) {
    Element elem = doc.createElement(tagName);
    node.appendChild(elem);

    return elem;
  }

  /**
   * Find element (tag) in the document and replace its value with
   * the value specified. If an attribute is specified, then its
   * value is replaced.
   *
   * @param tag The element that we are searching for
   */
  static public void replaceValue(Element document, String tag,
                                  String attribute, String value) throws TagNotFound, AttributeNotFound {
    Element element = firstElementRecursive(document, tag);

    if (element == null)
      throw new TagNotFound
          ("Substitution failed because could not find " + tag +
              " in xml template.");
    else {
      // System.out.println("Element before: " + element);
      if (attribute == null || attribute.length() == 0)
        replaceTextValue(element, value);
      else {
        replaceAttribute(element, attribute, value);
      }
      // System.out.println("Element After: " + element);
    }
  }

  /**
   * Helper method to replace the value of an attribute in an element.
   */
  static private void replaceAttribute(Element element, String attribute,
                                       String value) throws AttributeNotFound {
    NamedNodeMap nodeMap = element.getAttributes();

    Attr attr = (Attr) nodeMap.getNamedItem(attribute);

    if (attr == null)
      throw new AttributeNotFound
          ("Could not find attribute " + attribute +
              " in element " + element);
    else
      attr.setValue(value);
  }

  /**
   * Get the value of an attribute for the element passed in.
   */
  public static String getAttributeValue(Element element, String attribute)
      throws AttributeNotFound {
    String attributeValue = null;
    NamedNodeMap nodeMap = element.getAttributes();

    Attr attr = (Attr) nodeMap.getNamedItem(attribute);

    if (attr == null)
      throw new AttributeNotFound
          ("Could not find attribute " + attribute +
              " in element " + element);
    else
      attributeValue = attr.getNodeValue();

    return attributeValue;
  }
};