package com.visa.innovation.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceError {

	String code;
	String status;
	String message;

	public ServiceError() {

	}
	
	public ServiceError(String code, String status, String message) {
		this.code = code;
		this.status = status;
		this.message = message;
	}

	@JsonProperty("error_code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	@Override
	public String toString() {
		return "ServiceError [code=" + code + ", status=" + status + ", message=" + message + "]";
	}

	public static class Builder {
		private String code;
		private String status;
		private String message;

		public Builder code(String code) {
			this.code = code;
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

		public ServiceError build() {
			return new ServiceError(this);
		}
	}

	private ServiceError(Builder builder) {
		this.code = builder.code;
		this.status = builder.status;
		this.message = builder.message;
	}
}
