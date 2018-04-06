package com.visa.innovation.paymentservice.vdp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Error object returned by VDP
 * 
 * @author akakade
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VDPError {

	ResponseStatusVDP responseStatusVDP;

	public VDPError() {

	}

	@JsonProperty("responseStatus")
	public ResponseStatusVDP getResponseStatusVDP() {
		return responseStatusVDP;
	}

	public void setResponseStatusVDP(ResponseStatusVDP responseStatusVDP) {
		this.responseStatusVDP = responseStatusVDP;
	}

	public static class Builder {
		private ResponseStatusVDP responseStatusVDP;

		public Builder responseStatusVDP(ResponseStatusVDP responseStatusVDP) {
			this.responseStatusVDP = responseStatusVDP;
			return this;
		}

		public VDPError build() {
			return new VDPError(this);
		}
	}

	private VDPError(Builder builder) {
		this.responseStatusVDP = builder.responseStatusVDP;
	}

	@Override
	public String toString() {
		return "VDPError [responseStatusVDP=" + responseStatusVDP + "]";
	}
	
	
}
