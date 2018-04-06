package com.visa.innovation.paymentservice.vdp.model.refund;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRefundRequestVDP {
	
	String amount;
	String currency;
	String referenceId;
	
	public CreateRefundRequestVDP() {

	}

	public CreateRefundRequestVDP(String amount, String currency, String referenceId) {
		this.amount = amount;
		this.currency = currency;
		this.referenceId = referenceId;
	}

	@JsonProperty("amount")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@JsonProperty("currency")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@JsonProperty("referenceId")
	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	@Override
	public String toString() {
		return "RefundRequestVDP [amount=" + amount + ", currency=" + currency + ", referenceId=" + referenceId + "]";
	}

}
