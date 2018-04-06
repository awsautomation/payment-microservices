package com.visa.innovation.paymentservice.model.refund;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visa.innovation.paymentservice.model.BasePaymentRequest;

public class RefundRequest extends BasePaymentRequest{

	String captureId;
	String saleId;
	
	public RefundRequest() {
		
	}

	@JsonProperty("capture_id")
	public String getCaptureId() {
		return captureId;
	}

	public void setCaptureId(String captureId) {
		this.captureId = captureId;
	}

	@JsonProperty("sale_id")
	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	@Override
	public String toString() {
		return "RefundRequest [captureId=" + captureId + ", saleId=" + saleId + "]" + super.toString();
	}
	
	
}
