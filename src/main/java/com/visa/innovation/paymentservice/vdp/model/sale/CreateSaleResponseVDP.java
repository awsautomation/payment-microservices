package com.visa.innovation.paymentservice.vdp.model.sale;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.visa.innovation.paymentservice.model.Address;
import com.visa.innovation.paymentservice.vdp.model.PaymentVDP;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateSaleResponseVDP {
	
	String id;
	String status;
	String amount;
	String authCode;
	String referenceId;
	PaymentVDP payment;
	HashMap<String, String> merchantDefinedData;
	Address billTo;
	
	
	public CreateSaleResponseVDP() {

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

	@Override
	public String toString() {
		return "SaleResponseVDP [id=" + id + ", status=" + status + ", amount=" + amount + ", authCode=" + authCode
				+ ", referenceId=" + referenceId + ", payment=" + payment + ", merchantDefinedData="
				+ merchantDefinedData + "]";
	}

}
