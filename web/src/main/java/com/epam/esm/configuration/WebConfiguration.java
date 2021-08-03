package com.epam.esm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class contains spring web configuration
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 * @see WebMvcConfigurer
 */
@SpringBootConfiguration
@EnableTransactionManagement
@EntityScan(basePackages = "com.epam.esm")
public class WebConfiguration implements WebMvcConfigurer {
	@Value("UTF-8")
	private String encoding;
	@Value("localization/exception")
	private String fileName;

	/**
	 * Create bean MessageSource which will be used to get info from properties
	 * files
	 *
	 * @return the message source
	 */
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename(fileName);
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setDefaultEncoding(encoding);
		return messageSource;
	}
}
