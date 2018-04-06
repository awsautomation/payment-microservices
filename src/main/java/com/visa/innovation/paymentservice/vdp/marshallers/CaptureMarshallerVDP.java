package com.visa.innovation.paymentservice.vdp.marshallers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.model.Address;
import com.visa.innovation.paymentservice.model.Expiry;
import com.visa.innovation.paymentservice.model.PaymentResponse;
import com.visa.innovation.paymentservice.model.ServiceError;
import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.capture.CaptureRequest;
import com.visa.innovation.paymentservice.model.capture.CaptureResponse;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.Utils;
import com.visa.innovation.paymentservice.vdp.model.AddressVDP;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveAllVDP;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.PaymentVDP;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPError;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.capture.CreateCaptureRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.capture.CreateCaptureResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.capture.RetrieveCaptureByIdResponseVDP;

@Component
public class CaptureMarshallerVDP {

	@Autowired
	Utils utils;

	public CreateCaptureRequestVDP buildServiceCaptureRequest(CaptureRequest captureRequest) {

		Address address = captureRequest.getBillingAddress();
		AddressVDP addressVDP = null;
		if (address != null) {
			addressVDP = new AddressVDP.Builder().city(address.getCity()).country(address.getCountry())
					.firstName(address.getFirstName()).lastName(address.getLastName()).build();
		}

		CreateCaptureRequestVDP captureRequestVDP = new CreateCaptureRequestVDP.Builder()
				.amount(captureRequest.getAmount().toString()).currency(captureRequest.getCurrency())
				.referenceId(captureRequest.getOrderId()).billingAddress(addressVDP).build();

		CustomLogger.log(captureRequestVDP);
		return captureRequestVDP;
	}

	public ServiceResponse<CaptureResponse> buildCaptureResponse(
			VDPResponse<CreateCaptureResponseVDP> captureResponseVDP) {

		ServiceResponse<CaptureResponse> response = null;

		// TODO: remove builder pattern
		if (captureResponseVDP.getVdpError() != null) {
			VDPError vdpError = captureResponseVDP.getVdpError();
			response = new ServiceResponse.Builder<CaptureResponse>().serviceError(new ServiceError.Builder()
					.code(vdpError.getResponseStatusVDP().getCode()).status(vdpError.getResponseStatusVDP().getStatus())
					.message(vdpError.getResponseStatusVDP().getMessage()).build()).build();
		} else {
			CreateCaptureResponseVDP vdpResponse = captureResponseVDP.getResponse();
			PaymentResponse paymentResponse = new PaymentResponse.Builder().accountNumber("TODO").name("TODO")
					.type("TODO").expiry(new Expiry("TODO", "TODO")).build();
			// TODO: error handling for parse double
			CaptureResponse captureResponse = new CaptureResponse.Builder()
					.amount(Double.parseDouble(vdpResponse.getAmount())).authorizationId("TODO")
					.captureId(vdpResponse.getId()).orderId(vdpResponse.getReferenceId())
					.currency(vdpResponse.getCurrency()).payment(paymentResponse).created("TODO").updated("TODO")
					.validUntil("TODO").status(utils.getServiceStatus(vdpResponse.getStatus())).merchantId("TODO")
					.userId("TODO").build();

			response = new ServiceResponse.Builder<CaptureResponse>().response(captureResponse).build();
		}

		CustomLogger.log(response);
		return response;
	}

	public CaptureResponse buildCaptureResponse(BaseRetrieveAllVDP vdpResponse) {

		CaptureResponse captureResponse = null;
		if (vdpResponse != null) {
			// PaymentVDP paymentVDP = vdpResponse.get;
			// PaymentResponse paymentResponse = new
			// PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
			// .expiry(new Expiry(paymentVDP.getExpirationMonth(),
			// paymentVDP.getExpirationYear()))
			// .type(paymentVDP.getCardType()).name("TODO").build();
			captureResponse = new CaptureResponse.Builder().captureId(vdpResponse.getId())
					.amount(vdpResponse.getAmount()).currency(vdpResponse.getCurrency())
					.created(utils.stringMillisToDate(vdpResponse.getCreated())).merchantId("default").build();
		}
		return captureResponse;
	}

