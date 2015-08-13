package com.paymentech.orbital.sdk.configurator;

import com.paymentech.orbital.sdk.logger.LoggerIF;
import com.paymentech.orbital.sdk.util.filehandling.TemplateLoaderIF;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * <p><b>Title:</b> ConfiguratorIF.java</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * Paymentech. The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Published interface for the configurator class </p>
 */
public interface ConfiguratorIF {
  // template loaders
  String TEMPLATE_LOADER = "templateLoader";

  /**
   * Get the configurations map (linehandker.properties)
   *
   * @return
   */
  Map getConfigurations();

  /**
   * Get the xmlTemplates map
   *
   * @return
   */
  Map getXmlTemplates();

  /**
   * Get the EngineLogger which logs to engine.log
   *
   * @return
   * @deprecated
   */
  LoggerIF getEngineLogger();

  /**
   * Get the eCommerceLogger which logs to eCommerce.log
   *
   * @return
   * @deprecated
   */
  LoggerIF getEcommerceLogger();

  /**
   * Get the Apache-Commons Logger which logs to engine.log
   *
   * @return
   */
  Logger getCommonEngineLogger();

  /**
   * Get the Apache-Commons Logger which logs to eCommerce.log
   *
   * @return
   */
  Logger getCommonEcommerceLogger();

  /**
   * Get the TemplateLoaderIF interface used to load up XML template files
   *
   * @return
   */
  TemplateLoaderIF getTemplateLoader();

}
