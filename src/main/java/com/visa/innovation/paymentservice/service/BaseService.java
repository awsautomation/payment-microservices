package com.visa.innovation.paymentservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.visa.innovation.paymentservice.exception.ForbiddenVDPException;
import com.visa.innovation.paymentservice.model.auth.AuthResponse;
import com.visa.innovation.paymentservice.model.capture.CaptureResponse;
import com.visa.innovation.paymentservice.model.refund.RefundResponse;
import com.visa.innovation.paymentservice.model.sales.SalesResponse;
import com.visa.innovation.paymentservice.util.Constants;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.FilterResultsUtils;
import com.visa.innovation.paymentservice.util.Utils;
import com.visa.innovation.paymentservice.vdp.marshallers.AuthMarshallerVDP;
import com.visa.innovation.paymentservice.vdp.marshallers.CaptureMarshallerVDP;
import com.visa.innovation.paymentservice.vdp.marshallers.FilterMetaData;
import com.visa.innovation.paymentservice.vdp.marshallers.SaleMarshallerVDP;
import com.visa.innovation.paymentservice.vdp.marshallers.VDPDataMarshaller;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveAllVDP;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.PageVDP;
import com.visa.innovation.paymentservice.vdp.model.ResultsWrapperVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.utils.MethodTypes;
import com.visa.innovation.paymentservice.vdp.utils.Property;
import com.visa.innovation.paymentservice.vdp.utils.VisaAPIClient;
import com.visa.innovation.paymentservice.vdp.utils.VisaProperties;

public abstract class BaseService {

	@Autowired
	VisaAPIClient visaAPIClient;

	@Autowired
	FilterResultsUtils filterUtils;

	@Autowired
	Utils utils;

	@Autowired
	VDPDataMarshaller vdpDataMarshaller;

	@Autowired
	AuthMarshallerVDP authMarshaller;

	@Autowired
	CaptureMarshallerVDP captureMarshaller;

	@Autowired
	SaleMarshallerVDP salesMarshaller;

	@Autowired
	GetResponsesByIdUtils getResponseByIdUtils;

	@Autowired
	VisaProperties visaProperties;

