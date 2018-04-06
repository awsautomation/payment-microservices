package com.visa.innovation.paymentservice.exception;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApiError {
	 
    private int status;
    private String message;
    private List<String> errors;
 
    
    public ApiError() {

	}

	public ApiError(int status, String message, List<String> errors) {
        this.setStatus(status);
        this.setMessage(message);
        this.setErrors(errors);
    }
 
    public ApiError(int status, String message, String error) {
        this.setStatus(status);
        this.setMessage(message);
        this.setErrors(Arrays.asList(error));
    }
    
    @JsonProperty("status_code")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("error_message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	@JsonProperty("errors")
	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "ApiError [status=" + status + ", message=" + message + ", errors=" + errors + "]";
	}
	
	
}