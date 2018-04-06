package com.visa.innovation.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Expiry {

	String expiryMonth;
	String expiryYear;

	public Expiry() {

	}

	public Expiry(String expiryMonth, String expiryYear) {
		super();
		this.expiryMonth = expiryMonth;
		this.expiryYear = expiryYear;
	}

	@JsonProperty("month")
	public String getExpiryMonth() {
		return expiryMonth;
	}

	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}

	@JsonProperty("year")
	public String getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}

	@Override
	public String toString() {
		return "{" +"\"expiry_month\"" + ":" + expiryMonth + "," + "\"expiry_year\"" + ":" + expiryYear + "}";
	}

}
