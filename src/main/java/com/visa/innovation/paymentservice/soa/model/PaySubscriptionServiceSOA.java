package com.visa.innovation.paymentservice.soa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonInclude(Include.NON_NULL)
public class PaySubscriptionServiceSOA {

    @JacksonXmlProperty(isAttribute = true)
	private String run;
    
	public PaySubscriptionServiceSOA(String run) {
		this.run = run;
	}

	public PaySubscriptionServiceSOA() {
	
	}

	public String getRun() {
		return run;
	}

	public void setRun(String run) {
		this.run = run;
	}

	@Override
	public String toString() {
		return "PaySubscriptionServiceSOA [run=" + run + "]";
	}

}
