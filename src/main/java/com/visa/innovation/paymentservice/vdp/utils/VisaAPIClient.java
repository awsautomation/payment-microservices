package com.visa.innovation.paymentservice.vdp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SignatureException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.visa.innovation.paymentservice.exception.ForbiddenVDPException;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.vdp.model.ResponseStatusVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPError;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;

/**
 * Source: https://github.com/visa/SampleCode
 */

@Component
public class VisaAPIClient {

	@Autowired
	VisaProperties visaProperties;
	@Autowired
	XPayTokenGenerator xPayTokenGenerator;
	private static CloseableHttpClient XPayHttpClient;
	private static String corelationID = null;

	private CloseableHttpClient fetchXPayHttpClient() {
		XPayHttpClient = HttpClients.createDefault();
		return XPayHttpClient;
	}

	private HttpRequest createHttpRequest(MethodTypes methodType, String url) throws Exception {
		HttpRequest request = null;
		switch (methodType) {
		case GET:
			request = new HttpGet(url);
			break;
		case POST:
			request = new HttpPost(url);
			break;
		case PUT:
			request = new HttpPut(url);
			break;
		case DELETE:
			request = new HttpDelete(url);
			break;
		default:
			CustomLogger.log("Incompatible HTTP request method " + methodType);
		}
		return request;
	}

