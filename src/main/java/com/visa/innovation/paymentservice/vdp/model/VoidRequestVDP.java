package com.visa.innovation.paymentservice.vdp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VoidRequestVDP {
	
	String referenceId;

	public VoidRequestVDP() {
	
	}

	public VoidRequestVDP(String referenceId) {
		this.referenceId = referenceId;
	}

	@JsonProperty("referenceId")
	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	@Override
	public String toString() {
		return "VoidRequestVDP [referenceId=" + referenceId + "]";
	}
	
}
