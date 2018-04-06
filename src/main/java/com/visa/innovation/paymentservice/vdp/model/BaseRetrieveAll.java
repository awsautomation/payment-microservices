package com.visa.innovation.paymentservice.vdp.model;

/**
 * This Class is used as a base class to BaseRetrieveAllVDP so that we can use
 * the reference Id field irrespective of the Sales/Auth/Capture Objects being
 * accessed without the need to cast them. The Filter log uses generics , hence
 * having this class exposing only reference id through a base class helps in
 * filtering without the need to cast objects.
 * 
 * @author ntelukun
 *
 */
public class BaseRetrieveAll {
	private String referenceId;

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	@Override
	public String toString() {
		return "BaseRetrieveAll [referenceId=" + referenceId + "]";
	}

}
