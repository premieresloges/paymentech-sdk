package com.paymentech.orbital.sdk.request;

/**
 * <p><b>Title:</b> Fields</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * <p/>
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Static class of all the XML template element names</p>
 * <p><br><br> Under Construction</p>
 */
public class Fields {

  private Fields() {
    ;
  }

  // inner classes that represent the transactions and
  // field name (for convenience)
  public class NEW_ORDER_REQUEST {

    public final static String INDUSTRY_TYPE = "IndustryType";
    public final static String MESSAGE_TYPE = "MessageType";
    public final static String BIN = "BIN";
    public final static String MERCHANT_ID = "MerchantID";
    public final static String TERMINAL_ID = "TerminalID";
    public final static String CARD_BRAND = "CardBrand";
    public final static String ACCOUNT_NUM = "AccountNum";
    public final static String EXP = "Exp";
    public final static String CURRENCY_CODE = "CurrencyCode";
    public final static String CURRENCY_EXPONENT = "CurrencyExponent";
    public final static String CARD_SEC_VAL_IND = "CardSecValInd";
    public final static String CARD_SEC_VAL = "CardSecVal";
    public final static String DEBIT_CARD_ISSUE_NUM = "DebitCardIssueNum";
    public final static String DEBIT_CARD_START_DATE = "DebitCardStartDate";
    public final static String BCR_T_NUM = "BCRtNum";
    public final static String CHECK_DDA = "CheckDDA";
    public final static String BANK_ACCOUNT_TYPE = "BankAccountType";
    public final static String ECP_AUTH_METHOD = "ECPAuthMethod";
    public final static String BANK_PMT_DELV = "BankPmtDelv";
    public final static String AVS_ZIP = "AVSzip";
    public final static String AVS_ADDRESS_1 = "AVSaddress1";
    public final static String AVS_ADDRESS_2 = "AVSaddress2";
    public final static String AVS_CITY = "AVScity";
    public final static String AVS_STATE = "AVSstate";
    public final static String AVS_PHONE_NUM = "AVSphoneNum";
    public final static String AVS_NAME = "AVSname";
    public final static String AVS_COUNTRY_CODE = "AVScountryCode";
    public final static String CUSTOMER_PROFILE_FILE_ORDER_IND = "CustomerProfileFromOrderInd";
    public final static String CUSTOMER_REF_NUM = "CustomerRefNum";
    public final static String CUSTOMER_PROFILE_ORDER_OVERRIDE_IND = "CustomerProfileOrderOverrideInd";
    public final static String AUTHENTICATION_ECI_IND = "authenticationECIInd";
    public final static String CAVV = "CAVV";
    public final static String XID = "XID";
    public final static String PRIOR_AUTH_ID = "PriorAuthID";
    public final static String ORDER_ID = "OrderID";
    public final static String AMOUNT = "Amount";
    public final static String COMMENTS = "Comments";
    public final static String SHIPPING_REF = "ShippingRef";
    public final static String TAX_IND = "TaxInd";
    public final static String TAX = "Tax";
    public final static String AMEX_TRAN_ADV_ADDN_1 = "AMEXTranAdvAddn1";
    public final static String AMEX_TRAN_ADV_ADDN_2 = "AMEXTranAdvAddn2";
    public final static String AMEX_TRAN_ADV_ADDN_3 = "AMEXTranAdvAddn3";
    public final static String AMEX_TRAN_ADV_ADDN_4 = "AMEXTranAdvAddn4";
    public final static String AAV = "AAV";
    public final static String SD_MERCHANT_NAME = "SDMerchantName";
    public final static String SD_PRODUCT_DESCRIPTION = "SDProductDescription";
    public final static String SD_MERCHANT_CITY = "SDMerchantCity";
    public final static String SD_MERCHANT_PHONE = "SDMerchantPhone";
    public final static String SD_MERCHANT_URL = "SDMerchantURL";
    public final static String SD_MERCHANT_EMAIL = "SDMerchantEmail";
    public final static String RECURRING_IND = "RecurringInd";
    public final static String EUDD_COUNTRY_CODE = "EUDDCountryCode";
    public final static String EUDD_BANK_SORT_CODE = "EUDDBankSortCode";
    public final static String EUDD_RIB_CODE = "EUDDRibCode";
    public final static String PC_ORDER_NUM = "PCOrderNum";
    public final static String PC_DEST_ZIP = "PCDestZip";
    public final static String PC_DEST_NAME = "PCDestName";
    public final static String PC_DEST_ADDRESS_1 = "PCDestAddress1";
    public final static String PC_DEST_ADDRESS_2 = "PCDestAddress2";
    public final static String PC_DEST_CITY = "PCDestCity";
    public final static String PC_DEST_STATE = "PCDestState";
    private NEW_ORDER_REQUEST() {
      ;
    }
  }

  public class NEW_ORDER_RESPONSE {

    public final static String INDUSTRY_TYPE = "IndustryType";
    public final static String MESSAGE_TYPE = "MessageType";
    public final static String MERCHANT_ID = "MerchantID";
    public final static String TERMINAL_ID = "TerminalID";
    public final static String CARD_BRAND = "CardBrand";
    public final static String ORDER_ID = "OrderID";
    public final static String TX_REF_NUM = "TxRefNum";
    public final static String TX_REF_IDX = "TxRefIdx";
    public final static String PROC_STATUS = "ProcStatus";
    public final static String APPROVAL_STATUS = "ApprovalStatus";
    public final static String RESP_CODE = "RespCode";
    public final static String AVS_RESP_CODE = "AVSRespCode";
    public final static String CVVS_RESP_CODE = "CVV2RespCode";
    public final static String AUTH_CODE = "AuthCode";
    public final static String RECURRING_ADVICE_CD = "RecurringAdviceCd";
    public final static String CAVV_RESP_CODE = "CAVVRespCode";
    public final static String STATUS_MSG = "StatusMsg";
    public final static String RESP_MSG = "RespMsg";
    public final static String HOST_RESP_CODE = "HostRespCode";
    public final static String HOST_AVS_RESP_CODE = "HostAVSRespCode";
    public final static String HOST_CVV2_RESP_CODE = "HostCVV2RespCode";
    public final static String CUSTOMER_REF_NUM = "CustomerRefNum";
    public final static String CUSTOMER_NAME = "CustomerName";
    public final static String PROFILE_PROC_STATUS = "ProfileProcStatus";
    public final static String CUSTOMER_PROFILE_MESSAGE = "CustomerProfileMessage";
    public final static String RESP_TIME = "RespTime";
    private NEW_ORDER_RESPONSE() {
      ;
    }
  }


}