	public CaptureResponse buildCaptureResponse(BaseRetrieveAllVDP vdpResponse,
			BaseRetrieveByIdResponseVDP vdpCaptureByIdResponse) {

		CaptureResponse captureResponse = null;
		PaymentResponse paymentResponse = null;
		if (vdpResponse != null && vdpCaptureByIdResponse != null) {
			PaymentVDP paymentVDP = vdpCaptureByIdResponse.getPayment();
			if (paymentVDP != null) {
				paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
						.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear()))
						.type(paymentVDP.getCardType()).name("TODO").build();
			}
			captureResponse = new CaptureResponse.Builder().captureId(vdpResponse.getId())
					.amount(vdpResponse.getAmount()).currency(vdpResponse.getCurrency()).payment(paymentResponse)
					.created(utils.stringMillisToDate(vdpResponse.getCreated()))
					.status(utils.getServiceStatus(vdpCaptureByIdResponse.getStatus())).merchantId("default").build();
		}
		return captureResponse;
	}

	public CaptureResponse buildCaptureResponse(RetrieveCaptureByIdResponseVDP vdpCaptureByIdResponse) {

		CaptureResponse captureResponse = null;
		PaymentResponse paymentResponse = null;
		PaymentVDP paymentVDP = vdpCaptureByIdResponse.getPayment();
		if (paymentVDP != null) {
			paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
					.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear()))
					.type(paymentVDP.getCardType()).name("TODO").build();
		}
		captureResponse = new CaptureResponse.Builder().captureId(vdpCaptureByIdResponse.getId())
				.amount(vdpCaptureByIdResponse.getAmount()).currency(vdpCaptureByIdResponse.getCurrency())
				.created(vdpCaptureByIdResponse.getCreateDateTime())
				.status(utils.getServiceStatus(vdpCaptureByIdResponse.getStatus())).payment(paymentResponse)
				.orderId(utils.getOrderId(vdpCaptureByIdResponse.getReferenceId())).build();

		return captureResponse;
	}

	public ServiceResponse<CaptureResponse> buildupdateCaptureResponse(
			VDPResponse<ReverseVoidResponseVDP> reverseVoidResponseVDP,
			RetrieveCaptureByIdResponseVDP retrieveCaptureResponse) {

		ServiceResponse<CaptureResponse> response = null;
		if (reverseVoidResponseVDP.getVdpError() != null) {
			VDPError vdpError = reverseVoidResponseVDP.getVdpError();
			response = new ServiceResponse.Builder<CaptureResponse>().serviceError(new ServiceError.Builder()
					.code(vdpError.getResponseStatusVDP().getCode()).status(vdpError.getResponseStatusVDP().getStatus())
					.message(vdpError.getResponseStatusVDP().getMessage()).build()).build();
		} else {

			ReverseVoidResponseVDP vdpResponse = reverseVoidResponseVDP.getResponse();
			String orderId = utils.getOrderId(vdpResponse.getReferenceId());
			PaymentVDP paymentVDP = retrieveCaptureResponse.getPayment();
			// PaymentResponse paymentResponse = new
			// PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
			// .type(paymentVDP.getCardType())
			// .expiry(new Expiry(paymentVDP.getExpirationMonth(),
			// paymentVDP.getExpirationYear())).build();
			// TODO: error handling for parse double
			CaptureResponse captureResponse = new CaptureResponse.Builder()
					.amount(Double.parseDouble(vdpResponse.getAmount())).captureId(vdpResponse.getId()).orderId(orderId)
					.currency(vdpResponse.getCurrency()).created(retrieveCaptureResponse.getCreateDateTime())
					.updated(vdpResponse.getCreatedAt()).status(utils.getServiceStatus(vdpResponse.getStatus()))
					.build();

			response = new ServiceResponse.Builder<CaptureResponse>().response(captureResponse).build();
		}

		CustomLogger.log(response);
		return response;
	}

}
