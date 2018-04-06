package com.visa.innovation.paymentservice.wrappers.vdp;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.ValueType;
import org.springframework.beans.factory.annotation.Autowired;

import com.visa.innovation.paymentservice.exception.GenericVDPException;
import com.visa.innovation.paymentservice.exception.TransactionNotFoundException;
import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.UpdatePaymentRequest;
import com.visa.innovation.paymentservice.model.sales.SalesRequest;
import com.visa.innovation.paymentservice.model.sales.SalesResponse;
import com.visa.innovation.paymentservice.odata.service.EdmConstants;
import com.visa.innovation.paymentservice.service.SaleServiceVDP;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.FilterResultsUtils;
import com.visa.innovation.paymentservice.vdp.marshallers.FilterMetaData;
import com.visa.innovation.paymentservice.vdp.marshallers.VDPDataMarshaller;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.VoidRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.sale.CreateSaleRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.sale.CreateSaleResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.sale.RetrieveSaleByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.utils.Property;
import com.visa.innovation.paymentservice.vdp.utils.VisaAPIClient;
import com.visa.innovation.paymentservice.vdp.utils.VisaProperties;

public class SaleWrapperVDP {

	@Autowired
	VisaAPIClient visaAPIClient;
	@Autowired
	VDPDataMarshaller vdpDataMarshaller;
	@Autowired
	SaleServiceVDP saleServiceVDP;
	@Autowired
	FilterResultsUtils filterUtils;
	@Autowired
	VisaProperties visaProperties;

	String apiKeyVDP;
	String baseUri;
	String queryString;

	public SaleWrapperVDP() {
	}

	@PostConstruct
	private void init() {
		this.apiKeyVDP = visaProperties.getProperty(Property.API_KEY);
		this.baseUri = visaProperties.getProperty(Property.BASE_URI);
		this.queryString = "apikey=" + apiKeyVDP;
	}

	public ServiceResponse<SalesResponse> createSale(SalesRequest request) {
		CustomLogger.log("Sale request received");
		CustomLogger.log(request);

		CreateSaleRequestVDP saleRequestVDP = vdpDataMarshaller.buildServiceSaleRequest(request);

		VDPResponse<CreateSaleResponseVDP> response = null;
		response = saleServiceVDP.createSale(saleRequestVDP);

		ServiceResponse<SalesResponse> serviceResponse = vdpDataMarshaller.buildSaleResponse(response);
		return serviceResponse;
	}

	public ServiceResponse<SalesResponse> updateSales(UpdatePaymentRequest updateSaleRequest, String saleId)
			throws TransactionNotFoundException {

		CustomLogger.log("Update sale request received");

		VDPResponse<RetrieveSaleByIdResponseVDP> retrieveSaleResponse = null;
		VoidRequestVDP voidSaleRequestVDP = null;
		ServiceResponse<SalesResponse> updateSaleResponse = null;

		retrieveSaleResponse = saleServiceVDP.getSaleById(saleId);
		if (retrieveSaleResponse.getVdpError() == null) {
			voidSaleRequestVDP = vdpDataMarshaller.buildServiceVoidRequest(updateSaleRequest,
					retrieveSaleResponse.getResponse());

			VDPResponse<ReverseVoidResponseVDP> response = null;
			response = saleServiceVDP.updateSale(voidSaleRequestVDP, saleId);

			updateSaleResponse = vdpDataMarshaller.buildUpdateSaleResponse(response,
					retrieveSaleResponse.getResponse());
		} else {
			throw new TransactionNotFoundException(saleId, "Sale");
		}
		return updateSaleResponse;

	}

	public Entity getSaleById(String saleId) throws GenericVDPException {
		CustomLogger.log("Get Auth by Id request received");
		String resourcePath = visaProperties
				.getProperty(com.visa.innovation.paymentservice.vdp.utils.Property.RESOURCE_PATH_SALE_RETRIEVE_BY_ID);
		resourcePath = resourcePath.replace("{sales-id}", saleId);
		VDPResponse<RetrieveSaleByIdResponseVDP> response = null;
		Entity entity = null;
		CustomLogger.log("Get Sale by Id request received");

		response = saleServiceVDP.getSaleById(saleId);
		if (response.getVdpError() == null) {
			SalesResponse salesResponse = vdpDataMarshaller.buildSalesResponse(response.getResponse());
			entity = convertToODataEntity(salesResponse);
		}
		return entity;
	}

	public EntityCollection saleEntityCollection(FilterMetaData filterMetaData) {

		List<SalesResponse> results = saleServiceVDP.getAllSales(filterMetaData);
		EntityCollection retEntitySet = new EntityCollection();
		if (results != null) {
			for (SalesResponse response : results) {
				if (response != null) {
					Entity retEntity = convertToODataEntity(response);
					retEntitySet.getEntities().add(retEntity);
				}
			}
		}
		return retEntitySet;
	}

	private Entity convertToODataEntity(SalesResponse saleResponse) {

		Entity entity = new Entity();

		entity.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.saleId,
				ValueType.PRIMITIVE, saleResponse.getSaleId()))
				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.amount,
						ValueType.PRIMITIVE, saleResponse.getAmount()))
				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.currency,
						ValueType.PRIMITIVE, saleResponse.getCurrency()))
				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.payment,
						ValueType.PRIMITIVE, saleResponse.getPayment()))
				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.created,
						ValueType.PRIMITIVE, saleResponse.getCreated()))
				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.status,
						ValueType.PRIMITIVE, saleResponse.getStatus()))
				.addProperty(new org.apache.olingo.commons.api.data.Property(null, EdmConstants.orderId,
						ValueType.PRIMITIVE, saleResponse.getOrderId()));

		entity.setId(
				com.visa.innovation.paymentservice.odata.service.ODataUtil.createId("sales", saleResponse.getSaleId()));
		return entity;
	}
}
