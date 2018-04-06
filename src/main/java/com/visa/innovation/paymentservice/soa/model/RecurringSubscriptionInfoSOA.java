package com.visa.innovation.paymentservice.soa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RecurringSubscriptionInfoSOA {

	private String subscriptionID;
	
	public RecurringSubscriptionInfoSOA(String subscriptionID) {
		this.subscriptionID = subscriptionID;
	}

	public String getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(String subscriptionID) {
		this.subscriptionID = subscriptionID;
	}

	@Override
	public String toString() {
		return "RecurringSubscriptionInfoSOA [subscriptionID=" + subscriptionID + "]";
	}
	
}
