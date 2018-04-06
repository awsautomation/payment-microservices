package com.visa.innovation.paymentservice.vdp.marshallers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.model.Address;
import com.visa.innovation.paymentservice.model.Expiry;
import com.visa.innovation.paymentservice.model.PaymentResponse;
import com.visa.innovation.paymentservice.model.ServiceError;
import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.sales.SalesRequest;
import com.visa.innovation.paymentservice.model.sales.SalesResponse;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.Utils;
import com.visa.innovation.paymentservice.vdp.model.AddressVDP;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveAllVDP;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.PaymentVDP;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPError;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.sale.CreateSaleRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.sale.CreateSaleResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.sale.RetrieveSaleByIdResponseVDP;

@Component
public class SaleMarshallerVDP {

	@Autowired
	Utils utils;
	
	public CreateSaleRequestVDP buildServiceSaleRequest(SalesRequest salesRequest) {

		Address address = salesRequest.getBillingAddress();

		AddressVDP addressVDP = null;
		if (address != null) {
			addressVDP = new AddressVDP.Builder().city(address.getCity()).country(address.getCountry())
					.firstName(address.getFirstName()).lastName(address.getLastName()).build();
		}

		CreateSaleRequestVDP salesRequestVDP = new CreateSaleRequestVDP.Builder().amount(salesRequest.getAmount().toString())
				.currency(salesRequest.getCurrency())
				.payment(new PaymentVDP.Builder().cardNumber(salesRequest.getPayment().getToken())
						.expirationYear(salesRequest.getPayment().getExpiry().getExpiryYear())
						.expirationMonth(salesRequest.getPayment().getExpiry().getExpiryMonth()).build())
				.referenceId(salesRequest.getOrderId()).billingAddress(addressVDP).build();

		CustomLogger.log(salesRequestVDP);
		return salesRequestVDP;
	}

	public ServiceResponse<SalesResponse> buildSaleResponse(VDPResponse<CreateSaleResponseVDP> saleResponseVDP) {

		ServiceResponse<SalesResponse> response = null;

		if (saleResponseVDP.getVdpError() != null) {
			VDPError vdpError = saleResponseVDP.getVdpError();
			response = new ServiceResponse.Builder<SalesResponse>().serviceError(new ServiceError.Builder()
					.code(vdpError.getResponseStatusVDP().getCode()).status(vdpError.getResponseStatusVDP().getStatus())
					.message(vdpError.getResponseStatusVDP().getMessage()).build()).build();
		} else {
			CreateSaleResponseVDP vdpResponse = saleResponseVDP.getResponse();
			PaymentVDP paymentVDP = vdpResponse.getPayment();
			PaymentResponse paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
					.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear()))
					.type(paymentVDP.getCardType()).build();

			// TODO: error handling for parse double
			SalesResponse salesResponse = new SalesResponse.Builder()
					.amount(Double.parseDouble(vdpResponse.getAmount())).saleId(vdpResponse.getId())
					.orderId(vdpResponse.getReferenceId()).payment(paymentResponse)
					.status(utils.getServiceStatus(vdpResponse.getStatus())).authorizationId("TODO")
					.captureId("TODO").currency("TODO").created("TODO").updated("TODO").validUntil("TODO")
					.merchantId("TODO").userId("TODO").build();

