package com.visa.innovation.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UpdateStatus {

	VOID("void");
	
	String value;
	
	private UpdateStatus(String value) {
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
	public static UpdateStatus fromText(String text) {
		for (UpdateStatus status : UpdateStatus.values()) {
			if (status.getValue().equalsIgnoreCase(text)) {
				return status;
			}
		}
		throw new IllegalArgumentException();
	}
}
