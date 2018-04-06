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
import com.visa.innovation.paymentservice.model.capture.CaptureRequest;
import com.visa.innovation.paymentservice.model.capture.CaptureResponse;
import com.visa.innovation.paymentservice.util.CustomRequestValidator;
import com.visa.innovation.paymentservice.wrappers.soa.PaymentWrapperSOA;
import com.visa.innovation.paymentservice.wrappers.vdp.PaymentWrapperVDP;

@RestController
public class CaptureController {

	@Autowired
	PaymentWrapperVDP paymentWrapperVDP;
	@Autowired
	PaymentWrapperSOA paymentWrapperSOA;
	@Autowired
	CustomRequestValidator customRequestValidator;

	@RequestMapping(value = "/captures", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CaptureResponse> capture(@RequestHeader("x-api-key") String apiKey,
			@RequestHeader("on-behalf-of-user") String userId, @RequestHeader("x-card-id") String cardId,
			@Valid @RequestBody CaptureRequest captureRequest)
			throws GenericServiceException, InputParametersInvalidException {

		customRequestValidator.validatePaymentRequest(captureRequest);
		ServiceResponse<CaptureResponse> captureResponse = null;

		captureResponse = paymentWrapperSOA.createCapture(captureRequest, userId, apiKey, cardId);

		if (captureResponse.getServiceError() != null) {
			throw new GenericServiceException(captureResponse.getServiceError());

		} else {
			return new ResponseEntity<CaptureResponse>(captureResponse.getResponse(), HttpStatus.CREATED);
		}
	}

	@RequestMapping(value = "/captures/{captureId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CaptureResponse> updateCapture(@RequestHeader("x-api-key") String apiKey,
			@RequestHeader("on-behalf-of-user") String userId,
			@Valid @RequestBody UpdatePaymentRequest updateCaptureRequest, @PathVariable("captureId") String captureId)
			throws GenericServiceException, TransactionNotFoundException {

		ServiceResponse<CaptureResponse> captureResponse = null;

		captureResponse = paymentWrapperVDP.updateCapture(updateCaptureRequest, captureId);

		if (captureResponse.getServiceError() != null) {
			throw new GenericServiceException(captureResponse.getServiceError());

		} else {
			return new ResponseEntity<CaptureResponse>(captureResponse.getResponse(), HttpStatus.OK);
		}

	}

}
