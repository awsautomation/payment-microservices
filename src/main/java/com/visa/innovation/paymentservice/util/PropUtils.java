package com.visa.innovation.paymentservice.util;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropUtils {

	private Properties properties;
	@Autowired
	ConfigUtils configUtils;

	public PropUtils() {
		properties = new Properties();
	}

	@PostConstruct
	private void init() {
		properties.setProperty(Constants.CYBS_MERCHANT_ID_KEY, configUtils.getParameter(Constants.MERCHANT_ID_KEY));
		properties.setProperty(Constants.CYBS_KEY_FILE_NAME_KEY, configUtils.getParameter(Constants.KEY_FILE_NAME_KEY));
		properties.setProperty(Constants.CYBS_TARGET_API_VERISON_KEY, configUtils.getParameter(Constants.TARGET_API_VERSION_KEY));
	}

	public Properties getProperties() {
		return this.properties;
	}

}
