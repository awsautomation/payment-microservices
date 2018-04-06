package com.visa.innovation.paymentservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.exception.ForbiddenVDPException;
import com.visa.innovation.paymentservice.model.auth.AuthResponse;
import com.visa.innovation.paymentservice.util.Constants;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.FilterResultsUtils;
import com.visa.innovation.paymentservice.util.Utils;
import com.visa.innovation.paymentservice.vdp.marshallers.FilterMetaData;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.auth.CreateAuthRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.auth.CreateAuthResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.auth.RetrieveAuthById;
import com.visa.innovation.paymentservice.vdp.model.auth.ReverseAuthRequestVDP;
import com.visa.innovation.paymentservice.vdp.utils.MethodTypes;
import com.visa.innovation.paymentservice.vdp.utils.Property;
import com.visa.innovation.paymentservice.vdp.utils.VisaAPIClient;
import com.visa.innovation.paymentservice.vdp.utils.VisaProperties;

public class AuthServiceVDP extends BaseService {

	@Autowired
	VisaAPIClient visaAPIClient;
	@Autowired
	VisaProperties VisaProperties;
	@Autowired
	FilterResultsUtils filterResults;
	@Autowired
	Utils utils;

	String apiKeyVDP;
	String baseUri;
	String queryString;

	public AuthServiceVDP() {

	}

	@PostConstruct
	private void init() {
		this.apiKeyVDP = VisaProperties.getProperty(Property.API_KEY);
		this.baseUri = VisaProperties.getProperty(Property.BASE_URI);
		this.queryString = "apikey=" + apiKeyVDP;
	}

	public VDPResponse<CreateAuthResponseVDP> createAuth(CreateAuthRequestVDP authRequestVDP) {

		String resourcePath = VisaProperties.getProperty(Property.RESOURCE_PATH_AUTH);
		VDPResponse<CreateAuthResponseVDP> response = null;
		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, authRequestVDP,
					MethodTypes.POST, new HashMap<String, String>(), CreateAuthResponseVDP.class);
		} catch (ForbiddenVDPException e) {
			CustomLogger.logException(e);
			// e.printStackTrace();
		}

		return response;
	}

	/**
	 * Gets auth By ID Usage : for create Auth
	 * 
	 * @param authorizationId
	 * @return
	 */
	public VDPResponse<RetrieveAuthById> getAuthById(String authorizationId) {

		String resourcePath = VisaProperties.getProperty(Property.RESOURCE_PATH_AUTH_RETRIEVE_BY_ID);
		resourcePath = resourcePath.replace("{authorization-id}", authorizationId);

		VDPResponse<RetrieveAuthById> response = null;
		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, null, MethodTypes.GET,
					new HashMap<String, String>(), RetrieveAuthById.class);
		} catch (ForbiddenVDPException e) {
			CustomLogger.logException(e);
			// e.printStackTrace();
		}

		return response;
	}

	public VDPResponse<ReverseVoidResponseVDP> updateAuth(ReverseAuthRequestVDP reverseAuthRequestVDP,
			String authorizationId) {

		String resourcePath = VisaProperties.getProperty(Property.RESOURCE_PATH_AUTH_VOID);
		resourcePath = resourcePath.replace("{authorization-id}", authorizationId);

		VDPResponse<ReverseVoidResponseVDP> response = null;

		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, reverseAuthRequestVDP,
					MethodTypes.POST, new HashMap<String, String>(), ReverseVoidResponseVDP.class);
		} catch (Exception e) {
			CustomLogger.logException(e);
			// e.printStackTrace();
		}

		return response;
	}

	/**
	 * 
	 * @param filterMetaData
	 *            TODO
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AuthResponse> getAllAuth(FilterMetaData filterMetaData) {

		List<AuthResponse> results = new ArrayList<>();
		String resourcePath = VisaProperties
				.getProperty(com.visa.innovation.paymentservice.vdp.utils.Property.RESOURCE_PATH_AUTH);
		List<?> finalResults = null;

		try {
			finalResults = getAllX(resourcePath, AuthResponse.class, Constants.SERVICE_AUTH, filterMetaData);
		} catch (Exception e) {
			CustomLogger.logException(e);
		}

		results = (List<AuthResponse>) finalResults;
		CustomLogger.log(results);
		return results;
	}
}
