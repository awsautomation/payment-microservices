package com.visa.innovation.paymentservice.soa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class BaseSubscriptionResponseSOA {

	private int reasonCode;
	private String amount;
	private String authorizationCode;
	private String avsCode;
	private String avsCodeRaw;
	private String processorResponse;
	private String reconciliationId;
	private String ownerMerchantId;
	
	public BaseSubscriptionResponseSOA() {
		
	}
	@JacksonXmlProperty(localName="reasonCode")
	public int getReasonCode() {
		return reasonCode;
	}
	
	public void setReasonCode(int reasonCode) {
		this.reasonCode = reasonCode;
	}
	
	@JacksonXmlProperty(localName="amount")
	public String getAmount() {
		return amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	@JacksonXmlProperty(localName="authorizationCode")
	public String getAuthorizationCode() {
		return authorizationCode;
	}
	
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}
	
	@JacksonXmlProperty(localName="avsCode")
	public String getAvsCode() {
		return avsCode;
	}
	
	public void setAvsCode(String avsCode) {
		this.avsCode = avsCode;
	}
	
	@JacksonXmlProperty(localName="avsCodeRaw")
	public String getAvsCodeRaw() {
		return avsCodeRaw;
	}
	
	public void setAvsCodeRaw(String avsCodeRaw) {
		this.avsCodeRaw = avsCodeRaw;
	}
	
	@JacksonXmlProperty(localName="processorResponse")
	public String getProcessorResponse() {
		return processorResponse;
	}
	
	public void setProcessorResponse(String processorResponse) {
		this.processorResponse = processorResponse;
	}
	
	@JacksonXmlProperty(localName="reconciliationId")
	public String getReconciliationId() {
		return reconciliationId;
	}
	
	public void setReconciliationId(String reconciliationId) {
		this.reconciliationId = reconciliationId;
	}
	
	@JacksonXmlProperty(localName="ownerMerchantId")
	public String getOwnerMerchantId() {
		return ownerMerchantId;
	}
	
	public void setOwnerMerchantId(String ownerMerchantId) {
		this.ownerMerchantId = ownerMerchantId;
	}
	@Override
	public String toString() {
		return "SubscriptionResponseSOA [reasonCode=" + reasonCode + ", amount=" + amount + ", authorizationCode="
				+ authorizationCode + ", avsCode=" + avsCode + ", avsCodeRaw=" + avsCodeRaw + ", processorResponse="
				+ processorResponse + ", reconciliationId=" + reconciliationId + ", ownerMerchantId=" + ownerMerchantId
				+ "]";
	}

}
