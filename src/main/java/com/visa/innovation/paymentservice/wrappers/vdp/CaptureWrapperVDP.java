package com.visa.innovation.paymentservice.wrappers.vdp;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.ValueType;
import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.exception.GenericVDPException;
import com.visa.innovation.paymentservice.exception.TransactionNotFoundException;
import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.UpdatePaymentRequest;
import com.visa.innovation.paymentservice.model.capture.CaptureRequest;
import com.visa.innovation.paymentservice.model.capture.CaptureResponse;
import com.visa.innovation.paymentservice.odata.service.EdmConstants;
import com.visa.innovation.paymentservice.service.CaptureServiceVDP;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.FilterResultsUtils;
import com.visa.innovation.paymentservice.vdp.marshallers.FilterMetaData;
import com.visa.innovation.paymentservice.vdp.marshallers.VDPDataMarshaller;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.VoidRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.capture.CreateCaptureRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.capture.CreateCaptureResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.capture.RetrieveCaptureByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.utils.Property;
import com.visa.innovation.paymentservice.vdp.utils.VisaAPIClient;
import com.visa.innovation.paymentservice.vdp.utils.VisaProperties;

public class CaptureWrapperVDP {

	@Autowired
	VisaAPIClient visaAPIClient;
	@Autowired
	VDPDataMarshaller vdpDataMarshaller;
	@Autowired
	CaptureServiceVDP captureServiceVDP;
	@Autowired
	FilterResultsUtils filterUtils;
	@Autowired
	VisaProperties visaProperties;

	String apiKeyVDP;
	String baseUri;
	String queryString;

	public CaptureWrapperVDP() {
	}

	@PostConstruct
	private void init() {
		this.apiKeyVDP = visaProperties.getProperty(Property.API_KEY);
		this.baseUri = visaProperties.getProperty(Property.BASE_URI);
		this.queryString = "apikey=" + apiKeyVDP;
	}

	public ServiceResponse<CaptureResponse> createCapture(CaptureRequest request) {
		CustomLogger.log("Capture request received");

		CreateCaptureRequestVDP captureRequestVDP = vdpDataMarshaller.buildServiceCaptureRequest(request);

		VDPResponse<CreateCaptureResponseVDP> response = null;
		response = captureServiceVDP.createCapture(captureRequestVDP, request.getAuthorizationId());

		ServiceResponse<CaptureResponse> serviceResponse = vdpDataMarshaller.buildCaptureResponse(response);
		return serviceResponse;
	}

	public ServiceResponse<CaptureResponse> updateCapture(UpdatePaymentRequest updateCaptureRequest, String captureId)
			throws TransactionNotFoundException {

		VDPResponse<RetrieveCaptureByIdResponseVDP> retrieveCaptureResponse = null;
		VoidRequestVDP voidCaptureRequestVDP = null;
		ServiceResponse<CaptureResponse> updateCaptureResponse = null;

		retrieveCaptureResponse = captureServiceVDP.getCaptureById(captureId);
		if (retrieveCaptureResponse.getVdpError() == null) {
			voidCaptureRequestVDP = vdpDataMarshaller.buildServiceVoidRequest(updateCaptureRequest,
					retrieveCaptureResponse.getResponse());

			VDPResponse<ReverseVoidResponseVDP> response = null;
			response = captureServiceVDP.updateCapture(voidCaptureRequestVDP, captureId);

			updateCaptureResponse = vdpDataMarshaller.buildupdateCaptureResponse(response,
					retrieveCaptureResponse.getResponse());
		} else {
			throw new TransactionNotFoundException(captureId, "Capture");
		}
		return updateCaptureResponse;
	}

	/**
	 * Makes a call to captureService to fetch VDPResponse for Retrieve Capture
	 * by Id.
	 * 
	 * Converts the response to Odata Entity and returns the entity
	 * 
	 * @param captureId
	 * @return
	 * @throws GenericVDPException
	 */
	public Entity getCaptureById(String captureId) throws GenericVDPException {
		CustomLogger.log("Get Auth by Id request received");
		String resourcePath = visaProperties.getProperty(
				com.visa.innovation.paymentservice.vdp.utils.Property.RESOURCE_PATH_CAPTURE_RETRIEVE_BY_ID);
		resourcePath = resourcePath.replace("{capture-id}", captureId);
		VDPResponse<RetrieveCaptureByIdResponseVDP> response = null;
		Entity entity = null;
		CustomLogger.log("Get Auth by Id request received");

		response = captureServiceVDP.getCaptureById(captureId);
		if (response.getVdpError() == null) {
			CaptureResponse captureResponse = vdpDataMarshaller.buildCaptureResponse(response.getResponse());
			entity = convertToODataEntity(captureResponse);
		}
		return entity;
	}

	/**
	 * Creates a entity collection from a list of capture responses.
	 * 
	 * @param filterMetaData
	 *            TODO
	 * 
	 * @return
	 */

	public EntityCollection captureEntityCollection(FilterMetaData filterMetaData) {

		List<CaptureResponse> results = captureServiceVDP.getAllCapture(filterMetaData);
		EntityCollection retEntitySet = new EntityCollection();
		if (results != null) {
			for (CaptureResponse response : results) {
				if (response != null) {
					Entity retEntity = convertToODataEntity(response);
					retEntitySet.getEntities().add(retEntity);
				}
			}
		}
		return retEntitySet;
	}

	/**
	 * Converts to Capture Entity object
	 * 
	 * @param captureResponse
	 * @return
	 */
	private Entity convertToODataEntity(CaptureResponse captureResponse) {

		Entity entity = new Entity();

		if (captureResponse.getStatus() != null) {
			entity.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.status,
					ValueType.PRIMITIVE, captureResponse.getStatus()));
		}
		entity.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.captureId,
				ValueType.PRIMITIVE, captureResponse.getCaptureId()))
				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.amount,
						ValueType.PRIMITIVE, captureResponse.getAmount()))
				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.currency,
						ValueType.PRIMITIVE, captureResponse.getCurrency()))
				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.payment,
						ValueType.PRIMITIVE, captureResponse.getPayment()))
				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.orderId,
						ValueType.PRIMITIVE, captureResponse.getOrderId()))
				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.created,
						ValueType.PRIMITIVE, captureResponse.getCreated()))

				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.merchantId,
						ValueType.PRIMITIVE, captureResponse.getMerchantId()));

		entity.setId(com.visa.innovation.paymentservice.odata.service.ODataUtil.createId("capture",
				captureResponse.getCaptureId()));
		return entity;
	}

}
