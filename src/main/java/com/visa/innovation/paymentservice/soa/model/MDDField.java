package com.visa.innovation.paymentservice.soa.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class MDDField {

	int id;
	String value;
	
	public MDDField() {

	}
	
	public MDDField(int id, String value) {
		this.id = id;
		this.value = value;
	}

	@JacksonXmlProperty(isAttribute=true)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@JacksonXmlText
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "MDDField [id=" + id + ", value=" + value + "]";
	}
	
}
