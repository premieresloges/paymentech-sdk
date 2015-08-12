package com.paymentech.orbital.sdk.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;

import com.paymentech.orbital.sdk.configurator.Configurator;
import com.paymentech.orbital.sdk.engine.http.HttpEngineConstants;
import com.paymentech.orbital.sdk.interfaces.TemplateIF;

/**
 * <p><b>Title:</b> Template</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Concrete implementation of the TemplateIF interface. Java container for XML template file. </p>
 */
public class Template implements TemplateIF {
	
	private Template parent = null;
	
	public Template () { ; }
	
	public Template (String name) {
		this.name = name;
	}		
	
	public Template (String name, Template parent) {
		this.name = name;
		this.parent = parent;
	}	
	
	public TemplateIF getComplexRoot (String name) throws RequestConstructionException {
		return getComplexRoot (name, false);
	}
	
	public TemplateIF getComplexRoot (String name, boolean recursiveElement) throws RequestConstructionException {
		// this method can only be called from top level elements
		// this ensures that all complex root objects will 
		// be controlled here and can be contained in a single map
		if (complexRoots.containsKey(name)) {
			if (!recursiveElement) {
				if (usedComplexRoots.contains(name)) {
					throw new RequestConstructionException ("Muliple complex roots cannot be added: [" + name + "]");
				} else {
					usedComplexRoots.add(name);
				}
			}
			TemplateIF tp = (TemplateIF) complexRoots.get(name);
			return tp;
		} else {
			throw new RequestConstructionException ("Failed to find the complex root: [" + name + "]");
		}
	}

	public TemplateIF getRecursiveElement (String name) throws RequestConstructionException {
		List recursiveElementList = null;
		
		if (recursiveElements.containsKey(name)) {
			// a recursive element is also a complex root
			TemplateIF recursiveElement = (TemplateIF) getComplexRoot (name, true).clone(); 
			
			if (userDefinedRecursiveElementsMap.containsKey(name)) {
				recursiveElementList = (List) userDefinedRecursiveElementsMap.get(name);
			} else {
				recursiveElementList = new ArrayList ();
			}
			
			// add the new element and restore
			recursiveElementList.add(recursiveElement);
			
			userDefinedRecursiveElementsMap.put (name, recursiveElementList);
			
			return recursiveElement;
		} else {
			throw new RequestConstructionException ("Failed to find the recursive element: [" + name + "]");
		}	
	}
	
	public void setField (String name, String value) throws FieldNotFoundException {
		Field field = null;
		if ((name != null && name.length() > 0)
				&& (value != null && value.length() > 0)) {
			
			// the name of the field being set must be in the required list 
			// or the optional list
			if (this.optionalFields.containsKey(name)) {
				field = (Field) this.optionalFields.get (name);
			} else if (this.requiredFields.containsKey(name)) {
				field = (Field) this.requiredFields.get (name);
			} else {
				if (skipFieldNotFoundException) {
					try {
						Configurator.getInstance().getCommonEngineLogger()
							.warn("Failed to find field: [" + name + "]");	
					} catch (Exception e) { ; }
					return;
				} else {
					throw new FieldNotFoundException ("Failed to find field: [" + name + "]");
				}
			}
			
			// field shouldn't be null, but let's just check
			if (field == null) {
				if (skipFieldNotFoundException) {
					try {
						Configurator.getInstance().getCommonEngineLogger()
							.warn("Failed to find field: [" + name + "]");	
					} catch (Exception e) { ; }	
					return;
				} else {
					throw new FieldNotFoundException ("Failed to find field: [" + name + "]");
				}
			}
			
			field.setValue (value);
		}
	}	
	
	public void setFieldValue (String name, String value) throws FieldNotFoundException {
		setField (name, value);
	}
	
	public String getField (String name) {
		Field field = null;
		String returnValue = null;
		
		if (name != null && name.length() > 0) {
			
			// the name of the field being set must be in the required list 
			// or the optional list
			if (this.optionalFields.containsKey(name)) {
				field = (Field) this.optionalFields.get (name);
			} else if (this.requiredFields.containsKey(name)) {
				field = (Field) this.requiredFields.get (name);
			} 
			if (field != null) 
			{
				if (field.getValue() == null || field.getValue().trim().length() == 0) 
				{
					returnValue = field.getDefaultValue();
				} 
				else 
				{
					returnValue = field.getValue();
				}
			}
		}
		
		return returnValue;
	}
	
