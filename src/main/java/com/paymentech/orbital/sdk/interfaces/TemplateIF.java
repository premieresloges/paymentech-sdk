package com.paymentech.orbital.sdk.interfaces;

import com.paymentech.orbital.sdk.request.FieldNotFoundException;
import com.paymentech.orbital.sdk.request.RequestConstructionException;

import java.util.List;
import java.util.Map;

/**
 * <p><b>Title:</b> TemplateIF</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Published interface for the Template class. </p>
 */
public interface TemplateIF {

  /**
   * Gets child Complex Root Template instance from "name" parameter
   *
   * @param name
   * @return TemaplateIF
   */
  TemplateIF getComplexRoot(String name) throws RequestConstructionException;

  /**
   * Gets child Recursive Element Template instance from "name" parameter
   *
   * @param name
   * @return TemaplateIF
   */
  TemplateIF getRecursiveElement(String name) throws RequestConstructionException;

  /**
   * Gets child Complex Root Template instance from "name" parameter
   *
   * @param name
   * @param recursiveElement
   * @return TemaplateIF
   */
  TemplateIF getComplexRoot(String name, boolean recursiveElement) throws RequestConstructionException;

  /**
   * Sets data in XML request transaction
   *
   * @param name
   * @param value
   * @return
   * @deprecated Added to capture old XML elements that relate to Complex Roots in new template versions
   */
  void setField(String name, String value) throws FieldNotFoundException;

  /**
   * Sets data in XML request transaction
   *
   * @param name
   * @param value
   * @return
   */
  void setFieldValue(String name, String value) throws FieldNotFoundException;

  /**
   * Gets data from XML request transaction
   *
   * @param name
   * @return String
   */
  String getField(String name);

  /**
   * Clear data from XML request transaction (sets to null)
   *
   * @param name
   * @return
   */
  void removeField(String name);

  /**
   * Clear all data from XML request transaction (sets to null)
   *
   * @return
   */
  void clearFields();

  /**
   * Get name of this Template
   *
   * @return String
   */
  String getName();

  /**
   * Get the fully constructed XML (with user-defined data)
   *
   * @return String
   */
  String getXml();

  /**
   * Get Map of the current Recursive Elements contained in this Template
   *
   * @return Map
   */
  Map getRecursiveElements();

  /**
   * Get List of the Complex Roots contained in this Template that have been implemented
   *
   * @return Map
   */
  List getUsedComplexRoots();

  /**
   * Implemented to provide deep copy needs of certain Templates
   *
   * @return Object
   */
  Object clone();

  /**
   * Set boolean flag that tells Template to only display WARN message instead of throwing FieldNotFoundExceptions
   *
   * @param bool
   * @return
   */
  void setSkipFieldNotFoundException(boolean bool);

}
