package com.visa.innovation.paymentservice.exception;

import com.visa.innovation.paymentservice.model.ServiceError;

public class GenericServiceException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ServiceError error;
	public GenericServiceException(ServiceError error) {
		super();
		this.error = error;
	}
	public ServiceError getError() {
		return error;
	}
	public void setError(ServiceError error) {
		this.error = error;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