	// TODO: merge this with doXPayTokenRequest function
	/**
	 * This function is meant to make calls to VDP CYBS flex token API.
	 * 
	 * @param baseUri
	 * @param resourcePath
	 * @param body
	 * @param methodType
	 * @param responseType
	 * @return
	 * @throws ForbiddenVDPException
	 */
	public <S, T> VDPResponse<T> doTokenRequest(String baseUri, String resourcePath, S body, MethodTypes methodType,
			Class<T> responseType) throws ForbiddenVDPException {
		String url = visaProperties.getProperty(Property.END_POINT_TOKENS) + baseUri + resourcePath;
		ObjectMapper mapper = new ObjectMapper();
		String stringBody = null;
		try {
			stringBody = mapper.writeValueAsString(body);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpRequest request = null;
		try {
			request = createHttpRequest(methodType, url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setHeader("content-type", "application/json");

		if (request instanceof HttpPost) {
			((HttpPost) request).setEntity(new StringEntity(stringBody, "UTF-8"));
		}
		CustomLogger.log(url);
		CustomLogger.log(body);
		CloseableHttpResponse response = null;
		try {
			response = fetchXPayHttpClient().execute((HttpUriRequest) request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		VDPResponse<T> vdpResponse = null;
		CustomLogger.log(response.getStatusLine());
		HttpEntity entity = response.getEntity();

		try {
			if (response.getStatusLine().getStatusCode() == HttpStatus.FORBIDDEN.value()) {
				throw new ForbiddenVDPException();
			} else if (response.getStatusLine().getStatusCode() < 200
					|| response.getStatusLine().getStatusCode() >= 300) {
				vdpResponse = new VDPResponse.Builder<T>()
						.vdpError(mapper.readValue(entity.getContent(), VDPError.class)).build();
			} else {
				// logResponse(response);
				vdpResponse = new VDPResponse.Builder<T>().response(mapper.readValue(entity.getContent(), responseType))
						.build();
			}
		} catch (UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return vdpResponse;
	}

	/**
	 * Make calls to VDP CYBS Direct Endpoints.
	 * 
	 * @param baseUri
	 * @param resourcePath
	 * @param queryParams
	 * @param body
	 * @param methodType
	 * @param headers
	 * @param responseType
	 * @return
	 * @throws ForbiddenVDPException
	 */
	// TODO: Revisit the signature of the function., Look if testInfo is
	// required.
	public <S, T> VDPResponse<T> doXPayTokenRequest(String baseUri, String resourcePath, String queryParams, S body,
			MethodTypes methodType, Map<String, String> headers, Class<T> responseType) throws ForbiddenVDPException {

		String url = visaProperties.getProperty(Property.END_POINT) + baseUri + resourcePath + "?" + queryParams;

		CustomLogger.log(methodType.toString() + " " + url);
		CustomLogger.log("Request: " + body);
		ObjectMapper mapper = new ObjectMapper();
		String stringBody = null;
		if (body == null) {
			stringBody = "";
		} else {
			try {
				stringBody = mapper.writeValueAsString(body);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		HttpRequest request = null;
		try {
			request = createHttpRequest(methodType, url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.setHeader("content-type", "application/json");
		String xPayToken = null;

		// Attaching Query Params with Api key. Recommended by VDP.
		// String queryParameters = "apikey=" + apikey;
		try {
			xPayToken = xPayTokenGenerator.generateXpaytoken(resourcePath, queryParams, stringBody);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (corelationID == null) {
			corelationID = getCorrelationId();
		}
		request.setHeader("x-pay-token", xPayToken);
		request.setHeader("ex-correlation-id", corelationID);

		// TODO: Check if the headers part of the code is required. Remove if
		// not used.
		if (headers != null && headers.isEmpty() == false) {
			for (Entry<String, String> header : headers.entrySet()) {
				request.setHeader(header.getKey(), header.getValue());
			}
		}

		if (request instanceof HttpPost) {
			((HttpPost) request).setEntity(new StringEntity(stringBody, "UTF-8"));

		} else if (request instanceof HttpPut) {
			((HttpPut) request).setEntity(new StringEntity(stringBody, "UTF-8"));
		}

		CloseableHttpResponse response = null;
		try {
			response = fetchXPayHttpClient().execute((HttpUriRequest) request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return buildServiceUnavailableResponse();
		}
		VDPResponse<T> vdpResponse = null;
		CustomLogger.log(response.getStatusLine());
		HttpEntity entity = response.getEntity();

		try {
			if (response.getStatusLine().getStatusCode() == HttpStatus.FORBIDDEN.value()) {
				throw new ForbiddenVDPException();
			} else if (response.getStatusLine().getStatusCode() < 200
					|| response.getStatusLine().getStatusCode() >= 300) {
				vdpResponse = new VDPResponse.Builder<T>()
						.vdpError(mapper.readValue(entity.getContent(), VDPError.class)).build();
			} else {
				vdpResponse = new VDPResponse.Builder<T>().response(mapper.readValue(entity.getContent(), responseType))
						.build();
			}
		} catch (UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CustomLogger.log("Response: " + vdpResponse);
		return vdpResponse;
	}

	/**
	 * For Logging purposes, to view the raw response object(s) returned by VDP.
	 * Used for debugging purposes.
	 * 
	 * @param response
	 * @throws IOException
	 */
	private static void logResponse(CloseableHttpResponse response) throws IOException {
		Header[] h = response.getAllHeaders();

		// Get the response json object
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		// Print the response details
		HttpEntity entity = response.getEntity();
		CustomLogger.log("Response status : " + response.getStatusLine() + "\n");

		CustomLogger.log("Response Headers: \n");

		for (int i = 0; i < h.length; i++)
			CustomLogger.log(h[i].getName() + ":" + h[i].getValue());
		CustomLogger.log("\n Response Body:");

		if (!StringUtils.isEmpty(result.toString())) {
			ObjectMapper mapper = getObjectMapperInstance();
			Object tree;
			try {
				tree = mapper.readValue(result.toString(), Object.class);
				CustomLogger.log("ResponseBody: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree));
			} catch (JsonProcessingException e) {
				CustomLogger.log(e.getMessage());
			} catch (IOException e) {
				CustomLogger.log(e.getMessage());
			}
		}

		EntityUtils.consume(entity);
	}

	/**
	 * For Logging purposes, to view the raw response object(s) returned by VDP.
	 * Used for debugging purposes.
	 * 
	 * @param response
	 * @throws IOException
	 */
	private static void logRequestBody(String URI, String testInfo, String payload) {
		ObjectMapper mapper = getObjectMapperInstance();
		Object tree;
		CustomLogger.log("URI: " + URI);
		CustomLogger.log(testInfo);
		if (!StringUtils.isEmpty(payload)) {
			try {
				tree = mapper.readValue(payload, Object.class);
				CustomLogger.log("RequestBody: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree));
			} catch (JsonProcessingException e) {
				CustomLogger.log(e.getMessage());
			} catch (IOException e) {
				CustomLogger.log(e.getMessage());
			}
		}
	}

	/**
	 * Get Correlation Id for the API Call.
	 * 
	 * @return correlation Id
	 */
	private String getCorrelationId() {
		// Passing correlation Id header is optional while making an API call.
		return RandomStringUtils.random(10, true, true) + "_SC";
	}

	/**
	 * Get New Instance of ObjectMapper
	 * 
	 * @return
	 */
	protected static ObjectMapper getObjectMapperInstance() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true); // format
																	// json
		return mapper;
	}

	/**
	 * Used to build the object that constructs the 503 error.
	 * 
	 * @return
	 */
	private <T> VDPResponse<T> buildServiceUnavailableResponse() {

		return new VDPResponse.Builder<T>()
				.vdpError(new VDPError.Builder()
						.responseStatusVDP(
								new ResponseStatusVDP.Builder().status(HttpStatus.SERVICE_UNAVAILABLE.toString())
										.message("Underlying payment service unavailable. Please try again.").build())
						.build())
				.build();

	}

}
