package com.visa.innovation.paymentservice.vdp.marshallers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.model.ServiceError;
import com.visa.innovation.paymentservice.model.ServiceResponse;
import com.visa.innovation.paymentservice.model.TransactionType;
import com.visa.innovation.paymentservice.model.refund.OriginalTransaction;
import com.visa.innovation.paymentservice.model.refund.RefundRequest;
import com.visa.innovation.paymentservice.model.refund.RefundResponse;
import com.visa.innovation.paymentservice.soa.model.PaymentStatusEnum;
import com.visa.innovation.paymentservice.util.Constants;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.Utils;
import com.visa.innovation.paymentservice.vdp.model.BaseRetrieveByIdResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.ReverseVoidResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.VDPError;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.refund.CreateRefundRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.refund.CreateRefundResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.refund.RetrieveRefundByIdResponseVDP;

@Component
public class RefundMarshallerVDP {

	@Autowired
	Utils utils;

	public CreateRefundRequestVDP buildServiceRefundRequest(RefundRequest refundRequest, String userId, String apiKey,
			String cardId) {

		String hybridId = utils.generateHybridId(userId, apiKey, refundRequest.getOrderId(), cardId);
		return new CreateRefundRequestVDP(refundRequest.getAmount().toString(), refundRequest.getCurrency(), hybridId);
	}

	public ServiceResponse<RefundResponse> buildRefundResponse(RefundRequest refundRequest,
			VDPResponse<CreateRefundResponseVDP> refundResponseVDP,
			VDPResponse<BaseRetrieveByIdResponseVDP> retireveResponse, TransactionType transactionType) {

		ServiceResponse<RefundResponse> response = null;
		OriginalTransaction originalTransaction = null;

		// create refund failed
		if (refundResponseVDP.getVdpError() != null) {
			VDPError vdpError = refundResponseVDP.getVdpError();
			ServiceError serviceError = new ServiceError(vdpError.getResponseStatusVDP().getCode(),
					vdpError.getResponseStatusVDP().getStatus(), vdpError.getResponseStatusVDP().getMessage());
			response = new ServiceResponse<RefundResponse>(serviceError);
		} else { // create refund succeeded
			CreateRefundResponseVDP vdpResponse = refundResponseVDP.getResponse();
			String orderId = utils.getOrderId(vdpResponse.getReferenceId());
			if (retireveResponse.getVdpError() == null) {
				BaseRetrieveByIdResponseVDP retrievalResponse = retireveResponse.getResponse();
				originalTransaction = new OriginalTransaction(retrievalResponse.getAmount(),
						retrievalResponse.getCurrency(), retrievalResponse.getStatus());
				if (transactionType == TransactionType.SALE) {
					originalTransaction.setSaleId(refundRequest.getSaleId());
					originalTransaction.setType(Constants.TYPE_SALE);
				}
				if (transactionType == TransactionType.CAPTURE) {
					originalTransaction.setCaptureId(refundRequest.getCaptureId());
					originalTransaction.setType(Constants.TYPE_CAPTURE);
				}
			}

			RefundResponse refundResponse = new RefundResponse.Builder().orderId(orderId)
					.refundAmount(Double.parseDouble(vdpResponse.getAmount())).refundCurrency(vdpResponse.getCurrency())
					.originalTransaction(originalTransaction).status(PaymentStatusEnum.REFUNDED)
					.refundId(vdpResponse.getId()).build();
			response = new ServiceResponse<RefundResponse>(refundResponse);
		}
		CustomLogger.log("Response:" + response);
		return response;
	}

	public ServiceResponse<RefundResponse> buildUpdateRefundResponse(
			VDPResponse<ReverseVoidResponseVDP> reverseVoidResponseVDP,
			VDPResponse<RetrieveRefundByIdResponseVDP> retrieveRefundResponse) {

		ServiceResponse<RefundResponse> response = null;
		if (reverseVoidResponseVDP.getVdpError() != null) {
			VDPError vdpError = reverseVoidResponseVDP.getVdpError();
			response = new ServiceResponse.Builder<RefundResponse>().serviceError(new ServiceError.Builder()
					.code(vdpError.getResponseStatusVDP().getCode()).status(vdpError.getResponseStatusVDP().getStatus())
					.message(vdpError.getResponseStatusVDP().getMessage()).build()).build();
		} else {
			ReverseVoidResponseVDP vdpResponse = reverseVoidResponseVDP.getResponse();
			String orderId = utils.getOrderId(vdpResponse.getReferenceId());
			OriginalTransaction originalTransaction = null;
			if (retrieveRefundResponse.getVdpError() == null) {
				RetrieveRefundByIdResponseVDP refundByIdResponseVDP = retrieveRefundResponse.getResponse();
				originalTransaction = new OriginalTransaction(refundByIdResponseVDP.getAmount(),
						refundByIdResponseVDP.getCurrency(), refundByIdResponseVDP.getStatus());
				originalTransaction.setRefundId(refundByIdResponseVDP.getId());

			}
			RefundResponse refundResponse = new RefundResponse.Builder().orderId(orderId)
					.refundAmount(Double.parseDouble(vdpResponse.getAmount())).created(vdpResponse.getCreatedAt())
					.refundCurrency(vdpResponse.getCurrency()).refundId(vdpResponse.getId())
					.status(utils.getServiceStatus(vdpResponse.getStatus())).originalTransaction(originalTransaction)
					.build();
			response = new ServiceResponse.Builder<RefundResponse>().response(refundResponse).build();
		}

		CustomLogger.log(response);
		return response;

	}
}