package com.visa.innovation.paymentservice.vdp.utils;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.util.ConfigUtils;

/**
 * Source: https://github.com/visa/SampleCode
 */
@Component
public class VisaProperties {

	@Autowired
	ConfigUtils configUtils;
	Properties properties;

	public VisaProperties() {
	}

	@PostConstruct
	private void init() {
		properties = new Properties();
		try {
			properties.load(VisaProperties.class.getResourceAsStream("/endpoints.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Entry<String, String> entry : configUtils.getConfigParams().entrySet()) {
			properties.setProperty(entry.getKey(), entry.getValue());
		}
	}

	public String getProperty(Property property) {
		return (String) properties.get(property.getValue());
	}
	
}
