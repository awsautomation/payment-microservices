package com.visa.innovation.paymentservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.visa.innovation.paymentservice.interceptor.AuthInterceptor;
import com.visa.innovation.paymentservice.odata.service.CollectionProcessor;
import com.visa.innovation.paymentservice.odata.service.EdmEntityProcessor;
import com.visa.innovation.paymentservice.service.AuthServiceVDP;
import com.visa.innovation.paymentservice.service.CaptureServiceVDP;
import com.visa.innovation.paymentservice.service.FilterConsumer;
import com.visa.innovation.paymentservice.service.GetResponsesByIdUtils;
import com.visa.innovation.paymentservice.service.SaleServiceVDP;
import com.visa.innovation.paymentservice.service.VDPResultsProducer;
import com.visa.innovation.paymentservice.util.ConfigUtils;
import com.visa.innovation.paymentservice.util.FilterResultsUtils;
import com.visa.innovation.paymentservice.util.PropUtils;
import com.visa.innovation.paymentservice.vdp.utils.VisaProperties;
import com.visa.innovation.paymentservice.vdp.utils.XPayTokenGenerator;
import com.visa.innovation.paymentservice.wrappers.vdp.AuthWrapperVDP;
import com.visa.innovation.paymentservice.wrappers.vdp.CaptureWrapperVDP;
import com.visa.innovation.paymentservice.wrappers.vdp.PaymentWrapperVDP;
import com.visa.innovation.paymentservice.wrappers.vdp.RefundWrapperVDP;
import com.visa.innovation.paymentservice.wrappers.vdp.SaleWrapperVDP;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.visa.innovation.paymentservice")
@PropertySource(value = { "classpath:application.properties" })
public class ProjectConfiguration extends WebMvcConfigurerAdapter {

	@Value("${env}")
	private String env;

	@Bean(name = "propertySourcesPlaceholderConfigurer")
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean(name = "configUtils")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@DependsOn(value = "propertySourcesPlaceholderConfigurer")
	public ConfigUtils configUtils() {
		return new ConfigUtils(env);
	}

	@Bean(name = "visaProperties")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@DependsOn(value = "configUtils")
	public VisaProperties visaProperties() {
		return new VisaProperties();
	}
	
	@Bean(name = "propUtils")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@DependsOn(value = "configUtils")
	public PropUtils propUtils() {
		return new PropUtils();
	}
	
	@Bean(name = "xPayTokenGenerator")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public XPayTokenGenerator xPayTokenGenerator() {
		return new XPayTokenGenerator();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Bean
	public AuthInterceptor authInterceptor() {
		return new AuthInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor());
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public GetResponsesByIdUtils getResponseByIdUtils() {
		return new GetResponsesByIdUtils();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public AuthServiceVDP authServiceVDP() {
		return new AuthServiceVDP();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CaptureServiceVDP captureServiceVDP() {
		return new CaptureServiceVDP();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public SaleServiceVDP saleServiceVDP() {
		return new SaleServiceVDP();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public FilterResultsUtils filterResultUtils() {
		return new FilterResultsUtils();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public AuthWrapperVDP authWrapperVDP() {
		return new AuthWrapperVDP();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CaptureWrapperVDP captureWrapperVDP() {
		return new CaptureWrapperVDP();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public SaleWrapperVDP saleWrapperVDP() {
		return new SaleWrapperVDP();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public RefundWrapperVDP refundWrapperVDP() {
		return new RefundWrapperVDP();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public PaymentWrapperVDP paymentWrapperVDP() {
		return new PaymentWrapperVDP();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public FilterConsumer filterConsumer() {
		return new FilterConsumer();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public VDPResultsProducer vdpProducer() {
		return new VDPResultsProducer();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public CollectionProcessor collectionProcessor() {
		return new CollectionProcessor();
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public EdmEntityProcessor entityProcessor() {
		return new EdmEntityProcessor();
	}

}
