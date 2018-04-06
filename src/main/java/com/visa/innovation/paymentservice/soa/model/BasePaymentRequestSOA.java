package com.visa.innovation.paymentservice.soa.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "requestMessage")
@JsonInclude(Include.NON_NULL)
public class BasePaymentRequestSOA {

	@JacksonXmlProperty(isAttribute = true)
	private String xmlns;
	private String merchantReferenceCode;
	private UserDetailsSOA billTo;
	@JacksonXmlElementWrapper(useWrapping = false)
	private List<ItemSOA> item;
	private PurchaseTotalsSOA purchaseTotals;
	private CardSOA card;
	private RecurringSubscriptionInfoSOA recurringSubscriptionInfo;
	private MerchantDefinedData merchantDefinedData;

	
	public MerchantDefinedData getMerchantDefinedData() {
		return merchantDefinedData;
	}

	public void setMerchantDefinedData(MerchantDefinedData merchantDefinedData) {
		this.merchantDefinedData = merchantDefinedData;
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public String getMerchantReferenceCode() {
		return merchantReferenceCode;
	}

	public void setMerchantReferenceCode(String merchantReferenceCode) {
		this.merchantReferenceCode = merchantReferenceCode;
	}

	public CardSOA getCard() {
		return card;
	}

	public void setCard(CardSOA card) {
		this.card = card;
	}
	public RecurringSubscriptionInfoSOA getRecurringSubscriptionInfo() {
		return recurringSubscriptionInfo;
	}

	public void setRecurringSubscriptionInfo(RecurringSubscriptionInfoSOA recurringSubscriptionInfo) {
		this.recurringSubscriptionInfo = recurringSubscriptionInfo;
	}

	public UserDetailsSOA getBillTo() {
		return billTo;
	}

	public void setBillTo(UserDetailsSOA billTo) {
		this.billTo = billTo;
	}

	public PurchaseTotalsSOA getPurchaseTotals() {
		return purchaseTotals;
	}

	public void setPurchaseTotals(PurchaseTotalsSOA purchaseTotals) {
		this.purchaseTotals = purchaseTotals;
	}

	public List<ItemSOA> getItem() {
		return item;
	}

	public void setItem(List<ItemSOA> item) {
		this.item = item;
	}

	@Override
	public String toString() {
		return "BasePaymentRequestSOA [xmlns=" + xmlns + ", merchantReferenceCode=" + merchantReferenceCode
				+ ", billTo=" + billTo + ", item=" + item + ", purchaseTotals=" + purchaseTotals + ", card=" + card
				+ ", recurringSubscriptionInfo=" + recurringSubscriptionInfo + "]";
	}

}
