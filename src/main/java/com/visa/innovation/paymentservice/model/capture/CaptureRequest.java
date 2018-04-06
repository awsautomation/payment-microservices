package com.visa.innovation.paymentservice.model.capture;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visa.innovation.paymentservice.model.BasePaymentRequest;

public class CaptureRequest extends BasePaymentRequest{

	String authorizationId;
	
	public CaptureRequest() {
		
	}

	@NotNull(message="authorization_id is required")
	@JsonProperty("authorization_id")
	public String getAuthorizationId() {
		return authorizationId;
	}

	public void setAuthorizationId(String authorizationId) {
		this.authorizationId = authorizationId;
	}

	@Override
	public String toString() {
		return "CaptureRequest [authorizationId=" + authorizationId + "]";
	}
	
}
