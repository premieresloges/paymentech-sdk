package com.paymentech.orbital.sdk.request;

import java.util.*;

import com.paymentech.eis.tools.StringUtils;
import com.paymentech.orbital.sdk.configurator.Configurator;
import com.paymentech.orbital.sdk.interfaces.RequestIF;
import com.paymentech.orbital.sdk.interfaces.TemplateIF;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;

/**
 * <p><b>Title:</b> Request</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Orbital Gateway transaction request class. </p>
 */
public class Request implements RequestIF {

	private static Object syncLogID = new Object();

	// List of fields that require masking before being displayed.
	// Note: there is an identical list in Response.java
	final List<String> maskedFieldNames = Arrays.asList(
			"AccountNum",
			"CardSecVal",
			"CheckDDA",
			"BMLCustomerSSN",
			"CCAccountNum",
			"ECPAccountDDA",
			"OrbitalConnectionPassword",
			"CAVV",
			"AAV",
			"StartAccountNum",
			"EUDDIBAN"
			);

    public Request(String transType) throws InitializationException {
        this.transType = transType;
        this.setLogTransactionID(this.getNewLogTransactionIdentifier());
        
        // create the Template object
        // first, get a reference to the TemplateParser (which is a singleton)
        TemplateParser parser = TemplateParser.getInstance(Configurator.getInstance());
        
        String skipFieldNotFound = (String) Configurator.getInstance()
			.getConfigurations().get(RequestIF.SKIP_FIELD_NOT_FOUND_EXCEPTIONS);
        
        if ((skipFieldNotFound != null) && (skipFieldNotFound.equalsIgnoreCase("true"))) {
        	skipFieldNotFoundException = true;
        }
        
        // use the TemplateParser to build the Template object
        try {
        	template = parser.loadTemplate(transType);
        	
        	if (template != null) {
        		template.setSkipFieldNotFoundException (skipFieldNotFoundException);
        	}
        } catch (XMLTemplateNotFoundException e) {
        	// need to do this because we can't change the signature of this
        	// constructor which is already published
        	throw new InitializationException (e.getMessage());
        }
        
    }   
    
    public TemplateIF getComplexRoot (String rootName) throws RequestConstructionException {
    	return template.getComplexRoot(rootName);
    }
    
    public void setFieldValue(String fieldName, String value) throws FieldNotFoundException {
    	template.setField(fieldName, value);
    }
    
	public String getField(String fieldName) {
		return template.getField(fieldName);
	}     

    public String getType() {
        return transType;
    }

    public String getXML() 
    	throws InitializationException, RequestConstructionException {
    	return RequestBuilder.buildRequest(Configurator.getInstance().getCommonEngineLogger(), (Template)template);
    }

    public String getMaskedXML() 
    	throws RESyntaxException, InitializationException, RequestConstructionException {
        return this.maskXmlMessage(getXML());
    }

    public String getTraceNumber() {
        return this.traceNumber;
    }

    public void setTraceNumber(String traceNumber) {
        this.traceNumber = traceNumber;
    }

    public void clearField(String field) {
        template.removeField(field);
    }

    public void clearAllFields() {
        template.clearFields();
    }
    
    public long getLogTransactionID() {
        return logTransactionID;
    }

    public void setLogTransactionID(long transactionID) {
        this.logTransactionID = transactionID;
    }    
    
    public String toString() {
        return template.getXml();
    } 
    
    /**
     * 
     * @deprecated Use setFieldValue
     */    
    public void setField (String fieldName, String value) {
    	
    	try {
    		// since this is the old version of this 
    		// method, we have to expect that there
    		// might be a field that is not an include
    		//
    		// PriorAuthID
    		
    		if (fieldName.equalsIgnoreCase("PriorAuthID")) {
    			TemplateIF priorTemplate = template.getComplexRoot(RequestIF.PRIOR_AUTH_ID);
    			priorTemplate.setFieldValue("PriorAuthID", value);
    		} else {
    			setFieldValue(fieldName, value);
    		}
    		
    	} catch (FieldNotFoundException e) {
    		try {
    			Configurator.getInstance()
    				.getCommonEngineLogger().error(e.getMessage(), e);
    		} catch (Exception ex) { ; }
    	} catch (RequestConstructionException e) {
    		try {
    			Configurator.getInstance()
    				.getCommonEngineLogger().error(e.getMessage(), e);
    		} catch (Exception ex) { ; }
		}
    } 
    
    /**
     * 
     * @deprecated Use setFieldValue
     */      
    public String toXmlString() {
    	String returnVal = null;
    	
    	try {
    		returnVal = getXML();
    	} catch (Exception e) {
    		try {
    			Configurator.getInstance()
    				.getCommonEngineLogger().error(e.getMessage(), e);
    		} catch (Exception ex) { ; }
    	}
    	
    	return returnVal;
    }

    /**
     * 
     * @deprecated Use setFieldValue
     */      
    public String toMaskedXmlString() {
    	String returnVal = null;
    	
    	try {
    		returnVal = getMaskedXML();
    	} catch (Exception e) {
    		try {
    			Configurator.getInstance()
    				.getCommonEngineLogger().error(e.getMessage(), e);
    		} catch (Exception ex) { ; }
    	}
    	
    	return returnVal;
    }    
    
