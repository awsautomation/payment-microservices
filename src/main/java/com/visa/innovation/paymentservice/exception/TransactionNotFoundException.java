package com.visa.innovation.paymentservice.exception;

public class TransactionNotFoundException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1711457339147190830L;

	public TransactionNotFoundException(String transactionId, String transactionType){
		super(transactionType + "not found. Id=" + transactionId);
	}
}
