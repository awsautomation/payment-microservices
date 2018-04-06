package com.visa.innovation.paymentservice.vdp.model.tokenization;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visa.innovation.paymentservice.vdp.model.CardDetailsVDP;

/**
 * Token request to VDP
 *
 *
 */
public class TokenRequestVDP {

	String keyId;
	CardDetailsVDP cardDetailsVDP;

	public TokenRequestVDP() {

	}

	@JsonProperty("keyId")
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@JsonProperty("cardInfo")
	public CardDetailsVDP getCardDetailsVDP() {
		return cardDetailsVDP;
	}

	public void setCardDetailsVDP(CardDetailsVDP cardDetailsVDP) {
		this.cardDetailsVDP = cardDetailsVDP;
	}

	public static class Builder {
		private String keyId;
		private CardDetailsVDP cardDetailsVDP;

		public Builder keyId(String keyId) {
			this.keyId = keyId;
			return this;
		}

		public Builder cardDetailsVDP(CardDetailsVDP cardDetailsVDP) {
			this.cardDetailsVDP = cardDetailsVDP;
			return this;
		}

		public TokenRequestVDP build() {
			return new TokenRequestVDP(this);
		}
	}

	private TokenRequestVDP(Builder builder) {
		this.keyId = builder.keyId;
		this.cardDetailsVDP = builder.cardDetailsVDP;
	}

	@Override
	public String toString() {
		return "TokenRequestVDP [keyId=" + keyId + ", cardDetailsVDP=" + cardDetailsVDP + "]";
	}
	
}