			response = new ServiceResponse.Builder<SalesResponse>().response(salesResponse).build();
		}

		CustomLogger.log(response);
		return response;
	}
	
	public SalesResponse buildSalesResponse(BaseRetrieveAllVDP vdpResponse,
			BaseRetrieveByIdResponseVDP vdpSaleByIdResponse) {

		SalesResponse salesResponse = null;
		if (vdpResponse != null && vdpSaleByIdResponse != null) {
			PaymentVDP paymentVDP = vdpSaleByIdResponse.getPayment();
			PaymentResponse paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
					.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear()))
					.type(paymentVDP.getCardType()).name("TODO").build();
			salesResponse = new SalesResponse.Builder().saleId(vdpResponse.getId()).amount(vdpResponse.getAmount())
					.currency(vdpResponse.getCurrency()).payment(paymentResponse)
					.created(utils.stringMillisToDate(vdpResponse.getCreated()))
					.status(utils.getServiceStatus(vdpSaleByIdResponse.getStatus())) // .merchantId("default")
					.orderId(utils.getOrderId(vdpResponse.getReferenceId())).build();
		}
		return salesResponse;
	}

	public SalesResponse buildSalesResponse(BaseRetrieveAllVDP vdpResponse) {

		SalesResponse salesResponse = null;
		if (vdpResponse != null) {
			salesResponse = new SalesResponse.Builder().saleId(vdpResponse.getId()).amount(vdpResponse.getAmount())
					.currency(vdpResponse.getCurrency()).created(utils.stringMillisToDate(vdpResponse.getCreated())) // .merchantId("default")
					.orderId(utils.getOrderId(vdpResponse.getReferenceId())).build();
		}
		return salesResponse;
	}

	public SalesResponse buildSalesResponse(RetrieveSaleByIdResponseVDP vdpSalesByIdResponse) {

		SalesResponse salesResponse = null;
		PaymentVDP paymentVDP = vdpSalesByIdResponse.getPayment();
		PaymentResponse paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
				.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear()))
				.type(paymentVDP.getCardType()).name("TODO").build();
		salesResponse = new SalesResponse.Builder().saleId(vdpSalesByIdResponse.getId())
				.amount(vdpSalesByIdResponse.getAmount()).currency(vdpSalesByIdResponse.getCurrency())
				.created(vdpSalesByIdResponse.getCreateDateTime())
				.status(utils.getServiceStatus(vdpSalesByIdResponse.getStatus())).payment(paymentResponse)
				.orderId(utils.getOrderId(vdpSalesByIdResponse.getReferenceId())).build();

		return salesResponse;
	}
	
	public ServiceResponse<SalesResponse> buildUpdateSaleResponse(
			VDPResponse<ReverseVoidResponseVDP> reverseVoidResponseVDP, RetrieveSaleByIdResponseVDP retrieveSaleResponse) {

		ServiceResponse<SalesResponse> response = null;
		if (reverseVoidResponseVDP.getVdpError() != null) {
			VDPError vdpError = reverseVoidResponseVDP.getVdpError();
			response = new ServiceResponse.Builder<SalesResponse>().serviceError(new ServiceError.Builder()
					.code(vdpError.getResponseStatusVDP().getCode()).status(vdpError.getResponseStatusVDP().getStatus())
					.message(vdpError.getResponseStatusVDP().getMessage()).build()).build();
		} else {
			ReverseVoidResponseVDP vdpResponse = reverseVoidResponseVDP.getResponse();
			String orderId = utils.getOrderId(vdpResponse.getReferenceId());
			PaymentVDP paymentVDP = retrieveSaleResponse.getPayment();
			PaymentResponse paymentResponse = new PaymentResponse.Builder().accountNumber(paymentVDP.getCardNumber())
					.type(paymentVDP.getCardType())
					.expiry(new Expiry(paymentVDP.getExpirationMonth(), paymentVDP.getExpirationYear())).build();
	
			// TODO: error handling for parse double
			SalesResponse salesResponse = new SalesResponse.Builder()
					.amount(Double.parseDouble(vdpResponse.getAmount())).captureId(vdpResponse.getId()).orderId(orderId)
					.currency(vdpResponse.getCurrency()).payment(paymentResponse)
					.created(retrieveSaleResponse.getCreateDateTime()).updated(vdpResponse.getCreatedAt())
					.status(utils.getServiceStatus(vdpResponse.getStatus())).build();

			response = new ServiceResponse.Builder<SalesResponse>().response(salesResponse).build();
		}

		CustomLogger.log(response);
		return response;

	}
}
