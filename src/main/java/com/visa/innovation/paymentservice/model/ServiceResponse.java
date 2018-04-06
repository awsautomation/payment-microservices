package com.visa.innovation.paymentservice.model;

public class ServiceResponse<T> {

	T response;
	ServiceError serviceError;

	public ServiceResponse() {

	}

	public ServiceResponse(T response) {
		this.response = response;
	}

	public ServiceResponse(ServiceError serviceError) {
		this.serviceError = serviceError;
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

	public ServiceError getServiceError() {
		return serviceError;
	}

	public void setServiceError(ServiceError serviceError) {
		this.serviceError = serviceError;
	}

	public static class Builder<T> {
		private T response;
		private ServiceError serviceError;

		public Builder<T> response(T response) {
			this.response = response;
			return this;
		}

		public Builder<T> serviceError(ServiceError serviceError) {
			this.serviceError = serviceError;
			return this;
		}

		public ServiceResponse<T> build() {
			return new ServiceResponse<T>(this);
		}
	}

	private ServiceResponse(Builder<T> builder) {
		this.response = builder.response;
		this.serviceError = builder.serviceError;
	}

	@Override
	public String toString() {
		return "ServiceResponse [response=" + response + ", serviceError=" + serviceError + "]";
	}
	
	
}
