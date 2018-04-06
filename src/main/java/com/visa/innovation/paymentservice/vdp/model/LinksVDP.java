package com.visa.innovation.paymentservice.vdp.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LinksVDP {

	private List<PageVDP> next;
	private PageVDP self;

	@JsonProperty("next")
	public List<PageVDP> getNext() {
		return next;
	}

	public void setNext(List<PageVDP> next) {
		this.next = next;
	}

	@JsonProperty("self")
	public PageVDP getSelf() {
		return self;
	}

	public void setSelf(PageVDP self) {
		this.self = self;
	}

}
