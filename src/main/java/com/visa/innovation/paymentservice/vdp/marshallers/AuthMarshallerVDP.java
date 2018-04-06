package com.visa.innovation.paymentservice.vdp.marshallers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.model.Address;
import com.visa.innovation.paymentservice.model.Expiry;
import com.visa.innovation.paymentservice.model.PaymentResponse;
import com.visa.innovation.paymentservice.model.ServiceError;
import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.UpdatePaymentRequest;
import com.visa.innovation.paymentservice.model.auth.AuthRequest;
import com.visa.innovation.paymentservice.model.auth.AuthResponse;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.Utils;
import com.visa.innovation.paymentservice.vdp.model.AddressVDP;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveAllVDP;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.PaymentVDP;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPError;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.auth.CreateAuthRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.auth.CreateAuthResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.auth.RetrieveAuthById;
import com.visa.innovation.paymentservice.vdp.model.auth.ReverseAuthRequestVDP;

/**
 * The Class Responsibility is to collate the results from the VDP 'Retrieve all
 * authorizations API and Retrieve by authorization Id and map it to fields of
 * Payment service Authorization Model.
 * 
 * Gets Filtered responses from the PaymentWrapper Utilizes the methods for
 * retrieve a auth by id for each of the filtered result to get more details
 * about the auth and mapps to the authresponse
 * 
 * @author ntelukun
 *
 */
@Component
public class AuthMarshallerVDP {

	@Autowired
	Utils utils;

