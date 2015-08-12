package com.paymentech.orbital.sdk.interfaces;

/**
 * <p><b>Title:</b> ResponseIF</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Published interface for the response class. </p>
 */
public interface ResponseIF {
    /** Helper methods for xml message content * */

    /**
     * Indicates whether the xml returned by the Orbital Gateway is a QuickResponse
     * @return boolean
     */
    boolean isQuickResponse();

    /**
     * Indicates whether the xml returned by the Orbital Gateway contains an error message.
     * @return boolean
     */
    boolean isError();

    /**
     * Indicates whether the xml returned by the Orbital Gateway contained no errors.
     * @return boolean
     */
    boolean isGood();

    /**
     * Indicates Orbital Gateway approved the request.
     * @return boolean
     */
    boolean isApproved();

    /**
     * Indicates whether the Orbital Gateway declined the transaction.
     * @return boolean
     */
    boolean isDeclined();

    /**
     * Extracts and returns the authorization code from the xml message that was returned by the Orbital Gateway.
     * @return The authorization code.
     */
    String getAuthCode();

    /**
     * Extracts and returns the AVS response code from the xml message that was returned by the Orbital Gateway.
     * @return String authCode
     */
    String getAVSResponseCode();

    /**
     * Extracts and returns the CVV2 response code from the xml message that was returned by the Orbital Gateway.
     * @return String CVV2RespCode
     */
    String getCVV2RespCode();

    /**
     * Extracts and returns the status message from the xml message that was returned by the Orbital Gateway.
     * @return String Message
     */
    String getMessage();

    /**
     * Extracts and returns the process status from the xml message that was returned by the Orbital Gateway.
     * @return String Status
     */
    String getStatus();

    /**
     * Extracts and returns the response code from the xml message that was returned by the Orbital Gateway.
     * @return
     */
    String getResponseCode();

    /**
     * Extracts and returns the TxRefNum from the xml message that was returned by the Orbital Gateway.
     * @return TxRefNum
     */
    String getTxRefNum();

    /**
     * Extracts a value from the xml message that was returned by the Orbital Gateway.
     * @param xmlElementName One of the fields defined as constants in this interface (ResponseIF.java), or
     * a valid xml element name
     * @return The value contained by the xml element
     */
    String getValue(String xmlElementName);

    /**
     * Return the xml message returned by the Orbital Gateway
     * @return String The xml message
     */
    String toString();

    /**
     * Return the xml message returned by the Orbital Gateway
     * @return String The xml message
     */
    String toXmlString();

    /**
     * Return the xml message returned by the Orbital Gateway with AccountNum (credit card account number) masked out with
     * "X's".  Original message length is preserved.
     * @return String The xml message
     */
    String toMaskedXmlString();

    /** Configuration setting indicating response type (Host or Gateway) */
    String RESPONSE_TYPE_KEY = "Response.response_type";
}
