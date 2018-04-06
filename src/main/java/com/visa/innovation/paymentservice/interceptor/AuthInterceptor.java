package com.visa.innovation.paymentservice.interceptor;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.visa.innovation.paymentservice.exception.InputParametersInvalidException;
import com.visa.innovation.paymentservice.exception.MissingHeadersException;
import com.visa.innovation.paymentservice.util.Constants;
import com.visa.innovation.paymentservice.util.CustomLogger;

public class AuthInterceptor implements HandlerInterceptor {

	@Autowired
	private Environment environment;
	@Value("${isUseGateway}")
	private boolean isUseGateway;

	private static final String API_KEY = "gateway_api_key";
	private static final String SHARED_SECRET = "gateway_shared_secret";

	private boolean authenticated = true;

	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object arg2, Exception exception)
			throws Exception {
		if (!authenticated) {
			res.sendError(403, "Request Forbidden");
		}
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse resp, Object arg2, ModelAndView arg3)
			throws Exception {
		CustomLogger.log("post handle");
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj)
			throws InputParametersInvalidException, Exception {
		CustomLogger.log(request.getRequestURI());
		CustomLogger.log("Request received, authenticating");
		// allow loading Swagger API document and health check(s) page without
		// headers
		if (request.getRequestURI().equals("/swagger-ui.html")
				|| request.getRequestURI().matches("/webjars/springfox-swagger-ui/(.*)")
				|| request.getRequestURI().matches("(.*)/swagger-resources(.*)")
				|| request.getRequestURI().matches("(.*)/api-docs") || request.getRequestURI().matches("(.*)/ping")) {
			authenticated = true;
			return authenticated;
		}

		// bypass gateway authentication if disabled
		if (!isUseGateway) {
			checkHeaders(request.getHeader(Constants.USER_ID_HEADER_KEY),
					request.getHeader(Constants.API_KEY_HEADER_KEY));
			return true;
		}
		// allow getting Swagger api-docs JSON response without headers
		if (request.getRequestURI().equals("/api/v1/api-docs")) {
			return true;
		}

		String vendorKey = request.getHeader("x-vendor-key");

		checkHeaders(request.getHeader("on-behalf-of-user"), request.getHeader("x-api-key"), vendorKey,
				request.getHeader("x-card-id"), request.getRequestURI());
		if (getHash(vendorKey).equals(createHash(request.getRequestURI(), vendorKey))) {
			authenticated = true;
			CustomLogger.log("Request Authenticated");
		}

		if (!authenticated) {
			CustomLogger.log("Authentication failed");
			response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized request");
		}

		return authenticated;
	}

	private String getHash(String reqValue) {
		String hash = null;
		if (reqValue != null) {
			hash = reqValue.split(":")[2];
		}
		return hash;
	}

	private String createHash(String apiPath, String vendorKey)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String hash = null;
		String timeStamp = vendorKey.split(":")[1];
		String apiKey = environment.getProperty(API_KEY);
		String sharedSecret = environment.getProperty(SHARED_SECRET);
		if (apiKey != null && sharedSecret != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				String text = apiPath + apiKey + timeStamp + sharedSecret;
				md.update(text.getBytes("UTF-8"));
				hash = String.format("%064x", new java.math.BigInteger(1, md.digest()));
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return hash;
	}

	private void checkHeaders(String onBehalfOfUser, String xApiKey) throws MissingHeadersException {

		List<String> errors = new ArrayList<>();

		if (onBehalfOfUser == null || onBehalfOfUser.isEmpty()) {
			errors.add(Constants.INVALID_ON_BEHALF_OF_USER);
		}
		if (xApiKey == null || xApiKey.isEmpty()) {
			errors.add(Constants.INVALID_X_API_KEY);
		}

		if (errors.size() > 0) {
			CustomLogger.log("Authentication failure: headers required not found.");
			throw new MissingHeadersException(errors);
		}
	}

	private void checkHeaders(String onBehalfOfUser, String xApiKey, String xVendorKey, String cardId, String uri)
			throws MissingHeadersException {

		List<String> errors = new ArrayList<>();

		if (onBehalfOfUser == null || onBehalfOfUser.isEmpty()) {
			errors.add(Constants.INVALID_ON_BEHALF_OF_USER);

		}
		if (xVendorKey == null || xVendorKey.isEmpty()) {
			errors.add(Constants.INVALID_X_VENDOR_KEY);
		} else {
			String vendorKeyArr[] = xVendorKey.split(":");
			if (vendorKeyArr.length != 3) {
				errors.add(Constants.INVALID_X_VENDOR_KEY);
			}
		}
		if (xApiKey == null || xApiKey.isEmpty()) {
			errors.add(Constants.INVALID_X_API_KEY);
		}
		if (cardId == null || cardId.isEmpty()) {
			CustomLogger.log(uri);
			if (!uri.contains("/keys") && !uri.contains("/tokens") && !uri.contains("/ping")) {
				errors.add(Constants.MISSING_X_CARD_ID);
			}
		}
		if (errors.size() > 0) {
			CustomLogger.log("Authentication failure: headers required not found.");
			throw new MissingHeadersException(errors);
		}
	}
}