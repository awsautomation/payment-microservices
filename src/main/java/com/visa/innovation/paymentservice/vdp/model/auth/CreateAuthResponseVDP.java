package com.visa.innovation.paymentservice.vdp.model.auth;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visa.innovation.paymentservice.model.Address;
import com.visa.innovation.paymentservice.vdp.model.PaymentVDP;

//{
//	  "id" : "4900514997926235104103",
//	  "status" : "PendingCapture",
//	  "amount" : 0.00,
//	  "currency" : "USD",
//	  "authCode" : "888888",
//	  "referenceId" : "1490051499781",
//	  "payment" : {
//	    "cardNumber" : "xxxxxxxxxxxx1111",
//	    "cardExpirationMonth" : "10",
//	    "cardExpirationYear" : "2020"
//	  },
//	  "_links" : {
//	    "self" : {
//	      "href" : "https://sandbox.api.visa.com/cybersource/payments/v1/authorizations/4900514997926235104103",
//	      "method" : "GET"
//	    },
//	    "capture" : [ {
//	      "href" : "https://sandbox.api.visa.com/cybersource/payments/v1/authorizations/4900514997926235104103/captures",
//	      "method" : "POST"
//	    } ],
//	    "reversal" : [ {
//	      "href" : "https://sandbox.api.visa.com/cybersource/payments/v1/authorizations/4900514997926235104103/reversals",
//	      "method" : "POST"
//	    } ]
//	  }
//	}


/**
 * 
 * Response from VDP for get auth by id
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateAuthResponseVDP {

	String id;
	String status;
	String amount;
	String authCode;
	String referenceId;
	String currency;
	PaymentVDP payment;
	HashMap<String, String> merchantDefinedData;
	Address billTo;
	
	public CreateAuthResponseVDP() {
		
	}

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("amount")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@JsonProperty("authCode")
	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	@JsonProperty("referenceId")
	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	@JsonProperty("payment")
	public PaymentVDP getPayment() {
		return payment;
	}

	public void setPayment(PaymentVDP payment) {
		this.payment = payment;
	}

	@JsonProperty("merchantDefinedData")
	public HashMap<String, String> getMerchantDefinedData() {
		return merchantDefinedData;
	}

	public void setMerchantDefinedData(HashMap<String, String> merchantDefinedData) {
		this.merchantDefinedData = merchantDefinedData;
	}

	@JsonProperty("currency")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@JsonProperty("billTo")
	public Address getBillTo() {
		return billTo;
	}

	public void setBillTo(Address billTo) {
		this.billTo = billTo;
	}

	@Override
	public String toString() {
		return "AuthResponseVDP [id=" + id + ", status=" + status + ", amount=" + amount + ", authCode=" + authCode
				+ ", referenceId=" + referenceId + ", currency=" + currency + ", payment=" + payment
				+ ", merchantDefinedData=" + merchantDefinedData + ", billTo=" + billTo + "]";
	}
	
}
