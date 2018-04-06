package com.visa.innovation.paymentservice.vdp.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveByIdResponseVDP;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RetrieveAuthById extends BaseRetrieveByIdResponseVDP {

//	private String id;
//	private String status;
//	private Double amount;
//	private String currency;
//	private String referenceId;
//	private String createDateTime;
//	private PaymentVDP payment;
//	private AddressVDP billTo;
//
//	@JsonProperty("id")
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	@JsonProperty("status")
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}
//
//	@JsonProperty("amount")
//	public Double getAmount() {
//		return amount;
//	}
//
//	public void setAmount(Double amount) {
//		this.amount = amount;
//	}
//
//	@JsonProperty("currency")
//	public String getCurrency() {
//		return currency;
//	}
//
//	public void setCurrency(String currency) {
//		this.currency = currency;
//	}
//
//	@JsonProperty("referenceId")
//	public String getReferenceId() {
//		return referenceId;
//	}
//
//	public void setReferenceId(String referenceId) {
//		this.referenceId = referenceId;
//	}
//
//	@JsonProperty("createDateTime")
//	public String getCreateDateTime() {
//		return createDateTime;
//	}
//
//	public void setCreateDateTime(String createdDateTime) {
//		this.createDateTime = createdDateTime;
//	}
//
//	@JsonProperty("payment")
//	public PaymentVDP getPayment() {
//		return payment;
//	}
//
//	public void setPayment(PaymentVDP payment) {
//		this.payment = payment;
//	}
//
//	@JsonProperty("billTo")
//	public AddressVDP getBillTo() {
//		return billTo;
//	}
//
//	public void setBillTo(AddressVDP billTo) {
//		this.billTo = billTo;
//	}
//
//	@Override
//	public String toString() {
//		super();
//		return "RetrieveAuthById [id=" + id + ", status=" + status + ", amount=" + amount + ", currency=" + currency
//				+ ", referenceId=" + referenceId + ", createdDateTime=" + createDateTime + ", payment=" + payment
//				+ ", billTo=" + billTo + "]";
//	}

}
