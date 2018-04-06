package com.visa.innovation.paymentservice.soa.model.sale;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.visa.innovation.paymentservice.soa.model.BasePaymentRequestSOA;
import com.visa.innovation.paymentservice.soa.model.PaySubscriptionServiceSOA;

public class SaleRequestSOA extends BasePaymentRequestSOA{

	private PaySubscriptionServiceSOA ccAuthService;
	private PaySubscriptionServiceSOA ccCaptureService;
	
	public SaleRequestSOA() {
	
	}

	public SaleRequestSOA(PaySubscriptionServiceSOA ccAuthService, PaySubscriptionServiceSOA ccCaptureService) {
		this.ccAuthService = ccAuthService;
		this.ccCaptureService = ccCaptureService;
	}

	@JacksonXmlProperty(localName="ccAuthService")
	public PaySubscriptionServiceSOA getCcAuthService() {
		return ccAuthService;
	}

	public void setCcAuthService(PaySubscriptionServiceSOA ccAuthService) {
		this.ccAuthService = ccAuthService;
	}

	@JacksonXmlProperty(localName="ccCaptureService")
	public PaySubscriptionServiceSOA getCcCaptureService() {
		return ccCaptureService;
	}

	public void setCcCaptureService(PaySubscriptionServiceSOA ccCaptureService) {
		this.ccCaptureService = ccCaptureService;
	}

	@Override
	public String toString() {
		return "SaleRequestSOA [ccAuthService=" + ccAuthService + ", ccCaptureService=" + ccCaptureService + "]" + super.toString();
	}
	
}
