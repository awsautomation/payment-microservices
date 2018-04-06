package com.visa.innovation.paymentservice.model;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TransactionType {

	SALE("sale"), CAPTURE("capture");

	String value;

	private TransactionType(String value) {
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
	public static TransactionType fromText(String text) {
		
		 Optional<TransactionType> result = Arrays.stream(TransactionType.values())
			        .filter(type -> type.getValue().equalsIgnoreCase(text)).findAny();
			    if (result.isPresent()) {
			      return result.get();
			    }
			    throw new IllegalArgumentException();
			    
//		for (TransactionType type : TransactionType.values()) {
//			if (type.getValue().equalsIgnoreCase(text)) {
//				return type;
//			}
//		}
//		throw new IllegalArgumentException();
	}
}
