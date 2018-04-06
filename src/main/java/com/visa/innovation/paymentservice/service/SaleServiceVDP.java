package com.visa.innovation.paymentservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.exception.ForbiddenVDPException;
import com.visa.innovation.paymentservice.model.sales.SalesResponse;
import com.visa.innovation.paymentservice.util.Constants;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.Utils;
import com.visa.innovation.paymentservice.vdp.marshallers.FilterMetaData;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.VoidRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.sale.CreateSaleRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.sale.CreateSaleResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.sale.RetrieveSaleByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.utils.MethodTypes;
import com.visa.innovation.paymentservice.vdp.utils.Property;
import com.visa.innovation.paymentservice.vdp.utils.VisaAPIClient;

public class SaleServiceVDP extends BaseService {

	@Autowired
	VisaAPIClient visaAPIClient;

	@Autowired
	Utils utils;

	String apiKeyVDP;
	String baseUri;
	String queryString;

	public SaleServiceVDP() {
	}

	@PostConstruct
	private void init() {
		this.apiKeyVDP = visaProperties.getProperty(Property.API_KEY);
		this.baseUri = visaProperties.getProperty(Property.BASE_URI);
		this.queryString = "apikey=" + apiKeyVDP;
	}

	public VDPResponse<CreateSaleResponseVDP> createSale(CreateSaleRequestVDP saleRequestVDP) {

		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_SALES);
		VDPResponse<CreateSaleResponseVDP> response = null;
		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, saleRequestVDP,
					MethodTypes.POST, new HashMap<String, String>(), CreateSaleResponseVDP.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;

	}

	public VDPResponse<ReverseVoidResponseVDP> updateSale(VoidRequestVDP voidSaleRequestVDP, String saleId) {

		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_SALE_VOID);
		resourcePath = resourcePath.replace("{sale-id}", saleId);
		VDPResponse<ReverseVoidResponseVDP> response = null;
		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, voidSaleRequestVDP,
					MethodTypes.POST, new HashMap<String, String>(), ReverseVoidResponseVDP.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	public VDPResponse<RetrieveSaleByIdResponseVDP> getSaleById(String saleId) {

		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_SALE_RETRIEVE_BY_ID);
		resourcePath = resourcePath.replace("{sales-id}", saleId);

		VDPResponse<RetrieveSaleByIdResponseVDP> response = null;
		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, null, MethodTypes.GET,
					new HashMap<String, String>(), RetrieveSaleByIdResponseVDP.class);
		} catch (ForbiddenVDPException e) {
			CustomLogger.logException(e);
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * Fetches the sales from VDP API initially and then fetches all the results
	 * based on the initial result
	 * 
	 * Makes a call to get the results filtered based on the userId and appId
	 * combination
	 * 
	 * Makes calls to individual sales end points to get the status information.
	 * 
	 * Sorts the filtered results according to the created Time
	 * 
	 * @param filterMetaData
	 *            TODO
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<SalesResponse> getAllSales(FilterMetaData filterMetaData) {

		List<SalesResponse> results = new ArrayList<>();
		String resourcePath = visaProperties
				.getProperty(com.visa.innovation.paymentservice.vdp.utils.Property.RESOURCE_PATH_SALES);
		List<?> finalResults = null;
		try {
			finalResults = getAllX(resourcePath, SalesResponse.class, Constants.SERVICE_SALES, filterMetaData);

		} catch (Exception e) {
			CustomLogger.logException(e);
		}

		results = (List<SalesResponse>) finalResults;
		CustomLogger.log(results);
		return results;
	}

	// TODO : Combine get captures and sales calls
	// private List<SalesResponse> combineLists(List<SalesResponse>
	// salesResponse, List<CaptureResponse> captureResponse) {
	// return null;
	// }
}
