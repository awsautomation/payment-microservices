package com.visa.innovation.paymentservice.test.util;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.model.Address;
import com.visa.innovation.paymentservice.model.Expiry;
import com.visa.innovation.paymentservice.model.PaymentRequest;
import com.visa.innovation.paymentservice.model.TokenizationScheme;
import com.visa.innovation.paymentservice.model.auth.AuthRequest;

@Component
public class TestDataUtils {
	
	public final String USER_ID = "test-user";
	public final String API_KEY = "test-api-key";
	public final String X_VENDOR_KEY = "aa:aa:aa";

	public String getRandomId() {
		return Integer.toString(ThreadLocalRandom.current().nextInt(1, 101));
	}

	public double getRandomDouble(double min, double max) {
		return Math.round(ThreadLocalRandom.current().nextDouble(min, max + 1));
	}

	public int getRandomInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}

	public AuthRequest getMockAuthRequest() {

		PaymentRequest payment = new PaymentRequest.Builder().token("4916037865676358504106")
				.tokenizationScheme(TokenizationScheme.CYBERSOURCE).expiry(new Expiry("12", "2020")).build();
		Address billingAddress = new Address.Builder().firstName("Test").lastName("Test").city("SF").state("CA")
				.country("USA").street1("Test Street").postalCode("12345").email("null@null.com").build();
		AuthRequest authRequest = new AuthRequest(getRandomId(), getRandomDouble(1, 20), "USD", null, billingAddress,
				"20", payment);

		return authRequest;
	}
}
