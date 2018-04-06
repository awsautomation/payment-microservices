package com.visa.innovation.paymentservice.exception;

import java.util.List;

import com.visa.innovation.paymentservice.util.Constants;

public class InputParametersInvalidException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3611718259483854364L;
	private List<String> errors;
	
	public InputParametersInvalidException(List<String> errors){
		super(Constants.INAVLID_INPUT_PARAMETERS_ERROR);
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}
