package com.visa.innovation.paymentservice.exception;

import java.util.List;

import com.visa.innovation.paymentservice.util.Constants;


public class MissingHeadersException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1138439281779351374L;
	private List<String> errors;
	
	public MissingHeadersException(List<String> errors) {
		super(Constants.INVALID_HEADERS_ERROR);
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	
}
