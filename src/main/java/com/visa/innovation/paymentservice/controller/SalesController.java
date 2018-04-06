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
import com.visa.innovation.paymentservice.model.sales.SalesRequest;
import com.visa.innovation.paymentservice.model.sales.SalesResponse;
import com.visa.innovation.paymentservice.util.CustomRequestValidator;
import com.visa.innovation.paymentservice.wrappers.soa.PaymentWrapperSOA;
import com.visa.innovation.paymentservice.wrappers.vdp.PaymentWrapperVDP;

@RestController
public class SalesController {

	@Autowired
	PaymentWrapperVDP paymentWrapperVDP;
	@Autowired
	PaymentWrapperSOA paymentWrapperSOA;
	@Autowired
	CustomRequestValidator customRequestValidator;

	@RequestMapping(value = "/sales", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SalesResponse> sales(@RequestHeader("x-api-key") String apiKey,
			@RequestHeader("on-behalf-of-user") String userId, @RequestHeader("x-card-id") String cardId,
			@Valid @RequestBody SalesRequest salesRequest)
			throws GenericServiceException, InputParametersInvalidException {

		customRequestValidator.validatePaymentRequest(salesRequest);

		ServiceResponse<SalesResponse> saleResponse = null;

		// saleResponse = paymentWrapperVDP.createSale(salesRequest);
		saleResponse = paymentWrapperSOA.createSale(salesRequest, userId, apiKey, cardId);

		if (saleResponse.getServiceError() != null) {
			throw new GenericServiceException(saleResponse.getServiceError());
		}

		return new ResponseEntity<SalesResponse>(saleResponse.getResponse(), HttpStatus.CREATED);
	}

	// unused
	@RequestMapping(value = "/sales/{saleId}", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SalesResponse> updateSales(@RequestHeader("x-api-key") String apiKey,
			@RequestHeader("on-behalf-of-user") String userId,
			@Valid @RequestBody UpdatePaymentRequest updateSaleRequest, @PathVariable("saleId") String saleId)
			throws GenericServiceException, TransactionNotFoundException {

		ServiceResponse<SalesResponse> saleResponse = null;
		saleResponse = paymentWrapperVDP.updateSales(updateSaleRequest, saleId);

		if (saleResponse.getServiceError() != null) {
			throw new GenericServiceException(saleResponse.getServiceError());
		}

		return new ResponseEntity<SalesResponse>(saleResponse.getResponse(), HttpStatus.CREATED);
	}

}
