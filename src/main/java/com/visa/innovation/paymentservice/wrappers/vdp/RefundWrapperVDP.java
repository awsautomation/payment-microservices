package com.visa.innovation.paymentservice.wrappers.vdp;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.exception.TransactionNotFoundException;
import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.TransactionType;
import com.visa.innovation.paymentservice.model.UpdatePaymentRequest;
import com.visa.innovation.paymentservice.model.refund.RefundRequest;
import com.visa.innovation.paymentservice.model.refund.RefundResponse;
import com.visa.innovation.paymentservice.service.CaptureServiceVDP;
import com.visa.innovation.paymentservice.service.GetResponsesByIdUtils;
import com.visa.innovation.paymentservice.service.RefundServiceVDP;
import com.visa.innovation.paymentservice.service.SaleServiceVDP;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.vdp.marshallers.VDPDataMarshaller;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.VoidRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.refund.CreateRefundRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.refund.CreateRefundResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.refund.RetrieveRefundByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.utils.Property;
import com.visa.innovation.paymentservice.vdp.utils.VisaAPIClient;
import com.visa.innovation.paymentservice.vdp.utils.VisaProperties;

@Component
public class RefundWrapperVDP {

	@Autowired
	VisaAPIClient visaAPIClient;
	@Autowired
	VDPDataMarshaller vdpDataMarshaller;
	@Autowired
	RefundServiceVDP refundServiceVDP;
	@Autowired
	SaleServiceVDP saleServiceVDP;
	@Autowired
	CaptureServiceVDP caputureServiceVDP;
	@Autowired
	GetResponsesByIdUtils getResponseByIdUtils;
	@Autowired
	VisaProperties visaProperties;

	String apiKeyVDP;
	String baseUri;
	String queryString;

	public RefundWrapperVDP() {
	}

	@PostConstruct
	private void init() {
		this.apiKeyVDP = visaProperties.getProperty(Property.API_KEY);
		this.baseUri = visaProperties.getProperty(Property.BASE_URI);
		this.queryString = "apikey=" + apiKeyVDP;
	}

	public ServiceResponse<RefundResponse> createRefund(RefundRequest refundRequest, String userId, String apiKey,
			String cardId) {

		CustomLogger.log("Refund request received");
		CustomLogger.log(refundRequest);
		CreateRefundRequestVDP refundRequestVDP = vdpDataMarshaller.buildServiceRefundRequest(refundRequest, userId,
				apiKey, cardId);
		VDPResponse<CreateRefundResponseVDP> response = null;
		response = refundServiceVDP.createRefund(refundRequest, refundRequestVDP);

		ServiceResponse<RefundResponse> serviceResponse = null;

		if (refundRequest.getSaleId() != null) {
			VDPResponse<BaseRetrieveByIdResponseVDP> saleResponse = getResponseByIdUtils
					.getVDPResponseSaleById(refundRequest.getSaleId());
			serviceResponse = vdpDataMarshaller.buildRefundResponse(refundRequest, response, saleResponse,
					TransactionType.SALE);
		} else {
			VDPResponse<BaseRetrieveByIdResponseVDP> caputureResponse = getResponseByIdUtils
					.getVDPCaptureResponseById(refundRequest.getCaptureId());
			serviceResponse = vdpDataMarshaller.buildRefundResponse(refundRequest, response, caputureResponse,
					TransactionType.CAPTURE);
		}
		return serviceResponse;
	}

	public ServiceResponse<RefundResponse> updateRefund(UpdatePaymentRequest updateRefundRequest, String refundId)
			throws TransactionNotFoundException {

		VDPResponse<RetrieveRefundByIdResponseVDP> retrieveRefundResponse = null;
		VoidRequestVDP voidRefundRequestVDP = null;
		ServiceResponse<RefundResponse> updateRefundResponse = null;

		retrieveRefundResponse = refundServiceVDP.getRefundById(refundId);
		if (retrieveRefundResponse.getVdpError() == null) {
			voidRefundRequestVDP = vdpDataMarshaller.buildServiceVoidRequest(updateRefundRequest,
					retrieveRefundResponse.getResponse());

			VDPResponse<ReverseVoidResponseVDP> response = null;
			response = refundServiceVDP.updateRefund(voidRefundRequestVDP, refundId);

			updateRefundResponse = vdpDataMarshaller.buildUpdateRefundResponse(response, retrieveRefundResponse);
		} else {
			throw new TransactionNotFoundException(refundId, "Refund");
		}
		return updateRefundResponse;
	}
}