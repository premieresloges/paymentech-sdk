package com.paymentech.orbital.sdk.request;

import com.paymentech.eis.tools.StringUtils;
import com.paymentech.orbital.sdk.configurator.ConfiguratorIF;
import com.paymentech.orbital.sdk.engine.http.HttpEngineConstants;
import com.paymentech.orbital.sdk.interfaces.RequestIF;
import com.paymentech.orbital.sdk.pre40.TransactionMapper;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;
import org.apache.log4j.Logger;
import org.apache.regexp.RE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p><b>Title:</b> Template</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Class responsible for loading and parsing the XML template files. </p>
 */
public class TemplateParser {

  // keys used for looking up the complex root properties
  // if a transaction needs to have a nested node structure, then you
  // define a complex root include file that has the content of the
  // node (include files can be recursive)
  // the properties key to defined the complex root content is
  //		XMLTemplates.Request.ComplexRoot.
  // all of the complex root properties follow the format:
  //		XMLTemplates.Request.ComplexRoot. + <name of element> + [optional]
  // the optional part of the key can be one of the following:
  //		RecursiveElement + index - this supplies the name of the recursive include name in the template
  //										this should relate to another complex root definition
  //										index is base-1
  //		CountElement - this supplies the element that will be populated with the
  //								  total number of recursive elements
  //		EnforceGreaterThanZero - throw error if 0 recursive elements
  //		ChildIndexElement - element in recursive xml that will contain a internally
  //										 populated index
  //		MaxCount - max number of recursive elements allowed
  private static final String COMPLEX_ROOT_PARENT_INC = "XMLTemplates.Request.ComplexRoot.";
  private static final String RECURSIVE_ELEMENT = ".RecursiveElement";
  private static final String RECURSIVE_COUNT_ELEMENT = ".CountElement";
  private static final String ENFORCE_GREATER_THEN_ZERO = ".EnforceGreaterThanZero";
  private static final String CHILD_INDEX_ELEMENT = ".ChildIndexElement";
  private static final String MAX_COUNT = ".MaxCount";
  // instance to this singleton
  private static TemplateParser singleton = null;
  // keep a reference to the Configurations (linehandler.properties)
  private static ConfiguratorIF localConfig = null;
  // map of include template's raw xml content
  private static Map rawIncludeXMLContent = new HashMap();
  // since this parser can handle both pre-PTI40 and PTI40 templates,
  // we need to capture the version of the schema to perform
  // special logic on pre-PTI40 transactions
  private static String ptiVersion = null;
  // exact version as number
  private static int versionNumber = 0;
  // regx to parse out the field names and the include files (using
  private static String FIELDS_REGX = "\\[%\\s*([^\\s].*?[^\\s])\\s*%\\]";
  private static String COMPLEX_TYPES_REGX = "\\[#\\s*([^\\s].*?[^\\s])\\s*#\\]";
  private static String DEFAULT_VALUES_REGX = "=";
  // regex objects
  private static RE fieldsRegx = null;
  private static RE complexTypesRegx = null;
  private static RE defaultValuesRegx = null;
  // List of PTI40 templates
  // this will be used to determine if a template needs to be converted from
  // a pre-PTI40 version
  private static List pti40List = new ArrayList();

  // engineLogger
  private static Logger log;

  protected TemplateParser() {
    ;
  }

  public synchronized static TemplateParser getInstance(ConfiguratorIF config) throws InitializationException {

    try {
      if (singleton == null) {

        localConfig = config;

        // get the PTI schema version from the linehandler.properties file
        ptiVersion = (String) config.getConfigurations().get(HttpEngineConstants.DTD_VERSION_KEY);

        setPTIVersion(ptiVersion);

        // get a reference to the engine logger
        log = config.getCommonEngineLogger();

        // create the regex objects now since there can be
        // some overhead to creating these obects
        fieldsRegx = new RE(FIELDS_REGX);
        complexTypesRegx = new RE(COMPLEX_TYPES_REGX);
        defaultValuesRegx = new RE(DEFAULT_VALUES_REGX);

        // fill the PTI40
        pti40List.add(RequestIF.NEW_ORDER_TRANSACTION);
        pti40List.add(RequestIF.END_OF_DAY_TRANSACTION);
        pti40List.add(RequestIF.FLEX_CACHE_TRANSACTION);
        pti40List.add(RequestIF.REVERSE_TRANSACTION);
        pti40List.add(RequestIF.PROFILE_TRANSACTION);
        pti40List.add(RequestIF.MARK_FOR_CAPTURE_TRANSACTION);
        pti40List.add(RequestIF.INQUIRY);
        pti40List.add(RequestIF.ACCOUNT_UPDATER);
        pti40List.add(RequestIF.SAFETECH_FRAUD_ANALYSIS);

        singleton = new TemplateParser();
      }
    } catch (Throwable th) {
      log.error(th.getMessage(), th);
      throw new InitializationException(th.getMessage());
    }

    return singleton;
  }

