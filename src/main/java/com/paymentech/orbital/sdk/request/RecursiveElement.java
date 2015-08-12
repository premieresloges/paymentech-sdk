package com.paymentech.orbital.sdk.request;

/**
 * <p><b>Title:</b> RecursiveElement</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Simple Java bean to hold the characteristics and data of a single XML template recursive element</p>
 */
public class RecursiveElement {
	
	public RecursiveElement (String name, String countElement, String enforceCount,
		String maxCount) {
		this.name = name;
		this.countElement = countElement;
		
		if ((enforceCount != null) && (enforceCount.equalsIgnoreCase("yes"))) {
			this.enforceGreaterThanZero = true;
		}
	
		setMaxCount (maxCount);

	}
	
	public String getCountElement() {
		return countElement;
	}
	
	public void setCountElement(String countElement) {
		this.countElement = countElement;
	}
	
	public boolean isEnforceGreaterThanZero() {
		return enforceGreaterThanZero;
	}
	
	public void setEnforceGreaterThanZero(boolean enforceGreaterThanZero) {
		this.enforceGreaterThanZero = enforceGreaterThanZero;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getMaxCount() {
		return maxCount;
	}
	
	public void setMaxCount(String maxCount) {
		if (maxCount != null && maxCount.trim().length() > 0) {
			try {
				this.maxCount = Integer.parseInt(maxCount);
			} catch (NumberFormatException nfe) {
				this.maxCount = 99;
			}
		}
	}	
	
	private String name = null;
	private String countElement = null;
	private boolean enforceGreaterThanZero = false;
	private int maxCount = 0;
	
}
