package com.visa.innovation.paymentservice.soa.model.capture;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.visa.innovation.paymentservice.soa.model.BasePaymentResponseSOA;

public class CaptureResponseSOA extends BasePaymentResponseSOA{
	
	private SubscriptionResponseCaptureSOA ccCaptureReply;

	public CaptureResponseSOA() {
	
	}

	@JacksonXmlProperty(localName="ccCaptureReply")
	public SubscriptionResponseCaptureSOA getCcCaptureReply() {
		return ccCaptureReply;
	}

	public void setCcCaptureReply(SubscriptionResponseCaptureSOA ccCaptureReply) {
		this.ccCaptureReply = ccCaptureReply;
	}

	@Override
	public String toString() {
		return "CaptureResponseSOA [ccCaptureReply=" + ccCaptureReply + "]" + super.toString();
	}
	
}
