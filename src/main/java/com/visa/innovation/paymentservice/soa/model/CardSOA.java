package com.visa.innovation.paymentservice.soa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CardSOA {
	private String accountNumber;

	private int expirationMonth;

	private int expirationYear;

	private String cardType;

	private String cvNumber;

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public int getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(int expirationYear) {
		this.expirationYear = expirationYear;
	}

	public int getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(int expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCvNumber() {
		return cvNumber;
	}

	public void setCvNumber(String cvNumber) {
		this.cvNumber = cvNumber;
	}

	@Override
	public String toString() {
		return "CardSOA [accountNumber=" + accountNumber + ", expirationMonth=" + expirationMonth + ", expirationYear="
				+ expirationYear + ", cardType=" + cardType + ", cvNumber=" + cvNumber + "]";
	}

}
