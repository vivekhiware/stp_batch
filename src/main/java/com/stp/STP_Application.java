package com.stp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@EnableScheduling
@EnableCaching
@EnableAsync
@EnableAspectJAutoProxy
@EnableAutoConfiguration
@OpenAPIDefinition(info = @Info(title = "STP-PROCESS-API", version = "v1", description = "Process For STP"))
@SpringBootApplication(scanBasePackages = "com.stp")
public class STP_Application extends SpringBootServletInitializer {
	private static final Logger logger = LoggerFactory.getLogger(STP_Application.class);

	@Value("${spring.profiles.active}")
	private String activeProfile;

	public static void main(String[] args) {
		SpringApplication.run(STP_Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(STP_Application.class);
	}

	public void run(String... args) throws Exception {
		logger.info("Active profile: " + activeProfile);
	}
}
