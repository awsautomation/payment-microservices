package com.visa.innovation.paymentservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.exception.ForbiddenVDPException;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.Utils;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveAllVDP;
import com.visa.innovation.paymentservice.vdp.model.PageVDP;
import com.visa.innovation.paymentservice.vdp.model.ResultsWrapperVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.utils.MethodTypes;
import com.visa.innovation.paymentservice.vdp.utils.Property;
import com.visa.innovation.paymentservice.vdp.utils.VisaAPIClient;
import com.visa.innovation.paymentservice.vdp.utils.VisaProperties;

/**
 * This would act as the producer which feeds to the Blocking Queue. It is
 * responsible to fetch all the transactions from Cybersource and provide them
 * for filtering.
 * 
 * @author ntelukun
 *
 */
public class VDPResultsProducer implements Runnable {

	@Autowired
	Utils utils;

	@Autowired
	VisaAPIClient visaAPIClient;

	@Autowired
	VisaProperties visaProperties;

	BlockingQueue<List<BaseRetrieveAllVDP>> queue;
	List<PageVDP> nextList;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public VDPResultsProducer() {
		CustomLogger.log("Producer object created");
	}

	public VDPResultsProducer(BlockingQueue<List<BaseRetrieveAllVDP>> queue) {
		this.queue = queue;
	}

	public VDPResultsProducer(BlockingQueue<List<BaseRetrieveAllVDP>> queue, List<PageVDP> nextList) {
		this.queue = queue;
		this.nextList = nextList;
	}

	public boolean addtoQueue(List<BaseRetrieveAllVDP> resultList) {
		return this.queue.add(resultList);
	}

	public BlockingQueue<List<BaseRetrieveAllVDP>> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<List<BaseRetrieveAllVDP>> queue) {
		this.queue = queue;
	}

	public List<PageVDP> getNextList() {
		return nextList;
	}

	public void setNextList(List<PageVDP> nextList) {
		this.nextList = nextList;
	}

	/**
	 * This will feed the blocking queue with the results from VDP Cybs until we
	 * hit end of the page. We will need to make changes to when we implement
	 * pagination to the api.
	 */
	public void run() {
		if (nextList != null) {
			nextList.forEach(page -> {
				try {
					while (page != null && page.getLink() != null) {
						System.out.println(page.getLink());
						VDPResponse<ResultsWrapperVDP> subRes = getVDPPageResults(page.getLink());
						if (subRes.getVdpError() == null && subRes.getResponse() != null) {
							List<BaseRetrieveAllVDP> results = subRes.getResponse().getVdpResponse().getResponse();

							// making changes here to add blocking queue
							if (results != null) {
								try {
									CustomLogger.log("Adding to queue" + results.size());
									queue.put(results);
								} catch (InterruptedException e) {
									CustomLogger.logException(e);
								}
							}

							if (subRes.getResponse().getLinkResults().getNext() != null) {
								page = subRes.getResponse().getLinkResults().getNext().get(0);
							} else {
								page = null;
							}
							CustomLogger.log("result size:" + results.size());
						}
					}
					// Adding the poison pill
					CustomLogger.log("Adding poison pill condition");
					queue.add(new ArrayList<>());
				} catch (Exception e) {
					CustomLogger.log(e.getStackTrace());
				}
			});
		}
		CustomLogger.log("Adding poison pill condition");
		queue.add(new ArrayList<>());
		CustomLogger.log("Exiting the producer");
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

}