	public ServiceResponse<AuthResponse> buildAuthResponse(VDPResponse<CreateAuthResponseVDP> authResponseVDP) {

		ServiceResponse<AuthResponse> response = null;

		// TODO: remove builder pattern
		if (authResponseVDP.getVdpError() != null) {
			VDPError vdpError = authResponseVDP.getVdpError();
			response = new ServiceResponse.Builder<AuthResponse>().serviceError(new ServiceError.Builder()
					.code(vdpError.getResponseStatusVDP().getCode()).status(vdpError.getResponseStatusVDP().getStatus())
					.message(vdpError.getResponseStatusVDP().getMessage()).build()).build();
		} else {
			CreateAuthResponseVDP vdpResponse = authResponseVDP.getResponse();
			PaymentVDP paymentVDP = vdpResponse.getPayment();
			PaymentResponse paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
					.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear()))
					.type(paymentVDP.getCardType()).name("TODO").build();
			HashMap<String, String> merchantDefinedData = vdpResponse.getMerchantDefinedData();
			CustomLogger.log(merchantDefinedData);
			// TODO: error handling for parse double
			AuthResponse authResponse = new AuthResponse.Builder().amount(Double.parseDouble(vdpResponse.getAmount()))
					.authorizationId(vdpResponse.getId()).orderId(vdpResponse.getReferenceId()).payment(paymentResponse)
					.currency(vdpResponse.getCurrency()).created("TODO").updated("TODO").validUntil("TODO")
					.status(utils.getServiceStatus(vdpResponse.getStatus())).merchantId("TODO").build();
			response = new ServiceResponse<AuthResponse>(authResponse);
		}

		CustomLogger.log(response);
		return response;
	}

	public AuthResponse buildAuthResponse(BaseRetrieveAllVDP vdpAuthResponse,
			BaseRetrieveByIdResponseVDP vdpAuthByIdResponse) {

		AuthResponse authResponse = null;
		if (vdpAuthResponse != null && vdpAuthByIdResponse != null) {
			PaymentVDP paymentVDP = vdpAuthByIdResponse.getPayment();
			PaymentResponse paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
					.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear()))
					.type(paymentVDP.getCardType()).name("TODO").build();
			authResponse = new AuthResponse.Builder().authorizationId(vdpAuthResponse.getId())
					.amount(vdpAuthResponse.getAmount()).currency(vdpAuthResponse.getCurrency())
					.payment(paymentResponse).created(utils.stringMillisToDate(vdpAuthResponse.getCreated()))
					.status(utils.getServiceStatus(vdpAuthByIdResponse.getStatus())).merchantId("default")
					.orderId(utils.getOrderId(vdpAuthResponse.getReferenceId())).build();
		}
		return authResponse;
	}

	public AuthResponse buildAuthResponse(BaseRetrieveAllVDP vdpAuthResponse) {

		AuthResponse authResponse = null;
		if (vdpAuthResponse != null) {
			// PaymentVDP paymentVDP = vdpAuthByIdResponse.getPayment();
			// PaymentResponse paymentResponse = new
			// PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
			// .expiry(new Expiry(paymentVDP.getExpirationMonth(),
			// paymentVDP.getExpirationYear()))
			// .type(paymentVDP.getCardType()).name("TODO").build();
			authResponse = new AuthResponse.Builder().authorizationId(vdpAuthResponse.getId())
					.amount(vdpAuthResponse.getAmount()).currency(vdpAuthResponse.getCurrency())
					.created(utils.stringMillisToDate(vdpAuthResponse.getCreated())).merchantId("default")
					.orderId(utils.getOrderId(vdpAuthResponse.getReferenceId())).build();
		}
		return authResponse;
	}

	public AuthResponse buildAuthResponse(BaseRetrieveByIdResponseVDP vdpAuthbyIdResponse) {

		AuthResponse authResponse = null;
		PaymentVDP paymentVDP = vdpAuthbyIdResponse.getPayment();
		PaymentResponse paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
				.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear()))
				.type(paymentVDP.getCardType()).name("TODO").build();
		authResponse = new AuthResponse.Builder().authorizationId(vdpAuthbyIdResponse.getId())
				.amount(vdpAuthbyIdResponse.getAmount()).currency(vdpAuthbyIdResponse.getCurrency())
				.created(vdpAuthbyIdResponse.getCreateDateTime())
				.status(utils.getServiceStatus(vdpAuthbyIdResponse.getStatus())).payment(paymentResponse)
				.orderId(utils.getOrderId(vdpAuthbyIdResponse.getReferenceId())).build();

		return authResponse;
	}

	public CreateAuthRequestVDP buildServiceAuthRequest(AuthRequest authRequest) {

		HashMap<String, String> merchantDefinedData = new HashMap<>();
		merchantDefinedData.put("orderId", authRequest.getOrderId());

		Address address = authRequest.getBillingAddress();
		AddressVDP addressVDP = null;
		if (address != null) {
			addressVDP = new AddressVDP.Builder().city(address.getCity()).country(address.getCountry())
					.firstName(address.getFirstName()).lastName(address.getLastName()).build();
		}

		CreateAuthRequestVDP authRequestVDP = new CreateAuthRequestVDP.Builder()
				.amount(authRequest.getAmount().toString()).currency(authRequest.getCurrency())
				.payment(new PaymentVDP.Builder().cardNumber(authRequest.getPayment().getToken())
						.expirationYear(authRequest.getPayment().getExpiry().getExpiryYear())
						.expirationMonth(authRequest.getPayment().getExpiry().getExpiryMonth()).build())
				.referenceId(authRequest.getOrderId()).merchantDefinedData(merchantDefinedData)
				.billingAddress(addressVDP).build();

		CustomLogger.log(authRequestVDP);
		return authRequestVDP;
	}

	// TODO additional call to obtain these values
	public ReverseAuthRequestVDP buildServiceReverseAuthRequest(UpdatePaymentRequest updateAuthRequest,
			RetrieveAuthById retrieveAuthResponse) {

		ReverseAuthRequestVDP reverseAuthRequestVDP = new ReverseAuthRequestVDP(
				retrieveAuthResponse.getAmount().toString(), retrieveAuthResponse.getCurrency(),
				retrieveAuthResponse.getReferenceId());
		CustomLogger.log(reverseAuthRequestVDP);
		return reverseAuthRequestVDP;
	}

	public ServiceResponse<AuthResponse> buildUpdateAuthResponse(
			VDPResponse<ReverseVoidResponseVDP> reverseAuthResponseVDP, RetrieveAuthById retrieveAuthResponse) {

		ServiceResponse<AuthResponse> response = null;

		// TODO: remove builder pattern
		if (reverseAuthResponseVDP.getVdpError() != null) {
			VDPError vdpError = reverseAuthResponseVDP.getVdpError();
			response = new ServiceResponse.Builder<AuthResponse>().serviceError(new ServiceError.Builder()
					.code(vdpError.getResponseStatusVDP().getCode()).status(vdpError.getResponseStatusVDP().getStatus())
					.message(vdpError.getResponseStatusVDP().getMessage()).build()).build();

		} else {
			ReverseVoidResponseVDP vdpResponse = reverseAuthResponseVDP.getResponse();
			String orderId = utils.getOrderId(vdpResponse.getReferenceId());
			PaymentVDP paymentVDP = retrieveAuthResponse.getPayment();
			PaymentResponse paymentResponse = null;
			if (paymentVDP != null) {
				paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
						.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear()))
						.type(paymentVDP.getCardType()).build();

			}

			// TODO: error handling for parse double
			AuthResponse authResponse = new AuthResponse.Builder().amount(Double.parseDouble(vdpResponse.getAmount()))
					.authorizationId(vdpResponse.getId()).orderId(orderId).payment(paymentResponse)
					.currency(vdpResponse.getCurrency()).created(retrieveAuthResponse.getCreateDateTime())
					.updated(vdpResponse.getCreatedAt()).status(utils.getServiceStatus(vdpResponse.getStatus()))
					.build();
			response = new ServiceResponse.Builder<AuthResponse>().response(authResponse).build();
		}

		CustomLogger.log(response);
		return response;
	}

}
