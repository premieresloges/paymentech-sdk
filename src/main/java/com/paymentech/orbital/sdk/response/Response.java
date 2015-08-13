package com.paymentech.orbital.sdk.response;

import com.paymentech.eis.tools.StringUtils;
import com.paymentech.orbital.sdk.configurator.Configurator;
import com.paymentech.orbital.sdk.configurator.ConfiguratorIF;
import com.paymentech.orbital.sdk.interfaces.ResponseIF;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p><b>Title:</b> Response</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Sam Ayers</p> <p><b>Description:</b><br><br>
 * Response class.  Includes the xml returned by the Orbital Gateway (if any), along with helper methods for determining error
 * conditions and extracting xml element values. </p>
 */
public class Response implements ResponseIF {

  /**
   * Approval Status
   */
  public static final String APPROVAL_STATUS = "ApprovalStatus";
  /**
   * Authorization Code
   */
  public static final String AUTH_CODE = "AuthCode";
  /**
   * CVV2 Response Code
   */
  public static final String CVV2_RESP_CODE = "CVV2RespCode";
  /**
   * Process Status Code
   */
  public static final String PROC_STATUS = "ProcStatus";
  /**
   * Profile Process Status Code
   */
  public static final String PROFILE_PROC_STATUS = "ProfileProcStatus";
  /**
   * Process Status Message
   */
  public static final String STATUS_MESSAGE = "StatusMsg";
  /**
   * Profile Process Status Message
   */
  public static final String PROFILE_STATUS_MESSAGE = "CustomerProfileMessage";
  /**
   * Transaction Reference Number
   */
  public static final String TXREF_NUM = "TxRefNum";
  /**
   * Response Code
   */
  public static final String RESP_CODE = "RespCode";
  /**
   * Response Code from Host
   */
  public static final String HOST_RESP_CODE = "HostRespCode";
  /**
   * AVS Response Code
   */
  public static final String AVS_RESP_CODE = "AVSRespCode";
  /**
   * AVS Response Code from Host
   */
  public static final String HOST_AVS_RESP_CODE = "HostAVSRespCode";
  /**
   * Indicates Account Number (credit card account number)
   */
  public static final String ACCOUNT_NUM = "AccountNum";
  /**
   * Indicates Gateway response type
   */
  public static final String GATEWAY_TYPE = "gateway";
  /**
   * Indicates Host response type
   */
  public static final String HOST_TYPE = "host";
  /**
   * If present in raw message, indicates a quick response
   */
  public static final String QUICK_RESPONSE_IDENTIFIER = "<QuickResp";
  /**
   * If present in raw message, indicates a profile response
   */
  public static final String PROFILE_RESPONSE_IDENTIFIER = "<ProfileResp";
  /**
   * If present in raw message, indicates a Account Updater response
   */
  public static final String ACUPDT_RESPONSE_IDENTIFIER = "<AccountUpdaterResp";
  /**
   * Used for String not found
   */
  public static final int NOT_FOUND = -1;
  // List of fields that require masking before being displayed.
  // Note: there is an identical list in Request.java
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
  //message is the Stringified result
  protected String rawMessage = "";
  protected String approval;
  protected String procStatus;
  protected int iProcStatus;
  protected boolean quickResponse = false;
  protected boolean approved = false;
  protected boolean declined = false;
  protected boolean error = false;
  protected String cvv2;
  protected String txRefNum;
  protected String message;
  protected String authCode;
  protected boolean good;
  protected String respTag;
  protected String avsTag;
  protected String responseCode;
  protected String avs;
  private ConfiguratorIF configurator = null;
  private Map configurations = null;
  private Logger engineLogger;
  /**
   * Default constructor.
   */
  public Response() throws InitializationException {
    this.setConfigurator(Configurator.getInstance());
  }
  /**
   * Constructor that sets the configurator and the raw message into the response upon creation.
   *
   * @param configurator
   * @param rawMessage
   */
  public Response(String rawMessage) throws InitializationException {
    this.setConfigurator(Configurator.getInstance());
    this.setRawMessage(rawMessage);
  }

  /**
   * Extracts a value from the xml message that was returned by the Orbital Gateway.
   *
   * @param xmlElementName One of the fields defined as constants in this interface (ResponseIF.java), or
   *                       a valid xml element name
   * @return The value contained by the xml element
   */
  public String getValue(String xmlElementName) {
    return extractValue(this.rawMessage, xmlElementName);
  }

