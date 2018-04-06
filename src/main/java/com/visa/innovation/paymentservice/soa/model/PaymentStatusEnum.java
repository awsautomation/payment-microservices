package com.visa.innovation.paymentservice.soa.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PaymentStatusEnum {

	// active | expired | pending | captured | settled | refunded | voided
	EXPIRED("expired"), 
	PENDING_CAPTURE("pending capture"),
	PENDING_SETTLEMENT("pending settlement"),
	SETTLED("settled"), 
	REFUNDED("refunded"), 
	VOIDED("voided"),
	REVERSED("reversed");

	private String paymentStatus;

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	PaymentStatusEnum(String status) {
		setPaymentStatus(status);
	}

	@Override
	public String toString() {
		return paymentStatus;
	}

	@JsonCreator
	public static PaymentStatusEnum fromText(String status) {
		for (PaymentStatusEnum paymentStatus : PaymentStatusEnum.values()) {
			if (paymentStatus.getPaymentStatus().equalsIgnoreCase(status)) {
				return paymentStatus;
			}
		}
		throw new IllegalArgumentException();
	}

}
