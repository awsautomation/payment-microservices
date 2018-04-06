package com.visa.innovation.paymentservice.exception;

public class CallbackUrlException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4010339104397347710L;

	public CallbackUrlException(){
		super("Invalid callback URL/Callback URL not found.");
	}
	
}