  /**
   * Return a String of text that is contained between two delimiters
   *
   * @param startDelimiter
   * @param endDelimiter
   * @return
   */
  public String getValue(String startDelimiter, String endDelimiter) {
    //Assumes the startDelimiter and endDelimiter are raw delimiters
    // i.e. should not be encapsulated with xml start and end delimiters
    return extractValue(this.rawMessage, startDelimiter, endDelimiter);
  }

  /**
   * Get the configurator for this response object.
   *
   * @return
   */
  public ConfiguratorIF getConfigurator() {
    return this.configurator;
  }

  /**
   * Set the global configurations into the response object
   *
   * @param configurator
   */
  public void setConfigurator(ConfiguratorIF configurator) {
    this.configurator = configurator;
    this.configurations = configurator.getConfigurations();
    this.engineLogger = configurator.getCommonEngineLogger();
  }

  /**
   * Return the "raw" xml message that was returned by the Orbital Gateway
   *
   * @return String The xml message
   */
  public String toXmlString() {
    return rawMessage;
  }

  /**
   * Return the xml message that was returned by the Orbital Gateway, with the credit card account number masked.  Message
   * length is maintained to be the same as original.
   *
   * @return String The xml message
   */
  public String toMaskedXmlString() {
    String maskedMessage = this.maskXmlMessage(rawMessage);
    return maskedMessage;
  }

  /**
   * Indicates whether the xml message returned by the Gateway is a Quick Response
   *
   * @return boolean
   */
  public boolean isQuickResponse() {
    return this.quickResponse;
  }

  /** XML Element Names */

  /**
   * Indicates whether the xml message returned by the Gateway included an error result.
   *
   * @return boolean
   */
  public boolean isError() {
    return this.error;
  }

  /**
   * Indicates whether the xml message returned by the Gateway did not include an error result.
   *
   * @return boolean
   */
  public boolean isGood() {
    return this.good;
  }

  /**
   * Indicates whether the transaction was approved by the Orbital Gateway.
   *
   * @return boolean
   */
  public boolean isApproved() {
    return this.approved;
  }

  /**
   * Indicates whether the transaction was declined by the Orbital Gateway.
   *
   * @return boolean
   */
  public boolean isDeclined() {
    return this.declined;
  }

  /**
   * Authorization Code
   *
   * @return String The Authorization Code
   */
  public String getAuthCode() {
    return this.authCode;
  }

  /**
   * AVS Response Code
   *
   * @return String The AVS Response Code
   */
  public String getAVSResponseCode() {
    return this.avs;
  }

  /**
   * CVV2 Response Code
   *
   * @return String CVV2 Response Code
   */
  public String getCVV2RespCode() {
    return this.cvv2;
  }

  /**
   * Process Status Code.
   *
   * @return The Process Status Code.
   */
  public String getStatus() {
    return this.procStatus;
  }

  /**
   * Process Status Message
   *
   * @return The Process Status Message
   */
  public String getMessage() {
    return this.message;
  }

  /**
   * Response Code
   *
   * @return String The Response Code
   */
  public String getResponseCode() {
    return this.responseCode;
  }

  /**
   * Transaction Reference Number
   *
   * @return String The Transaction Reference Number
   */
  public String getTxRefNum() {
    return this.txRefNum;
  }

  /**
   * Raw XML Message
   *
   * @return String The Raw XML Message.
   */
  public String getRawMessage() {
    return rawMessage;
  }