	public void removeField (String name) {		
		Field field = null;
		
		if (name != null && name.length() > 0) {
			
			// the name of the field being set must be in the required list 
			// or the optional list
			if (this.optionalFields.containsKey(name)) {
				field = (Field) this.optionalFields.get (name);
			} else if (this.requiredFields.containsKey(name)) {
				field = (Field) this.requiredFields.get (name);
			} 
			
			if (field != null) {
				field.setValue(null);
			}
			
		}		
	}
	
	public void clearFields () {
		Iterator itr = null;
		Field field = null;
		
		itr = this.optionalFields.entrySet().iterator();
		
		while (itr.hasNext()) {
			field = (Field) ((Entry)itr.next()).getValue();
			field.setValue(null);
		}
		
		itr = this.requiredFields.entrySet().iterator();
		
		while (itr.hasNext()) {
			field = (Field) ((Entry)itr.next()).getValue();
			field.setValue(null);
		}		
		
	}
	
	public String getName() {
		return name;
	}	
	
	public String getXml() {
		return xml;
	}	
	
	public Map getRecursiveElements() {
		return recursiveElements;
	}	
	
	public List getUsedComplexRoots() {
		return usedComplexRoots;
	}	
	
	public Object clone () {
		Template newTemplate = new Template (this.name);
		
		newTemplate.setXml(this.xml);
		newTemplate.setChildIndexElement(this.ChildIndexElement);
		
		// even though the following are lists and maps we don't
		// need to do a deep copy. a reference is good enough 
		newTemplate.setComplexRoots(this.complexRoots);
		newTemplate.setRecursiveElements(this.recursiveElements);
		
		// we do need to do a deep copy on these maps since they hold
		// the user data
		Iterator optionalFieldItr = optionalFields.entrySet().iterator();
		while (optionalFieldItr.hasNext()) {
			Entry entry = (Entry) optionalFieldItr.next();
			newTemplate.getOptionalFields().put(entry.getKey(), ((Field)entry.getValue()).clone());
		}
		
		Iterator requiredFieldItr = requiredFields.entrySet().iterator();
		while (requiredFieldItr.hasNext()) {
			Entry entry = (Entry) requiredFieldItr.next();
			newTemplate.getRequiredFields().put(entry.getKey(), ((Field)entry.getValue()).clone());
		}
		
		return newTemplate;
	}
	
	public void setSkipFieldNotFoundException (boolean bool) {
		skipFieldNotFoundException = bool;
	}
	
	protected Map getComplexRoots() {
		return complexRoots;
	}
	
	protected void setComplexRoots(Map complexRoots) {
		this.complexRoots = complexRoots;
	}
	
	protected void setName(String name) {
		this.name = name;
	}
	
	protected Map getUserDefinedRecursiveElementsMap () {
		return userDefinedRecursiveElementsMap;
	}
	
	protected void setUserDefinedRecursiveElementsMap (Map userDefinedRecursiveElementsMap) {
		this.userDefinedRecursiveElementsMap = userDefinedRecursiveElementsMap;
	}	
	
	protected Map getOptionalFields() {
		return optionalFields;
	}
	
	protected void setOptionalFields(Map optionalFields) {
		this.optionalFields = optionalFields;
	}
	
	protected Map getRequiredFields() {
		return requiredFields;
	}
	
	protected void setRequiredFields(Map requiredFields) {
		this.requiredFields = requiredFields;
	}	
	
	protected String getChildIndexElement() {
		return ChildIndexElement;
	}
	
	protected void setChildIndexElement(String childIndexElement) {
		ChildIndexElement = childIndexElement;
	}
	
	protected void setXml(String xml) {
		this.xml = xml;
	}
	
	protected void setRecursiveElements(Map recursiveElements) {
		this.recursiveElements = recursiveElements;
	}
	
	protected void setUsedComplexRoots(List usedComplexRoots) {
		this.usedComplexRoots = usedComplexRoots;
	}	
	
	private boolean skipFieldNotFoundException = false;
	private String name = null;	
	private String xml = null;	
	private String ChildIndexElement = null;
	private Map userDefinedRecursiveElementsMap = new HashMap();
	private Map requiredFields = new HashMap();
	private Map optionalFields = new HashMap();	
	private Map complexRoots = new HashMap();
	private Map recursiveElements = new HashMap();
	private List usedComplexRoots = new ArrayList();	

}
