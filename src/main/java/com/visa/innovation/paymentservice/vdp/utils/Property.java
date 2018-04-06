package com.visa.innovation.paymentservice.vdp.utils;

/**
 * Source: https://github.com/visa/SampleCode
 */
public enum Property {

    SHARED_SECRET("vdp_shared_secret"),
    KEYSTORE_PATH("keyStorePath"),
    IS_USE_RSA_ENCRYPTION("is_use_rsa_encryption"),
    API_KEY("vdp_api_key"),
    END_POINT("visaUrl"),
    END_POINT_TOKENS("visaTokenUrl"),
    BASE_URI("baseURI"),
	RESOURCE_PATH_AUTH("resourcePathAuth"),
	RESOURCE_PATH_CAPTURE("resourcePathCapture"),
	RESOURCE_PATH_CAPTURES("resourcePathCaptures"),
	RESOURCE_PATH_SALES("resourcePathSales"),
	RESOURCE_PATH_KEYS("resourcePathKeys"),
	RESOURCE_PATH_TOKENS("resourcePathTokens"),
	RESOURCE_PATH_AUTH_RETRIEVE_BY_ID("resourcePathAuthGetById"),
	RESOURCE_PATH_AUTH_VOID("resourcePathReverseAuth"),
	RESOURCE_PATH_CAPTURE_VOID("resourcePathVoidCapture"),
	RESOURCE_PATH_SALE_VOID("resourcePathVoidSale"),
	RESOURCE_PATH_REFUND_VOID("resourcePathVoidRefund"),
	RESOURCE_PATH_CAPTURE_REFUND("resourcePathRefundCapture"),
	RESOURCE_PATH_SALE_REFUND("resourcePathRefundSale"),
	RESOURCE_PATH_CAPTURE_RETRIEVE_BY_ID("resourcePathCaptureGetById"),
	RESOURCE_PATH_SALE_RETRIEVE_BY_ID("resourcePathSalesGetById"),
	RESOURCE_PATH_REFUND_RETRIEVE_BY_ID("resourcePathRefundGetById");

    String value;

    private Property(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
