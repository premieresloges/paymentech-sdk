/* $Header:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/orbital/sdk/pre40/TransactionMapper.java-arc   1.3   Apr 20 2011 08:45:08   rbhaskha  $ */

package com.paymentech.orbital.sdk.pre40;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.paymentech.orbital.sdk.interfaces.RequestIF;
import com.paymentech.orbital.sdk.request.FieldNotFoundException;
import com.paymentech.orbital.sdk.request.RequestConstructionException;
import com.paymentech.orbital.sdk.request.Template;
import com.paymentech.orbital.sdk.request.TemplateParser;
import com.paymentech.orbital.sdk.request.XMLTemplateNotFoundException;
import com.paymentech.orbital.sdk.util.exceptions.InitializationException;

/**
 * <p><b>Title:</b> TransactionMapper</p> <p>(C)opyright 2007, Chase Paymentech Solutions, LLC. All rights reserved
 * 
 * The copyright notice above does not evidence any actual or intended
 * publication of such source code.
 * The copyright notice above does not evidence any actual or intended publication of such source code.</p>
 * <p><b>Author:</b> Scott Monahan</p><p><b>Description:</b><br><br> Converts pre-PTI40 template data to PTI40+ specifications </p>
 */
public class TransactionMapper {
	
    private static Logger logger = Logger.getLogger(TransactionMapper.class);
	
	private static List newOrderTransactions = new ArrayList ();
	private static List reversalTransactions = new ArrayList ();
	private static List flexCacheTransactions = new ArrayList ();
	
	static {
		
		// add the pre40 transactions that map to neworder 
		newOrderTransactions.add(RequestIF.CC_AUTHORIZE.toUpperCase());
		newOrderTransactions.add(RequestIF.CC_RECURRING_REFUND.toUpperCase());
		newOrderTransactions.add(RequestIF.CC_ECOMMERCE_REFUND.toUpperCase());
		newOrderTransactions.add(RequestIF.CC_RECURRING_AUTH_CAPTURE.toUpperCase());			
		newOrderTransactions.add(RequestIF.ECP_AUTH.toUpperCase());
		newOrderTransactions.add(RequestIF.ECP_FORCE_DEPOSIT.toUpperCase());
		newOrderTransactions.add(RequestIF.ECP_REFUND.toUpperCase());
		newOrderTransactions.add(RequestIF.CC_FORCE.toUpperCase());
		newOrderTransactions.add(RequestIF.MOTO.toUpperCase());
		newOrderTransactions.add(RequestIF.MOTO_REFUND.toUpperCase());
		newOrderTransactions.add(RequestIF.PC2_AUTH.toUpperCase());
		newOrderTransactions.add(RequestIF.PC2_RECURRING_AUTH_CAPTURE.toUpperCase());
		newOrderTransactions.add(RequestIF.SWITCH_SOLO_AUTH.toUpperCase());	
		newOrderTransactions.add(RequestIF.SWITCH_SOLO_REFUND.toUpperCase());	
		
		
		// add the pre40 transactions that map to reversal
		reversalTransactions.add(RequestIF.VOID_REQUEST.toUpperCase());
		reversalTransactions.add(RequestIF.PARTIAL_VOID.toUpperCase());
		
		// add the pre40 transactions that map to flexcache
		flexCacheTransactions.add(RequestIF.FLEXCACHE.toUpperCase());
		flexCacheTransactions.add(RequestIF.FLEXCACHE_BATCH.toUpperCase());
		flexCacheTransactions.add(RequestIF.FLEXCACHE_MFC.toUpperCase());	
		
	}

	private TransactionMapper () { ; }
	
	public static Template getMappedTemplate (TemplateParser parser, String pre40Template) 
		throws InitializationException, XMLTemplateNotFoundException, FieldNotFoundException, RequestConstructionException {
		Template template = null;
		
		// everything is normalized to uppercase
		pre40Template = pre40Template.toUpperCase();
		
		if (newOrderTransactions.contains(pre40Template)) {
			
			template = parser.loadTemplate(RequestIF.NEW_ORDER_TRANSACTION, false);
			
			template.setField("IndustryType", getIndustryType (pre40Template));
			template.setField("MessageType", getDefaultMessageType (pre40Template));
			
			if (pre40Template.equalsIgnoreCase(RequestIF.PC2_AUTH)) {
				template.setField("CardSecVal", "705");
			}
			
			// ECP Defaults
			if ((pre40Template.equalsIgnoreCase(RequestIF.ECP_AUTH))
				|| (pre40Template.equalsIgnoreCase(RequestIF.ECP_CAPTURE))
				|| (pre40Template.equalsIgnoreCase(RequestIF.ECP_FORCE_DEPOSIT))
				|| (pre40Template.equalsIgnoreCase(RequestIF.ECP_REFUND))){
				
				// set the card brand
				template.setField("CardBrand", "EC");
				
				if (!pre40Template.equalsIgnoreCase(RequestIF.ECP_CAPTURE)) {
					template.setField("BankPmtDelv", "B");
					template.setField("BankAccountType", "C");
				}
			}
			
			// Switch Solo Defaults
			if ((pre40Template.equalsIgnoreCase(RequestIF.SWITCH_SOLO_AUTH))
				|| (pre40Template.equalsIgnoreCase(RequestIF.SWITCH_SOLO_REFUND))
				|| (pre40Template.equalsIgnoreCase(RequestIF.SWITCH_SOLO_CAPTURE))){	
				
				// set the card brand
				template.setField("CardBrand", "SW");
				
				if (pre40Template.equalsIgnoreCase(RequestIF.SWITCH_SOLO_AUTH)) {
					template.setField("CurrencyCode", "826");
					template.setField("DebitCardStartDate", "1200");
					template.setField("DebitCardIssueNum", "1");	
				}
			}
			
		} else if (flexCacheTransactions.contains(pre40Template)) {
			
			template = parser.loadTemplate(RequestIF.FLEX_CACHE_TRANSACTION, false);
			
			// normal flexcache transactions, including batch should not require
			// any additional mapping, but FlexCache MFC will need to map the 
			// correct flexaction value
			if (pre40Template.equalsIgnoreCase(RequestIF.FLEXCACHE_MFC)) {
				template.setField("FlexAction", "RedemptionCompletion");
			}
			
		} else if (pre40Template.equalsIgnoreCase(RequestIF.BATCH_SETTLEMENT)) {
			
			// EOD should map directly over to new transaction
			template = parser.loadTemplate(RequestIF.END_OF_DAY_TRANSACTION, false);
			
		} else if (reversalTransactions.contains(pre40Template)) {
			
			// reversals (or voids) will map over directly
			template = parser.loadTemplate(RequestIF.REVERSE_TRANSACTION, false);
			
		} else  if (pre40Template.equalsIgnoreCase(RequestIF.PROFILE_MANAGEMENT)) {
			
			// nothing special to change here. The profile action is required in pre40
			// templates, so it will map over directly
			template = parser.loadTemplate(RequestIF.PROFILE_TRANSACTION, false);
			
		} else if (pre40Template.equalsIgnoreCase(RequestIF.CC_CAPTURE)
				|| pre40Template.equalsIgnoreCase(RequestIF.ECP_CAPTURE) 
				|| pre40Template.equalsIgnoreCase(RequestIF.SWITCH_SOLO_CAPTURE) ) {
			//  MFCs will map over directly
			template = parser.loadTemplate(RequestIF.MARK_FOR_CAPTURE_TRANSACTION, false);
		}
		
		return template;
	}
	
