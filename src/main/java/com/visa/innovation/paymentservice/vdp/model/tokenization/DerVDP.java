package com.visa.innovation.paymentservice.vdp.model.tokenization;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DerVDP {

	private String format;
	private String algorithm;
	private String publicKey;
	
	public DerVDP() {

	}
	
	@JsonProperty("format")
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	@JsonProperty("algorithm")
	public String getAlgorithm() {
		return algorithm;
	}
	
	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}
	
	@JsonProperty("publicKey")
	public String getPublicKey() {
		return publicKey;
	}
	
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@Override
	public String toString() {
		return "DerVDP [format=" + format + ", algorithm=" + algorithm + ", publicKey=" + publicKey + "]";
	}
	
}
