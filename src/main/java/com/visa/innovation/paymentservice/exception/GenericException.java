package com.visa.innovation.paymentservice.exception;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.visa.innovation.paymentservice.util.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GenericException extends Exception  {
	 
    /**
	 * 
	 */
	private static final long serialVersionUID = -1138439281779351374L;
	private String message;
   private List<String> errors;
	
	public GenericException(List<String> errors) {
		super(Constants.INVALID_CARD_LENGTH_ERROR);
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
   
    public GenericException() {

	}

	public GenericException(String message) {
       
        this.setMessage(message);
       
    }  

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("error_message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ApiError [ message=" + message + "]";
	}
	
	
}