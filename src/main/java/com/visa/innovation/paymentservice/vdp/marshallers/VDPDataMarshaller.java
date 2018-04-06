package com.visa.innovation.paymentservice.vdp.marshallers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.model.Expiry;
import com.visa.innovation.paymentservice.model.PaymentResponse;
import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.TransactionType;
import com.visa.innovation.paymentservice.model.UpdatePaymentRequest;
import com.visa.innovation.paymentservice.model.auth.AuthRequest;
import com.visa.innovation.paymentservice.model.auth.AuthResponse;
import com.visa.innovation.paymentservice.model.capture.CaptureRequest;
import com.visa.innovation.paymentservice.model.capture.CaptureResponse;
import com.visa.innovation.paymentservice.model.refund.RefundRequest;
import com.visa.innovation.paymentservice.model.refund.RefundResponse;
import com.visa.innovation.paymentservice.model.sales.SalesRequest;
import com.visa.innovation.paymentservice.model.sales.SalesResponse;
import com.visa.innovation.paymentservice.soa.model.PaymentStatusEnum;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.Utils;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveAllVDP;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.VoidRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.auth.CreateAuthRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.auth.CreateAuthResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.auth.RetrieveAuthById;
import com.visa.innovation.paymentservice.vdp.model.auth.ReverseAuthRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.capture.CreateCaptureRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.capture.CreateCaptureResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.capture.RetrieveCaptureByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.refund.CreateRefundRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.refund.CreateRefundResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.refund.RetrieveRefundByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.sale.CreateSaleRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.sale.CreateSaleResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.sale.RetrieveSaleByIdResponseVDP;

@Component
public class VDPDataMarshaller {

	@Autowired
	AuthMarshallerVDP authMarshallerVDP;
	@Autowired
	CaptureMarshallerVDP captureMarshallerVDP;
	@Autowired
	SaleMarshallerVDP saleMarshallerVDP;
	@Autowired
	RefundMarshallerVDP refundMarshallerVDP;
	@Autowired
	Utils utils;

	public ServiceResponse<AuthResponse> buildAuthResponse(VDPResponse<CreateAuthResponseVDP> authResponseVDP) {

		return authMarshallerVDP.buildAuthResponse(authResponseVDP);
	}

	public CreateAuthRequestVDP buildServiceAuthRequest(AuthRequest authRequest) {

		return authMarshallerVDP.buildServiceAuthRequest(authRequest);

	}

	public ReverseAuthRequestVDP buildServiceReverseAuthRequest(UpdatePaymentRequest updateAuthRequest,
			RetrieveAuthById retrieveAuthResponse) {

		return authMarshallerVDP.buildServiceReverseAuthRequest(updateAuthRequest, retrieveAuthResponse);

	}

	public ServiceResponse<AuthResponse> buildUpdateAuthResponse(
			VDPResponse<ReverseVoidResponseVDP> reverseAuthResponseVDP, RetrieveAuthById retrieveAuthResponse) {

		return authMarshallerVDP.buildUpdateAuthResponse(reverseAuthResponseVDP, retrieveAuthResponse);

	}

	public CreateCaptureRequestVDP buildServiceCaptureRequest(CaptureRequest captureRequest) {

		return captureMarshallerVDP.buildServiceCaptureRequest(captureRequest);

	}

	public ServiceResponse<CaptureResponse> buildCaptureResponse(
			VDPResponse<CreateCaptureResponseVDP> captureResponseVDP) {

		return captureMarshallerVDP.buildCaptureResponse(captureResponseVDP);

	}

	public CreateSaleRequestVDP buildServiceSaleRequest(SalesRequest salesRequest) {

		return saleMarshallerVDP.buildServiceSaleRequest(salesRequest);

	}

	public ServiceResponse<SalesResponse> buildSaleResponse(VDPResponse<CreateSaleResponseVDP> saleResponseVDP) {

		return saleMarshallerVDP.buildSaleResponse(saleResponseVDP);

	}

	public ServiceResponse<CaptureResponse> buildupdateCaptureResponse(
			VDPResponse<ReverseVoidResponseVDP> reverseVoidResponseVDP,
			RetrieveCaptureByIdResponseVDP retrieveCaptureResponse) {

		return captureMarshallerVDP.buildupdateCaptureResponse(reverseVoidResponseVDP, retrieveCaptureResponse);

	}

	public ServiceResponse<SalesResponse> buildUpdateSaleResponse(
			VDPResponse<ReverseVoidResponseVDP> reverseVoidResponseVDP,
			RetrieveSaleByIdResponseVDP retrieveSaleResponse) {

		return saleMarshallerVDP.buildUpdateSaleResponse(reverseVoidResponseVDP, retrieveSaleResponse);

	}

