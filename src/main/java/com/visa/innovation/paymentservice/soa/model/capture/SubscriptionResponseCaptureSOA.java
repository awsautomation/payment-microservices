package com.visa.innovation.paymentservice.soa.model.capture;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.visa.innovation.paymentservice.soa.model.BaseSubscriptionResponseSOA;

public class SubscriptionResponseCaptureSOA extends BaseSubscriptionResponseSOA {

	private String createdAt;

	public SubscriptionResponseCaptureSOA() {

	}

	@JacksonXmlProperty(localName="requestDateTime")
	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "SubscriptionResponseCaptureSOA [createdAt=" + createdAt + "]" + super.toString();
	}
	
	
}
