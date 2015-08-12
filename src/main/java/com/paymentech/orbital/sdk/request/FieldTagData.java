package com.paymentech.orbital.sdk.request;

/**
 * <p><b>Title:</b> TagData</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Basava Parvataneni</p> <p><b>Description:</b><br><br>
 * Tag information used and helper methods used for template substitutions. </p>
 */
public class FieldTagData {
    private String name;
    private boolean defaultFlag = false;
    private String defaultValue = "";
    private boolean required = false;
    private String patternString;
    private boolean dirty;

    public FieldTagData() {
    }

    public FieldTagData(String name, String patternString, boolean required, boolean defaultFlag, String defaultValue) {
        this.name = name;
        this.patternString = patternString;
        this.required = required;
        this.defaultFlag = defaultFlag;
        this.defaultValue = defaultValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getPatternString() {
        return patternString;
    }

    public void setPatternString(String patternString) {
        this.patternString = patternString;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
