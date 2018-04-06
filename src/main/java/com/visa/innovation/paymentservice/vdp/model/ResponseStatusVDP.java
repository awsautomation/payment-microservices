package com.visa.innovation.paymentservice.vdp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Part of error object coming back from VDP
 * @author akakade
 *
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseStatusVDP {

	String code;
	String severity;
	String info;
	String status;
	String message;

	public ResponseStatusVDP() {

	}

	@JsonProperty("code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty("severity")
	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	@JsonProperty("info")
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static class Builder {
		private String code;
		private String severity;
		private String info;
		private String status;
		private String message;

		public Builder code(String code) {
			this.code = code;
			return this;
		}

		public Builder severity(String severity) {
			this.severity = severity;
			return this;
		}

		public Builder info(String info) {
			this.info = info;
			return this;
		}

		public Builder status(String status) {
			this.status = status;
			return this;
		}

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public ResponseStatusVDP build() {
			return new ResponseStatusVDP(this);
		}
	}

	private ResponseStatusVDP(Builder builder) {
		this.code = builder.code;
		this.severity = builder.severity;
		this.info = builder.info;
		this.status = builder.status;
		this.message = builder.message;
	}

	@Override
	public String toString() {
		return "ResponseStatusVDP [code=" + code + ", severity=" + severity + ", info=" + info + ", status=" + status
				+ ", message=" + message + "]";
	}
	
	
}
