package com.visa.innovation.paymentservice.vdp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LinkSelfVDP {

	private PageVDP result;

	@JsonProperty("self")
	public PageVDP getResult() {
		return result;
	}

	public void setResult(PageVDP result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "LinkSelfVDP [result=" + result + ", getResult()=" + getResult() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

}
