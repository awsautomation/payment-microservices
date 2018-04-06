package com.visa.innovation.paymentservice.vdp.model.sale;

import java.util.HashMap;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visa.innovation.paymentservice.vdp.model.AddressVDP;
import com.visa.innovation.paymentservice.vdp.model.PaymentVDP;

public class CreateSaleRequestVDP {

	PaymentVDP payment;
	String amount;
	String currency;
	String referenceId;
	HashMap<String, String> merchantDefinedData;
	AddressVDP billingAddress;
	AddressVDP shippingAddress;

	public CreateSaleRequestVDP() {

	}

	@NotNull(message = "cardExpirationYear is required")
	@JsonProperty("payment")
	public PaymentVDP getPayment() {
		return payment;
	}

	public void setPayment(PaymentVDP payment) {
		this.payment = payment;
	}

	@NotNull(message = "amount is required")
	@JsonProperty("amount")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
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

	@JsonProperty("referenceId")
	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	@JsonProperty("merchantDefinedData")
	public HashMap<String, String> getMerchantDefinedData() {
		return merchantDefinedData;
	}

	public void setMerchantDefinedData(HashMap<String, String> merchantDefinedData) {
		this.merchantDefinedData = merchantDefinedData;
	}

	@JsonProperty("billTo")
	public AddressVDP getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(AddressVDP billingAddress) {
		this.billingAddress = billingAddress;
	}

	@JsonProperty("shipTo")
	public AddressVDP getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(AddressVDP shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	@Override
	public String toString() {
		return "SaleRequestVDP [payment=" + payment + ", amount=" + amount + ", currency=" + currency + ", referenceId="
				+ referenceId + ", merchantDefinedData=" + merchantDefinedData + ", billingAddress=" + billingAddress
				+ ", shippingAddress=" + shippingAddress + "]";
	}

	public static class Builder {
		private PaymentVDP payment;
		private String amount;
		private String currency;
		private String referenceId;
		private HashMap<String, String> merchantDefinedData;
		private AddressVDP billingAddress;
		private AddressVDP shippingAddress;

		public Builder payment(PaymentVDP payment) {
			this.payment = payment;
			return this;
		}

		public Builder amount(String amount) {
			this.amount = amount;
			return this;
		}

		public Builder currency(String currency) {
			this.currency = currency;
			return this;
		}

		public Builder referenceId(String referenceId) {
			this.referenceId = referenceId;
			return this;
		}

		public Builder merchantDefinedData(HashMap<String, String> merchantDefinedData) {
			this.merchantDefinedData = merchantDefinedData;
			return this;
		}

		public Builder billingAddress(AddressVDP billingAddress) {
			this.billingAddress = billingAddress;
			return this;
		}

		public Builder shippingAddress(AddressVDP shippingAddress) {
			this.shippingAddress = shippingAddress;
			return this;
		}

		public CreateSaleRequestVDP build() {
			return new CreateSaleRequestVDP(this);
		}
	}

	private CreateSaleRequestVDP(Builder builder) {
		this.payment = builder.payment;
		this.amount = builder.amount;
		this.currency = builder.currency;
		this.referenceId = builder.referenceId;
		this.merchantDefinedData = builder.merchantDefinedData;
		this.billingAddress = builder.billingAddress;
		this.shippingAddress = builder.shippingAddress;
	}
}
