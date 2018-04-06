package com.visa.innovation.paymentservice.service;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.exception.ForbiddenVDPException;
import com.visa.innovation.paymentservice.model.refund.RefundRequest;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.VoidRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.refund.CreateRefundRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.refund.CreateRefundResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.refund.RetrieveRefundByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.utils.MethodTypes;
import com.visa.innovation.paymentservice.vdp.utils.Property;
import com.visa.innovation.paymentservice.vdp.utils.VisaAPIClient;
import com.visa.innovation.paymentservice.vdp.utils.VisaProperties;

@Component
public class RefundServiceVDP {

	@Autowired
	VisaAPIClient visaAPIClient;
	@Autowired
	VisaProperties visaProperties;

	String apiKeyVDP;
	String baseUri;
	String queryString;

	public RefundServiceVDP() {
	}

	@PostConstruct
	private void init() {
		this.apiKeyVDP = visaProperties.getProperty(Property.API_KEY);
		this.baseUri = visaProperties.getProperty(Property.BASE_URI);
		this.queryString = "apikey=" + apiKeyVDP;
	}

	public VDPResponse<CreateRefundResponseVDP> createRefund(RefundRequest refundRequest,
			CreateRefundRequestVDP refundRequestVDP) {

		String resourcePath;
		if (refundRequest.getCaptureId() != null) {
			resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_CAPTURE_REFUND);
			resourcePath = resourcePath.replace("{capture-id}", refundRequest.getCaptureId());
		} else {
			resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_SALE_REFUND);
			resourcePath = resourcePath.replace("{sales-id}", refundRequest.getSaleId());
		}

		VDPResponse<CreateRefundResponseVDP> response = null;
		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, refundRequestVDP,
					MethodTypes.POST, new HashMap<String, String>(), CreateRefundResponseVDP.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	public VDPResponse<RetrieveRefundByIdResponseVDP> getRefundById(String refundId) {

		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_REFUND_RETRIEVE_BY_ID);
		resourcePath = resourcePath.replace("{refund-id}", refundId);

		VDPResponse<RetrieveRefundByIdResponseVDP> response = null;
		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, null, MethodTypes.GET,
					new HashMap<String, String>(), RetrieveRefundByIdResponseVDP.class);
		} catch (ForbiddenVDPException e) {
			e.printStackTrace();
		}
		return response;
	}

	public VDPResponse<ReverseVoidResponseVDP> updateRefund(VoidRequestVDP voidRefundRequest, String refundId) {

		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_REFUND_VOID);
		resourcePath = resourcePath.replace("{refund-id}", refundId);

		VDPResponse<ReverseVoidResponseVDP> response = null;

		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, voidRefundRequest,
					MethodTypes.POST, new HashMap<String, String>(), ReverseVoidResponseVDP.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}
}
