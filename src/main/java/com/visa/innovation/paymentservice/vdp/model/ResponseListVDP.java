package com.visa.innovation.paymentservice.vdp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Encapsulates the Results returned by VDP , when a getAllxxxx call is made.
 * Acts as base for all auth, all capture and all sales.
 * 
 * @author ntelukun
 *
 */
public class ResponseListVDP {

	List<BaseRetrieveAllVDP> results;

	@JsonProperty("results")
	public List<BaseRetrieveAllVDP> getResponse() {
		return results;
	}

	public void setResults(List<BaseRetrieveAllVDP> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "ResponseListVDP [results=" + results + "]";
	}

}
