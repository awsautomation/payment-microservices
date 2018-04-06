package com.visa.innovation.paymentservice.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class PaymentRequest {

	String token;
	TokenizationScheme tokenizationScheme;
	Expiry expiry;

	public PaymentRequest() {

	}

	@NotNull
	@JsonProperty("token")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@NotNull
	@JsonProperty("tokenization_scheme")
	public TokenizationScheme getTokenizationScheme() {
		return tokenizationScheme;
	}

	public void setTokenizationScheme(TokenizationScheme tokenizationScheme) {
		this.tokenizationScheme = tokenizationScheme;
	}

	@NotNull
	@JsonProperty("expiry")
	public Expiry getExpiry() {
		return expiry;
	}

	public void setExpiry(Expiry expiry) {
		this.expiry = expiry;
	}

	@Override
	public String toString() {
		return "Payment [token=" + token + ", tokenizationScheme=" + tokenizationScheme + ", expiry=" + expiry + "]";
	}

	public static class Builder {
		private String token;
		private TokenizationScheme tokenizationScheme;
		private Expiry expiry;

		public Builder token(String token) {
			this.token = token;
			return this;
		}

		public Builder tokenizationScheme(TokenizationScheme tokenizationScheme) {
			this.tokenizationScheme = tokenizationScheme;
			return this;
		}

		public Builder expiry(Expiry expiry) {
			this.expiry = expiry;
			return this;
		}

		public PaymentRequest build() {
			return new PaymentRequest(this);
		}
	}

	private PaymentRequest(Builder builder) {
		this.token = builder.token;
		this.tokenizationScheme = builder.tokenizationScheme;
		this.expiry = builder.expiry;
	}
}
