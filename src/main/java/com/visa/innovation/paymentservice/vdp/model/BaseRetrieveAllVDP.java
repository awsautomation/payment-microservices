package com.visa.innovation.paymentservice.vdp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * map get all auth response from VDP
 * 
 * @author ntelukun
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseRetrieveAllVDP extends BaseRetrieveAll {

	private String id;
	private String accountSuffix;
	private double amount;
	private String currency;
	private String requestDateTime;
	private String cardType;
	private LinkSelfVDP links;

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	public void setId(String authorizationId) {
		this.id = authorizationId;
	}

	@JsonProperty("accountSuffix")
	public String getAccountSuffix() {
		return accountSuffix;
	}

	public void setAccountSuffix(String accountSuffix) {
		this.accountSuffix = accountSuffix;
	}

	@JsonProperty("amount")
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@JsonProperty("currency")
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@JsonProperty("requestDateTime")
	public String getCreated() {
		return requestDateTime;
	}

	public void setCreated(String created) {
		this.requestDateTime = created;
	}

	@JsonProperty("cardType")
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	@JsonProperty("_links")
	public LinkSelfVDP getLinks() {
		return links;
	}

	public void setLinks(LinkSelfVDP links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "BaseRetrieveAllVDP [id=" + id + ", accountSuffix=" + accountSuffix + ", amount=" + amount
				+ ", currency=" + currency + ", requestDateTime=" + requestDateTime + ", cardType=" + cardType
				+ ", links=" + links + "]";
	}

}
