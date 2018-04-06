package com.visa.innovation.paymentservice.soa.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ItemSOA {

	@JacksonXmlProperty(isAttribute = true)
	private int id;

	private double unitPrice;
	private int quantity;

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
