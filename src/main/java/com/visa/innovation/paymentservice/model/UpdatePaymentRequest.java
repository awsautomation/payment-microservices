package com.visa.innovation.paymentservice.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * This class is used for updating auth/capture/sales. Currently for voiding.
 *
 */
public class UpdatePaymentRequest {

	UpdateStatus status;

	public UpdatePaymentRequest() {
		
	}
	
	@NotNull(message="status is required")
	@JsonProperty("status")
	public UpdateStatus getStatus() {
		return status;
	}

	public void setStatus(UpdateStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UpdateAuthRequest [status=" + status + "]";
	}
	
	
}
