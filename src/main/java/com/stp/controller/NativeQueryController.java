package com.stp.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stp.service.NativeServ;
import com.stp.service.impl.EntryFetchService;
//import com.stp.utility.ExcelService;
import com.stp.utility.ResponseBean;
import com.stp.utility.TTumRequest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RequestMapping(value = "/api_ttum")
public class NativeQueryController {

	private static final Logger logger = LoggerFactory.getLogger(NativeQueryController.class);

	private final NativeServ nativeServ;
	private final ObjectMapper objectMapper;
	private final EntryFetchService entryFetchService;

	@Autowired
	public NativeQueryController(NativeServ nativeServ, ObjectMapper objectMapper,
			EntryFetchService entryFetchService) {
		this.nativeServ = nativeServ;
		this.objectMapper = objectMapper;
		this.entryFetchService = entryFetchService;
	}

	// Helper method to create a common error response
	private ResponseBean createErrorResponse(String methodName, Exception e) {
		logger.error("Error processing {} request: ", methodName, e);
		ResponseBean bean = new ResponseBean();
		bean.setStatus("FAILED");
		bean.setMessage("Error processing " + methodName + ": " + e.getMessage());
		return bean;
	}

	@Async
	@PostMapping(value = "fetchQuery")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "QueryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "QueryFallback")
	public CompletableFuture<ResponseBean> fetchQuery() {
		try {
			List<?> queryResults = nativeServ.fetchTTumQuery();
			ResponseBean response = entryFetchService.fetchEntries(queryResults, "Query");
			logger.info("Successfully fetched query results.");
			return CompletableFuture.completedFuture(response);
		} catch (Exception e) {
			return CompletableFuture.completedFuture(createErrorResponse("fetchQuery", e));
		}
	}

	@Async
	@PostMapping(value = "fetchCount")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "QueryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "QueryFallback")
	public CompletableFuture<ResponseBean> fetchDetailCountByDate(@RequestBody String json) {
		logger.info("Received JSON: " + json); // Log the received JSON
		try {
			TTumRequest accObject = objectMapper.readValue(json, TTumRequest.class);
			List<?> queryResults = nativeServ.executeNativeQueryCount(accObject);
			ResponseBean response = entryFetchService.fetchEntries(queryResults, "Detail");
			logger.info("Successfully fetched detail for request: {}", accObject);
			return CompletableFuture.completedFuture(response);
		} catch (Exception e) {
			return CompletableFuture.completedFuture(createErrorResponse("fetchDetail", e));
		}
	}

	@Async
	@PostMapping(value = "fetchDetail")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "QueryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "QueryFallback")
	public CompletableFuture<ResponseBean> fetchDetail(@RequestBody String json) {
		logger.info("Received JSON: " + json); // Log the received JSON
		try {
			TTumRequest accObject = objectMapper.readValue(json, TTumRequest.class);
			List<?> queryResults = nativeServ.executeNativeQuery(accObject);
			ResponseBean response = entryFetchService.fetchEntries(queryResults, "Detail");
			logger.info("Successfully fetched detail for request: {}", accObject);
			return CompletableFuture.completedFuture(response);
		} catch (Exception e) {
			return CompletableFuture.completedFuture(createErrorResponse("fetchDetail", e));
		}
	}

	// report query
	@Async
	@PostMapping(value = "fetchReportQuery")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "QueryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "QueryFallback")
	public CompletableFuture<ResponseBean> fetchReportQuery() {
		try {
			List<?> queryResults = nativeServ.fetchReport();
			ResponseBean response = entryFetchService.fetchEntries(queryResults, "Query");
			logger.info("Successfully fetched query results.");
			return CompletableFuture.completedFuture(response);
		} catch (Exception e) {
			return CompletableFuture.completedFuture(createErrorResponse("fetchQuery", e));
		}
	}

	@PostMapping(value = "fetchDetailReport")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "QueryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "QueryFallback")
	public CompletableFuture<ResponseBean> fetchReportDetail(@RequestBody String json, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			TTumRequest accObject = objectMapper.readValue(json, TTumRequest.class);
			List<Object[]> stp_REPORTQUERYDatatable = nativeServ.STP_REPORTQUERYDatatable(request, response, accObject);
			logger.error("fetchDetailReport: {}", accObject);
			ResponseBean resp = entryFetchService.fetchEntries(stp_REPORTQUERYDatatable, "SUCCESS");
			logger.info("Successfully fetched query results.");
			return CompletableFuture.completedFuture(resp);
		} catch (Exception e) {
			return CompletableFuture.completedFuture(createErrorResponse("SUCCESS", e));
		}
	}

	@PostMapping(value = "fetchDetailReportExcel")
	public ResponseEntity<byte[]> fetchDetailReportExcel(@RequestBody String json, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		TTumRequest accObject = null;
		try {
			accObject = objectMapper.readValue(json, TTumRequest.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		byte[] excelFile = nativeServ.STP_REPORTQUERY(request, response, accObject);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=report.xlsx");
		return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
	}

//	@Autowired
//	private ExcelService excelService;
//
//	@PostMapping("download")
//	public ResponseEntity<byte[]> downloadExcel() throws IOException {
//		// Sample data to populate the Excel file
//		List<String[]> data = Arrays.asList(new String[] { "Data1", "Data2", "Data3" },
//				new String[] { "Data4", "Data5", "Data6" }, new String[] { "Data7", "Data8", "Data9" });
//
//		byte[] excelFile = excelService.generateExcel(data);
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Disposition", "attachment; filename=report.xlsx");
//
//		return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
//	}

	// Fallback methods
	public CompletableFuture<ResponseBean> QueryFallback(Throwable t) {
		logger.error("Circuit breaker triggered for fetchQuery due to: {}", t.getMessage());
		ResponseBean fallbackResponse = new ResponseBean();
		fallbackResponse.setStatus("FAILED");
		fallbackResponse.setMessage("Circuit breaker triggered for fetchQuery. Please try again later.");
		return CompletableFuture.completedFuture(fallbackResponse);
	}

	public CompletableFuture<ResponseBean> QueryRateLimiterFallback(Throwable t) {
		logger.error("Rate limit exceeded for fetchQuery due to: {}", t.getMessage());
		ResponseBean fallbackResponse = new ResponseBean();
		fallbackResponse.setStatus("FAILED");
		fallbackResponse.setMessage("Rate limit exceeded. Please try again later.");
		return CompletableFuture.completedFuture(fallbackResponse);
	}
}
