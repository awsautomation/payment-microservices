package com.visa.innovation.paymentservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.visa.innovation.paymentservice.exception.GenericServiceException;
import com.visa.innovation.paymentservice.exception.InputParametersInvalidException;
import com.visa.innovation.paymentservice.exception.TransactionNotFoundException;
import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.UpdatePaymentRequest;
import com.visa.innovation.paymentservice.model.refund.RefundRequest;
import com.visa.innovation.paymentservice.model.refund.RefundResponse;
import com.visa.innovation.paymentservice.util.CustomRequestValidator;
import com.visa.innovation.paymentservice.wrappers.vdp.PaymentWrapperVDP;

@RestController
public class RefundController {

	@Autowired
	PaymentWrapperVDP paymentWrapperVDP;
	@Autowired
	CustomRequestValidator customRequestValidator;

	@RequestMapping(value = "/refunds", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RefundResponse> createRefund(@RequestHeader("x-api-key") String apiKey,
			@RequestHeader("on-behalf-of-user") String userId, @Valid @RequestBody RefundRequest refundRequest,
			@RequestHeader("x-card-id") String cardId) throws GenericServiceException, InputParametersInvalidException {

		customRequestValidator.validatePaymentRequest(refundRequest);
		customRequestValidator.validateRefundRequest(refundRequest);
		ServiceResponse<RefundResponse> refundResponse = null;
		refundResponse = paymentWrapperVDP.createRefund(refundRequest, userId, apiKey, cardId);

		if (refundResponse.getServiceError() != null) {
			throw new GenericServiceException(refundResponse.getServiceError());

		} else {
			return new ResponseEntity<RefundResponse>(refundResponse.getResponse(), HttpStatus.CREATED);
		}

	}

	@RequestMapping(value = "/refunds/{refundId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RefundResponse> updateRefund(@RequestHeader("x-api-key") String apiKey,
			@RequestHeader("on-behalf-of-user") String userId,
			@Valid @RequestBody UpdatePaymentRequest updateRefundRequest, @PathVariable("refundId") String refundId)
			throws GenericServiceException, TransactionNotFoundException {

		ServiceResponse<RefundResponse> refundResponse = null;

		refundResponse = paymentWrapperVDP.updateRefund(updateRefundRequest, refundId);

		if (refundResponse.getServiceError() != null) {
			throw new GenericServiceException(refundResponse.getServiceError());

		} else {
			return new ResponseEntity<RefundResponse>(refundResponse.getResponse(), HttpStatus.OK);
		}

	}

}
