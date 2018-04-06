package com.visa.innovation.paymentservice.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BasePaymentRequest {

	private String orderId;
	private Double amount;
	private String currency;
	private Address shippingAddress;
	private Address billingAddress;

	
	public BasePaymentRequest() {
		
	}

	public BasePaymentRequest(String orderId, Double amount, String currency, Address shippingAddress,
			Address billingAddress) {
		super();
		this.orderId = orderId;
		this.amount = amount;
		this.currency = currency;
		this.shippingAddress = shippingAddress;
		this.billingAddress = billingAddress;
	}

	@JsonProperty("order_id")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@NotNull(message = "amount is required")
	@JsonProperty("amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@NotNull(message = "currency is required")
	@JsonProperty("currency")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@JsonProperty("shipping_address")
	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@Valid
	@JsonProperty("billing_address")
	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	@Override
	public String toString() {
		return "BasePaymentRequest [orderId=" + orderId + ", amount=" + amount + ", currency=" + currency
				+ ", shippingAddress=" + shippingAddress + ", billingAddress=" + billingAddress + "]";
	}

	public static class Builder {
		private String orderId;
		private Double amount;
		private String currency;
		private Address shippingAddress;
		private Address billingAddress;

		public Builder orderId(String orderId) {
			this.orderId = orderId;
			return this;
		}

		public Builder amount(Double amount) {
			this.amount = amount;
			return this;
		}

		public Builder currency(String currency) {
			this.currency = currency;
			return this;
		}

		public Builder shippingAddress(Address shippingAddress) {
			this.shippingAddress = shippingAddress;
			return this;
		}

		public Builder billingAddress(Address billingAddress) {
			this.billingAddress = billingAddress;
			return this;
		}

		public BasePaymentRequest build() {
			return new BasePaymentRequest(this);
		}
	}

	private BasePaymentRequest(Builder builder) {
		this.orderId = builder.orderId;
		this.amount = builder.amount;
		this.currency = builder.currency;
		this.shippingAddress = builder.shippingAddress;
		this.billingAddress = builder.billingAddress;
	}
}
