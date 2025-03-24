package com.stp.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stp.service.NativeServ;
import com.stp.utility.ResponseBean;
import com.stp.utility.TTumRequest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

//
@RestController
@RequestMapping(value = "/api_ttumbk")
public class NativeQueryControllerBk {

	private static final Logger logger = LoggerFactory.getLogger(NativeQueryControllerBk.class);

	private final NativeServ nativeServ;
	private final ObjectMapper objectMapper;

	@Autowired
	public NativeQueryControllerBk(NativeServ nativeServ, ObjectMapper objectMapper) {
		this.nativeServ = nativeServ;
		this.objectMapper = objectMapper;
	}

	/*
	 * @Async
	 * 
	 * @PostMapping(value = "fetchQuery")
	 * 
	 * @RateLimiter(name = "QueryRateLimiter", fallbackMethod =
	 * "QueryRateLimiterFallback")
	 * 
	 * @CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod =
	 * "QueryFallback") public CompletableFuture<ResponseBean> fetchAccess() {
	 * ResponseBean bean = new ResponseBean(); try { List<STP_TTUMQuery>
	 * fetchTTumQuery = nativeServ.fecthTTumQuery(); if (!fetchTTumQuery.isEmpty())
	 * { bean.setData(fetchTTumQuery); bean.setStatus("SUCCESS");
	 * bean.setMessage("Query entries found."); } else { bean.setStatus("FAILED");
	 * bean.setMessage("No Query entries exist."); } } catch (Exception e) {
	 * logger.error("Error fetching Query entries: ", e); bean.setStatus("FAILED");
	 * bean.setMessage("Error fetching Query entries: " + e.getMessage()); } return
	 * CompletableFuture.completedFuture(bean); }
	 * 
	 * @Async
	 * 
	 * @PostMapping(value = "fetchDetail")
	 * 
	 * @RateLimiter(name = "QueryRateLimiter", fallbackMethod =
	 * "QueryRateLimiterFallback")
	 * 
	 * @CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod =
	 * "QueryFallback") public CompletableFuture<ResponseBean>
	 * fetchAccess(@RequestParam(required = false) String json) { ResponseBean bean
	 * = new ResponseBean(); try { TTumRequest accObject =
	 * objectMapper.readValue(json, TTumRequest.class); List<Object[]>
	 * executeNativeQuery = nativeServ.executeNativeQuery(accObject); if
	 * (executeNativeQuery.size() > 0) { bean.setData(executeNativeQuery);
	 * bean.setStatus("SUCCESS"); bean.setMessage("TTUM Maker entries found."); }
	 * else { bean.setStatus("FAILED");
	 * bean.setMessage("No TTUM Maker entries exist."); } } catch (Exception e) {
	 * logger.error("Error fetching TTUM Maker entries: ", e);
	 * bean.setStatus("FAILED");
	 * bean.setMessage("Error fetching TTUM Maker entries: " + e.getMessage()); }
	 * return CompletableFuture.completedFuture(bean); }
	 */

	@Async
	@PostMapping(value = "fetchQuery")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "QueryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "QueryFallback")
	public CompletableFuture<ResponseBean> fetchQuery() {
		return processQuery(() -> nativeServ.fetchTTumQuery());
	}

	@Async
	@PostMapping(value = "fetchDetail")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "QueryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "QueryFallback")
	public CompletableFuture<ResponseBean> fetchDetail(@RequestParam(required = false) String json) {
		return processQuery(() -> {
			TTumRequest accObject = objectMapper.readValue(json, TTumRequest.class);
			return nativeServ.executeNativeQuery(accObject);
		});
	}

	// Fallback method for fetchQuery circuit breaker
	public CompletableFuture<ResponseBean> QueryFallback(Throwable t) {
		logger.error("Circuit breaker activated for fetchQuery: {}", t.getMessage());
		ResponseBean fallbackResponse = new ResponseBean();
		fallbackResponse.setStatus("FAILED");
		fallbackResponse.setMessage("Circuit breaker triggered for fetchQuery. Please try again later.");
		return CompletableFuture.completedFuture(fallbackResponse);
	}

	public CompletableFuture<ResponseBean> QueryRateLimiterFallback(Throwable t) {
		logger.error("Rate limit exceeded for fetchQuery: {}", t.getMessage());
		ResponseBean fallbackResponse = new ResponseBean();
		fallbackResponse.setStatus("FAILED");
		fallbackResponse.setMessage("Rate limit exceeded. Please try again later.");
		return CompletableFuture.completedFuture(fallbackResponse);
	}

	// Common method to handle both query and detail fetching
	private CompletableFuture<ResponseBean> processQuery(QueryProcessor processor) {
		ResponseBean bean = new ResponseBean();
		try {
			List<?> results = processor.processQuery();
			if (!results.isEmpty()) {
				bean.setData(results);
				bean.setStatus("SUCCESS");
				bean.setMessage("Query entries found.");
			} else {
				bean.setStatus("FAILED");
				bean.setMessage("No entries found.");
			}
		} catch (Exception e) {
			logger.error("Error processing query: ", e);
			bean.setStatus("FAILED");
			bean.setMessage("Error processing query: " + e.getMessage());
		}
		return CompletableFuture.completedFuture(bean);
	}

	// Functional interface for processing queries
	@FunctionalInterface
	interface QueryProcessor {
		List<?> processQuery() throws Exception;
	}
}
