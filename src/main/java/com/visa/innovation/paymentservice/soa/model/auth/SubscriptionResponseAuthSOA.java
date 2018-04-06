package com.visa.innovation.paymentservice.soa.model.auth;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.visa.innovation.paymentservice.soa.model.BaseSubscriptionResponseSOA;

public class SubscriptionResponseAuthSOA extends BaseSubscriptionResponseSOA {

	private String createdAt;
	
	public SubscriptionResponseAuthSOA() {

	}

	public SubscriptionResponseAuthSOA(String createdAt) {
		this.createdAt = createdAt;
	}

	@JacksonXmlProperty(localName="authorizedDateTime")
	public String getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "SubscriptionResponseAuthSOA [createdAt=" + createdAt + "]";
	}
	
}
