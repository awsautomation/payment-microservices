package com.visa.innovation.paymentservice.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;
import com.visa.innovation.paymentservice.soa.model.PaymentStatusEnum;
import com.visa.innovation.paymentservice.vdp.marshallers.FilterMetaData;
import com.visa.innovation.paymentservice.vdp.model.ResultsWrapperVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.utils.Property;
import com.visa.innovation.paymentservice.vdp.utils.VisaProperties;

@Component
public class Utils {

	@Autowired
	VisaProperties visaProperties;
	
	public Utils() {
	}

	/**
	 * Get the queryParams in sorted order from the url
	 * 
	 * @param url
	 * @return
	 */
	public String getQueryParams(String url) {
		String queryParams = null;
		StringBuilder sb = new StringBuilder();
		if (url != null && url.length() != 0 && url.contains("?")) {
			queryParams = url.split("\\?")[1]; // Considering 1st value as it
												// contains queryparams.
			String[] queryParamArr = queryParams.split("&");
			Arrays.sort(queryParamArr);
			// constructing a sorted queryParam string
			for (String param : queryParamArr) {
				sb.append(param);
				sb.append("&");
			}
		}
		String paramString = sb.toString().substring(0, sb.length() - 1);
		Pattern p = Pattern.compile("\\blimit=20\\b", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(paramString);
		String result = m.replaceAll("limit=100");
		System.out.println(result);
		return result;
	}

	/**
	 * Returns the resourcePath from the url
	 * 
	 * @param url
	 * @return
	 */
	public String getResourcePath(String url) {
		String resourcePath = null;
		String baseUri = visaProperties.getProperty(Property.BASE_URI);
		// TODO: Write a regex to extract the resource path
		resourcePath = url.substring(url.lastIndexOf(baseUri), url.length()).replaceAll(baseUri, "").split("\\?")[0];
		return resourcePath;
	}

	/**
	 * Will convert a String milliseconds to Date
	 * 
	 * Returns the date in the format yyyy-MM-dd HH-mm:ss
	 * 
	 * It returns GMT time in 12 hour format ,
	 * 
	 * @param millisec
	 * @return
	 */
	public String stringMillisToDate(String millisec) {
		long millisecs = Long.parseLong(millisec);
		//
		// Date date = DateFormat.
		Date date = new Date(millisecs);
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd h:mm:ss a");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		return formatter.format(date).toString();
	}

	/**
	 * Combines the user id , api key and order id(optional) into a hybrid id.
	 * 
	 * The userid , apikey are hashed and appended by a order id with delimiter
	 * as "::"
	 * 
	 * This hybrid id is used as reference id when creating an
	 * auth/capture/sales.
	 * 
	 * This is used to identify the transaction wrt a user and an app.
	 * 
	 * @param userId
	 * @param apiKey
	 * @param orderId
	 * @return
	 */

	public String generateHybridId(String userId, String apiKey, String orderId, String cardId) {

		if (orderId == null) {
			orderId = "";
		}
		Integer hash = Objects.hashCode(userId, apiKey, cardId);
		if (hash < 0) {
			hash = hash * -1;
		}

		String hybridId = Integer.toString(hash);
		return hybridId + Constants.HYBRID_ID_DELIMITER + orderId;

	}

	/**
	 * Return the order id from the hybrid id.
	 * 
	 * @param hybridId
	 * @return
	 */

	public String getOrderId(String hybridId) {

		int beginIndex = hybridId.lastIndexOf(Constants.HYBRID_ID_DELIMITER) + 2;
		return hybridId.substring(beginIndex, hybridId.length());
	}

	/**
	 * Returns the user id and api key combined hash given a hybrid id.
	 * 
	 * @param hybridId
	 * @return
	 */

	public String getUserAppCardComboId(String hybridId) {
		int endIndex = hybridId.lastIndexOf(Constants.HYBRID_ID_DELIMITER);
		if (endIndex == -1) {
			return null;
		}
		return hybridId.substring(0, endIndex);
	}

	/**
	 * Generates the user id and api key combination. used for filtering the
	 * results from VDP
	 * 
	 * @param userId
	 * @param apiKey
	 * @return
	 */
	public String generateUserAppCardComboId(String userId, String apiKey, String cardId) {
		Integer hash = Objects.hashCode(userId, apiKey, cardId);
		if (hash < 0) {
			hash = hash * -1;
		}

		String hybridId = Integer.toString(hash);
		return hybridId;
	}

	/**
	 * Maps the status coming in from VDP to payment service Status Enum.
	 * 
	 * @param vdpStatus
	 * @return
	 */
	public PaymentStatusEnum getServiceStatus(String vdpStatus) {

		if (vdpStatus.equals(Constants.STATUS_PENDING_CAPTURE_VDP)) {
			return PaymentStatusEnum.PENDING_CAPTURE;
		} else if (vdpStatus.equals(Constants.STATUS_PENDING_SETTLEMENT_VDP)) {
			return PaymentStatusEnum.PENDING_SETTLEMENT;
		} else if (vdpStatus.equals(Constants.STATUS_VOIDED_VDP) || vdpStatus.equals(Constants.STATUS_AUTH_REVERSED)) {
			return PaymentStatusEnum.VOIDED;
		} else if (vdpStatus.equalsIgnoreCase(Constants.STATUS_SETTLED_SUCCESSFULLY)) {
			return PaymentStatusEnum.SETTLED;
		} else if (vdpStatus.equalsIgnoreCase(Constants.STATUS_REFUNDED)) {
			return PaymentStatusEnum.REFUNDED;
		}
		return null;
	}
	
	public RedissonClient getCacheClient() {
    	final Config config = new Config();
    	String cacheURI = System.getenv().get("CACHE_URI");
        if (cacheURI == null) {
        	CustomLogger.log("WARNING: CACHE_URI environment variable is missing");
        	return null;
        }
    	config.useSingleServer().setAddress(cacheURI);
    	RedissonClient redisson = Redisson.create(config);
    	return redisson;
	}

	public String getCacheKey(String serviceType, FilterMetaData filterMetaData) {
		String cacheKey = "";
		if(serviceType != null && filterMetaData != null) {
			cacheKey = Constants.CACHE_KEY_PREFIX + "-" + serviceType + "-" + filterMetaData.toString();
		}
		return cacheKey;
	}

	public void invalidateCache(String serviceType, String userId, String apiKey, String cardId) {
		RedissonClient redisson = getCacheClient();
		if(redisson != null) {
			FilterMetaData filterMetaData = new FilterMetaData.Builder().appId(apiKey).cardId(cardId).userId(userId).build();
			String cacheKey = getCacheKey(serviceType, filterMetaData);
	    	RBucket<VDPResponse<ResultsWrapperVDP>> bucket = redisson.getBucket(cacheKey);
	    	bucket.deleteAsync();
		}
	}

}