    /**
     * 
     * @deprecated 
     */
    public void setType(String transType) {
    	// this method is kept only to ensure 
    	// backward compatibility
    }

    /**
     * 
     * @deprecated 
     */
    public boolean hasFieldErrors() {
    	// this method is kept only to ensure 
    	// backward compatibility
    	return true;
    }
    
    /**
     * 
     * @deprecated 
     */
    public List getFieldErrors() {
    	// this method is kept only to ensure 
    	// backward compatibility
    	return new ArrayList();
    }
    
    /**
     * 
     * @deprecated 
     */
    public Configurator getConfigurator() {
    	// this method is kept only to ensure 
    	// backward compatibility    	
    	try {
    		return Configurator.getInstance();
    	} catch (Throwable th) {
    		;
    	}
    	
    	return null;
    }    
    
    /**
     * 
     * @deprecated 
     */    
	public void validate() throws XMLTemplateNotFoundException, FieldNotFoundException {
    	// this method is kept only to ensure 
    	// backward compatibility
		;
	}       
	
    /**
     * 
     * @deprecated 
     */    	
    public String getXmlLineString() {
    	// this method is kept only to ensure 
    	// backward compatibility
    	return template.getXml();
    }

    /**
     * 
     * @deprecated 
     */        
    public void setXmlLineString(String xmlLineString) {
    	// this method is kept only to ensure 
    	// backward compatibility
        ;
    }	
    
    /**
     * 
     * @deprecated 
     */     
    public String getXmlString() {
    	// this method is kept only to ensure 
    	// backward compatibility
    	return template.getXml();
    }

    /**
     * 
     * @deprecated 
     */     
    public void setXmlString(String xmlString) {
    	// this method is kept only to ensure 
    	// backward compatibility
        ;
    }    

    protected String maskXmlMessage(String messageToMask) throws RESyntaxException {

    	// Mask predefined fields
    	for (String fieldName : maskedFieldNames) {
    		messageToMask = maskField(messageToMask, fieldName);	
    	}

    	String maskField = null;
    	try
    	{
    		maskField = (String) Configurator.getInstance().getConfigurations().get("MaskFieldList");
    	}
    	catch ( Exception e)
    	{
    		;
    	}
    	// mask merchant specific field list
    	if ( maskField != null)
    	{
    		String[] maskFieldArray = maskField.split(",");
    		for( int ctr = 0 ; ctr < maskFieldArray.length ; ctr++)
    		{
    			messageToMask = maskField(messageToMask, maskFieldArray[ctr] );	
    		}
    	}
    	//return the result
    	return messageToMask;
    }

    private String maskField (String messageToMask, String tag) throws RESyntaxException {
    	String returnVal = messageToMask;
    	String value = null;
    	StringBuffer maskedVal = new StringBuffer();
    	
    	// build regx
    	RE re = new RE ("<" + tag + ">(.*?)</" + tag + ">");
    	
    	if (re.match(messageToMask)) {
    		
    		// get the value of the tag out
    		value = re.getParen(1);
    		
    		if (!StringUtils.isEmpty(value)) {
    		
	    		// determine masking try based on the field type
	    		if (tag.equalsIgnoreCase(ACCOUNT_NUM)) {
	    			// mask all but last 4 digits
	    			if (value.length() > 4) {
	    				maskedVal.append("XXXXXXXXXXXX" + value.substring(value.length() - 4));
	    			}
	    		} else if (tag.equalsIgnoreCase(CARD_SEC_VALUE)) {
	    			// just mask everything out
	    			maskedVal.append("XXX");
	    		} else {
	    			// asking to mask a field that this function doesn't know about
	    			// just mask it anyway (just can't be too careful)
	    			for (int i = 0; i < value.length(); i++) {
	    				maskedVal.append("X");
	    			}
	    		}
	    		
	    		re = new RE ("<" + tag + ">\\s*" + value + "\\s*</" + tag + ">");
	    		
	    		returnVal = re.subst(messageToMask, "<" + tag + ">" + maskedVal.toString() + "</" + tag + ">");
    		
    		}
    	}
    	
    	return returnVal;
    }

    private long getNewLogTransactionIdentifier() {
        synchronized( syncLogID ) {
            return logTransactionIdentifier++;
        }
    }
    
    // indicate whether or not to skip FieldNotFoundExceptions
    private boolean skipFieldNotFoundException = false;
    
    // reference to the Template object
    private TemplateIF template = null;
    
    // Transaction type
    private String transType = null;
    
    // store the trace number
    private String traceNumber = "";

    // uniquie value for creating a transaction
    private long logTransactionID = 0;
    
    // value used by all transaction to create logTransactionID
    private static long logTransactionIdentifier = 0;
    
    /** Xml Field Element name for Account Number (credit card account number) */
    private static final String ACCOUNT_NUM = "AccountNum";

    /** Xml Field Element name for Card Security Value */
    private static final String CARD_SEC_VALUE = "CardSecVal";
    
}
