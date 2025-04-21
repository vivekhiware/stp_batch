package com.stp.controller;

import static com.stp.utility.GenericCLass.STATUS_FAILED;
import static com.stp.utility.GenericCLass.STATUS_SUCCESS;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stp.service.NativeServ;
import com.stp.service.impl.EntryFetchService;
import com.stp.utility.ResponseBean;
import com.stp.utility.StpProcessRequest;
import com.stp.utility.TTumRequest;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RequestMapping(value = "/api_ttum")
public class NativeQueryController {

	private static final Logger logger = LoggerFactory.getLogger(NativeQueryController.class);
	private static String detail = "Detail";
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
		bean.setStatus(STATUS_FAILED);
		bean.setMessage("Error processing " + methodName + ": " + e.getMessage());
		return bean;
	}

	@Async
	@PostMapping(value = "fetchQuery")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "queryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "queryFallback")
	public CompletableFuture<ResponseBean> fetchQuery() {
		try {
			List<?> queryResults = nativeServ.fetchTTumQuery();
			ResponseBean response = entryFetchService.fetchEntries(queryResults, "Query");
			return CompletableFuture.completedFuture(response);
		} catch (Exception e) {
			return CompletableFuture.completedFuture(createErrorResponse("fetchQuery", e));
		}
	}

	@Async
	@PostMapping(value = "fetchCount")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "queryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "queryFallback")
	public CompletableFuture<ResponseBean> fetchDetailCountByDate(@RequestBody String json) {
		logger.debug("Query result: {}", json);

		try {
			TTumRequest accObject = objectMapper.readValue(json, TTumRequest.class);
			List<?> queryResults = nativeServ.executeNativeQueryCount(accObject);
			ResponseBean response = entryFetchService.fetchEntries(queryResults, detail);
			return CompletableFuture.completedFuture(response);
		} catch (Exception e) {
			return CompletableFuture.completedFuture(createErrorResponse(detail, e));
		}
	}

	@Async
	@PostMapping(value = "fetchDetail")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "queryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "queryFallback")
	public CompletableFuture<ResponseBean> fetchDetail(@RequestBody String json) {
		try {
			TTumRequest accObject = objectMapper.readValue(json, TTumRequest.class);
			List<?> queryResults = nativeServ.executeNativeQuery(accObject);
			ResponseBean response = entryFetchService.fetchEntries(queryResults, detail);
			return CompletableFuture.completedFuture(response);
		} catch (Exception e) {
			return CompletableFuture.completedFuture(createErrorResponse(detail, e));
		}
	}

	// report query
	@Async
	@PostMapping(value = "fetchReportQuery")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "queryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "queryFallback")
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

	@Async
	@PostMapping(value = "fetchDetailReport")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "queryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "queryFallback")
	public CompletableFuture<ResponseBean> fetchReportDetail(@RequestBody String json, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			TTumRequest accObject = objectMapper.readValue(json, TTumRequest.class);
			List<Object[]> stpReportquerydatatable = nativeServ.stpReportQueryDatatable(request, response, accObject);
			logger.info("fetchDetailReport: {}", accObject);
			ResponseBean resp = entryFetchService.fetchEntries(stpReportquerydatatable, STATUS_SUCCESS);
			logger.info("Successfully fetched query results.");
			return CompletableFuture.completedFuture(resp);
		} catch (Exception e) {
			return CompletableFuture.completedFuture(createErrorResponse(STATUS_FAILED, e));
		}
	}

	@PostMapping(value = "fetchDetailReportExcel")
	public ResponseEntity<byte[]> fetchDetailReportExcel(@RequestBody String json, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		TTumRequest accObject = null;
		try {
			accObject = objectMapper.readValue(json, TTumRequest.class);
		} catch (JsonProcessingException e) {
			logger.error("Error parsing request body", e);

		}
		byte[] excelFile = nativeServ.stpReportQuery(request, response, accObject);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=report.xlsx");
		return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
	}

	// Fallback methods
	public CompletableFuture<ResponseBean> queryFallback(Throwable t) {
		logger.error("Circuit breaker triggered for fetchQuery due to: {}", t.getMessage());
		ResponseBean fallbackResponse = new ResponseBean();
		fallbackResponse.setStatus(STATUS_FAILED);
		fallbackResponse.setMessage("Circuit breaker triggered for fetchQuery. Please try again later.");
		return CompletableFuture.completedFuture(fallbackResponse);
	}

	public CompletableFuture<ResponseBean> queryRateLimiterFallback(Throwable t) {
		logger.error("Rate limit exceeded for fetchQuery due to: {}", t.getMessage());
		ResponseBean fallbackResponse = new ResponseBean();
		fallbackResponse.setStatus(STATUS_FAILED);
		fallbackResponse.setMessage("Rate limit exceeded. Please try again later.");
		return CompletableFuture.completedFuture(fallbackResponse);
	}

	@Async
	@PostMapping(value = "fetchCountBatch")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "queryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "queryFallback")
	public CompletableFuture<ResponseBean> fetchDetailBatchCountByDate(@RequestBody String json) {
		logger.info("Received JSON: {}", json);
		try {
			TTumRequest accObject = objectMapper.readValue(json, TTumRequest.class);
			List<?> queryResults = nativeServ.executeNativeQueryCountBatch(accObject);
			ResponseBean response = entryFetchService.fetchEntries(queryResults, detail);
			logger.info("Successfully fetched detail for request: {}", accObject);
			return CompletableFuture.completedFuture(response);
		} catch (Exception e) {
			return CompletableFuture.completedFuture(createErrorResponse(detail, e));
		}
	}

