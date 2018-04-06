package com.visa.innovation.paymentservice.soa.model.capture;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.visa.innovation.paymentservice.soa.model.BasePaymentRequestSOA;

public class CaptureRequestSOA extends BasePaymentRequestSOA {

	private PaySubscriptionServiceCaptureSOA ccCaptureService;

	public CaptureRequestSOA() {
	}

	public CaptureRequestSOA(PaySubscriptionServiceCaptureSOA ccCaptureService) {
		this.ccCaptureService = ccCaptureService;
	}

	@JacksonXmlProperty(localName="ccCaptureService")
	public PaySubscriptionServiceCaptureSOA getCcCaptureService() {
		return ccCaptureService;
	}

	public void setCcCaptureService(PaySubscriptionServiceCaptureSOA ccCaptureService) {
		this.ccCaptureService = ccCaptureService;
	}

	@Override
	public String toString() {
		return "CaptureRequestSOA [ccCaptureService=" + ccCaptureService + "]";
	}
	
}
