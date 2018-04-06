package com.visa.innovation.paymentservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.visa.innovation.paymentservice.exception.ForbiddenVDPException;
import com.visa.innovation.paymentservice.exception.GenericException;
import com.visa.innovation.paymentservice.exception.GenericVDPException;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.tokenization.KeyResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.tokenization.TokenRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.tokenization.TokenResponseVDP;
import com.visa.innovation.paymentservice.wrappers.vdp.TokenWrapperVDP;

@RestController
@RequestMapping(value = "")
public class TokenController {

	@Autowired
	TokenWrapperVDP tokenWrapperVDP;

	@RequestMapping(value = "/keys", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<KeyResponseVDP> getKey(@RequestHeader("x-api-key") String apiKey,
			@RequestHeader("on-behalf-of-user") String userId) throws GenericVDPException {

		CustomLogger.log("Key request received");

		VDPResponse<KeyResponseVDP> keyResponse = null;
		keyResponse = tokenWrapperVDP.getKey();

		if (keyResponse.getVdpError() != null) {
			throw new GenericVDPException(keyResponse.getVdpError());
		}

		return new ResponseEntity<KeyResponseVDP>(keyResponse.getResponse(), HttpStatus.OK);

	}

	@RequestMapping(value = "/tokens", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TokenResponseVDP> generateToken(@RequestHeader("x-api-key") String apiKey,
			@RequestHeader("on-behalf-of-user") String userId, @Valid @RequestBody TokenRequestVDP tokenRequestVDP)
			throws GenericVDPException, ForbiddenVDPException, GenericException {

		VDPResponse<TokenResponseVDP> tokenResponse = null;
		tokenResponse = tokenWrapperVDP.generateToken(tokenRequestVDP);

		if (tokenResponse.getVdpError() != null) {
			throw new GenericVDPException(tokenResponse.getVdpError());
		}

		return new ResponseEntity<TokenResponseVDP>(tokenResponse.getResponse(), HttpStatus.OK);
	}
}