//	fetchDetail
	@Async
	@PostMapping(value = "fetchDetailBulk")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "queryRateLimiterFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "queryFallback")
	public CompletableFuture<ResponseBean> fetchDetailBulk(@RequestBody String json) {
		logger.info("Received JSON: {}", json);
		try {
			TTumRequest accObject = objectMapper.readValue(json, TTumRequest.class);
			List<?> queryResults = nativeServ.executeNativeQueryBatch(accObject);
			ResponseBean response = entryFetchService.fetchEntries(queryResults, detail);
			logger.info("Successfully fetched detail for request: {}", accObject);
			return CompletableFuture.completedFuture(response);
		} catch (Exception e) {
			return CompletableFuture.completedFuture(createErrorResponse(detail, e));
		}
	}

	@Async("asyncExecutor")
	@PostMapping("/{role}Insert")
	public CompletableFuture<ResponseEntity<ResponseBean>> updateProcess(@RequestParam(required = false) String json,
			@RequestParam(required = false) String type, @RequestParam(required = false) String level,
			@RequestParam(required = false) String process, @PathVariable String role) {
		logger.info("type: {}", type);
		logger.info("level: {}", level);
		if (json == null || json.trim().isEmpty()) {
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseBean(STATUS_FAILED, "No JSON data provided.", null)));
		}

		try {
			List<StpProcessRequest> stpProcessRequest = parseJsonToList(json, StpProcessRequest.class);
			int processLevelStp = nativeServ.processLevelStp(stpProcessRequest, type, level, process);
			if (processLevelStp > 0) {
				return CompletableFuture.completedFuture(
						ResponseEntity.ok(new ResponseBean(STATUS_SUCCESS, "  Records Process Successfully.", null)));
			} else {
				return CompletableFuture.completedFuture(
						ResponseEntity.ok(new ResponseBean(STATUS_FAILED, " Failed Records Process.", null)));
			}
		} catch (Exception e) {
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseBean("FAILED", "Failed to process records.", null)));

		}
	}

	private <T> List<T> parseJsonToList(String json, Class<T> clazz) throws JsonProcessingException {
		return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
	}
}
