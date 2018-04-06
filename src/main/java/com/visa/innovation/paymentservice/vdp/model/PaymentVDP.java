package com.visa.innovation.paymentservice.vdp.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Used in several requests to VDP
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentVDP {

	String cardNumber;
	String expirationMonth;
	String expirationYear;
	String cardType;

	public PaymentVDP() {

	}

	@NotNull(message = "cardNumber is required")
	@JsonProperty("cardNumber")
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	@NotNull(message = "cardExpirationMonth is required")
	@JsonProperty("cardExpirationMonth")
	public String getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	@NotNull(message = "cardExpirationYear is required")
	@JsonProperty("cardExpirationYear")
	public String getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}

	@JsonProperty("cardType")
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public static class Builder {
		private String cardNumber;
		private String expirationMonth;
		private String expirationYear;
		private String cardType;

		public Builder cardNumber(String cardNumber) {
			this.cardNumber = cardNumber;
			return this;
		}

		public Builder expirationMonth(String expirationMonth) {
			this.expirationMonth = expirationMonth;
			return this;
		}

		public Builder expirationYear(String expirationYear) {
			this.expirationYear = expirationYear;
			return this;
		}

		public Builder cardType(String cardType) {
			this.cardType = cardType;
			return this;
		}

		public PaymentVDP build() {
			return new PaymentVDP(this);
		}
	}

	private PaymentVDP(Builder builder) {
		this.cardNumber = builder.cardNumber;
		this.expirationMonth = builder.expirationMonth;
		this.expirationYear = builder.expirationYear;
		this.cardType = builder.cardType;
	}

	@Override
	public String toString() {
		return "PaymentVDP [cardNumber=" + cardNumber + ", expirationMonth=" + expirationMonth + ", expirationYear="
				+ expirationYear + ", cardType=" + cardType + "]";
	}
	
}