  /**
   * Raw XML Message
   *
   * @param rawMessage The message to be set into the response object
   */
  public void setRawMessage(String rawMessage) {
    boolean isProfile = false;
    boolean isAcUpdt = false;

    this.rawMessage = rawMessage;
    //Determine if the returned raw message is a Quick Response
    if (rawMessage.indexOf(QUICK_RESPONSE_IDENTIFIER) == NOT_FOUND) {
      this.quickResponse = false;
    } else {
      this.quickResponse = true;
    }

    // check if this is a profile response
    if (rawMessage.indexOf(PROFILE_RESPONSE_IDENTIFIER) != NOT_FOUND) {
      isProfile = true;
    }
    // check if this is a account updater response
    if (rawMessage.indexOf(ACUPDT_RESPONSE_IDENTIFIER) != NOT_FOUND) {
      isAcUpdt = true;
    }

    //Extract the approval status
    this.approval = this.extractValue(rawMessage, APPROVAL_STATUS);

    // Extract the ProcStatus element
    if (isProfile || isAcUpdt) {
      this.procStatus = this.extractValue(rawMessage, PROFILE_PROC_STATUS);
    } else {
      this.procStatus = this.extractValue(rawMessage, PROC_STATUS);
    }

    if (!StringUtils.isEmpty(procStatus)) {
      String strippedProcStatus = stripLeadingZeroes(procStatus);
      this.iProcStatus = Integer.parseInt(strippedProcStatus);
    }

    /**
     * There is an error if 1) there exists an approval code > 1, 2) there is no approval code but there exists a non-zero
     * proc-status, or 3) Both Approval-code and proc-status are missing.
     */
    //Determine the Approved/Declined and Error status
    if (!StringUtils.isEmpty(approval)) {
      String strippedApproval = stripLeadingZeroes(approval);
      int iApproval = Integer.parseInt(strippedApproval);
      if (iApproval == 1) {
        approved = true;
      }
      if (iApproval == 0) {
        declined = true;
      }
      if (iApproval > 1) {
        error = true;
      }
    } else if (!StringUtils.isEmpty(procStatus)) {
      if (iProcStatus == 0) {
        error = false;
      } else {
        error = true;
      }
    }
    // Extract the General Response information
    this.cvv2 = this.extractValue(rawMessage, CVV2_RESP_CODE);
    this.txRefNum = this.extractValue(rawMessage, TXREF_NUM);

    if (isProfile || isAcUpdt) {
      this.message = this.extractValue(rawMessage, PROFILE_STATUS_MESSAGE);
    } else {
      this.message = this.extractValue(rawMessage, STATUS_MESSAGE);
    }

    this.authCode = this.extractValue(rawMessage, AUTH_CODE);
    this.good = !error;

		/* Use the Response Type to determine which fields to use
     * in determining the Response Code and AVS Response Code
		 */

    String responseType = getResponseType();
    if ((!StringUtils.isEmpty(responseType)) && responseType.equalsIgnoreCase(HOST_TYPE)) {
      //Response type is Host
      this.respTag = HOST_RESP_CODE;
      this.avsTag = HOST_AVS_RESP_CODE;
    } else {
      //Response type is Gateway
      this.respTag = RESP_CODE;
      this.avsTag = AVS_RESP_CODE;
    }
    this.responseCode = this.extractValue(rawMessage, respTag);
    this.avs = this.extractValue(rawMessage, avsTag);
  }

  /**
   * Response Type
   *
   * @return String The Response Type
   */
  protected String getResponseType() {
    String responseType = (String) configurations.get(ResponseIF.RESPONSE_TYPE_KEY);
    if (this.isQuickResponse()) {
      responseType = GATEWAY_TYPE;
    } else {
      if ((StringUtils.isEmpty(responseType)) ||
          ((!responseType.equalsIgnoreCase(GATEWAY_TYPE)) && (!responseType.equalsIgnoreCase(HOST_TYPE)))) {
        engineLogger.warn(responseType + " is an invalid Response.response_type. Defaulting to [gateway]");
        responseType = GATEWAY_TYPE;
      }
    }
    return responseType;
  }

  /**
   * Extract an xml element value from the raw xml message.
   *
   * @param rawMessage     The raw xml message
   * @param xmlElementName The xml element name for the value to be extracted
   * @return String The xml element's value
   */
  protected String extractValue(String rawMessage, String xmlElementName) {
    //This method assumes the incoming xmlElementName is to be wrapped with xml style delimiters
    //Must initially include the possibility of attributes in the xml start tag,
    //so we need to search using 2 different patterns for the start tag.
    String startDelimiter1 = "<" + xmlElementName + " ";  // with attributes
    String startDelimiter2 = "<" + xmlElementName + ">";  // without attributes

    //No attributes in the end tag - can use entire tag as end delimiter
    String endDelimiter = "</" + xmlElementName + ">";

    //The preValue may include potential attributes in the start tag so we
    //need to search for 2 different patterns, with or without attributes.
    String preValue = extractValue(rawMessage, startDelimiter1, endDelimiter);
    if (preValue == null)
      preValue = extractValue(rawMessage, startDelimiter2, endDelimiter);
    if (preValue == null) {
      //Nothing found, return null
      return null;
    } else {
      //String was found - return text starting at the close of the start tag to the end
      int index = preValue.indexOf(">") + 1;
      if (index == NOT_FOUND) {
        //No ">" found at the end of the start tag... xml is likely malformed
        return null;
      } else {
        //Return the final value from between the tags
        String value = preValue.substring(index);
        return value;
      }
    }
  }

