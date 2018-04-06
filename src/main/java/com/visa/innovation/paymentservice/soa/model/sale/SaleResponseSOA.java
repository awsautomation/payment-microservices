package com.visa.innovation.paymentservice.soa.model.sale;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.visa.innovation.paymentservice.soa.model.BasePaymentResponseSOA;
import com.visa.innovation.paymentservice.soa.model.auth.SubscriptionResponseAuthSOA;
import com.visa.innovation.paymentservice.soa.model.capture.SubscriptionResponseCaptureSOA;

public class SaleResponseSOA extends BasePaymentResponseSOA {
	
	private SubscriptionResponseAuthSOA ccAuthReply;
	private SubscriptionResponseCaptureSOA ccCaptureReply;
	
	public SaleResponseSOA() {
	
	}

	@JacksonXmlProperty(localName="ccAuthReply")
	public SubscriptionResponseAuthSOA getCcAuthReply() {
		return ccAuthReply;
	}

	public void setCcAuthReply(SubscriptionResponseAuthSOA ccAuthReply) {
		this.ccAuthReply = ccAuthReply;
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
		return "SaleResponseSOA [ccAuthReply=" + ccAuthReply + ", ccCaptureReply=" + ccCaptureReply + "]" + super.toString();
	}

}