  // used for testing only to manually set the PTIVersion
  protected static void setPTIVersion(String ptiVer) throws InitializationException {
    ptiVersion = ptiVer;

    // try to get the exact version number of the schema being used
    ptiVersion = ptiVersion.toUpperCase();

    try {
      versionNumber = Integer.parseInt(ptiVersion.substring(3));
    } catch (Throwable th) {
      throw new InitializationException("Invalid DTDVersion version set in linehandler.properties (format: PTIxx): " + ptiVersion);
    }
  }

  public synchronized Template loadTemplate(String templateName) throws XMLTemplateNotFoundException,
      InitializationException {
    return loadTemplate(templateName, !(pti40List.contains(templateName)));
  }

  public synchronized Template loadTemplate(String templateName, boolean checkForPriorVersion) throws XMLTemplateNotFoundException,
      InitializationException {
    Template topLevelTemplate = null;

    try {

      if (checkForPriorVersion) {
        topLevelTemplate = TransactionMapper.getMappedTemplate(this, templateName);
        return topLevelTemplate;
      } else {
        // create a new Template class
        topLevelTemplate = new Template(templateName);
      }

      // get the template from the rawXMLTemplate map
      String rawXML = (String) localConfig.getXmlTemplates().get(templateName);

      if (rawXML == null || rawXML.length() == 0) {
        log.error("XML Template could not be found: [" + templateName + "]");
        throw new XMLTemplateNotFoundException
            ("XML Template could not be found: [" + templateName + "]");
      }

      topLevelTemplate.setXml(rawXML);

      // first, parse out the fields
      List fieldsList = parseOnExpression(fieldsRegx, rawXML);

      // seperate the fields into complex and optional fields
      parseFields(topLevelTemplate, fieldsList);

      // set the complex roots if any
      setComplexRoots(topLevelTemplate);

    } catch (XMLTemplateNotFoundException xmle) {
      throw xmle;
    } catch (Throwable th) {
      log.error(th.getMessage(), th);
      throw new InitializationException(th.getMessage());
    }

    return topLevelTemplate;
  }

  private void setComplexRoots(Template parent) throws InitializationException {

    // see if this template has any complex roots
    List complexTypesList = parseOnExpression(complexTypesRegx, parent.getXml());

    if (complexTypesList.size() > 0) {

      for (int i = 0; i < complexTypesList.size(); i++) {

        // get the reference name of the complex root
        String childTemplateName = (String) complexTypesList.get(i);

        // each complex root will be a new template
        Template childTemplate = new Template(childTemplateName, parent);

        // each of the complex roots will have a .inc with the XML content
        String filePath = (String) localConfig.getConfigurations().get
            (COMPLEX_ROOT_PARENT_INC + childTemplateName);

        // try to load the file up
        if (rawIncludeXMLContent.containsKey(childTemplateName)) {
          childTemplate.setXml((String) rawIncludeXMLContent.get(childTemplateName));
        } else {
          childTemplate.setXml(localConfig.getTemplateLoader().loadTemplate(filePath));
          rawIncludeXMLContent.put(childTemplateName, childTemplate.getXml());
        }

        // call this function again so we can continue down the rabbit hole
        setComplexRoots(childTemplate);

        // now that the children to this child template have been set, we
        // can continue setting the children for the parameter template
        // passed in (confused yet?)

        // now that we have the content, we can strip out the fields
        List fieldsList = parseOnExpression(fieldsRegx, childTemplate.getXml());
        parseFields(childTemplate, fieldsList);

        // set up the recursive elements (if any)
        setRecursiveElements(childTemplate);

        // now there is a chance that this is a child element that we are reading in,
        // so we need to check for a property that only really applies to child line item elements
        String childIndexElement = (String) localConfig.getConfigurations().get
            (COMPLEX_ROOT_PARENT_INC + childTemplateName + CHILD_INDEX_ELEMENT);

        if (!StringUtils.isEmpty(childIndexElement)) {
          childTemplate.setChildIndexElement(childIndexElement);
        }

        // now that everything has been set, we can save this as a complex type
        parent.getComplexRoots().put(childTemplateName, childTemplate);

      }

    }

  }

