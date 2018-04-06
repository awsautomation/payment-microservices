package com.visa.innovation.paymentservice.model.auth;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visa.innovation.paymentservice.model.Address;
import com.visa.innovation.paymentservice.model.BasePaymentRequest;
import com.visa.innovation.paymentservice.model.PaymentRequest;

public class AuthRequest extends BasePaymentRequest {

	String merchantId;
	PaymentRequest payment;

	public AuthRequest() {

	}
	
	public AuthRequest(String merchantId, PaymentRequest payment) {
		super();
		this.merchantId = merchantId;
		this.payment = payment;
	}
	
	public AuthRequest(String orderId, Double amount, String currency, Address shippingAddress,
			Address billingAddress, String merchantId, PaymentRequest payment) {
		super(orderId, amount, currency, shippingAddress, billingAddress);
		this.merchantId = merchantId;
		this.payment = payment;
	}

	@JsonProperty("merchant_id")
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	@NotNull(message = "payment should not be blank")
	@JsonProperty("payment")
	public PaymentRequest getPayment() {
		return payment;
	}

	public void setPayment(PaymentRequest payment) {
		this.payment = payment;
	}

	@Override
	public String toString() {
		return "AuthRequest [merchantId=" + merchantId + ", payment=" + payment + "]" + super.toString();
	}

}
