package com.visa.innovation.paymentservice.model.refund;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.visa.innovation.paymentservice.soa.model.PaymentStatusEnum;

@JsonInclude(Include.NON_NULL)
public class RefundResponse {

	String refundId;
	String orderId;
	Double refundAmount;
	String refundCurrency;
	String created;
	String updated;
	String merchantId;
	PaymentStatusEnum status;
	String userId;
	OriginalTransaction originalTransaction;

	public RefundResponse() {

	}

	@JsonProperty("refund_id")
	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	@JsonProperty("order_id")
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@JsonProperty("refund_amount")
	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	@JsonProperty("refund_currency")
	public String getRefundCurrency() {
		return refundCurrency;
	}

	public void setRefundCurrency(String refundCurrency) {
		this.refundCurrency = refundCurrency;
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

	@JsonProperty("merchant_id")
	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	@JsonProperty("status")
	public PaymentStatusEnum getStatus() {
		return status;
	}

	public void setStatus(PaymentStatusEnum status) {
		this.status = status;
	}

	@JsonProperty("user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@JsonProperty("transaction")
	public OriginalTransaction getRefundTransaction() {
		return originalTransaction;
	}

	public void setRefundTransaction(OriginalTransaction refundTransaction) {
		this.originalTransaction = refundTransaction;
	}

	@Override
	public String toString() {
		return "RefundResponse [refundId=" + refundId + ", orderId=" + orderId + ", refundAmount=" + refundAmount
				+ ", refundCurrency=" + refundCurrency + ", created=" + created + ", updated=" + updated
				+ ", merchantId=" + merchantId + ", status=" + status + ", userId=" + userId + ", refundTransaction="
				+ originalTransaction + "]";
	}

	public static class Builder {
		private String refundId;
		private String orderId;
		private Double refundAmount;
		private String refundCurrency;
		private String created;
		private String updated;
		private String merchantId;
		private PaymentStatusEnum status;
		private String userId;
		private OriginalTransaction originalTransaction;

		public Builder refundId(String refundId) {
			this.refundId = refundId;
			return this;
		}

		public Builder orderId(String orderId) {
			this.orderId = orderId;
			return this;
		}

		public Builder refundAmount(Double refundAmount) {
			this.refundAmount = refundAmount;
			return this;
		}

		public Builder refundCurrency(String refundCurrency) {
			this.refundCurrency = refundCurrency;
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

		public Builder merchantId(String merchantId) {
			this.merchantId = merchantId;
			return this;
		}

		public Builder status(PaymentStatusEnum status) {
			this.status = status;
			return this;
		}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder originalTransaction(OriginalTransaction originalTransaction) {
			this.originalTransaction = originalTransaction;
			return this;
		}

		public RefundResponse build() {
			return new RefundResponse(this);
		}
	}

	private RefundResponse(Builder builder) {
		this.refundId = builder.refundId;
		this.orderId = builder.orderId;
		this.refundAmount = builder.refundAmount;
		this.refundCurrency = builder.refundCurrency;
		this.created = builder.created;
		this.updated = builder.updated;
		this.merchantId = builder.merchantId;
		this.status = builder.status;
		this.userId = builder.userId;
		this.originalTransaction = builder.originalTransaction;
	}
}
