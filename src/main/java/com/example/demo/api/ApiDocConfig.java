package com.example.demo.api;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.google.common.base.Predicates;

import io.swagger.models.Swagger;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;

/**
 * Configuration class with annotation Primary, that makes that Spring boot recognizes as a high order configuration
 * class<br>
 * <br>
 * This class makes the configuration for the Swagger 2 API Documentation tool and its inner tools (UI/v2/docs) Allow to
 * generate an API documentation in UI style
 * 
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 * @see Configuration
 * @see Swagger
 * @see Swagger2DocumentationConfiguration
 */
@Configuration
@EnableSwagger2
@Primary
public class ApiDocConfig
{

	/**
	 * Generates the basic Docket that will capture all the API annotations provided by Swagger
	 * 
	 * <b>NOTE:</b> Currently, is ignoring all endpoints with the prefix /error and /actuator, because this are not
	 * endpoints properly for MyPW, but inner Spring boot endpoints
	 * 
	 * @return Docket object that configures the API Documentation tool
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(Predicates.not(PathSelectors.regex("/error.*")))
				.paths(Predicates.not(PathSelectors.regex("/info.*")))
				.paths(Predicates.not(PathSelectors.regex("/loggers.*")))
				.paths(Predicates.not(PathSelectors.regex("/features.*")))
				.paths(Predicates.not(PathSelectors.regex("/refresh.*")))
				.paths(Predicates.not(PathSelectors.regex("/trace.*")))
				.paths(Predicates.not(PathSelectors.regex("/resume.*")))
				.paths(Predicates.not(PathSelectors.regex("/heapdump.*")))
				.paths(Predicates.not(PathSelectors.regex("/archaius.*")))
				.paths(Predicates.not(PathSelectors.regex("/env.*")))
				.paths(Predicates.not(PathSelectors.regex("/restart.*")))
				.paths(Predicates.not(PathSelectors.regex("/health.*")))
				.paths(Predicates.not(PathSelectors.regex("/beans.*")))
				.paths(Predicates.not(PathSelectors.regex("/metrics.*")))
				.paths(Predicates.not(PathSelectors.regex("/mappings.*")))
				.paths(Predicates.not(PathSelectors.regex("/pause.*")))
				.paths(Predicates.not(PathSelectors.regex("/auditevents.*")))
				.paths(Predicates.not(PathSelectors.regex("/dump.*")))
				.paths(Predicates.not(PathSelectors.regex("/autoconfig.*")))
				.paths(Predicates.not(PathSelectors.regex("/configprops.*")))
				.paths(Predicates.not(PathSelectors.regex("/actuator.*")))
				.build().apiInfo(apiInfo());
	}

	/**
	 * This method allows Swagger 2 to put custom information for out API UI
	 * 
	 * @return the API Information object, with the contact data and name of the generated API
	 */
	private ApiInfo apiInfo() {
		return new ApiInfo(
			"Employee REST API Documentation - Web Services", 
			"REST API documentation for usage of Employee Webservices", 
			"v1.0", 
			"Terms of service", 
			new Contact("Rodrigo Moncada", "https://www.linkedin.com/in/rodrigo-moncada-418686a9/", 
			 		"rodrigo.moncada@live.com"), 
			"", "", Collections.emptyList());
	}
}