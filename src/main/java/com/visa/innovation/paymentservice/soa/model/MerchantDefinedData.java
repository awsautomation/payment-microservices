package com.visa.innovation.paymentservice.soa.model;

import java.util.ArrayList;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class MerchantDefinedData {
	
	ArrayList<MDDField> mddFields;

	
	public MerchantDefinedData() {
		
	}

	public MerchantDefinedData(ArrayList<MDDField> mddFields) {
		this.mddFields = mddFields;
	}

	@JacksonXmlProperty(localName="mddField")
	@JacksonXmlElementWrapper(useWrapping = false)
	public ArrayList<MDDField> getMddFields() {
		return mddFields;
	}

	public void setMddFields(ArrayList<MDDField> mddFields) {
		this.mddFields = mddFields;
	}

}
