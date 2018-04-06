package com.visa.innovation.paymentservice.service;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.exception.ForbiddenVDPException;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.utils.MethodTypes;
import com.visa.innovation.paymentservice.vdp.utils.Property;
import com.visa.innovation.paymentservice.vdp.utils.VisaAPIClient;
import com.visa.innovation.paymentservice.vdp.utils.VisaProperties;

/**
 * Provides methods which fetch the transaction result by Ids.
 * 
 * @author ntelukun
 *
 */
public class GetResponsesByIdUtils {

	@Autowired
	VisaAPIClient visaAPIClient;
	@Autowired
	VisaProperties visaProperties;

	String apiKeyVDP;
	String baseUri;
	String queryString;

	public GetResponsesByIdUtils(String apiKeyVDP, String baseUri, String queryString) {
		this.apiKeyVDP = apiKeyVDP;
		this.baseUri = baseUri;
		this.queryString = "apikey=" + apiKeyVDP;
	}

	public GetResponsesByIdUtils() {
	}

	@PostConstruct
	private void init() {
		this.apiKeyVDP = visaProperties.getProperty(Property.API_KEY);
		this.baseUri = visaProperties.getProperty(Property.BASE_URI);
		this.queryString = "apikey=" + apiKeyVDP;
	}

	/**
	 * Get Auth by id
	 * 
	 * @param id
	 * @return
	 */
	public VDPResponse<BaseRetrieveByIdResponseVDP> getVDPAuthResponseById(String authorizationId) {
		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_AUTH_RETRIEVE_BY_ID);
		resourcePath = resourcePath.replace("{authorization-id}", authorizationId);
		return getVDPResponseById(resourcePath);
	}

	/**
	 * Get Capture By Id
	 * 
	 * @param id
	 * @return
	 */

	public VDPResponse<BaseRetrieveByIdResponseVDP> getVDPCaptureResponseById(String captureId) {
		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_CAPTURE_RETRIEVE_BY_ID);
		resourcePath = resourcePath.replace("{capture-id}", captureId);
		return getVDPResponseById(resourcePath);
	}

	/**
	 * Get Sales by id
	 * 
	 * @param id
	 * @return
	 */
	public VDPResponse<BaseRetrieveByIdResponseVDP> getVDPResponseSaleById(String saleId) {
		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_SALE_RETRIEVE_BY_ID);
		resourcePath = resourcePath.replace("{sales-id}", saleId);
		return getVDPResponseById(resourcePath);
	}

	/**
	 * Makes a call to VDP to fetch auth/capture/sales by id
	 * 
	 * @param resourcePath
	 * @return
	 */
	private VDPResponse<BaseRetrieveByIdResponseVDP> getVDPResponseById(String resourcePath) {
		VDPResponse<BaseRetrieveByIdResponseVDP> response = null;
		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, null, MethodTypes.GET,
					new HashMap<String, String>(), BaseRetrieveByIdResponseVDP.class);
		} catch (ForbiddenVDPException e) {
			CustomLogger.logException(e);
		}
		return response;
	}

}
