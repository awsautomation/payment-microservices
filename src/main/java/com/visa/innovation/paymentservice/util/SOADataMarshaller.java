package com.visa.innovation.paymentservice.util;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.model.Expiry;
import com.visa.innovation.paymentservice.model.PaymentResponse;
import com.visa.innovation.paymentservice.model.ServiceError;
import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.auth.AuthRequest;
import com.visa.innovation.paymentservice.model.auth.AuthResponse;
import com.visa.innovation.paymentservice.model.capture.CaptureRequest;
import com.visa.innovation.paymentservice.model.capture.CaptureResponse;
import com.visa.innovation.paymentservice.model.sales.SalesRequest;
import com.visa.innovation.paymentservice.model.sales.SalesResponse;
import com.visa.innovation.paymentservice.soa.model.BasePaymentResponseSOA;
import com.visa.innovation.paymentservice.soa.model.MDDField;
import com.visa.innovation.paymentservice.soa.model.MerchantDefinedData;
import com.visa.innovation.paymentservice.soa.model.PaySubscriptionServiceSOA;
import com.visa.innovation.paymentservice.soa.model.PurchaseTotalsSOA;
import com.visa.innovation.paymentservice.soa.model.RecurringSubscriptionInfoSOA;
import com.visa.innovation.paymentservice.soa.model.UserDetailsSOA;
import com.visa.innovation.paymentservice.soa.model.auth.AuthRequestSOA;
import com.visa.innovation.paymentservice.soa.model.auth.AuthResponseSOA;
import com.visa.innovation.paymentservice.soa.model.capture.CaptureRequestSOA;
import com.visa.innovation.paymentservice.soa.model.capture.CaptureResponseSOA;
import com.visa.innovation.paymentservice.soa.model.capture.PaySubscriptionServiceCaptureSOA;
import com.visa.innovation.paymentservice.soa.model.sale.SaleRequestSOA;
import com.visa.innovation.paymentservice.soa.model.sale.SaleResponseSOA;
import com.visa.innovation.paymentservice.vdp.model.PaymentVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.auth.RetrieveAuthById;
import com.visa.innovation.paymentservice.vdp.model.sale.RetrieveSaleByIdResponseVDP;

@Component
public class SOADataMarshaller {

	@Autowired
	Utils utils;

	/**
	 * Builds final auth response from SOA auth response
	 * 
	 * @param authRequest
	 *            incoming request from client
	 * @param authResponseSOA
	 *            auth response from SOA
	 * @return
	 */

	public ServiceResponse<AuthResponse> buildAuthResponse(AuthRequest authRequest, AuthResponseSOA authResponseSOA,
			VDPResponse<RetrieveAuthById> getAuthByIdResponse) {

		ServiceResponse<AuthResponse> response = null;
		PaymentResponse paymentResponse = null;

		if (Constants.DECISION_ACCEPT.equalsIgnoreCase(authResponseSOA.getDecision())) {

			String orderId = utils.getOrderId(authResponseSOA.getMerchantReferenceCode());

			if (getAuthByIdResponse.getVdpError() == null) {
				PaymentVDP paymentVDP = getAuthByIdResponse.getResponse().getPayment();
				paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
						.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear()))
						.type(paymentVDP.getCardType()).build();
			}

			AuthResponse authResponse = new AuthResponse.Builder().authorizationId(authResponseSOA.getRequestID())
					.amount(Double.parseDouble(authResponseSOA.getCcAuthReply().getAmount()))
					.currency(authResponseSOA.getPurchaseTotals().getCurrency()).orderId(orderId)
					.created(authResponseSOA.getCcAuthReply().getCreatedAt()).payment(paymentResponse)
					.status(utils.getServiceStatus(Constants.STATUS_PENDING_CAPTURE_VDP)).build();

