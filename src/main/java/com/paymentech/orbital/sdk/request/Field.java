package com.paymentech.orbital.sdk.request;

/**
 * <p><b>Title:</b> Field</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Simple Java bean to hold the characteristics and data of a single XML template element </p>
 */
public class Field {
	
	public Field (String name) {
		this.name = name;
	}
	
	public Field (String name, String defaultValue) {
		this.name = name;
		this.defaultValue = defaultValue;
	}	
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}	
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Object clone () {
		return new Field (this.name, this.defaultValue);
	}
	
	private String name = null;
	private String defaultValue = null;	
	private String value = null;
}
