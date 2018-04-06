package com.visa.innovation.paymentservice.model.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visa.innovation.paymentservice.model.PaymentResponse;
import com.visa.innovation.paymentservice.soa.model.PaymentStatusEnum;

@JsonInclude(Include.NON_NULL)
public class AuthResponse {

	String authorizationId;
	Double amount;
	String currency;
	String orderId;
	String created;
	String updated;
	String validUntil;
	String merchantId;
	String userId;
	PaymentStatusEnum status;
	PaymentResponse payment;

	public AuthResponse() {

	}

	@JsonProperty("authorization_id")
	public String getAuthorizationId() {
		return authorizationId;
	}

	public void setAuthorizationId(String authorizationId) {
		this.authorizationId = authorizationId;
	}

	@JsonProperty("amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@JsonProperty("currency")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@JsonProperty("order_id")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@JsonProperty("created")
	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	@JsonProperty("updated")
	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	@JsonProperty("valid_until")
	public String getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(String validUntil) {
		this.validUntil = validUntil;
	}

	@JsonProperty("merchant_id")
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	@JsonProperty("user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@JsonProperty("payment")
	public PaymentResponse getPayment() {
		return payment;
	}

	public void setPayment(PaymentResponse payment) {
		this.payment = payment;
	}

	@JsonProperty("status")
	public PaymentStatusEnum getStatus() {
		return status;
	}

	public void setStatus(PaymentStatusEnum status) {
		this.status = status;
	}

	/**
	 * Returns a valid json String except for payment , which is processed in
	 * the controller. Make sure any additions/deletions to the fields will
	 * still return valid Json string.
	 */
	@Override
	public String toString() {
		return "{ \"authorizationId\"" + ":" + authorizationId + ", \"amount\"" + ":" + amount + ", \"currency\"" + ":"
				+ currency + ", \"orderId\"" + ":" + orderId + ", \"created\"" + ":" + created + ", \"updated\"" + ":"
				+ updated + ", \"validUntil\"" + ":" + validUntil + ", \"merchantId\"" + ":" + merchantId
				+ ", \"userId\"" + ":" + userId + ", \"status\"" + ":" + status + ", \"payment\"" + ":" + payment + "}";
	}

	public static class Builder {
		private String authorizationId;
		private Double amount;
		private String currency;
		private String orderId;
		private String created;
		private String updated;
		private String validUntil;
		private String merchantId;
		private String userId;
		private PaymentStatusEnum status;
		private PaymentResponse payment;

		public Builder authorizationId(String authorizationId) {
			this.authorizationId = authorizationId;
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

		public Builder orderId(String orderId) {
			this.orderId = orderId;
			return this;
		}

		public Builder created(String created) {
			this.created = created;
			return this;
		}

		public Builder updated(String updated) {
			this.updated = updated;
			return this;
		}

		public Builder validUntil(String validUntil) {
			this.validUntil = validUntil;
			return this;
		}

		public Builder merchantId(String merchantId) {
			this.merchantId = merchantId;
			return this;
		}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder status(PaymentStatusEnum status) {
			this.status = status;
			return this;
		}

		public Builder payment(PaymentResponse payment) {
			this.payment = payment;
			return this;
		}

		public AuthResponse build() {
			return new AuthResponse(this);
		}
	}

	private AuthResponse(Builder builder) {
		this.authorizationId = builder.authorizationId;
		this.amount = builder.amount;
		this.currency = builder.currency;
		this.orderId = builder.orderId;
		this.created = builder.created;
		this.updated = builder.updated;
		this.validUntil = builder.validUntil;
		this.merchantId = builder.merchantId;
		this.userId = builder.userId;
		this.status = builder.status;
		this.payment = builder.payment;
	}
}
