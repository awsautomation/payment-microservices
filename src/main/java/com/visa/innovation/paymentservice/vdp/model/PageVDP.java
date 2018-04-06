package com.visa.innovation.paymentservice.vdp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is model for the "next" section in the _links object received as part of
 * the getAllX()
 * 
 * @author ntelukun
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageVDP {

	private String link;
	private String title;
	private String method;

	@JsonProperty("href")
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("method")
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return "PageVDP [link=" + link + ", title=" + title + ", method=" + method + "]";
	}

}
