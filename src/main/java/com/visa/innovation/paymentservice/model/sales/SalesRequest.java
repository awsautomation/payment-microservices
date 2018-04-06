package com.visa.innovation.paymentservice.model.sales;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visa.innovation.paymentservice.model.BasePaymentRequest;
import com.visa.innovation.paymentservice.model.PaymentRequest;

public class SalesRequest extends BasePaymentRequest {

	String merchantId;
	PaymentRequest payment;

	public SalesRequest() {

	}

	@JsonProperty("merchant_id")
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	@NotNull(message="payment should not be blank")
	@JsonProperty("payment")
	public PaymentRequest getPayment() {
		return payment;
	}

	public void setPayment(PaymentRequest payment) {
		this.payment = payment;
	}

	@Override
	public String toString() {
		return "SalesRequest [merchantId=" + merchantId + ", payment=" + payment + "]";
	}

}
