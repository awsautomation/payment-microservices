package com.visa.innovation.paymentservice.vdp.model.capture;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visa.innovation.paymentservice.vdp.model.AddressVDP;

/**
 * 
 * VDP request for create capture
 *
 */
public class CreateCaptureRequestVDP {

	String amount;
	String currency;
	String referenceId;
	HashMap<String, String> merchantDefinedData;
	AddressVDP billingAddress;
	AddressVDP shippingAddress;

	public CreateCaptureRequestVDP() {

	}

	@JsonProperty("amount")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

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

	public static class Builder {
		private String amount;
		private String currency;
		private String referenceId;
		private HashMap<String, String> merchantDefinedData;
		private AddressVDP billingAddress;
		private AddressVDP shippingAddress;

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

		public CreateCaptureRequestVDP build() {
			return new CreateCaptureRequestVDP(this);
		}
	}

	private CreateCaptureRequestVDP(Builder builder) {
		this.amount = builder.amount;
		this.currency = builder.currency;
		this.referenceId = builder.referenceId;
		this.merchantDefinedData = builder.merchantDefinedData;
		this.billingAddress = builder.billingAddress;
		this.shippingAddress = builder.shippingAddress;
	}

	@Override
	public String toString() {
		return "CaptureRequestVDP [amount=" + amount + ", currency=" + currency + ", referenceId=" + referenceId
				+ ", merchantDefinedData=" + merchantDefinedData + ", billingAddress=" + billingAddress
				+ ", shippingAddress=" + shippingAddress + "]";
	}
	
	
}
