package com.paymentech.orbital.sdk.interfaces;

import java.util.List;

import org.apache.regexp.RESyntaxException;

import com.paymentech.orbital.sdk.request.FieldNotFoundException;
import com.paymentech.orbital.sdk.request.RequestConstructionException;
import com.paymentech.orbital.sdk.request.XMLTemplateNotFoundException;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;

/**
 * <p><b>Title:</b> RequestIF.java</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Published interface for the request class. </p>
 */
public interface RequestIF {
    /**
     * Get the logger transactionID for this request
     * @param fieldName
     * @return
     */
    long getLogTransactionID();

    /**
     * Set the logger transactionID for this request
     * @param fieldName
     * @param value
     */
    void setLogTransactionID(long transactionID);
    // field name and value

    /**
     * Get the field and value - as 'BIN' and '0001'
     * @param fieldName
     * @return
     */
    String getField(String fieldName);

    /**
     * Set the field and value into the fields map
     * @param fieldName
     * @param value
     */
    void setFieldValue(String fieldName, String value) throws FieldNotFoundException;

    /**
     * Get  of the transaction type
     * @return
     */
    String getType();

    /**
     * Get the trace number for Automatic Retries
     * @return
     */
    String getTraceNumber();

    /**
     * Set the trace number for Automatic Retries
     * @param traceNumber
     */
    void setTraceNumber(String traceNumber);

    /**
     * Gets raw template xml string of the request object
     * @return
     */
    String toString();

    /**
     * Gets xml string of the request object
     * with the user and default values set
     * @return
     */
    String getXML()
    	throws InitializationException, RequestConstructionException;

    /**
     * Gets xml string of the request object, with AccountNum and CardSecVal masked out with "X's".  Original
     * message length is preserved.
     * @return
     */
    String getMaskedXML() 
    	throws RESyntaxException, InitializationException, RequestConstructionException;

    /**
     * Clear the field from the fields map
     * @param fieldName
     */
    void clearField(String fieldName);

    /** 
     * Clear all the fields from the fields map 
     */
    void clearAllFields();
    
    TemplateIF getComplexRoot(String rootName) throws RequestConstructionException;
    
    /**
     * Gets xml string of the request object
     * with the user and default values set
     * @return
     * @deprecated Use getXML
     */
    String toXmlString(); 
    
    /**
     * Gets xml string of the request object, with AccountNum and CardSecVal masked out with "X's".  Original
     * message length is preserved.
     * @return
     * @deprecated Use getMaskedXML
     */    
    String toMaskedXmlString();
    
    /**
     * Set the field and value into the fields map
     * @param fieldName
     * @param value
     * @deprecated Use setFieldValue
     */    
    void setField(String fieldName, String value);    

    /**
     * 
     * @deprecated 
     */
    boolean hasFieldErrors();
    
    /**
     * 
     * @deprecated 
     */
    List getFieldErrors();
    
    /**
     * 
     * @deprecated 
     */    
	void validate() throws XMLTemplateNotFoundException, FieldNotFoundException;    

    /**
     * @deprecated 
     */
    String VOID_REQUEST = "Void";
    /**
     * @deprecated 
     */    
    String BATCH_SETTLEMENT = "BatchSettlement";
    /**
     * @deprecated 
     */    
    String PARTIAL_VOID = "PartialVoid";
    /**
     * @deprecated 
     */    
    String CC_AUTHORIZE = "CC.Authorize";
    /**
     * @deprecated 
     */    
    String CC_CAPTURE = "CC.MarkForCapture";
    /**
     * @deprecated 
     */    
    String CC_RECURRING_REFUND = "CC.RecurringRefund";
    /**
     * @deprecated 
     */    
    String CC_FORCE = "CC.Force";
    /**
     * @deprecated 
     */    
    String CC_RECURRING_AUTH_CAPTURE = "CC.RecurringAuthCap";
    /**
     * @deprecated 
     */    
    String CC_ECOMMERCE_REFUND = "CC.eCommerceRefund";
    /**
     * @deprecated 
     */    
    String EFALCON_AUTH_CAPTURE = "eFalcon.AuthCap";
    /**
     * @deprecated 
     */    
    String PC2_RECURRING_AUTH_CAPTURE = "PC2.AuthCapRecurring";
    /**
     * @deprecated 
     */    
    String PC2_AUTH = "PC2.Auth";
    /**
     * @deprecated 
     */    
    String ECP_AUTH = "ECP.Authorize";
    /**
     * @deprecated 
     */    
    String ECP_CAPTURE = "ECP.Capture";
    /**
     * @deprecated 
     */    
    String ECP_FORCE_DEPOSIT = "ECP.ForceDeposit";
    /**
     * @deprecated 
     */    
    String ECP_REFUND = "ECP.Refund";
    /**
     * @deprecated 
     */    
    String SWITCH_SOLO_CAPTURE = "SwitchSolo.Capture";
    /**
     * @deprecated 
     */    
    String SWITCH_SOLO_REFUND = "SwitchSolo.Refund";
    /**
     * @deprecated 
     */    
    String SWITCH_SOLO_AUTH = "SwitchSolo.Auth";
    /**
     * @deprecated 
     */    
    String PROFILE_MANAGEMENT = "Profile.Management";
    /**
     * @deprecated 
     */    
	String FLEXCACHE = "FlexCache.StandAlone";
    /**
     * @deprecated 
     */	
	String FLEXCACHE_BATCH = "FlexCache.Batch";
    /**
     * @deprecated 
     */	
	String FLEXCACHE_MFC = "FlexCache.MFC";
    /**
     * @deprecated 
     */	
	String MOTO = "MOTO";
    /**
     * @deprecated 
     */	
   	String MOTO_REFUND = "MOTO.Refund";
   	
   	// PTI40+ Templates
   	String NEW_ORDER_TRANSACTION = "NewOrder";
   	String END_OF_DAY_TRANSACTION = "EOD";
   	String FLEX_CACHE_TRANSACTION = "FlexCache";
   	String MARK_FOR_CAPTURE_TRANSACTION = "MFC";
   	String PROFILE_TRANSACTION = "Profile";
   	String REVERSE_TRANSACTION = "Reverse";
   	String INQUIRY = "Inquiry";
   	String ACCOUNT_UPDATER="AccountUpdater";
   	String SAFETECH_FRAUD_ANALYSIS="SafetechFraudAnalysis";
   	
   	// Include files
   	String PC3_CORE = "PC3Core";
   	String PC3_LINE_ITEMS = "PC3LineItems";
   	String SETTLE_REJECT_BIN = "SettleRejectBin";
   	String PRIOR_AUTH_ID = "PriorAuthID";
   	String FRAUD_ANALYSIS="FraudAnalysis";
   	String SOFT_MERCHANT_DESCRIPTORS="SoftMerchantDescriptors";
   	
   	// linehandler.properties setting to change FieldNotFoundException to WARN
   	// message only
   	String SKIP_FIELD_NOT_FOUND_EXCEPTIONS = "skipFieldNotFoundExceptions";

}
