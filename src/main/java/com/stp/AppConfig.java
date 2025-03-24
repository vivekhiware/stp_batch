package com.stp;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfig {

	@Bean
	@Qualifier("asyncExecutor") // Qualify the bean name to match the one injected in UPIController
	public TaskExecutor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(50); // Minimum number of threads
		executor.setMaxPoolSize(200); // Maximum number of threads
		executor.setQueueCapacity(500); // Queue size before starting new threads
		executor.setThreadNamePrefix("AsyncThread-");
		executor.setKeepAliveSeconds(60);
		executor.initialize();
		return executor;
	}
}
