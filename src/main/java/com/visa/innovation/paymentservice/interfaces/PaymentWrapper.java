package com.visa.innovation.paymentservice.interfaces;

import com.visa.innovation.paymentservice.exception.GenericVDPException;
import com.visa.innovation.paymentservice.exception.TransactionNotFoundException;
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

/**
 * 
 * @author ntelukun, akakade This interface should be implemented for any
 *         payment gateway we use. Example : Cybersource, Stripe etc.
 *
 */
public interface PaymentWrapper {

	ServiceResponse<AuthResponse> createAuth(AuthRequest request) throws GenericVDPException;

	ServiceResponse<AuthResponse> updateAuth(UpdatePaymentRequest updateAuthRequest, String authorizationId)
			throws TransactionNotFoundException;

	ServiceResponse<CaptureResponse> createCapture(CaptureRequest request);

	ServiceResponse<CaptureResponse> updateCapture(UpdatePaymentRequest updateAuthRequest, String captureId)
			throws TransactionNotFoundException;

	ServiceResponse<SalesResponse> createSale(SalesRequest request);

	ServiceResponse<SalesResponse> updateSales(UpdatePaymentRequest updateSaleRequest, String saleId)
			throws TransactionNotFoundException;

	ServiceResponse<RefundResponse> createRefund(RefundRequest refundRequest, String userId, String apiKey, String cardId);

	ServiceResponse<RefundResponse> updateRefund(UpdatePaymentRequest updateRefundRequest, String refundId)
			throws TransactionNotFoundException;

}
