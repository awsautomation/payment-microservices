package com.visa.innovation.paymentservice.vdp.model.tokenization;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 *Request to get key from VDP. Used in tokenization process.
 *
 */
public class KeyRequestVDP {
	
	String encryptionType;

	public KeyRequestVDP() {
	
	}
	
	public KeyRequestVDP(String encryptionType) {
		this.encryptionType = encryptionType;
	}

	@JsonProperty("encryptionType")
	public String getEncryptionType() {
		return encryptionType;
	}

	public void setEncryptionType(String encryptionType) {
		this.encryptionType = encryptionType;
	}

	@Override
	public String toString() {
		return "KeyRequestVDP [encryptionType=" + encryptionType + "]";
	}

}
