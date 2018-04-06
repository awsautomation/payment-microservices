package com.visa.innovation.paymentservice.soa.model.auth;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.visa.innovation.paymentservice.soa.model.BasePaymentResponseSOA;

public class AuthResponseSOA extends BasePaymentResponseSOA {

	private SubscriptionResponseAuthSOA ccAuthReply;
	
	public AuthResponseSOA() {

	}

	public AuthResponseSOA(SubscriptionResponseAuthSOA ccAuthReply) {
		this.ccAuthReply = ccAuthReply;
	}

	@JacksonXmlProperty(localName="ccAuthReply")
	public SubscriptionResponseAuthSOA getCcAuthReply() {
		return ccAuthReply;
	}

	public void setCcAuthReply(SubscriptionResponseAuthSOA ccAuthReply) {
		this.ccAuthReply = ccAuthReply;
	}

	@Override
	public String toString() {
		return "AuthResponseSOA [ccAuthReply=" + ccAuthReply + "]" + super.toString();
	}
	
}
