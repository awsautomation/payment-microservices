package com.visa.innovation.paymentservice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseList<T> {

	List<T> response;

	@JsonProperty("data")
	public List<T> getResponse() {
		return response;
	}

	public void setResponse(List<T> response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "ResponseList [response=" + response + "]";
	}

}
