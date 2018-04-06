package com.visa.innovation.paymentservice.vdp.model;

public class PaymentSearchRequest {

	String offset;
	String limit;
	String query;
	
	
	public String getOffset() {
		return offset;
	}


	public void setOffset(String offset) {
		this.offset = offset;
	}


	public String getLimit() {
		return limit;
	}


	public void setLimit(String limit) {
		this.limit = limit;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	@Override
	public String toString() {
		return "PaymentSearchRequest [offset=" + offset + ", limit=" + limit + ", query=" + query + "]";
	}
	
}
