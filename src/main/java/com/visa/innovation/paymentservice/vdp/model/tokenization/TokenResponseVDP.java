package com.visa.innovation.paymentservice.vdp.model.tokenization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Token response from VDP
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponseVDP {

	String keyId;
	String token;
	String maskedPan;
	String cardType;
	String signedFields;
	String signature;
	
	public TokenResponseVDP() {

	}

	@JsonProperty("keyId")
	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	@JsonProperty("token")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@JsonProperty("maskedPan")
	public String getMaskedPan() {
		return maskedPan;
	}

	public void setMaskedPan(String maskedPan) {
		this.maskedPan = maskedPan;
	}

	@JsonProperty("cardType")
	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	@JsonProperty("signedFields")
	public String getSignedFields() {
		return signedFields;
	}

	public void setSignedFields(String signedFields) {
		this.signedFields = signedFields;
	}

	@JsonProperty("signature")
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Override
	public String toString() {
		return "TokenResponseVDP [keyId=" + keyId + ", token=" + token + ", maskedPan=" + maskedPan + ", cardType="
				+ cardType + ", signedFields=" + signedFields + ", signature=" + signature + "]";
	}
	
}
