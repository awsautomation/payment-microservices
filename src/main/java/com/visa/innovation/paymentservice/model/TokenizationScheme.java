package com.visa.innovation.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TokenizationScheme {

	CYBERSOURCE("cybersource"), STRIPE("stripe");

	String value;

	private TokenizationScheme(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

	@JsonCreator
	public static TokenizationScheme fromText(String text) {
		for (TokenizationScheme scheme : TokenizationScheme.values()) {
			if (scheme.getValue().equalsIgnoreCase(text)) {
				return scheme;
			}
		}
		throw new IllegalArgumentException();
	}

}
