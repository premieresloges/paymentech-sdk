/* NodeWriterImpl.java		Implements the com.paymentech.eis.tools.xml.INodeWriter	
 * interface.  The implementation is based on Apaches DOMWriter class which is
 * distributed as an example with Xerces 1.0.  Currently the only supported 
 * encoding is UTF-8.  
 *
 * (C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 *
 * 
 *
 * $Header:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/xml/NodeWriterImpl.java-arc   1.1   Feb 07 2007 08:03:34   byounie  $
 *
 * Written by:
 *		$Author:   byounie  $		$Revision:   1.1  $	$Date:   Feb 07 2007 08:03:34  $	$State$
 *
 * $Locker$
 * $Source$
 *
 * Revision history:
 *
 * $Log:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/eis/tools/xml/NodeWriterImpl.java-arc  $
// 
//    Rev 1.1   Feb 07 2007 08:03:34   byounie
// Changed copyright
// 
//    Rev 1.0   Sep 05 2006 17:35:42   bkisiel
// Initial revision.
 * 
 * 1     12/09/03 8:24a Sayers
 * 
 * 2     4/02/01 4:28p Jpalmiero
 * Changed package from com.pt to com.paymentech
*/

// Package declaration
package com.paymentech.eis.tools.xml;

// Standard Java imports

import org.w3c.dom.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

// XML imports

/**
 * NodeWriterImpl -  Implementation class for the INodeWriter interface.  This
 * class was adopted directly from Apache's DOMWriter class.
 *
 * @author jpalmiero
 * @version $Revision: 1.1  $
 */
public class NodeWriterImpl extends INodeWriter {
  /**
   * Default constructor.
   */
  NodeWriterImpl() {
  }

  /**
   * printNode		Prints the node to the given OutputStream.
   *
   * @params theNode    The node to print
   * @params os      The output stream.
   */
  public void printNode(Node theNode, OutputStream os)
      throws IOException, DOMException {
    StringBuffer buf = nodeToString(theNode);

    for (int i = 0; i < buf.length(); i++) {
      os.write((char) buf.charAt(i));
    }

    os.flush();
  }

  public void printNode(Node theNode, Writer writer)
      throws IOException, DOMException {
    StringBuffer buf = nodeToString(theNode);

    for (int i = 0; i < buf.length(); i++) {
      writer.write((char) buf.charAt(i));
    }

    writer.flush();
  }

  /**
   * nodeToString		Returns a string buffer containing the printed node
   * with UTF8 encoding.
   */
  public StringBuffer nodeToString(Node theNode) {
    StringBuffer buf = new StringBuffer();

    // is there anything to do?
    if (theNode != null)
      appendNodeText(theNode, buf);

    return buf;
  }

  /**
   * appendNodeText	Appends to the StringBuffer, 'buf', the text
   * associated with 'node'.  This method will recursively call itself
   * for nodes having children.  This method was adapted directly from
   * Apaches DOMWriter class.
   *
   * @params node    The node to print.
   * @params buf      The buffer to which to append the text.
   */
  private void appendNodeText(Node node, StringBuffer buf) {
    int type = node.getNodeType();
    switch (type) {

      // print document
      case Node.DOCUMENT_NODE: {
        String Encoding = "UTF-8";

        buf.append("<?xml version=\"1.0\" encoding=\"" + Encoding +
            "\"?>");

        appendNodeText(((Document) node).getDocumentElement(), buf);
        break;
      }

      // print element with attributes
      case Node.ELEMENT_NODE: {
        buf.append('<');
        buf.append(node.getNodeName());
        Attr attrs[] = sortAttributes(node.getAttributes());

        for (int i = 0; i < attrs.length; i++) {
          Attr attr = attrs[i];
          buf.append(' ');
          buf.append(attr.getNodeName());
          buf.append("=\"");
          buf.append(normalize(attr.getNodeValue(), false));
          buf.append('"');
        }

        buf.append(">");

        NodeList children = node.getChildNodes();
        if (children != null) {
          int len = children.getLength();
          for (int i = 0; i < len; i++)
            appendNodeText(children.item(i), buf);
        }
        break;
      }

      // handle entity reference nodes
      case Node.ENTITY_REFERENCE_NODE: {
        NodeList children = node.getChildNodes();
        if (children != null) {
          int len = children.getLength();
          for (int i = 0; i < len; i++) {
            appendNodeText(children.item(i), buf);
          }
        }
        break;
      }

      // print cdata sections
      case Node.CDATA_SECTION_NODE: {
        buf.append(normalize(node.getNodeValue(), false));
        break;
      }

      // print text
      case Node.TEXT_NODE: {
        buf.append(normalize(node.getNodeValue(), false));
        break;
      }

      // print processing instruction
      case Node.PROCESSING_INSTRUCTION_NODE: {
        buf.append("<?");
        buf.append(node.getNodeName());

        String data = node.getNodeValue();
        if (data != null && data.length() > 0) {
          buf.append(' ');
          buf.append(data);
        }
        buf.append("?>");
        break;
      }

    } // end switch

    if (type == Node.ELEMENT_NODE) {
      buf.append("</");
      buf.append(node.getNodeName());
      buf.append(">");
    }

  } // end appendNodeText()

  /**
   * sortAttributes  Returns a sorted list of Attr's from the given
   * NamedNodeMap instance. This function was taken directly from
   * Apache's DOMWriter class.
   */
  protected Attr[] sortAttributes(NamedNodeMap attrs) {

    int len = (attrs != null) ? attrs.getLength() : 0;
    Attr array[] = new Attr[len];
    for (int i = 0; i < len; i++) {
      array[i] = (Attr) attrs.item(i);
    }
    for (int i = 0; i < len - 1; i++) {
      String name = array[i].getNodeName();
      int index = i;
      for (int j = i + 1; j < len; j++) {
        String curName = array[j].getNodeName();
        if (curName.compareTo(name) < 0) {
          name = curName;
          index = j;
        }
      }
      if (index != i) {
        Attr temp = array[i];
        array[i] = array[index];
        array[index] = temp;
      }
    }

    return (array);

  } // sortAttributes(NamedNodeMap):Attr[]

  /**
   * Normalizes a DOM text string by replacing special characters
   * with their normalized UTF-8 encoding equivalent.  This method
   * was taken straight from Apache's DOMWriter example class.
   *
   * @params s    The text to normalize
   * @returns The normalized text
   */
  protected String normalize(String s, boolean canonical) {
    StringBuffer str = new StringBuffer();

    int len = (s != null) ? s.length() : 0;

    for (int i = 0; i < len; i++) {
      char ch = s.charAt(i);

      switch (ch) {
        case '<': {
          str.append("&lt;");
          break;
        }

        case '>': {
          str.append("&gt;");
          break;
        }

        case '&': {
          str.append("&amp;");
          break;
        }

        case '"': {
          str.append("&quot;");
          break;
        }

        case '\r':
        case '\n': {
          if (canonical) {
            str.append("&#");
            str.append(Integer.toString(ch));
            str.append(';');
            break;
          }
          // else, default append char
        }
        default: {
          str.append(ch);
        }
      }
    }

    return (str.toString());

  } // normalize(String):String


};

