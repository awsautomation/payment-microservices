package com.visa.innovation.paymentservice.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.exception.InputParametersInvalidException;
import com.visa.innovation.paymentservice.model.BasePaymentRequest;
import com.visa.innovation.paymentservice.model.refund.RefundRequest;

@Component
public class CustomRequestValidator {

	public void validateRefundRequest(RefundRequest refundRequest) throws InputParametersInvalidException {
		List<String> errors = new ArrayList<String>();

		if (refundRequest.getCaptureId().trim().isEmpty() && refundRequest.getSaleId().trim().isEmpty()) {
			errors.add(Constants.INVALID_REFUND_REQUEST);
		}

		if (errors.size() > 0) {
			throw new InputParametersInvalidException(errors);
		}
	}

	public void validatePaymentRequest(BasePaymentRequest request) throws InputParametersInvalidException {

		List<String> errors = new ArrayList<String>();
		if (request.getAmount().compareTo(0.0) <= 0) {
			errors.add(Constants.INVALID_AMOUNT);
		}

		if (errors.size() > 0) {
			throw new InputParametersInvalidException(errors);
		}
	}

}
