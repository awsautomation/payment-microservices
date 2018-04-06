package com.visa.innovation.paymentservice.vdp.model;

/**
 * Our own response object consisting of success and error responses coming back
 * from VDP
 *
 * @param <T>
 */
public class VDPResponse<T> {

	@Override
	public String toString() {
		return "VDPResponse [response=" + response + ", vdpError=" + vdpError + "]";
	}

	T response;
	VDPError vdpError;

	public VDPResponse() {
		super();
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

	public VDPError getVdpError() {
		return vdpError;
	}

	public void setVdpError(VDPError vdpError) {
		this.vdpError = vdpError;
	}

	public static class Builder<T> {
		private T response;
		private VDPError vdpError;

		public Builder<T> response(T response) {
			this.response = response;
			return this;
		}

		public Builder<T> vdpError(VDPError vdpError) {
			this.vdpError = vdpError;
			return this;
		}

		public VDPResponse<T> build() {
			return new VDPResponse<T>(this);
		}
	}

	private VDPResponse(Builder<T> builder) {
		this.response = builder.response;
		this.vdpError = builder.vdpError;
	}
}
