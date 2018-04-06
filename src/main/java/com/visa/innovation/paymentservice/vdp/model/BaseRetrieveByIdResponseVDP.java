package com.visa.innovation.paymentservice.vdp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseRetrieveByIdResponseVDP {

	String id;
	String status;
	Double amount;
	String currency;
	String referenceId;
	private String createDateTime;
	private PaymentVDP payment;
	private AddressVDP billTo;
	
	
	public String getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(String createdDateTime) {
		this.createDateTime = createdDateTime;
	}
	public PaymentVDP getPayment() {
		return payment;
	}
	public void setPayment(PaymentVDP payment) {
		this.payment = payment;
	}
	public AddressVDP getBillTo() {
		return billTo;
	}
	public void setBillTo(AddressVDP billTo) {
		this.billTo = billTo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	@Override
	public String toString() {
		return "BaseRetrieveByIdResponseVDP [id=" + id + ", status=" + status + ", amount=" + amount + ", currency="
				+ currency + ", referenceId=" + referenceId + ", createdDateTime=" + createDateTime + ", payment="
				+ payment + ", billTo=" + billTo + "]";
	}
	
	
}
