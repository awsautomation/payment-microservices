package com.visa.innovation.paymentservice.vdp.marshallers;

public class FilterMetaData {

	public String userId;
	public String appId;
	public String cardId;

	public static class Builder {
		private String userId;
		private String appId;
		private String cardId;

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder appId(String appId) {
			this.appId = appId;
			return this;
		}

		public Builder cardId(String cardId) {
			this.cardId = cardId;
			return this;
		}

		public FilterMetaData build() {
			return new FilterMetaData(this);
		}
	}

	private FilterMetaData(Builder builder) {
		this.userId = builder.userId;
		this.appId = builder.appId;
		this.cardId = builder.cardId;
	}
	
    public String toString() {
        return userId + "-" + appId + "-" + cardId;
    }

}