	private static String getIndustryType (String pre40Template) throws RequestConstructionException {
		// MO=08, RC=52, EC=59	
		
		// this is a big "if-else" latter just to keep things straight
		// setting the industry code is very important in mapping from
		// prePTI40 template to PTI40+ templates
		if (pre40Template.equalsIgnoreCase(RequestIF.CC_AUTHORIZE)) {
			return "EC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.CC_ECOMMERCE_REFUND)) {
			return "EC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.ECP_AUTH)) {
			return "EC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.ECP_CAPTURE)) {
			return "EC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.ECP_FORCE_DEPOSIT)) {
			return "EC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.ECP_REFUND)) {
			return "EC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.CC_FORCE)) {
			return "EC";	
		} else if (pre40Template.equalsIgnoreCase(RequestIF.PC2_AUTH)) {
			return "EC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.SWITCH_SOLO_AUTH)) {
			return "EC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.SWITCH_SOLO_REFUND)) {
			return "EC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.SWITCH_SOLO_CAPTURE)) {
			return "EC";			
		} else if (pre40Template.equalsIgnoreCase(RequestIF.CC_RECURRING_AUTH_CAPTURE)) {
			return "RC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.CC_RECURRING_REFUND)) {
			return "RC";	
		} else if (pre40Template.equalsIgnoreCase(RequestIF.PC2_RECURRING_AUTH_CAPTURE)) {
			return "RC";			
		} else if (pre40Template.equalsIgnoreCase(RequestIF.MOTO)) {
			return "MO";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.MOTO_REFUND)) {
			return "MO";
		}
		
		throw new RequestConstructionException ("Unable to map industry type for transaction: [" + pre40Template + "]");
	
	}
	
	private static String getDefaultMessageType (String pre40Template) throws RequestConstructionException {

		if (pre40Template.equalsIgnoreCase(RequestIF.CC_AUTHORIZE)) {
			return "A";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.CC_ECOMMERCE_REFUND)) {
			return "FR";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.ECP_AUTH)) {
			return "AC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.ECP_CAPTURE)) {
			return "C";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.ECP_FORCE_DEPOSIT)) {
			return "AC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.ECP_REFUND)) {
			return "FR";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.CC_FORCE)) {
			return "AC";	
		} else if (pre40Template.equalsIgnoreCase(RequestIF.PC2_AUTH)) {
			return "A";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.SWITCH_SOLO_AUTH)) {
			return "AC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.SWITCH_SOLO_REFUND)) {
			return "FR";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.SWITCH_SOLO_CAPTURE)) {
			return "C";			
		} else if (pre40Template.equalsIgnoreCase(RequestIF.CC_RECURRING_AUTH_CAPTURE)) {
			return "AC";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.CC_RECURRING_REFUND)) {
			return "FR";	
		} else if (pre40Template.equalsIgnoreCase(RequestIF.PC2_RECURRING_AUTH_CAPTURE)) {
			return "AC";			
		} else if (pre40Template.equalsIgnoreCase(RequestIF.MOTO)) {
			return "A";
		} else if (pre40Template.equalsIgnoreCase(RequestIF.MOTO_REFUND)) {
			return "FR";
		}
		
		throw new RequestConstructionException ("Unable to map default message type for transaction: [" + pre40Template + "]");
	
	}	
	
}


/* $Log:   //sslmfile2/public/pvcs/PaymentechSDK/Java/JavaSDK/src/com/paymentech/orbital/sdk/pre40/TransactionMapper.java-arc  $
// 
//    Rev 1.3   Apr 20 2011 08:45:08   rbhaskha
// removed apache JCL ref
// 
//    Rev 1.2   Feb 07 2007 08:04:56   byounie
// Changed copyright
// 
//    Rev 1.1   Dec 04 2006 08:42:48   rbhaskha
// removed ECP_CAPTURE & SWITCH_SOLO_CAPTURE transaction from new order to MFC block
*/
