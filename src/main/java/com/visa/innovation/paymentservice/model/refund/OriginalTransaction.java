package com.visa.innovation.paymentservice.model.refund;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OriginalTransaction {

	String captureId;
	String saleId;
	String refundId;
	String currency;
	String status;
	String type;
	Double amount;

	public OriginalTransaction() {

	}

	public OriginalTransaction(Double amount, String currency, String status) {
		this.currency = currency;
		this.status = status;
		this.amount = amount;
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

	@JsonProperty("currency")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@JsonProperty("refund_id")
	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	@Override
	public String toString() {
		return "OriginalTransaction [captureId=" + captureId + ", saleId=" + saleId + ", refundId=" + refundId
				+ ", currency=" + currency + ", status=" + status + ", type=" + type + ", amount=" + amount + "]";
	}

}
