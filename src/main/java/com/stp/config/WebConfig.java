package com.stp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${cors.allowed.origin}")
	private String allowedOrigin;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // Allow all endpoints
				.allowedOrigins(allowedOrigin)
				.allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS") // Allow these methods
				.allowedHeaders("*") // Allow all headers
				.allowCredentials(true) // Allow credentials (cookies, authorization headers, etc.)
				.maxAge(3600); // Cache CORS preflight request for 1 hour
	}
}
