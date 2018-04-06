package com.visa.innovation.paymentservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.exception.ForbiddenVDPException;
import com.visa.innovation.paymentservice.model.capture.CaptureResponse;
import com.visa.innovation.paymentservice.util.Constants;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.Utils;
import com.visa.innovation.paymentservice.vdp.marshallers.FilterMetaData;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.VoidRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.capture.CreateCaptureRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.capture.CreateCaptureResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.capture.RetrieveCaptureByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.utils.MethodTypes;
import com.visa.innovation.paymentservice.vdp.utils.Property;
import com.visa.innovation.paymentservice.vdp.utils.VisaAPIClient;

public class CaptureServiceVDP extends BaseService {

	@Autowired
	VisaAPIClient visaAPIClient;

	@Autowired
	Utils utils;

	String apiKeyVDP;
	String baseUri;
	String queryString;

	public CaptureServiceVDP() {
	}

	@PostConstruct
	private void init() {
		this.apiKeyVDP = visaProperties.getProperty(Property.API_KEY);
		this.baseUri = visaProperties.getProperty(Property.BASE_URI);
		this.queryString = "apikey=" + apiKeyVDP;
	}

	public VDPResponse<CreateCaptureResponseVDP> createCapture(CreateCaptureRequestVDP captureRequestVDP,
			String authorizationId) {

		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_CAPTURE);
		resourcePath = resourcePath.replace("{authorization-id}", authorizationId);

		VDPResponse<CreateCaptureResponseVDP> response = null;

		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, captureRequestVDP,
					MethodTypes.POST, new HashMap<String, String>(), CreateCaptureResponseVDP.class);
		} catch (Exception e) {
			CustomLogger.logException(e);
			e.printStackTrace();
		}

		return response;

	}

	public VDPResponse<ReverseVoidResponseVDP> updateCapture(VoidRequestVDP voidCaptureRequestVDP, String captureId) {

		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_CAPTURE_VOID);
		resourcePath = resourcePath.replace("{capture-id}", captureId);

		VDPResponse<ReverseVoidResponseVDP> response = null;

		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, voidCaptureRequestVDP,
					MethodTypes.POST, new HashMap<String, String>(), ReverseVoidResponseVDP.class);
		} catch (Exception e) {
			CustomLogger.logException(e);
			e.printStackTrace();
		}

		return response;

	}

	/**
	 * 
	 * @param captureId
	 * @return
	 */
	public VDPResponse<RetrieveCaptureByIdResponseVDP> getCaptureById(String captureId) {

		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_CAPTURE_RETRIEVE_BY_ID);
		resourcePath = resourcePath.replace("{capture-id}", captureId);

		VDPResponse<RetrieveCaptureByIdResponseVDP> response = null;
		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, null, MethodTypes.GET,
					new HashMap<String, String>(), RetrieveCaptureByIdResponseVDP.class);
		} catch (ForbiddenVDPException e) {
			CustomLogger.logException(e);
		}
		return response;
	}

	/**
	 * Returns a list of captures relevant to the user + app + card combination.
	 * 
	 * @param filterMetaData
	 * 
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CaptureResponse> getAllCapture(FilterMetaData filterMetaData) {

		List<CaptureResponse> results = new ArrayList<>();
		String resourcePath = visaProperties
				.getProperty(com.visa.innovation.paymentservice.vdp.utils.Property.RESOURCE_PATH_CAPTURES);
		List<?> finalResults = null;

		try {
			finalResults = getAllX(resourcePath, CaptureResponse.class, Constants.SERVICE_CAPTURE, filterMetaData);

		} catch (Exception e) {
			CustomLogger.logException(e);
		}

		results = (List<CaptureResponse>) finalResults;
		CustomLogger.log(results);
		return results;
	}

}
