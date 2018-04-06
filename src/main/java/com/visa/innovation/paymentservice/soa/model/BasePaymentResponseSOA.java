package com.visa.innovation.paymentservice.soa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "replyMessage")
@JsonInclude(Include.NON_NULL)
public class BasePaymentResponseSOA {

	private String merchantReferenceCode;
	private String requestID;
	private String decision;
	private int reasonCode;
	private String requestToken;
	private String missingField;
	private PurchaseTotalsSOA purchaseTotals;
	private String invalidField;
	
	@JacksonXmlProperty(localName="invalidField")
	public String getInvalidField() {
		return invalidField;
	}

	public void setInvalidField(String invalidField) {
		this.invalidField = invalidField;
	}

	@JacksonXmlProperty(localName="missingField")
	public String getMissingField() {
		return missingField;
	}

	public void setMissingField(String missingField) {
		this.missingField = missingField;
	}

	@JacksonXmlProperty(localName="merchantReferenceCode")
	public String getMerchantReferenceCode() {
		return merchantReferenceCode;
	}

	public void setMerchantReferenceCode(String merchantReferenceCode) {
		this.merchantReferenceCode = merchantReferenceCode;
	}

	@JacksonXmlProperty(localName="requestID")
	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	@JacksonXmlProperty(localName="decision")
	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	@JacksonXmlProperty(localName="reasonCode")
	public int getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(int reasonCode) {
		this.reasonCode = reasonCode;
	}

	@JacksonXmlProperty(localName="requestToken")
	public String getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	@JacksonXmlProperty(localName="purchaseTotals")
	public PurchaseTotalsSOA getPurchaseTotals() {
		return purchaseTotals;
	}

	public void setPurchaseTotals(PurchaseTotalsSOA purchaseTotals) {
		this.purchaseTotals = purchaseTotals;
	}

	@Override
	public String toString() {
		return "BasePaymentResponseSOA [merchantReferenceCode=" + merchantReferenceCode + ", requestID=" + requestID
				+ ", decision=" + decision + ", reasonCode=" + reasonCode + ", requestToken=" + requestToken
				+ ", missingField=" + missingField + ", purchaseTotals=" + purchaseTotals + "]";
	}

}
