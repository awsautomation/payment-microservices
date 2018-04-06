package com.visa.innovation.paymentservice.vdp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmbeddedResultsVDP<T> {
	
	List<T> results;

	@JsonProperty("results")
	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

}
