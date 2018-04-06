package com.visa.innovation.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetServiceResponse {
	private String data;

	public GetServiceResponse(String data) {
		this.data = data;
	}

	@JsonProperty("data")
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