			response = new ServiceResponse<AuthResponse>(authResponse);
		} else {
			ServiceError error = generateServiceError(authResponseSOA);
			response = new ServiceResponse<AuthResponse>(error);
		}
		CustomLogger.log(response);
		return response;
	}

	public AuthRequestSOA buildServiceAuthRequest(AuthRequest authRequest, String userId, String apiKey,
			String cardId) {

		String hybridId = utils.generateHybridId(userId, apiKey, authRequest.getOrderId(), cardId);

		AuthRequestSOA request = new AuthRequestSOA();
		request.setMerchantReferenceCode(hybridId);
		request.setXmlns(Constants.SOA_XMLNS);
		request.setPurchaseTotals(new PurchaseTotalsSOA(authRequest.getCurrency(), authRequest.getAmount().toString()));
		request.setCcAuthService(new PaySubscriptionServiceSOA("true"));
		request.setRecurringSubscriptionInfo(new RecurringSubscriptionInfoSOA(authRequest.getPayment().getToken()));

		if (authRequest.getBillingAddress() != null) {
			UserDetailsSOA billTo = new UserDetailsSOA.Builder()
					.firstName(authRequest.getBillingAddress().getFirstName())
					.lastName(authRequest.getBillingAddress().getLastName())
					.street1(authRequest.getBillingAddress().getStreet1())
					.city(authRequest.getBillingAddress().getCity()).state(authRequest.getBillingAddress().getState())
					.country(authRequest.getBillingAddress().getCountry())
					.postalCode(authRequest.getBillingAddress().getPostalCode())
					.email(authRequest.getBillingAddress().getEmail()).build();
			request.setBillTo(billTo);
		}

		ArrayList<MDDField> mddFields = new ArrayList<>();
		mddFields.add(new MDDField(1, authRequest.getOrderId()));
		mddFields.add(new MDDField(2, authRequest.getMerchantId()));
		request.setMerchantDefinedData(new MerchantDefinedData(mddFields));

		return request;

	}

	/**
	 * Builds final capture response from SOA capture response
	 * 
	 * @param captureRequest
	 *            incoming request from client
	 * @param captureResponseSOA
	 *            capture response from SOA
	 * @return
	 */
	public ServiceResponse<CaptureResponse> buildCaptureResponse(CaptureRequest request,
			CaptureResponseSOA captureResponseSOA, VDPResponse<RetrieveAuthById> getAuthByIdResponse) {

		ServiceResponse<CaptureResponse> response = null;
		PaymentResponse paymentResponse = null;

		if (Constants.DECISION_ACCEPT.equalsIgnoreCase(captureResponseSOA.getDecision())) {

			String orderId = utils.getOrderId(captureResponseSOA.getMerchantReferenceCode());

			if (getAuthByIdResponse.getVdpError() == null) {
				PaymentVDP paymentVDP = getAuthByIdResponse.getResponse().getPayment();
				paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
						.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear()))
						.type(paymentVDP.getCardType()).build();
			}

			CaptureResponse captureResponse = new CaptureResponse.Builder().amount(request.getAmount())
					.currency(captureResponseSOA.getPurchaseTotals().getCurrency())
					.authorizationId(request.getAuthorizationId()).captureId(captureResponseSOA.getRequestID())
					.created(captureResponseSOA.getCcCaptureReply().getCreatedAt()).orderId(orderId)
					.status(utils.getServiceStatus(Constants.STATUS_PENDING_SETTLEMENT_VDP)).payment(paymentResponse)
					.build();

			response = new ServiceResponse.Builder<CaptureResponse>().response(captureResponse).build();
		} else {
			ServiceError error = generateServiceError(captureResponseSOA);
			response = new ServiceResponse<CaptureResponse>(error);
		}
		CustomLogger.log(response);
		return response;
	}

	public CaptureRequestSOA buildServiceCaptureRequest(CaptureRequest captureRequest, String userId, String apiKey,
			String cardId) {

		String hybridId = utils.generateHybridId(userId, apiKey, captureRequest.getOrderId(), cardId);

		CaptureRequestSOA request = new CaptureRequestSOA();
		request.setXmlns(Constants.SOA_XMLNS);
		request.setMerchantReferenceCode(hybridId);
		request.setCcCaptureService(new PaySubscriptionServiceCaptureSOA("true", captureRequest.getAuthorizationId()));
		request.setPurchaseTotals(
				new PurchaseTotalsSOA(captureRequest.getCurrency(), captureRequest.getAmount().toString()));

		ArrayList<MDDField> mddFields = new ArrayList<>();
		mddFields.add(new MDDField(1, captureRequest.getOrderId()));
		request.setMerchantDefinedData(new MerchantDefinedData(mddFields));
		return request;
	}

	public SaleRequestSOA buildServiceSaleRequest(SalesRequest salesRequest, String userId, String apiKey,
			String cardId) {

		String hybridId = utils.generateHybridId(userId, apiKey, salesRequest.getOrderId(), cardId);

		SaleRequestSOA request = new SaleRequestSOA();
		request.setXmlns(Constants.SOA_XMLNS);
		request.setMerchantReferenceCode(hybridId);
		request.setCcAuthService(new PaySubscriptionServiceSOA("true"));
		request.setCcCaptureService(new PaySubscriptionServiceSOA("true"));
		request.setPurchaseTotals(
				new PurchaseTotalsSOA(salesRequest.getCurrency(), salesRequest.getAmount().toString()));
		request.setRecurringSubscriptionInfo(new RecurringSubscriptionInfoSOA(salesRequest.getPayment().getToken()));

		if (salesRequest.getBillingAddress() != null) {
			UserDetailsSOA billTo = new UserDetailsSOA.Builder()
					.firstName(salesRequest.getBillingAddress().getFirstName())
					.lastName(salesRequest.getBillingAddress().getLastName())
					.street1(salesRequest.getBillingAddress().getStreet1())
					.city(salesRequest.getBillingAddress().getCity()).state(salesRequest.getBillingAddress().getState())
					.country(salesRequest.getBillingAddress().getCountry())
					.postalCode(salesRequest.getBillingAddress().getPostalCode())
					.email(salesRequest.getBillingAddress().getEmail()).build();
			request.setBillTo(billTo);
		}

		ArrayList<MDDField> mddFields = new ArrayList<>();
		mddFields.add(new MDDField(1, salesRequest.getOrderId()));
		mddFields.add(new MDDField(2, salesRequest.getMerchantId()));
		request.setMerchantDefinedData(new MerchantDefinedData(mddFields));
		return request;
	}

	/**
	 * Builds final sale response from SOA sale response
	 * 
	 * @param saleRequest
	 *            incoming request from client
	 * @param saleResponseSOA
	 *            sale response from SOA
	 * @return
	 */
	public ServiceResponse<SalesResponse> buildSaleResponse(SalesRequest request, SaleResponseSOA saleResponseSOA,
			VDPResponse<RetrieveSaleByIdResponseVDP> getSaleByIdResponse) {

		ServiceResponse<SalesResponse> response = null;
		PaymentResponse paymentResponse = null;

		if (Constants.DECISION_ACCEPT.equalsIgnoreCase(saleResponseSOA.getDecision())) {

			String orderId = utils.getOrderId(saleResponseSOA.getMerchantReferenceCode());

			if (getSaleByIdResponse.getVdpError() == null) {
				PaymentVDP paymentVDP = getSaleByIdResponse.getResponse().getPayment();
				paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
						.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear()))
						.type(paymentVDP.getCardType()).build();
			}

			// TODO: updated, valid until, merchantId
			SalesResponse salesResponse = new SalesResponse.Builder().saleId(saleResponseSOA.getRequestID())
					.amount(Double.parseDouble(saleResponseSOA.getCcAuthReply().getAmount()))
					.currency(saleResponseSOA.getPurchaseTotals().getCurrency()).orderId(orderId)
					.created(saleResponseSOA.getCcAuthReply().getCreatedAt()).payment(paymentResponse)
					.status(utils.getServiceStatus(Constants.STATUS_PENDING_SETTLEMENT_VDP)).build();

			response = new ServiceResponse.Builder<SalesResponse>().response(salesResponse).build();
		} else {
			ServiceError error = generateServiceError(saleResponseSOA);
			response = new ServiceResponse<SalesResponse>(error);
		}

		CustomLogger.log(response);
		return response;
	}

	// TODO: better error handling design later
	public ServiceError generateServiceError(BasePaymentResponseSOA soaResponse) {
		String message = null;
		String code = Integer.toString(soaResponse.getReasonCode());
		String status = null;
		int reasonCode = soaResponse.getReasonCode();

		if (reasonCode == 102) {
			if (soaResponse.getInvalidField() != null) {
				message = "Invalid field(s): " + soaResponse.getInvalidField();
			} else {
				message = "Invalid field(s) in request";
			}
			status = HttpStatus.BAD_REQUEST.toString();
		} else if (reasonCode == 101) {
			message = "Missing fields: " + soaResponse.getMissingField();
			status = HttpStatus.BAD_REQUEST.toString();
		} else if (reasonCode == 150 || reasonCode == 151 || reasonCode == 152
				|| "ERROR".equalsIgnoreCase(soaResponse.getDecision())) {
			message = "Underlying payment service failed. Please try again.";
			status = HttpStatus.SERVICE_UNAVAILABLE.toString();
		} else if (reasonCode == 202) {
			message = "Card expired";
			status = HttpStatus.BAD_REQUEST.toString();
		} else if (reasonCode == 204) {
			message = "Insufficient funds";
			status = HttpStatus.BAD_REQUEST.toString();
		} else if (reasonCode == 210) {
			message = "Credit limit reached";
			status = HttpStatus.BAD_REQUEST.toString();
		} else if (reasonCode == 235) {
			message = "The requested capture amount exceeds the originally authorized amount.";
			status = HttpStatus.BAD_REQUEST.toString();
		} else if (reasonCode == 243) {
			message = "The transaction has already been settled or reversed.";
			status = HttpStatus.BAD_REQUEST.toString();
		} else {
			message = "Error from underlying card service";
			status = HttpStatus.SERVICE_UNAVAILABLE.toString();
		}
		return new ServiceError(code, status, message);
	}
}
