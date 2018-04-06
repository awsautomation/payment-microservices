package com.visa.innovation.paymentservice.soa.model.auth;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.visa.innovation.paymentservice.soa.model.BasePaymentRequestSOA;
import com.visa.innovation.paymentservice.soa.model.PaySubscriptionServiceSOA;

public class AuthRequestSOA extends BasePaymentRequestSOA {

	private PaySubscriptionServiceSOA ccAuthService;

	public AuthRequestSOA() {
	
	}

	public AuthRequestSOA(PaySubscriptionServiceSOA ccAuthService) {
		this.ccAuthService = ccAuthService;
	}

	@JacksonXmlProperty(localName="ccAuthService")
	public PaySubscriptionServiceSOA getCcAuthService() {
		return ccAuthService;
	}

	public void setCcAuthService(PaySubscriptionServiceSOA ccAuthService) {
		this.ccAuthService = ccAuthService;
	}

	@Override
	public String toString() {
		return "AuthRequestSOA [ccAuthService=" + ccAuthService + "]" + super.toString();
	}
}