	public CreateRefundRequestVDP buildServiceRefundRequest(RefundRequest refundRequest, String userId, String apiKey,
			String cardId) {

		return refundMarshallerVDP.buildServiceRefundRequest(refundRequest, userId, apiKey, cardId);

	}

	public ServiceResponse<RefundResponse> buildRefundResponse(RefundRequest refundRequest,
			VDPResponse<CreateRefundResponseVDP> refundResponseVDP,
			VDPResponse<BaseRetrieveByIdResponseVDP> responseSaleResponse, TransactionType transactionType) {

		return refundMarshallerVDP.buildRefundResponse(refundRequest, refundResponseVDP, responseSaleResponse,
				transactionType);

	}

	public ServiceResponse<RefundResponse> buildUpdateRefundResponse(
			VDPResponse<ReverseVoidResponseVDP> updateRefundResponse,
			VDPResponse<RetrieveRefundByIdResponseVDP> retrieveRefundResponse) {
		return refundMarshallerVDP.buildUpdateRefundResponse(updateRefundResponse, retrieveRefundResponse);
	}

	public AuthResponse buildAuthResponse(BaseRetrieveAllVDP vdpAuthResponse, RetrieveAuthById vdpAuthByIdResponse) {

		return authMarshallerVDP.buildAuthResponse(vdpAuthResponse, vdpAuthByIdResponse);

	}

	public AuthResponse buildAuthResponse(BaseRetrieveAllVDP vdpAuthResponse) {

		return authMarshallerVDP.buildAuthResponse(vdpAuthResponse);

	}

	public AuthResponse buildAuthResponse(RetrieveAuthById vdpAuthbyIdResponse) {

		return authMarshallerVDP.buildAuthResponse(vdpAuthbyIdResponse);

	}

	public SalesResponse buildSalesResponse(BaseRetrieveAllVDP vdpResponse,
			RetrieveSaleByIdResponseVDP vdpSaleByIdResponse) {

		return saleMarshallerVDP.buildSalesResponse(vdpResponse, vdpSaleByIdResponse);

	}

	public SalesResponse buildSalesResponse(BaseRetrieveAllVDP vdpResponse) {

		return saleMarshallerVDP.buildSalesResponse(vdpResponse);

	}

	public SalesResponse buildSalesResponse(RetrieveSaleByIdResponseVDP vdpSalesByIdResponse) {

		return saleMarshallerVDP.buildSalesResponse(vdpSalesByIdResponse);

	}

	public CaptureResponse buildCaptureResponse(BaseRetrieveAllVDP vdpResponse) {

		return captureMarshallerVDP.buildCaptureResponse(vdpResponse);

	}

	public CaptureResponse buildCaptureResponse(BaseRetrieveAllVDP vdpResponse,
			RetrieveCaptureByIdResponseVDP vdpCaptureByIdResponse) {

		return captureMarshallerVDP.buildCaptureResponse(vdpResponse, vdpCaptureByIdResponse);

	}

	public CaptureResponse buildCaptureResponse(RetrieveCaptureByIdResponseVDP vdpCaptureByIdResponse) {

		return captureMarshallerVDP.buildCaptureResponse(vdpCaptureByIdResponse);

	}

	public VoidRequestVDP buildServiceVoidRequest(UpdatePaymentRequest updatePaymentRequest,
			BaseRetrieveByIdResponseVDP retrieveCaptureByIdResponseVDP) {
		VoidRequestVDP voidRequestVDP = new VoidRequestVDP(retrieveCaptureByIdResponseVDP.getReferenceId());
		return voidRequestVDP;
	}

	public ServiceResponse<AuthResponse> buildDummyUpdateAuthResponse() {
		PaymentResponse paymentResponse = new PaymentResponse.Builder().accountNumber("TODO")
				.expiry(new Expiry("TODO", "TODO")).type("TODO").name("TODO").build();

		// TODO: error handling for parse double
		AuthResponse authResponse = new AuthResponse.Builder().amount(Double.parseDouble("1.00"))
				.authorizationId("TODO").orderId("TODO").payment(paymentResponse).currency("TODO").created("TODO")
				.updated("TODO").validUntil("TODO").status(PaymentStatusEnum.PENDING_CAPTURE).merchantId("TODO")
				.build();
		CustomLogger.log(authResponse);
		return new ServiceResponse.Builder<AuthResponse>().response(authResponse).build();
	}
}
