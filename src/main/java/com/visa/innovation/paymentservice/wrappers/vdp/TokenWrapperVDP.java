package com.visa.innovation.paymentservice.wrappers.vdp;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.exception.ForbiddenVDPException;
import com.visa.innovation.paymentservice.exception.GenericException;
import com.visa.innovation.paymentservice.util.Constants;
import com.visa.innovation.paymentservice.util.CustomLogger;
import com.visa.innovation.paymentservice.util.TokenizationUtils;
import com.visa.innovation.paymentservice.vdp.model.VDPResponse;
import com.visa.innovation.paymentservice.vdp.model.tokenization.KeyRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.tokenization.KeyResponseVDP;
import com.visa.innovation.paymentservice.vdp.model.tokenization.TokenRequestVDP;
import com.visa.innovation.paymentservice.vdp.model.tokenization.TokenResponseVDP;
import com.visa.innovation.paymentservice.vdp.utils.MethodTypes;
import com.visa.innovation.paymentservice.vdp.utils.Property;
import com.visa.innovation.paymentservice.vdp.utils.VisaAPIClient;
import com.visa.innovation.paymentservice.vdp.utils.VisaProperties;

@Component
public class TokenWrapperVDP {

	@Autowired
	VisaAPIClient visaAPIClient;
	@Autowired
	TokenizationUtils panEncryptor;
	@Autowired
	VisaProperties visaProperties;

	String apiKeyVDP;
	String baseUri;
	String queryString;

	public TokenWrapperVDP() {
	}

	@PostConstruct
	private void init() {
		this.apiKeyVDP = visaProperties.getProperty(Property.API_KEY);
		this.baseUri = visaProperties.getProperty(Property.BASE_URI);
		this.queryString = "apikey=" + apiKeyVDP;
	}

	private static boolean validate(final String pan) {
		int sum = 0;
		boolean alternate = false;
		for (int i = pan.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(pan.substring(i, i + 1));
			if (alternate) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			alternate = !alternate;
		}
		// System.out.println("sum " + sum);

		return (sum % 10 == 0);

	}

	public VDPResponse<TokenResponseVDP> generateToken(TokenRequestVDP tokenRequestVDP)
			throws ForbiddenVDPException, GenericException {

		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_TOKENS);
		VDPResponse<TokenResponseVDP> tokenResponse = null;
		String pan = tokenRequestVDP.getCardDetailsVDP().getCardNumber();

		if (pan.length() != 16) {
			throw new GenericException("Card Length is wrong");
		}

		// code change here (adding luhn check) //
		String new_pan = "4" + pan.substring(1);

		if (validate(new_pan)) {

			tokenRequestVDP.getCardDetailsVDP().setCardNumber(new_pan);
		} else {
			// System.out.println("PAN"+Constants.DUMMY_PAN);
			tokenRequestVDP.getCardDetailsVDP().setCardNumber(Constants.DUMMY_PAN);
		}

		// System.out.println("tokenRequestVDP
		// "+tokenRequestVDP.getCardDetailsVDP().getCardNumber());

		tokenResponse = visaAPIClient.doTokenRequest(baseUri, resourcePath, tokenRequestVDP, MethodTypes.POST,
				TokenResponseVDP.class);

		if (tokenResponse.getVdpError() == null) {
			tokenResponse.getResponse().setMaskedPan("xxxxxxxxxxxx" + pan.substring(12, 16));
		}
		CustomLogger.log(tokenResponse);

		return tokenResponse;
	}

	public VDPResponse<KeyResponseVDP> getKey() {

		String resourcePath = visaProperties.getProperty(Property.RESOURCE_PATH_KEYS);
		String isUseRsaEncryption = visaProperties.getProperty(Property.IS_USE_RSA_ENCRYPTION);
		KeyRequestVDP keyRequestVDP = null;

		if (isUseRsaEncryption.equalsIgnoreCase("true")) {
			keyRequestVDP = new KeyRequestVDP(Constants.ENCRYPTION_TYPE_RSA);
		} else {
			keyRequestVDP = new KeyRequestVDP(Constants.ENCRYPTION_TYPE_NONE);
		}

		VDPResponse<KeyResponseVDP> response = null;
		try {
			response = visaAPIClient.doXPayTokenRequest(baseUri, resourcePath, queryString, keyRequestVDP,
					MethodTypes.POST, new HashMap<String, String>(), KeyResponseVDP.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;

	}

}
