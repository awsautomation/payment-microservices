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
import com.visa.innovation.paymentservice.model.auth.AuthRequest;
import com.visa.innovation.paymentservice.model.auth.AuthResponse;
import com.visa.innovation.paymentservice.util.CustomRequestValidator;
import com.visa.innovation.paymentservice.wrappers.soa.PaymentWrapperSOA;
import com.visa.innovation.paymentservice.wrappers.vdp.PaymentWrapperVDP;

@RestController
@RequestMapping(value = "")
public class AuthController {

	@Autowired
	PaymentWrapperVDP paymentWrapperVDP;
	@Autowired
	PaymentWrapperSOA paymentWrapperSOA;
	@Autowired
	CustomRequestValidator customRequestValidator;

	@RequestMapping(value = "/authorizations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthResponse> createAuth(@RequestHeader("x-api-key") String apiKey,
			@RequestHeader("on-behalf-of-user") String userId, @RequestHeader("x-card-id") String cardId,
			@Valid @RequestBody AuthRequest authRequest)
			throws GenericServiceException, InputParametersInvalidException {

		customRequestValidator.validatePaymentRequest(authRequest);
		ServiceResponse<AuthResponse> authResponse = null;
		authResponse = paymentWrapperSOA.createAuth(authRequest, userId, apiKey, cardId);

		if (authResponse.getServiceError() != null) {
			throw new GenericServiceException(authResponse.getServiceError());
		} else {
			return new ResponseEntity<AuthResponse>(authResponse.getResponse(), HttpStatus.CREATED);
		}

	}

	@RequestMapping(value = "/authorizations/{authorizationId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthResponse> updateAuth(@RequestHeader("x-api-key") String apiKey,
			@RequestHeader("on-behalf-of-user") String userId,
			@Valid @RequestBody UpdatePaymentRequest updateAuthRequest,
			@PathVariable("authorizationId") String authorizationId)
			throws GenericServiceException, TransactionNotFoundException {

		ServiceResponse<AuthResponse> authResponse = null;
		authResponse = paymentWrapperVDP.updateAuth(updateAuthRequest, authorizationId);

		if (authResponse.getServiceError() != null) {

			throw new GenericServiceException(authResponse.getServiceError());

		} else {
			return new ResponseEntity<AuthResponse>(authResponse.getResponse(), HttpStatus.OK);
		}

	}

}