package com.visa.innovation.paymentservice.exception;

import com.visa.innovation.paymentservice.vdp.model.VDPError;

public class GenericVDPException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	VDPError vdpError;
	
	 public GenericVDPException(VDPError vdpError) {
		super();
		this.vdpError = vdpError;
	}

	public VDPError getVDPError() {
		return vdpError;
	}

	public void setVDPError(VDPError vdpError) {
		this.vdpError = vdpError;
	}

}
