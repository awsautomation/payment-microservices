package com.visa.innovation.paymentservice.wrappers.vdp;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.exception.GenericVDPException;
import com.visa.innovation.paymentservice.exception.TransactionNotFoundException;
import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.UpdatePaymentRequest;
import com.visa.innovation.paymentservice.model.auth.AuthRequest;
import com.visa.innovation.paymentservice.model.auth.AuthResponse;
import com.visa.innovation.paymentservice.odata.service.EdmConstants;
import com.visa.innovation.paymentservice.service.AuthServiceVDP;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.FilterResultsUtils;
import com.visa.innovation.paymentservice.vdp.marshallers.FilterMetaData;
import com.visa.innovation.paymentservice.vdp.marshallers.VDPDataMarshaller;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.auth.CreateAuthRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.auth.CreateAuthResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.auth.RetrieveAuthById;
import com.visa.innovation.paymentservice.vdp.model.auth.ReverseAuthRequestVDP;
import com.visa.innovation.paymentservice.vdp.utils.VisaAPIClient;
import com.visa.innovation.paymentservice.vdp.utils.VisaProperties;

public class AuthWrapperVDP {

	// TODO : Move the api key to VisaAPIClient. the doXPayTokenRequest should
	// only take in queryParams if needed.
	@Autowired
	VisaAPIClient visaAPIClient;
	@Autowired
	VisaProperties visaProperties;
	@Autowired
	VDPDataMarshaller vdpDataMarshaller;
	@Autowired
	FilterResultsUtils filterUtils;
	@Autowired
	AuthServiceVDP authServiceVDP;

	String apiKeyVDP;
	String baseUri;
	String queryString;

	public AuthWrapperVDP() {

	}

	@PostConstruct
	private void init() {
		this.apiKeyVDP = visaProperties.getProperty(com.visa.innovation.paymentservice.vdp.utils.Property.API_KEY);
		this.baseUri = visaProperties.getProperty(com.visa.innovation.paymentservice.vdp.utils.Property.BASE_URI);
		this.queryString = "apikey=" + apiKeyVDP;
	}

	public ServiceResponse<AuthResponse> createAuth(AuthRequest authRequest) {

		CustomLogger.log("Auth request received");
		CustomLogger.log(authRequest);

		CreateAuthRequestVDP authRequestVDP = vdpDataMarshaller.buildServiceAuthRequest(authRequest);

		VDPResponse<CreateAuthResponseVDP> response = null;
		response = authServiceVDP.createAuth(authRequestVDP);

		ServiceResponse<AuthResponse> serviceResponse = vdpDataMarshaller.buildAuthResponse(response);
		return serviceResponse;
	}

	/**
	 * Makes a call to authService to fetch VDPResponse for Retrieve Auth by Id.
	 * 
	 * Converts the response to Odata Entity and returns the entity
	 * 
	 * @param authorizationId
	 * @return
	 * @throws GenericVDPException
	 */
	public Entity getAuthById(String authorizationId) throws GenericVDPException {
		CustomLogger.log("Get Auth by Id request received");
		String resourcePath = visaProperties
				.getProperty(com.visa.innovation.paymentservice.vdp.utils.Property.RESOURCE_PATH_AUTH_RETRIEVE_BY_ID);
		resourcePath = resourcePath.replace("{authorization-id}", authorizationId);
		VDPResponse<RetrieveAuthById> response = null;
		Entity entity = null;
		CustomLogger.log("Get Auth by Id request received");

		response = authServiceVDP.getAuthById(authorizationId);
		if (response.getVdpError() == null) {
			AuthResponse authResponse = vdpDataMarshaller.buildAuthResponse(response.getResponse());
			entity = convertToODataEntity(authResponse);
		}
		return entity;
	}

	public ServiceResponse<AuthResponse> updateAuth(UpdatePaymentRequest updateAuthRequest, String authorizationId)
			throws TransactionNotFoundException {
		CustomLogger.log("Update auth request received");

		String resourcePath = visaProperties
				.getProperty(com.visa.innovation.paymentservice.vdp.utils.Property.RESOURCE_PATH_AUTH_VOID);
		resourcePath = resourcePath.replace("{authorization-id}", authorizationId);

		VDPResponse<RetrieveAuthById> retrieveAuthResponse = null;
		ReverseAuthRequestVDP reverseAuthRequestVDP = null;
		ServiceResponse<AuthResponse> updateAuthResponse = null;

		retrieveAuthResponse = authServiceVDP.getAuthById(authorizationId);

		if (retrieveAuthResponse.getVdpError() == null) {

			reverseAuthRequestVDP = vdpDataMarshaller.buildServiceReverseAuthRequest(updateAuthRequest,
					retrieveAuthResponse.getResponse());

			VDPResponse<ReverseVoidResponseVDP> response = null;
			response = authServiceVDP.updateAuth(reverseAuthRequestVDP, authorizationId);

			updateAuthResponse = vdpDataMarshaller.buildUpdateAuthResponse(response,
					retrieveAuthResponse.getResponse());

		} else {
			throw new TransactionNotFoundException(authorizationId, "Authorization");
		}

		return updateAuthResponse;
	}

	/**
	 * Converts auth response object to Odata Entity object
	 * 
	 * @param authResponse
	 * @return
	 */
	private Entity convertToODataEntity(AuthResponse authResponse) {

		Entity entity = new Entity();
		// Property payment = null;
		if (authResponse.getPayment() != null) {
			entity.addProperty(
					new Property(null, EdmConstants.payment, ValueType.PRIMITIVE, authResponse.getPayment()));
		}
		entity.addProperty(new Property(null, EdmConstants.authorizationId, ValueType.PRIMITIVE,
				authResponse.getAuthorizationId()))
				.addProperty(new Property(null, EdmConstants.amount, ValueType.PRIMITIVE, authResponse.getAmount()))
				.addProperty(new Property(null, EdmConstants.currency, ValueType.PRIMITIVE, authResponse.getCurrency()))
				.addProperty(new Property(null, EdmConstants.created, ValueType.PRIMITIVE, authResponse.getCreated()))
				.addProperty(new Property(null, EdmConstants.status, ValueType.PRIMITIVE, authResponse.getStatus()))
				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.orderId,
						ValueType.PRIMITIVE, authResponse.getOrderId()));

		entity.setId(com.visa.innovation.paymentservice.odata.service.ODataUtil.createId("authorizations",
				authResponse.getAuthorizationId()));
		return entity;
	}

	/**
	 * Creates a list of all auth entities.
	 * 
	 * @param filterMetaData
	 *            TODO
	 * 
	 * @return
	 */
	public EntityCollection authEntityCollection(FilterMetaData filterMetaData) {

		List<AuthResponse> results = authServiceVDP.getAllAuth(filterMetaData);
		EntityCollection retEntitySet = new EntityCollection();
		if (results != null) {
			for (AuthResponse response : results) {
				if (response != null) {
					Entity retEntity = convertToODataEntity(response);
					retEntitySet.getEntities().add(retEntity);
				}
			}
		}
		return retEntitySet;
	}
}
