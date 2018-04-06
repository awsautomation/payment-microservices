package com.visa.innovation.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.wrappers.vdp.PaymentWrapperVDP;

@RestController
@RequestMapping(value = "")

public class HealthCheckController {

	@Autowired
	PaymentWrapperVDP paymentWrapper;

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public ResponseEntity<String> testPing() {

		CustomLogger.log("Ping request received");
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

}
