package com.visa.innovation.paymentservice.vdp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Main wrapper POJO for wrapping the VDP Response for CYBS endpoints.
 * 
 * @author ntelukun
 *
 */
public class ResultsWrapperVDP {

	LinksVDP linkResults;
	ResponseListVDP vdpResponse;

	@JsonProperty("_links")
	public LinksVDP getLinkResults() {
		return linkResults;
	}

	public void setLinkResults(LinksVDP linkResults) {
		this.linkResults = linkResults;
	}

	@JsonProperty("_embedded")
	public ResponseListVDP getVdpResponse() {
		return vdpResponse;
	}

	public void setVdpResponse(ResponseListVDP vdpResponse) {
		this.vdpResponse = vdpResponse;
	}

	@Override
	public String toString() {
		return "ResultsWrapperVDP [vdpResponse=" + vdpResponse + "]";
	}
}