	/**
	 * Would make a initial request to VDP and wrap the results into VDPResponse
	 * 
	 * Will Return a vdpResponse object with ResultsWrapper object if request is
	 * successful
	 * 
	 * Will return a vdpResponse object with vdpError object if request has
	 * failed/no response received.
	 * 
	 * @param baseUri
	 * @param resourcePath
	 * @param queryParams
	 * @return
	 */
	public VDPResponse<ResultsWrapperVDP> getBaseResult(String baseUri, String resourcePath) {
		VDPResponse<ResultsWrapperVDP> vdpResponse = null;

		// setting the limit to 100 which is the maximum, to reduce the number
		// of network calls made to VDP
		String apiKey = visaProperties.getProperty(Property.API_KEY);
		String queryParam = "apikey=" + apiKey + "&limit=100&offset=1";
		try {
			vdpResponse = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryParam, null, MethodTypes.GET,
					new HashMap<String, String>(), ResultsWrapperVDP.class);

		} catch (Exception e) {
			CustomLogger.logException(e);
		}
		return vdpResponse;
	}

	/**
	 * Get All VDP Page Results
	 * 
	 * @param url
	 * @return
	 * @throws ForbiddenVDPException
	 */
	public VDPResponse<ResultsWrapperVDP> getVDPPageResults(String url) throws ForbiddenVDPException {
		VDPResponse<ResultsWrapperVDP> vdpResponse = null;
		// Adding apikey to the url , so that it becomes part of the queryparam
		// and gets sorted "lexicographically"
		String apiKeyVDP = visaProperties.getProperty(Property.API_KEY);
		url = url + "&" + "apikey=" + apiKeyVDP;
		String queryParams = utils.getQueryParams(url);
		String baseUri = visaProperties.getProperty(Property.BASE_URI);
		String resourcePath = utils.getResourcePath(url);

		vdpResponse = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryParams, null, MethodTypes.GET,
				new HashMap<String, String>(), ResultsWrapperVDP.class);
		return vdpResponse;
	}

	/**
	 * Makes the initial call to getAllAtuh VDPCybs and initiates the filtering
	 * process.
	 * 
	 * Returns the List of Consolidated List of auth Response objects.
	 * 
	 * @param filterMetaData
	 * 
	 * 
	 * @return
	 */
	public <T> List<T> getAllX(String resourcePath, T classType, String serviceType, FilterMetaData filterMetaData) {
		CustomLogger.log("Get All request received");
		VDPResponse<ResultsWrapperVDP> vdpResponse = null;
		List<T> finalResults = null;
		ResultsWrapperVDP vdpResults = null;
		BlockingQueue<List<BaseRetrieveAllVDP>> queue = new LinkedBlockingQueue<>();
		String baseUri = visaProperties.getProperty(Property.BASE_URI);
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();

		// check cache here, so that we can avoid the vdp call
		RedissonClient redisson = utils.getCacheClient();
		RBucket<List<T>> bucket = null;
		if (redisson != null) {
			String cacheKey = utils.getCacheKey(serviceType, filterMetaData);
			bucket = redisson.getBucket(cacheKey);
			finalResults = bucket.get();
		}
		if (finalResults == null) {
			vdpResponse = getBaseResult(baseUri, resourcePath);
			System.out.println(vdpResponse);
			if (context != null && vdpResponse != null && vdpResponse.getVdpError() == null
					&& (vdpResults = vdpResponse.getResponse()) != null) {
				List<BaseRetrieveAllVDP> resultList = vdpResults.getVdpResponse() != null
						? vdpResults.getVdpResponse().getResponse() : null;
				List<PageVDP> nextList = vdpResults.getLinkResults() != null ? vdpResults.getLinkResults().getNext()
						: null;

				// Producer is fed with the initial set of results from VDP
				// Cybs.
				VDPResultsProducer producer = context.getBean(VDPResultsProducer.class);
				producer.setQueue(queue);
				producer.setNextList(nextList);
				producer.addtoQueue(resultList);

				FilterConsumer consumer = context.getBean(FilterConsumer.class);
				consumer.setFilterMetaData(filterMetaData);
				consumer.setQueue(queue);

				Thread prod = new Thread(producer);
				Thread cons = new Thread(consumer);

				cons.start();
				prod.start();

				//
				try {
					cons.join();
					prod.join();

				} catch (InterruptedException e) {
					CustomLogger.logException(e);
				}

				List<BaseRetrieveAllVDP> filteredResults = consumer.getFinalResults();
				finalResults = filteredResultsByServiceType(filteredResults, classType, serviceType);

				// Set results in cache
				if (bucket != null) {
					bucket.setAsync(finalResults);
				}
			}
		}

		if (finalResults != null) {
			CustomLogger.log("filtered output size" + finalResults.size());
		}
		return finalResults;
	}

	/**
	 * A facade which informs the filter fetch result what kind of object has to
	 * built as the final Response.
	 * 
	 * @param filteredList
	 * @param classType
	 * @param serviceType
	 * @return
	 */
	private <T> List<T> filteredResultsByServiceType(List<BaseRetrieveAllVDP> filteredList, T classType,
			String serviceType) {

		if (filteredList == null || filteredList.size() == 0) {
			return null;
		}

		List<T> finalResults = new ArrayList<>();
		switch (serviceType) {
		case Constants.SERVICE_AUTH:
			finalResults = filterFetchResults(filteredList, classType, Constants.SERVICE_AUTH);
			break;
		case Constants.SERVICE_CAPTURE:
			finalResults = filterFetchResults(filteredList, classType, Constants.SERVICE_CAPTURE);
			break;
		case Constants.SERVICE_SALES:
			finalResults = filterFetchResults(filteredList, classType, Constants.SERVICE_SALES);
			break;
		case Constants.SERVICE_REFUND:
			finalResults = filterFetchResults(filteredList, classType, Constants.SERVICE_REFUND);
			break;
		default:
			break;

		}

		return finalResults;
	}

	/**
	 * Parallelizes the process of making Auth/Capture/Sales/Refund by Id calls
	 * to VDP to get status, and payment information.
	 *
	 * Will return a null if a error occurred on VDP / 404 was returned by VDP.
	 *
	 * @param <T>
	 *
	 * @param finalResults
	 * @return
	 */
	public <T> List<T> filterFetchResults(List<BaseRetrieveAllVDP> finalResults, T classType, String serviceType) {

		final ExecutorService servicePool = Executors.newFixedThreadPool(finalResults.size());
		List<T> authResponses = new ArrayList<>();
		List<Callable<T>> callables = new ArrayList<>();
		finalResults.forEach(vdpResult -> {
			@SuppressWarnings("unchecked")
			Callable<T> callable = () -> {
				T response = null;
				String vdpResultId = vdpResult.getId();
				VDPResponse<BaseRetrieveByIdResponseVDP> vdpResp = null;
				// System.out.println(vdpResp.getResponse().toString());
				switch (serviceType) {
				case Constants.SERVICE_AUTH:
					vdpResp = getResponseByIdUtils.getVDPAuthResponseById(vdpResultId);
					response = (T) getMarshalledResponse(Constants.SERVICE_AUTH, AuthResponse.class, vdpResp,
							vdpResult);
					break;
				case Constants.SERVICE_CAPTURE:
					vdpResp = getResponseByIdUtils.getVDPCaptureResponseById(vdpResultId);
					response = (T) getMarshalledResponse(Constants.SERVICE_CAPTURE, CaptureResponse.class, vdpResp,
							vdpResult);
					break;
				case Constants.SERVICE_SALES:
					vdpResp = getResponseByIdUtils.getVDPResponseSaleById(vdpResultId);
					response = (T) getMarshalledResponse(Constants.SERVICE_SALES, SalesResponse.class, vdpResp,
							vdpResult);
					break;
				case Constants.SERVICE_REFUND:
					response = (T) getMarshalledResponse(Constants.SERVICE_REFUND, RefundResponse.class, vdpResp,
							vdpResult);
					break;
				default:
					break;

				}

				return response;
			};
			callables.add(callable);
		});

		try {
			authResponses = servicePool.invokeAll(callables).stream().map(future -> {
				try {
					return future.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
					return null;
				}
			}).collect(Collectors.toList());
		} catch (InterruptedException e) {
			CustomLogger.log(e.getStackTrace());
			e.printStackTrace();
		} finally {
			if (!servicePool.isTerminated()) {
				System.err.println("cancel non-finished tasks");
			}
			servicePool.shutdownNow();
		}

		servicePool.shutdown();
		return authResponses;
	}

	/**
	 * will wrap the vdp response , vdp response by id into the data contract of
	 * the payment service.
	 * 
	 * @param responseType
	 * @param responseClass
	 * @param vdpResponseById
	 * @param vdpBaseResponse
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T getMarshalledResponse(String responseType, T responseClass,
			VDPResponse<BaseRetrieveByIdResponseVDP> vdpResponseById, BaseRetrieveAllVDP vdpBaseResponse) {
		T response = null;
		if (responseType.equalsIgnoreCase(Constants.SERVICE_AUTH)) {
			if (vdpResponseById.getVdpError() == null) {
				BaseRetrieveByIdResponseVDP authByIdResp = vdpResponseById.getResponse();
				response = (T) authMarshaller.buildAuthResponse(vdpBaseResponse, authByIdResp);
			} else {
				response = (T) authMarshaller.buildAuthResponse(vdpBaseResponse);
			}
		} else if (responseType.equalsIgnoreCase(Constants.SERVICE_CAPTURE)) {
			if (vdpResponseById.getVdpError() == null) {
				BaseRetrieveByIdResponseVDP captureByIdResp = vdpResponseById.getResponse();
				response = (T) captureMarshaller.buildCaptureResponse(vdpBaseResponse, captureByIdResp);
			} else {
				response = (T) captureMarshaller.buildCaptureResponse(vdpBaseResponse);
			}
		} else if (responseType.equalsIgnoreCase(Constants.SERVICE_SALES)) {
			if (vdpResponseById.getVdpError() == null) {
				BaseRetrieveByIdResponseVDP saleByIdResp = vdpResponseById.getResponse();
				response = (T) salesMarshaller.buildSalesResponse(vdpBaseResponse, saleByIdResp);
			} else {
				response = (T) salesMarshaller.buildSalesResponse(vdpBaseResponse);
			}
		} else if (responseType.equalsIgnoreCase(Constants.SERVICE_REFUND)) {
			// TODO: Implement Refund Service
			// if (vdpResponseById.getVdpError() == null) {
			// BaseRetrieveByIdResponseVDP saleByIdResp =
			// vdpResponseById.getResponse();
			// response = (T)
			// salesMarshaller.buildSalesResponse(vdpBaseResponse,
			// saleByIdResp);
			// } else {
			// response = (T)
			// salesMarshaller.buildSalesResponse(vdpBaseResponse);
			// }
		}

		return response;
	}

}
