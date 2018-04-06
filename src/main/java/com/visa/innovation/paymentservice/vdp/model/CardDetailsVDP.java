package com.visa.innovation.paymentservice.vdp.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used in several requests to VDP
 * 
 *
 */
public class CardDetailsVDP {

	String cardNumber;
	String cardType;
	String cardExpirationMonth;
	String cardExpirationYear;

	public CardDetailsVDP() {

	}

	@NotNull(message="cardNumber is required")
	@JsonProperty("cardNumber")
	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	@JsonProperty("cardType")
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	@JsonProperty("getCardExpirationMonth")
	public String getCardExpirationMonth() {
		return cardExpirationMonth;
	}

	public void setCardExpirationMonth(String cardExpirationMonth) {
		this.cardExpirationMonth = cardExpirationMonth;
	}

	@JsonProperty("getCardExpirationYear")
	public String getCardExpirationYear() {
		return cardExpirationYear;
	}

	public void setCardExpirationYear(String cardExpirationYear) {
		this.cardExpirationYear = cardExpirationYear;
	}

	@Override
	public String toString() {
		return "CardDetailsVDP [cardNumber=" + cardNumber + ", cardType=" + cardType + "]";
	}

	public static class Builder {
		private String cardNumber;
		private String cardType;
		private String cardExpirationMonth;
		private String cardExpirationYear;

		public Builder cardNumber(String cardNumber) {
			this.cardNumber = cardNumber;
			return this;
		}

		public Builder cardType(String cardType) {
			this.cardType = cardType;
			return this;
		}

		public Builder cardExpirationMonth(String cardExpirationMonth) {
			this.cardExpirationMonth = cardExpirationMonth;
			return this;
		}

		public Builder cardExpirationYear(String cardExpirationYear) {
			this.cardExpirationYear = cardExpirationYear;
			return this;
		}

		public CardDetailsVDP build() {
			return new CardDetailsVDP(this);
		}
	}

	private CardDetailsVDP(Builder builder) {
		this.cardNumber = builder.cardNumber;
		this.cardType = builder.cardType;
		this.cardExpirationMonth = builder.cardExpirationMonth;
		this.cardExpirationYear = builder.cardExpirationYear;
	}
}
