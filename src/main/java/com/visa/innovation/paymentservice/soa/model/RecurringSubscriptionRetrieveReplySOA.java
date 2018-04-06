package com.visa.innovation.paymentservice.soa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RecurringSubscriptionRetrieveReplySOA {

	private boolean approvalRequired;
	private boolean automaticRenew;
	private String cardAccountNumber;
	private int cardExpirationMonth;
	private int cardExpirationYear;
	private String cardType;
	private String city;
	private String country;
	private String currency;
	private String email;
	private String frequency;
	private String firstName;
	private String lastName;
	private String ownerMerchantID;
	private String paymentMethod;
	private String paymentsRemaining;
	private String postalCode;
	private int reasonCode;
	private String state;
	private String status;
	private String street1;
	private String street2;
	private String subscriptionID;

	public boolean isApprovalRequired() {
		return approvalRequired;
	}

	public void setApprovalRequired(boolean approvalRequired) {
		this.approvalRequired = approvalRequired;
	}

	public boolean isAutomaticRenew() {
		return automaticRenew;
	}

	public void setAutomaticRenew(boolean automaticRenew) {
		this.automaticRenew = automaticRenew;
	}

	public String getCardAccountNumber() {
		return cardAccountNumber;
	}

	public void setCardAccountNumber(String cardAccountNumber) {
		this.cardAccountNumber = cardAccountNumber;
	}

	public int getCardExpirationMonth() {
		return cardExpirationMonth;
	}

	public void setCardExpirationMonth(int cardExpirationMonth) {
		this.cardExpirationMonth = cardExpirationMonth;
	}

	public int getCardExpirationYear() {
		return cardExpirationYear;
	}

	public void setCardExpirationYear(int cardExpirationYear) {
		this.cardExpirationYear = cardExpirationYear;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOwnerMerchantID() {
		return ownerMerchantID;
	}

	public void setOwnerMerchantID(String ownerMerchantID) {
		this.ownerMerchantID = ownerMerchantID;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentsRemaining() {
		return paymentsRemaining;
	}

	public void setPaymentsRemaining(String paymentsRemaining) {
		this.paymentsRemaining = paymentsRemaining;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public int getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(int reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(String subscriptionID) {
		this.subscriptionID = subscriptionID;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

}