  /**
   * Extract a String located between two delimiters
   *
   * @param rawMessage     The message from which the value will be extracted
   * @param startDelimiter The starting delimiter for the String to be extracted
   * @param endDelimiter   The ending delimiter for the String to be extracted
   * @return String The String between the delimiters
   */
  protected String extractValue(String rawMessage, String startDelimiter, String endDelimiter) {
    //locate the beginning of the value within the message
    int beginIndex = rawMessage.indexOf(startDelimiter);
    if (beginIndex == NOT_FOUND) {
      return null;
    }
    beginIndex += startDelimiter.length();

    //locate the end of the value within the message
    int endIndex = rawMessage.indexOf(endDelimiter);
    if (endIndex == NOT_FOUND) {
      return null;
    }
    // extract the value
    String value = rawMessage.substring(beginIndex, endIndex);
    return value;
  }

  protected String stripLeadingZeroes(String stringToStrip) {
    if ((stringToStrip != null) && (stringToStrip.length() > 1) && (stringToStrip.startsWith("0"))) {
      StringBuffer strippedBuffer = new StringBuffer();
      char[] arrayToStrip = stringToStrip.toCharArray();
      boolean pastLeadingZeroes = false;
      for (int i = 0; i < arrayToStrip.length; i++) {
        if (arrayToStrip[i] != '0' || pastLeadingZeroes) {
          pastLeadingZeroes = true;
          strippedBuffer.append(arrayToStrip[i]);
        }
      }
      return strippedBuffer.toString();
    } else {
      //If no leading zeroes, just return what was passed in
      return stringToStrip;
    }
  }

  protected String maskXmlMessage(String messageToMask) {

    // Mask predefined fields
    for (String fieldName : maskedFieldNames) {
      messageToMask = maskField(messageToMask, fieldName);
    }

    String maskField = null;
    try {
      maskField = (String) Configurator.getInstance().getConfigurations().get("MaskFieldList");
    } catch (Exception e) {
      ;
    }
    // mask merchant specific field list
    if (maskField != null) {
      String[] maskFieldArray = maskField.split(",");
      for (int ctr = 0; ctr < maskFieldArray.length; ctr++) {
        messageToMask = maskField(messageToMask, maskFieldArray[ctr]);
      }
    }
    return messageToMask;
  }

  protected String maskField(String messageToMask, String fieldToMask) {
    String fieldValue = this.extractValue(messageToMask, fieldToMask);
    if (StringUtils.isEmpty(fieldValue)) {
      //The field is not present in the message, simply return the message
      return messageToMask;
    } else {
      //Create a string of X's, the same length of the field value
      // (in order to preserve the original xml message length)
      StringBuffer sbFieldMask = new StringBuffer();
      for (int i = 0; i < fieldValue.length(); i++) {
        sbFieldMask.append("X");
      }
      String fieldMask = sbFieldMask.toString();
      //Define the ending field delimiter
      String endingDelimiter = "</" + fieldToMask + ">";
      //Get the first part of the response - must account for possible field attributes
      int firstPartPlusFieldValueIndex = messageToMask.indexOf(endingDelimiter);
      String firstPartPlusFieldValue = messageToMask.substring(0, firstPartPlusFieldValueIndex);
      //Remove the field value by backing up to the last ">")
      int firstIndex = firstPartPlusFieldValue.lastIndexOf(">") + 1;
      String firstPart = messageToMask.substring(0, firstIndex);
      int secondIndex = messageToMask.indexOf(endingDelimiter);
      //Get the second part of the response
      String secondPart = messageToMask.substring(secondIndex);
      //Create the masked version of the message
      String maskedMessage = firstPart + fieldMask + secondPart;
      return maskedMessage;
    }
  }
}
