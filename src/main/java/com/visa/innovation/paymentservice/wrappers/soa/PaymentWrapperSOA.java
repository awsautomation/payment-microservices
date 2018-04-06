package com.visa.innovation.paymentservice.wrappers.soa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.auth.AuthRequest;
import com.visa.innovation.paymentservice.model.auth.AuthResponse;
import com.visa.innovation.paymentservice.model.capture.CaptureRequest;
import com.visa.innovation.paymentservice.model.capture.CaptureResponse;
import com.visa.innovation.paymentservice.model.sales.SalesRequest;
import com.visa.innovation.paymentservice.model.sales.SalesResponse;
import com.visa.innovation.paymentservice.service.AuthServiceVDP;
import com.visa.innovation.paymentservice.service.SaleServiceVDP;
import com.visa.innovation.paymentservice.soa.model.auth.AuthRequestSOA;
import com.visa.innovation.paymentservice.soa.model.auth.AuthResponseSOA;
import com.visa.innovation.paymentservice.soa.model.capture.CaptureRequestSOA;
import com.visa.innovation.paymentservice.soa.model.capture.CaptureResponseSOA;
import com.visa.innovation.paymentservice.soa.model.sale.SaleRequestSOA;
import com.visa.innovation.paymentservice.soa.model.sale.SaleResponseSOA;
import com.visa.innovation.paymentservice.util.Constants;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.SOAConnectionsUtils;
import com.visa.innovation.paymentservice.util.SOADataMarshaller;
import com.visa.innovation.paymentservice.util.Utils;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.auth.RetrieveAuthById;
import com.visa.innovation.paymentservice.vdp.model.sale.RetrieveSaleByIdResponseVDP;

@Component
public class PaymentWrapperSOA {

	@Autowired
	SOADataMarshaller soaDataMarshaller;
	@Autowired
	SOAConnectionsUtils soaConnectionsUtils;
	@Autowired
	SaleServiceVDP saleServiceVDP;
	@Autowired
	AuthServiceVDP authServiceVDP;
	@Autowired
	Utils utils;

	public ServiceResponse<CaptureResponse> createCapture(CaptureRequest request, String userId, String apiKey,
			String cardId) {

		CustomLogger.log("Capture request received");
		CustomLogger.log(request);

		VDPResponse<RetrieveAuthById> getAuthByIdResponse = null;

		CaptureRequestSOA captureRequestSOA = soaDataMarshaller.buildServiceCaptureRequest(request, userId, apiKey,
				cardId);

		CaptureResponseSOA captureResponseSOA = soaConnectionsUtils.makeSOARequest(captureRequestSOA,
				CaptureResponseSOA.class);

		if (Constants.DECISION_ACCEPT.equalsIgnoreCase(captureResponseSOA.getDecision())) {
			getAuthByIdResponse = authServiceVDP.getAuthById(request.getAuthorizationId());
			// invalidate cache
			utils.invalidateCache(Constants.SERVICE_CAPTURE, userId, apiKey, cardId);
		}

		ServiceResponse<CaptureResponse> response = soaDataMarshaller.buildCaptureResponse(request, captureResponseSOA,
				getAuthByIdResponse);

		return response;
	}

	public ServiceResponse<AuthResponse> createAuth(AuthRequest authRequest, String userId, String apiKey,
			String cardId) {

		CustomLogger.log("Auth request received");
		CustomLogger.log(authRequest);

		VDPResponse<RetrieveAuthById> getAuthByIdResponse = null;

		AuthRequestSOA requestMessageSOA = soaDataMarshaller.buildServiceAuthRequest(authRequest, userId, apiKey,
				cardId);

		AuthResponseSOA soaResponse = soaConnectionsUtils.makeSOARequest(requestMessageSOA, AuthResponseSOA.class);

		if (Constants.DECISION_ACCEPT.equalsIgnoreCase(soaResponse.getDecision())) {
			getAuthByIdResponse = authServiceVDP.getAuthById(soaResponse.getRequestID());
			// invalidate cache
			utils.invalidateCache(Constants.SERVICE_AUTH, userId, apiKey, cardId);
		}
		ServiceResponse<AuthResponse> response = soaDataMarshaller.buildAuthResponse(authRequest, soaResponse,
				getAuthByIdResponse);

		return response;
	}

	public ServiceResponse<SalesResponse> createSale(SalesRequest request, String userId, String apiKey,
			String cardId) {

		CustomLogger.log("Sale request received");
		CustomLogger.log(request);

		VDPResponse<RetrieveSaleByIdResponseVDP> getSaleByIdResponse = null;

		SaleRequestSOA saleRequestSOA = soaDataMarshaller.buildServiceSaleRequest(request, userId, apiKey, cardId);

		SaleResponseSOA saleResponseSOA = soaConnectionsUtils.makeSOARequest(saleRequestSOA, SaleResponseSOA.class);

		if (Constants.DECISION_ACCEPT.equalsIgnoreCase(saleResponseSOA.getDecision())) {
			getSaleByIdResponse = saleServiceVDP.getSaleById(saleResponseSOA.getRequestID());
			// invalidate cache
			utils.invalidateCache(Constants.SERVICE_SALES, userId, apiKey, cardId);
		}

		ServiceResponse<SalesResponse> response = soaDataMarshaller.buildSaleResponse(request, saleResponseSOA,
				getSaleByIdResponse);

		return response;
	}

}
