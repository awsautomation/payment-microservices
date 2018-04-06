package com.visa.innovation.paymentservice.vdp.model.tokenization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Response of get key request from VDP
 *
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyResponseVDP {

	String keyId;
	DerVDP der;
	JwkVDP jwkVDP;
	

	public KeyResponseVDP() {

	}

	@JsonProperty("keyId")
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@JsonProperty("der")
	public DerVDP getDer() {
		return der;
	}

	public void setDer(DerVDP der) {
		this.der = der;
	}
	
	@JsonProperty("jwk")
	public JwkVDP getJwkVDP() {
		return jwkVDP;
	}

	public void setJwkVDP(JwkVDP jwkVDP) {
		this.jwkVDP = jwkVDP;
	}

	@Override
	public String toString() {
		return "KeyResponseVDP [keyId=" + keyId + ", der=" + der + "]";
	}

}
