package com.visa.innovation.paymentservice.soa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class PurchaseTotalsSOA {
	
	private String currency;
	private String grandTotalAmount;
	
	public PurchaseTotalsSOA() {

	}

	public PurchaseTotalsSOA(String currency, String amount) {
		this.currency = currency;
		this.grandTotalAmount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getGrandTotalAmount() {
		return grandTotalAmount;
	}

	public void setGrandTotalAmount(String amount) {
		this.grandTotalAmount = amount;
	}

	@Override
	public String toString() {
		return "PurchaseTotalsSOA [currency=" + currency + ", grandTotalAmount=" + grandTotalAmount + "]";
	}

}
