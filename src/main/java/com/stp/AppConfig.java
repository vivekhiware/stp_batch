package com.stp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AppConfig {

	@Bean(name = "asyncExecutor")
	@Primary
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
