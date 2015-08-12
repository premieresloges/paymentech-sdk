package com.paymentech.orbital.sdk.request;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

/**
 * <p><b>Title:</b> RequestBuilder</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Takes a fully populated Template and creates the constructed XML transaction </p>
 */
public class RequestBuilder {
	
	private RequestBuilder () { ; }
	
	public static String buildRequest (Logger log, Template template) throws RequestConstructionException {
		String response = null;
		
		try {
			
			// start by making sure that all the required fields for the top most template are populated
			validateRequiredFields (template.getRequiredFields());
		
			// now we can start filling in the complex roots
			response = setComplexRoots(template);
			
			// replace the field values (required and optional)
			response = replaceFields(response, template.getRequiredFields());
			response = replaceFields(response, template.getOptionalFields());			
			
			// remove all the unused complex root elements that we find
			response = blankOutInclude(response, ".*?");
		
		} catch (RequestConstructionException rce) {
			throw rce;
		} catch (Throwable th) {
			log.error(th.getMessage(), th);
			throw new RequestConstructionException (th.getMessage());			
		}
		
		return response;
	}
	
	private static String setComplexRoots (Template template) throws RESyntaxException, 
		RequestConstructionException, FieldNotFoundException {
		String filledComplexRootsXML = template.getXml();
		Template crTemplate = null;
		
		if (template.getUsedComplexRoots().size() > 0) {
			
			for (int i = 0; i < template.getUsedComplexRoots().size(); i++) {
				if (template.getRecursiveElements().containsKey(template.getUsedComplexRoots().get(i))) {
					template.getUsedComplexRoots().remove(i);
				}
			}			
			
			for (int i = 0; i < template.getUsedComplexRoots().size(); i++) {
				
				crTemplate = (Template) template.getComplexRoots().get(template.getUsedComplexRoots().get(i));
				
				// call this method again to set the childern complex roots (if any)
				String childComplexRootsXML = setComplexRoots(crTemplate);
				
				// try to fill the recursive elements if any
				childComplexRootsXML = setRecursiveElements(crTemplate);
			
				// now we fill the complex root template
				// first, make sure all the required fields are there
				validateRequiredFields (crTemplate.getRequiredFields());
									
				// replace the required and optional fields for this complex root
				childComplexRootsXML = replaceFields(childComplexRootsXML, crTemplate.getRequiredFields());
				childComplexRootsXML = replaceFields(childComplexRootsXML, crTemplate.getOptionalFields());
				
				// add the complex root to the main template
				filledComplexRootsXML = replaceInclude (filledComplexRootsXML, crTemplate.getName(), childComplexRootsXML);
				
			}			
			
		}
	
		return filledComplexRootsXML;
	}
	