  private void setRecursiveElements(Template template) {
    int index = 1;

    // look up the additional optional complex root properties
    // first look for recursive elements (there can be more then one)
    String recursiveElement = (String) localConfig.getConfigurations().get
        (COMPLEX_ROOT_PARENT_INC + template.getName() + RECURSIVE_ELEMENT
            + Integer.toString(index));

    while (recursiveElement != null && recursiveElement.length() > 0) {

      String countElement = (String) localConfig.getConfigurations().get
          (COMPLEX_ROOT_PARENT_INC + template.getName() + RECURSIVE_ELEMENT
              + Integer.toString(index) + RECURSIVE_COUNT_ELEMENT);

      String enforceGreaterThanZero = (String) localConfig.getConfigurations().get
          (COMPLEX_ROOT_PARENT_INC + template.getName() + RECURSIVE_ELEMENT
              + Integer.toString(index) + ENFORCE_GREATER_THEN_ZERO);

      String maxCount = (String) localConfig.getConfigurations().get
          (COMPLEX_ROOT_PARENT_INC + template.getName() + RECURSIVE_ELEMENT
              + Integer.toString(index) + MAX_COUNT);

      RecursiveElement recursiveElementType = new RecursiveElement(recursiveElement, countElement,
          enforceGreaterThanZero, maxCount);

      template.getRecursiveElements().put(recursiveElement, recursiveElementType);

      // see if we have anymore
      index++;
      recursiveElement = (String) localConfig.getConfigurations().get
          (COMPLEX_ROOT_PARENT_INC + template.getName() + RECURSIVE_ELEMENT + Integer.toString(index));

    }
  }

  private void parseFields(Template template, List fieldsList) {
    String fieldContent = null;
    String[] optionalSplit = null;

    // now lets just spin through the fields and split them out
    // in optional and required
    for (int i = 0; i < fieldsList.size(); i++) {

      fieldContent = (String) fieldsList.get(i);

      if (fieldContent.indexOf("=") == -1) {
        // this is a required field
        template.getRequiredFields().put(fieldContent, new Field(fieldContent));
        continue;
      }

      if (fieldContent.indexOf("=") > -1) {
        // this is an optional field
        // which may have a default value attached to it
        optionalSplit = defaultValuesRegx.split(fieldContent);

        if (optionalSplit.length > 1) {
          // we have a default value
          template.getOptionalFields().put(optionalSplit[0].trim(),
              new Field(optionalSplit[0].trim(), optionalSplit[1].trim()));
        } else {
          // this is just an optional tag
          template.getOptionalFields().put(optionalSplit[0].trim(),
              new Field(optionalSplit[0].trim()));
        }
      }
      // Check the username & password is in the template
      String userName = (String) localConfig.getConfigurations().get("OrbitalConnectionUsername");
      String password = (String) localConfig.getConfigurations().get("OrbitalConnectionPassword");
      if (template.getOptionalFields().containsKey("OrbitalConnectionUsername") && userName != null) {
        template.getOptionalFields().put("OrbitalConnectionUsername",
            new Field("OrbitalConnectionUsername", userName));
      }
      if (template.getRequiredFields().containsKey("OrbitalConnectionUsername") && userName != null) {
        template.getOptionalFields().put("OrbitalConnectionUsername",
            new Field("OrbitalConnectionUsername", userName));
      }
      if (template.getOptionalFields().containsKey("OrbitalConnectionPassword") && password != null) {
        template.getOptionalFields().put("OrbitalConnectionPassword",
            new Field("OrbitalConnectionPassword", password));
      }
      if (template.getRequiredFields().containsKey("OrbitalConnectionPassword") && password != null) {
        template.getOptionalFields().put("OrbitalConnectionPassword",
            new Field("OrbitalConnectionPassword", password));
      }
    }
  }

  private List parseOnExpression(RE r, String rawXML) {
    List list = new ArrayList();
    int position = 0;

    while (r.match(rawXML, position)) {
      list.add(r.getParen(1));
      position = r.getParenEnd(1);
    }

    return list;
  }

}
