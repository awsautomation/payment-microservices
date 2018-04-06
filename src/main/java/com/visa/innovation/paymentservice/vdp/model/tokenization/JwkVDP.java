package com.visa.innovation.paymentservice.vdp.model.tokenization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwkVDP {

	String kty;
	String use;
	String kid;
	String n;
	String e;
	
	public JwkVDP() {
	}

	@JsonProperty("kty")
	public String getKty() {
		return kty;
	}

	public void setKty(String kty) {
		this.kty = kty;
	}

	@JsonProperty("use")
	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

	@JsonProperty("kid")
	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	@JsonProperty("n")
	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	@JsonProperty("e")
	public String getE() {
		return e;
	}

	public void setE(String e) {
		this.e = e;
	}

	@Override
	public String toString() {
		return "JwkVDP [kty=" + kty + ", use=" + use + ", kid=" + kid + ", n=" + n + ", e=" + e + "]";
	}
	
}