	private static String  setRecursiveElements (Template template) throws RESyntaxException, 
		RequestConstructionException, FieldNotFoundException {
		String parentXML = template.getXml();
		StringBuffer reBuffer = null;
		String recursiveElementXML = null;
		RecursiveElement recusiveElement = null;
		
		// begin with the recursive elements
		if (template.getRecursiveElements().size() > 0) {
			
			Iterator recursiveItr = template.getRecursiveElements().entrySet().iterator();
			
			while (recursiveItr.hasNext()) {
				
				recusiveElement = (RecursiveElement) ((Entry) recursiveItr.next()).getValue();
				
				List recursiveElementData 
				= (List)template.getUserDefinedRecursiveElementsMap().get(recusiveElement.getName());

				// we know that we are going to add the core content for this recursive
				if (recusiveElement.isEnforceGreaterThanZero()) {
					// make sure that we have more then one user added recursive templates
					if (recursiveElementData == null || recursiveElementData.size() == 0) {
						throw new RequestConstructionException ("Complex root [" + template.getName() +
								" must have 1 or more recursive elements defined");
					}
				}
					
				// make sure the user didn't add to many recursive elements
				if (recursiveElementData.size() > recusiveElement.getMaxCount()) {
					throw new RequestConstructionException ("Complex root [" + template.getName() +
							" can have a maximum of [" + recusiveElement.getMaxCount() + "]" +
							" recursive elements. [" + recusiveElement.getName() + "] have been defined.");
				}
				
				// check if the core element has an element that will contain the count of the recursive elements
				if (recusiveElement.getCountElement() != null 
						&& recusiveElement.getCountElement().trim().length() > 0) {
					template.setField(recusiveElement.getCountElement(), Integer.toString(recursiveElementData.size()));
				}
				
				// this template object has its own data to fill it in,
				// so lets create a new StringBuffer and get to fill'in
				reBuffer = new StringBuffer ();
				
				// start filling in the data for each individual 
				for (int ii = 0; ii < recursiveElementData.size(); ii++) {
					// each item in this list is a template
					Template recursiveTemplate = (Template) recursiveElementData.get(ii);
					
					// set the index element (if there is one)
					if (recursiveTemplate.getChildIndexElement() != null 
							&& recursiveTemplate.getChildIndexElement().trim().length() > 0) {
						recursiveTemplate.setField(recursiveTemplate.getChildIndexElement(), Integer.toString(ii + 1));
					}
					
					// make sure all the required fields are present
					validateRequiredFields (recursiveTemplate.getRequiredFields());
					
					// replace the required and optional fields
					recursiveElementXML = replaceFields(recursiveTemplate.getXml(), recursiveTemplate.getRequiredFields());
					recursiveElementXML = replaceFields(recursiveElementXML, recursiveTemplate.getOptionalFields());
					
					// add to the result string
					reBuffer.append(recursiveElementXML);
				}
			
				// this set of recursive elements is complete so we can add it to it's associated complex root
				if (reBuffer != null) {
					parentXML = replaceInclude(parentXML, recusiveElement.getName(), reBuffer.toString());
				} else {
					parentXML = blankOutInclude(parentXML, recusiveElement.getName());
				}
			
			}
			
		}			
		
		return parentXML;
	}
	
	private static String blankOutInclude (String xml, String includeName) throws RESyntaxException {
		return replaceInclude (xml, includeName, "");
	}
	
	private static String replaceInclude (String xml, String includeName, String replaceWith) throws RESyntaxException {
		RE re = new RE ("\\[#\\s*(" + includeName + ")\\s*#\\]");
		if (re.match(xml)) {
			xml = re.subst(xml, replaceWith);
		}
		return xml;
	}
	
	private static String replaceFields (String xml, Map fields) throws RESyntaxException {
		Field field = null;
		String value = null;
		String returnXml = xml;
		
		if ((xml != null) && (fields != null)) {
			Iterator itr = fields.entrySet().iterator();
			while (itr.hasNext()) {
				field = (Field) ((Entry)itr.next()).getValue();
				
				if (field.getValue() == null || field.getValue().trim().length() == 0) {
					value = field.getDefaultValue();
				} else {
					value = field.getValue();
				}
				
				if (value == null) {
					value = "";
				}
				
				RE re = new RE ("\\[%\\s*(" + field.getName() + "=.*? | " + field.getName() + ")\\s*%\\]");
				if (re.match(returnXml)) {
					returnXml = re.subst(returnXml, value);
				}
			}
			
		}
		return returnXml;
	}	
	
	private static void validateRequiredFields (Map requiredFieldsMap) throws FieldNotFoundException {
		if (requiredFieldsMap != null && requiredFieldsMap.size() > 0) {
			Iterator requiredItr = requiredFieldsMap.entrySet().iterator();
			while (requiredItr.hasNext()) {
				Field field = (Field)((Entry)requiredItr.next()).getValue();
				if (field.getValue() == null || field.getValue().trim().length() == 0) {
					throw new FieldNotFoundException ("Required field has not been set: [" + field.getName() + "]");
				}
			}
			
		}
	}

}
