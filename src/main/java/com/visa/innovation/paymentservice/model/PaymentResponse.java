package com.visa.innovation.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class PaymentResponse {

	String type;
	String accountNumber;
	String name;
	Expiry expiry;

	public PaymentResponse() {

	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("account_number")
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("expiry")
	public Expiry getExpiry() {
		return expiry;
	}

	public void setExpiry(Expiry expiry) {
		this.expiry = expiry;
	}

	@Override
	public String toString() {
		return "{" + "\"type\"" + ":" + "\"" + type + "\"" + "," + "\"account_number\"" + ":" + "\"" + accountNumber
				+ "\"" + "," + "\"name\"" + ":" + "\"" + name + "\"" + "," + "\"expiry\"" + ":" + expiry + "}";
	}

	public static class Builder {
		private String type;
		private String accountNumber;
		private String name;
		private Expiry expiry;

		public Builder type(String type) {
			this.type = type;
			return this;
		}

		public Builder accountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder expiry(Expiry expiry) {
			this.expiry = expiry;
			return this;
		}

		public PaymentResponse build() {
			return new PaymentResponse(this);
		}
	}

	private PaymentResponse(Builder builder) {
		this.type = builder.type;
		this.accountNumber = builder.accountNumber;
		this.name = builder.name;
		this.expiry = builder.expiry;
	}
}
