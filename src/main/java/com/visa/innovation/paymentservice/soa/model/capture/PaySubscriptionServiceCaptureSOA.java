package com.visa.innovation.paymentservice.soa.model.capture;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.visa.innovation.paymentservice.soa.model.PaySubscriptionServiceSOA;

public class PaySubscriptionServiceCaptureSOA extends PaySubscriptionServiceSOA {
	
	String authRequestId;

	public PaySubscriptionServiceCaptureSOA(String run, String authRequestId) {
		super(run);
		this.authRequestId = authRequestId;
	}

	public PaySubscriptionServiceCaptureSOA() {
		
	}

	@JacksonXmlProperty(localName="authRequestID")
	public String getAuthRequestId() {
		return authRequestId;
	}

	public void setAuthRequestId(String authRequestId) {
		this.authRequestId = authRequestId;
	}

	@Override
	public String toString() {
		return "PaySubscriptionServiceCaptureSOA [authRequestId=" + authRequestId + "]" + super.toString();
	}
	
	

}
