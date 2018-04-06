package com.visa.innovation.paymentservice.test.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.visa.innovation.paymentservice.model.auth.AuthRequest;
import com.visa.innovation.paymentservice.model.auth.AuthResponse;
import com.visa.innovation.paymentservice.test.configuration.TestConfig;
import com.visa.innovation.paymentservice.test.util.TestDataUtils;
import com.visa.innovation.paymentservice.util.CustomLogger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
public class AuthControllerTest {

	MockMvc mockMvc;

	@Autowired
	WebApplicationContext webApplicationContext;
	@Autowired
	TestDataUtils testDataUtils;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void test_create_auth_soa_success() throws Exception {

		CustomLogger.log("test_create_auth_soa_success");
		AuthRequest authRequest = testDataUtils.getMockAuthRequest();
		ObjectMapper mapper = new ObjectMapper();
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/authorizations")
				.contentType(MediaType.APPLICATION_JSON).header("on-behalf-of-user", testDataUtils.USER_ID)
				.header("x-api-key", testDataUtils.API_KEY).header("x-vendor-key", testDataUtils.X_VENDOR_KEY)
				.param("return_pan", "true").content(mapper.writeValueAsString(authRequest)));
		result.andExpect(MockMvcResultMatchers.status().isCreated());
		result.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));

		AuthResponse authResponse = mapper.readValue(result.andReturn().getResponse().getContentAsByteArray(),
				AuthResponse.class);
		CustomLogger.log(authResponse);
		Assert.assertNotNull(authResponse.getAuthorizationId());
	}
}
