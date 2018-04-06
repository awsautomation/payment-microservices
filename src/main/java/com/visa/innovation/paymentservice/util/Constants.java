package com.visa.innovation.paymentservice.util;

public class Constants {

	// SOA Constants
	public static final String AUTH = "ccAuthService_run";
	public static final String CAPTURE = "ccCaptureService_run";
	public static final String TOKEN_CREATE = "paySubscriptionCreateService_run";
	public static final String CURRENCY = "USD";
	public static final String STATUS_CAPTURED = "captured";
	public static final String DECISION_ACCEPT = "ACCEPT";
	public static final String DECISION_REJECT = "REJECT";
	public static final String DECISION_ERROR = "ERROR";
	public static final String SOA_XMLNS = "urn:schemas-cybersource-com:transaction-data-1.90";

	// VDP Constants
	public static final String STATUS_PENDING_CAPTURE_VDP = "PendingCapture";
	public static final String STATUS_PENDING_SETTLEMENT_VDP = "PendingSettlement";
	public static final String STATUS_VOIDED_VDP = "Voided";
	public static final String STATUS_AUTH_REVERSED = "AuthReversed";
	public static final String STATUS_SETTLED_SUCCESSFULLY = "SettledSuccessfully";
	public static final String STATUS_REFUNDED = "refunded";
	
	public static final String ENCRYPTION_TYPE_RSA = "RsaOaep256";
	public static final String ENCRYPTION_TYPE_NONE = "None";

	// Misc
	public static final String TYPE_CAPTURE = "capture";
	public static final String TYPE_SALE = "sale";
	public static final String DUMMY_PAN = "4111111111111111";

	// Validation and Error Strings
	public static final String INVALID_REFUND_REQUEST = "one of capture_id or sale_id is required";
	public static final String INVALID_AMOUNT = "invalid amount";
	public static final String BLANK_FIRST_NAME_ERROR = "first_name may not be blank";
	public static final String BLANK_LAST_NAME_ERROR = "last_name may not be blank";
	public static final String BLANK_USER_ID_ERROR = "user_id may not be blank";
	public static final String BLANK_ASYNC_URL_ERROR = "callback_url may not be blank";
	public static final String INVALID_ASYNC_URL_ERROR = "callback_url invalid. Must be of format http://host_url/cardstatuses/{id}";
	public static final String INAVLID_INPUT_PARAMETERS_ERROR = "One or more input parameters are invalid";
	public static final String INVALID_HEADERS_ERROR = "One or more headers are invalid/missing";
	public static final String INVALID_CARD_LENGTH_ERROR = "Card Length is invalid/missing";
	public static final String INVALID_ON_BEHALF_OF_USER = "header on-behalf-of-user is missing/invalid";
	public static final String INVALID_X_VENDOR_KEY = "header x-vendor-key is missing/invalid";
	public static final String INVALID_X_API_KEY = "header x-api-key is missing/invalid";
	public static final String MISSING_X_CARD_ID = "header x-card-id is required";
	public static final String BLANK_AMOUNT_ERROR = "amount may not be blank";
	public static final String BLANK_MID_ERROR = "mid may not be blank";
	public static final String BLANK_CARD_ID_ERROR = "card_id may not be blank";
	public static final String BLANK_CARD_NO_ERROR = "card number is required";
	public static final String MARQETA_UNREACHABLE_ERROR = "Connection to Marqeta server failed. Please try again.";

	public static final String HYBRID_ID_DELIMITER = "::";

	public static final String TYPE = "type";
	public static final String EXPIRY = "expiry";
	public static final String PAYMENT = "payment";
	public static final String ACCOUNTNUMBER = "account_number";
	public static final String NAME = "name";
	public static final String EXPIRYMONTH = "expiry_month";
	public static final String EXPIRYYEAR = "expiry_year";

	// For Service Factory
	public static final String SERVICE_AUTH = "auth";
	public static final String SERVICE_CAPTURE = "capture";
	public static final String SERVICE_SALES = "sales";
	public static final String SERVICE_REFUND = "refunds";
	
	//For Cache keys
	public static final String CACHE_KEY_PREFIX = "pymntservice";
	
	//Header Key Names
	public static final String API_KEY_HEADER_KEY = "x-api-key";
	public static final String VENDOR_KEY_HEADER_KEY = "x-vendor-key";
	public static final String USER_ID_HEADER_KEY = "on-behalf-of-user";
	
	public static final String API_KEY_KEY = "gateway_api_key";
	public static final String SHARED_SECRET_KEY = "gateway_shared_secret";
	public static final String BASE_URL_KEY = "base_url";
	public static final String MERCHANT_ID_KEY = "merchant_id";
	public static final String KEY_FILE_NAME_KEY = "key_file_name";
	public static final String TARGET_API_VERSION_KEY = "target_api_version";
	public static final String IS_USE_RSA_ENCRYPTION_KEY = "is_use_rsa_encryption";
	
	//CYBS SOA Key Names
	public static final String CYBS_MERCHANT_ID_KEY = "merchantID";
	public static final String CYBS_KEY_FILE_NAME_KEY = "keyFilename";
	public static final String CYBS_TARGET_API_VERISON_KEY = "targetAPIVersion";
	
	
}
