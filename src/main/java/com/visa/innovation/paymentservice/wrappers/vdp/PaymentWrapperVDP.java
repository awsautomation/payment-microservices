package com.visa.innovation.paymentservice.wrappers.vdp;

import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.exception.GenericVDPException;
import com.visa.innovation.paymentservice.exception.TransactionNotFoundException;
import com.visa.innovation.paymentservice.interfaces.PaymentWrapper;
import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.UpdatePaymentRequest;
import com.visa.innovation.paymentservice.model.auth.AuthRequest;
import com.visa.innovation.paymentservice.model.auth.AuthResponse;
import com.visa.innovation.paymentservice.model.capture.CaptureRequest;
import com.visa.innovation.paymentservice.model.capture.CaptureResponse;
import com.visa.innovation.paymentservice.model.refund.RefundRequest;
import com.visa.innovation.paymentservice.model.refund.RefundResponse;
import com.visa.innovation.paymentservice.model.sales.SalesRequest;
import com.visa.innovation.paymentservice.model.sales.SalesResponse;

public class PaymentWrapperVDP implements PaymentWrapper {

	@Autowired
	AuthWrapperVDP authWrapperVDP;
	@Autowired
	CaptureWrapperVDP captureWrapperVDP;
	@Autowired
	SaleWrapperVDP saleWrapperVDP;
	@Autowired
	RefundWrapperVDP refundWrapperVDP;

	public PaymentWrapperVDP() {
	}

	@Override
	public ServiceResponse<AuthResponse> createAuth(AuthRequest request) throws GenericVDPException {
		return authWrapperVDP.createAuth(request);
	}

	@Override
	public ServiceResponse<AuthResponse> updateAuth(UpdatePaymentRequest updateAuthRequest, String authorizationId)
			throws TransactionNotFoundException {
		return authWrapperVDP.updateAuth(updateAuthRequest, authorizationId);
	}

	@Override
	public ServiceResponse<CaptureResponse> createCapture(CaptureRequest request) {
		return captureWrapperVDP.createCapture(request);
	}

	@Override
	public ServiceResponse<CaptureResponse> updateCapture(UpdatePaymentRequest updateCaptureRequest, String captureId)
			throws TransactionNotFoundException {
		return captureWrapperVDP.updateCapture(updateCaptureRequest, captureId);
	}

	@Override
	public ServiceResponse<SalesResponse> createSale(SalesRequest request) {
		return saleWrapperVDP.createSale(request);
	}

	@Override
	public ServiceResponse<SalesResponse> updateSales(UpdatePaymentRequest updateSaleRequest, String saleId)
			throws TransactionNotFoundException {
		return saleWrapperVDP.updateSales(updateSaleRequest, saleId);
	}

	@Override
	public ServiceResponse<RefundResponse> createRefund(RefundRequest refundRequest, String userId, String apiKey,
			String cardId) {
		return refundWrapperVDP.createRefund(refundRequest, userId, apiKey, cardId);
	}

	@Override
	public ServiceResponse<RefundResponse> updateRefund(UpdatePaymentRequest updateRefundRequest, String refundId)
			throws TransactionNotFoundException {
		return refundWrapperVDP.updateRefund(updateRefundRequest, refundId);
	}

}
