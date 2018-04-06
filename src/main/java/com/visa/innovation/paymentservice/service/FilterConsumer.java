package com.visa.innovation.paymentservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.FilterResultsUtils;
import com.visa.innovation.paymentservice.vdp.marshallers.FilterMetaData;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveAllVDP;

/**
 * It is responsible to consume the results which are fed to block queue and
 * send them for filtering. The consumer acts as the collator of filtered
 * results across the pages that are filtered.
 * 
 * @author ntelukun
 *
 */
public class FilterConsumer implements Runnable {

	@Autowired
	FilterResultsUtils filterUtils;

	BlockingQueue<List<BaseRetrieveAllVDP>> queue = null;
	private List<BaseRetrieveAllVDP> finalResults = null;

	private FilterMetaData filterMetaData;

	public FilterMetaData getFilterMetaData() {
		return filterMetaData;
	}

	public void setFilterMetaData(FilterMetaData filterMetaData) {
		this.filterMetaData = filterMetaData;
	}

	public FilterConsumer() {
		finalResults = new ArrayList<>();
		CustomLogger.log("New Consumer object created");
	}

	public BlockingQueue<List<BaseRetrieveAllVDP>> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<List<BaseRetrieveAllVDP>> queue) {
		this.queue = queue;
	}

	public List<BaseRetrieveAllVDP> getFinalResults() {
		return finalResults;
	}

	public void setFinalResults(List<BaseRetrieveAllVDP> finalResults) {
		this.finalResults = finalResults;
	}

	/**
	 * This keeps on feeding off from Blocking queue , waits when the Blocking
	 * queue does not have results
	 */
	public void run() {
		List<BaseRetrieveAllVDP> rawResults = null;
		try {
			// The poison pill condition where it knows that the producer is no
			// longer producing the results. The consumer will wait for the
			// filter utils to complete its task and then return.
			rawResults = queue.take();
			while (rawResults != null && rawResults.size() != 0) {
				if (rawResults.size() == 0) {
					CustomLogger.log("Exiting the consumer as producer intimates no more results are being produced");
					break;
				}
				List<BaseRetrieveAllVDP> filterLists = filterUtils.filterResults(rawResults, BaseRetrieveAllVDP.class,
						filterMetaData);
				finalResults.addAll(filterLists);
				rawResults = queue.take();
				CustomLogger.log("Final results size" + finalResults.size());
			}
			CustomLogger.log("Exiting the consumer");
		} catch (InterruptedException e) {
			CustomLogger.logException(e);
		}
	}
}
