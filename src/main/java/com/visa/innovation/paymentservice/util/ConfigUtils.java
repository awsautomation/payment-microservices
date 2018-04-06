package com.visa.innovation.paymentservice.util;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.vdp.utils.Property;

@Component
public class ConfigUtils {

	HashMap<String, String> configParams;
	@Value("${isUseRsaEncryption}")
	private Boolean isUseRsaEncryption;
	@Value("${merchant_id}")
	private String merchantID;
	@Value("${key_file_name}")
	private String keyFilename;
	@Value("${target_api_version}")
	private String targetAPIVersion;
	@Value("${api_key}")
	private String apiKey;
	@Value("${shared_secret}")
	private String sharedSecret;

	public ConfigUtils() {
	}

	public ConfigUtils(String env) {
		configParams = new HashMap<String, String>();
	}

	@PostConstruct
	private void init() {
		fetchAllParamFromLocal();
	}

	private void fetchAllParamFromLocal() {
		System.out.println("Loading config from local properties file");
		configParams.put(Constants.MERCHANT_ID_KEY, merchantID);
		configParams.put(Constants.KEY_FILE_NAME_KEY, keyFilename);
		configParams.put(Constants.TARGET_API_VERSION_KEY, targetAPIVersion);
		configParams.put(Property.API_KEY.getValue(), apiKey);
		configParams.put(Property.SHARED_SECRET.getValue(), sharedSecret);
		configParams.put(Constants.IS_USE_RSA_ENCRYPTION_KEY, isUseRsaEncryption.toString());
		System.out.println(configParams);
	}

	public String getParameter(String key) {
		if (key == null) {
			return null;
		}
		return configParams.get(key);
	}

	public HashMap<String, String> getConfigParams() {
		return configParams;
	}

	public void setConfigParams(HashMap<String, String> configParams) {
		this.configParams = configParams;
	}

}
