package com.visa.innovation.paymentservice.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.olingo.server.api.ODataApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.visa.innovation.paymentservice.util.CustomLogger;

@RestController
@RequestMapping(method = RequestMethod.GET)
public class ODataController extends EdmController {

	@RequestMapping(value = "/authorizations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getAllAuth(HttpServletRequest req) throws ODataApplicationException {
		ResponseEntity<String> response = process(req);
		return getResponse(response);
	}

	@RequestMapping(value = "/authorizations({id})", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getAuthById(HttpServletRequest req) throws ODataApplicationException {
		ResponseEntity<String> response = process(req);
		String formatted = getFormattedResponseById(response.getBody());
		CustomLogger.log("Response:" + formatted);
		return new ResponseEntity<String>(formatted, response.getHeaders(), HttpStatus.OK);
	}

	@RequestMapping(value = "/sales", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getAllSales(HttpServletRequest req) throws ODataApplicationException {
		ResponseEntity<String> response = process(req);
		return getResponse(response);
	}

	@RequestMapping(value = "/sales({id})", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getSaleById(HttpServletRequest req) throws ODataApplicationException {
		ResponseEntity<String> response = process(req);
		String formatted = getFormattedResponseById(response.getBody());
		CustomLogger.log("Response:" + formatted);
		return new ResponseEntity<String>(formatted, response.getHeaders(), HttpStatus.OK);
	}

	@RequestMapping(value = "/captures", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getAllCaptures(HttpServletRequest req) throws ODataApplicationException {
		ResponseEntity<String> response = process(req);
		return getResponse(response);
	}

	@RequestMapping(value = "/captures({id})", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getCaptureById(HttpServletRequest req) throws ODataApplicationException {
		ResponseEntity<String> response = process(req);
		String formatted = getFormattedResponseById(response.getBody());
		CustomLogger.log("Get Capture by Id Response:" + formatted);
		return new ResponseEntity<String>(formatted, response.getHeaders(), HttpStatus.OK);
	}

	/**
	 * Parses the response String and removes the { & } to make the response
	 * String a valid Json.
	 * 
	 * @param rawResponse
	 * @return
	 */
	private String getFormattedResponseById(String rawResponse) {
		String formatted = null;
		if (rawResponse != null) {
			formatted = rawResponse.replace("\\", "").replace("\"{", "{").replace("}\"", "}");
		}
		return formatted;
	}

	/**
	 * Extracts the response and parses the response String and removes the { &
	 * } to make the response String a valid Json.
	 * 
	 * @param rawResponse
	 * @return
	 */
	private String getFormattedResponse(String rawResponse) {
		String formatted = null;
		if (rawResponse != null) {
			String values = rawResponse.split("value")[1];
			formatted = values.substring(2, values.length() - 1).replace("\\", "").replace("\"{", "{").replace("}\"",
					"}");
		}
		formatted = rawResponse.replace("\\", "").replace("\"{", "{").replace("}\"", "}");
		return formatted;

	}

	private boolean isValuesEmpty(String rawResponse) {
		String values = rawResponse.split("value")[1];
		String format = values.substring(2, values.length()).replace("}", "");
		if (format.length() == 2) {
			return true;
		}
		return false;
	}

	private ResponseEntity<String> getResponse(ResponseEntity<String> response) {
		if (!isValuesEmpty(response.getBody())) {
			String formatted = getFormattedResponse(response.getBody());
			CustomLogger.log("Get all Sales Response:" + formatted);
			return new ResponseEntity<String>(formatted, response.getHeaders(), HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(response.getBody(), response.getHeaders(), HttpStatus.OK);
		}
	}

}
