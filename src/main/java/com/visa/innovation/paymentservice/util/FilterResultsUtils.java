package com.visa.innovation.paymentservice.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.vdp.marshallers.FilterMetaData;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveAll;

public class FilterResultsUtils {

	@Autowired
	Utils utils;

	String identifier = null;

	/**
	 * Returns a filtered list from the total list . Filtering is based upon the
	 * hybrid id(user id + app id ).
	 * 
	 * Parallelizes the process of filtering by partitioning the list and
	 * filtering in each of the lists.
	 * 
	 * Returns a filtered list, the size of the list will be 0 if there are no
	 * items matching the hybrid id.
	 * 
	 * @param resultsList
	 * @param requestClass
	 * @param filterMetaData
	 *            TODO
	 * @return
	 */
	public <T> List<T> filterResults(List<T> resultsList, Class<T> requestClass, FilterMetaData filterMetaData) {

		// Each list would be of size 100 at the maximum.Dividing the list into
		// partitions of 15 each
		final int partitionSize = 15;

		List<List<T>> partionedLists = getListPartitions(resultsList, partitionSize);
		List<T> finalResults = new ArrayList<>();

		// Init the thread pool with the number of partions created.
		ExecutorService servicePool = Executors.newFixedThreadPool(partionedLists.size());

		// Creating a list of callables which will be run on threads to filter
		// out the matched user+app context
		List<Callable<List<T>>> callables = getTasks(partionedLists);
		if (filterMetaData != null) {
			identifier = utils.generateUserAppCardComboId(filterMetaData.userId, filterMetaData.appId,
					filterMetaData.cardId);
		}
		List<List<T>> filteredResults = new ArrayList<>();
		try {
			filteredResults = servicePool.invokeAll(callables).stream().map(future -> {
				try {
					return future.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
					return null;
				}
			}).collect(Collectors.toList());

			// Shutting down the Executor Service.
			servicePool.shutdown();
		} catch (InterruptedException e) {
			CustomLogger.log(e.getStackTrace());
			e.printStackTrace();
		} finally {
			if (!servicePool.isTerminated()) {
				System.err.println("cancel non-finished tasks");
			}
			servicePool.shutdownNow();
		}

		// Collating all the filteredResults from different threads into a
		// single list
		filteredResults.forEach(result -> {
			result.forEach(authObj -> {
				finalResults.add(authObj);
			});
		});
		return finalResults;

	}

	/**
	 * Divides the given list into lists each of size equal to the given
	 * partition size.
	 * 
	 * @param list
	 * @param partitionSize
	 * @return
	 */
	private <S> List<List<S>> getListPartitions(List<S> list, int partitionSize) {
		List<List<S>> partitions = new LinkedList<List<S>>();
		for (int i = 0; i < list.size(); i += partitionSize) {
			partitions.add(list.subList(i, Math.min(i + partitionSize, list.size())));
		}
		return partitions;
	}

	/**
	 * Creates a callable for each list in the list of lists
	 * 
	 * The callable filters the list based on the userId+AppId combination
	 * 
	 * @param partionedLists
	 * @return
	 */
	private <S> List<Callable<List<S>>> getTasks(List<List<S>> partionedLists) {

		List<Callable<List<S>>> callables = new ArrayList<>();
		partionedLists.forEach(list -> {
			Callable<List<S>> callable = () -> {
				List<S> filteredList = getFilteredResults(identifier, list);
				return filteredList;
			};
			callables.add(callable);
		});
		return callables;
	}

	/**
	 * Looks up each item's reference id and filters them based on the
	 * userAppComboId
	 * 
	 * @param userAppIdentifier
	 * @param partionList
	 * @return
	 */
	private <T> List<T> getFilteredResults(String userAppIdentifier, List<T> partionList) {
		List<T> filteredList = new ArrayList<>();

		for (T auth : partionList) {
			BaseRetrieveAll authType = (BaseRetrieveAll) auth;
			String userAppComboId = utils.getUserAppCardComboId(authType.getReferenceId());
			if (authType.getReferenceId() != null && userAppComboId != null
					&& userAppComboId.equals(userAppIdentifier)) {
				filteredList.add(auth);
			}
		}
		return filteredList;
	}

}
