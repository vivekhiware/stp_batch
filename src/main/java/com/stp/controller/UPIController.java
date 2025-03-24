package com.stp.controller;

import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_ADJUSTMENT_REPORT;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_ADJUSTMENT_REPORTEnquiry;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_DRC_NPCI;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_DRC_NPCIEnquiry;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_MULTIREVERSAL;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_MULTIREVERSALEnquiry;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_NONCBS_IW;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_NONCBS_IWEnquiry;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_NONCBS_OW;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_NONCBS_OWEnquiry;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_RET_DATA;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_RET_DATAEnquiry;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_TCC_DATA;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_TCC_DATAEnquiry;
import static com.stp.autojob.IsoFormatterUpi.request1201STP_UPI_ADJUSTMENT_REPORTRepeat;
import static com.stp.autojob.IsoFormatterUpi.request1201STP_UPI_DRC_NPCIRepeat;
import static com.stp.autojob.IsoFormatterUpi.request1201STP_UPI_MULTIREVERSALRepeat;
import static com.stp.autojob.IsoFormatterUpi.request1201STP_UPI_NONCBS_IWRepeat;
import static com.stp.autojob.IsoFormatterUpi.request1201STP_UPI_NONCBS_OWRepeat;
import static com.stp.autojob.IsoFormatterUpi.request1201STP_UPI_RET_DATARepeat;
import static com.stp.autojob.IsoFormatterUpi.request1201STP_UPI_TCC_DATARepeat;

import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_NSTL_NETSET_TTUM;
import static com.stp.autojob.IsoFormatterUpi.request1200STP_UPI_NSTL_NETSET_TTUMEnquiry;
import static com.stp.autojob.IsoFormatterUpi.request1201STP_UPI_NSTL_NETSET_TTUMRepeat;

import static com.stp.autojob.IsoFormatterUpi.responseList1210;
import static com.stp.autojob.IsoFormatterUpi.responsePrint;
import static com.stp.utility.GenericCLass.HOST;
import static com.stp.utility.GenericCLass.PORT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
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
import com.iso.config.IsoV93Message;
import com.iso.config.IsoV93MessageRes;
import com.iso.config.main.ISOCommunicationExample;
import com.stp.job.imps.STP_IMPS_IW_NETWORK_DECLINEsheduledTask;
import com.stp.job.upi.STP_UPI_ADJUSTMENT_REPORTscheduledTask;
import com.stp.job.upi.STP_UPI_DRC_NPCIscheduledTask;
import com.stp.job.upi.STP_UPI_MULTIREVERSALscheduledTask;
import com.stp.job.upi.STP_UPI_NONCBS_IWscheduledTask;
import com.stp.job.upi.STP_UPI_NONCBS_OWscheduledTask;
import com.stp.job.upi.STP_UPI_NSTL_NETSET_TTUMsheduledTask;
import com.stp.job.upi.STP_UPI_RET_DATAscheduledTask;
import com.stp.job.upi.STP_UPI_TCC_DATAscheduledTask;
import com.stp.model.db1.STP_UPI;
import com.stp.model.db1.STP_UPI_ADJUSTMENT_REPORT;
import com.stp.model.db1.STP_UPI_DRC_NPCI;
import com.stp.model.db1.STP_UPI_MULTIREVERSAL;
import com.stp.model.db1.STP_UPI_NONCBS_IW;
import com.stp.model.db1.STP_UPI_NONCBS_OW;
import com.stp.model.db1.STP_UPI_NSTL_NETSET_TTUM;
import com.stp.model.db1.STP_UPI_RET_DATA;
import com.stp.model.db1.STP_UPI_TCC_DATA;
import com.stp.service.ServiceUpi;
import com.stp.service.impl.EntryFetchService;
import com.stp.utility.ResponseBean;
import com.stp.utility.TTumRequest;
import com.stp.utility.TypeEnum;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
@RequestMapping("/api_upi")
public class UPIController {

	private static final Logger logger = LoggerFactory.getLogger(UPIController.class);

	private final ServiceUpi serviceUpi;
	private final EntryFetchService entryFetchService;
	private final TaskExecutor taskExecutor;
	private final ObjectMapper objectMapper;

	@Autowired
	public UPIController(ServiceUpi serviceUpi, ObjectMapper objectMapper, EntryFetchService entryFetchService,
			@Qualifier("asyncExecutor") TaskExecutor taskExecutor) {
		this.serviceUpi = serviceUpi;
		this.objectMapper = objectMapper;
		this.entryFetchService = entryFetchService;
		this.taskExecutor = taskExecutor;
	}

	/**
	 * Fetch Maker View
	 */
	@Async("asyncExecutor")
	@PostMapping("/{role}View")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "handleFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "handleFallback")
	@TimeLimiter(name = "QueryTimer", fallbackMethod = "handleFallback")
	public CompletableFuture<ResponseBean> fetchMaker(@PathVariable String role, @RequestBody String json) {
		TTumRequest accObject = null;
		try {
			accObject = objectMapper.readValue(json, TTumRequest.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return fetchEntries(role, accObject);
	}

	/**
	 * Common method to fetch entries based on role
	 */
	private CompletableFuture<ResponseBean> fetchEntries(String role, TTumRequest accObject) {
		ResponseBean response = null;
		String type = accObject.getStatus();
		TypeEnum data = TypeEnum.valueOf(type);
		Integer queryid = accObject.getQueryid();
		if (queryid == 21) {
			List<STP_UPI_NSTL_NETSET_TTUM> details = serviceUpi.viewSTP_UPI_NSTL_NETSET_TTUM(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
			details = null;
		} else if (queryid == 22) {
			List<STP_UPI_ADJUSTMENT_REPORT> details = serviceUpi.viewSTP_UPI_ADJUSTMENT_REPORT(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
			details = null;
		} else if (queryid == 23) {
			List<STP_UPI_MULTIREVERSAL> details = serviceUpi.viewSTP_UPI_MULTIREVERSAL(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
			details = null;
		} else if (queryid == 24) {
			List<STP_UPI_TCC_DATA> details = serviceUpi.viewSTP_UPI_TCC_DATA(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
			details = null;
		} else if (queryid == 25) {
			List<STP_UPI_RET_DATA> details = serviceUpi.viewSTP_UPI_RET_DATA(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
			details = null;
		} else if (queryid == 26) {
			List<STP_UPI_DRC_NPCI> details = serviceUpi.viewSTP_UPI_DRC_NPCI(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
			details = null;
		} else if (queryid == 27) {
			List<STP_UPI_NONCBS_IW> details = serviceUpi.viewSTP_UPI_NONCBS_IW(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
			details = null;
		} else if (queryid == 28) {
			List<STP_UPI_NONCBS_OW> details = serviceUpi.viewSTP_UPI_NONCBS_OW(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
			details = null;
		}
		return CompletableFuture.completedFuture(response);
	}

	/**
	 * Save Records dynamically based on role
	 */

	@Async("asyncExecutor") // This indicates that the method will be executed asynchronously
	@PostMapping("/{role}Insert") // Handles POST requests for a dynamic URL path with role
	public CompletableFuture<ResponseEntity<ResponseBean>> saveRecord(@RequestParam(required = false) String json,
			@RequestParam(required = false) String type, @PathVariable String role) {

		logger.error("TYPE ::", type, null); // Logs the type for debugging purposes

		// If no JSON data is provided, return a BAD_REQUEST response
		if (json == null || json.trim().isEmpty()) {
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseBean("FAILED", "No JSON data provided.", null)));
		}
		try {
			// Based on the type, parse the JSON into corresponding objects
			if (type.equalsIgnoreCase("21")) {
				List<STP_UPI_NSTL_NETSET_TTUM> stpUpiList = parseJsonToList(json, STP_UPI_NSTL_NETSET_TTUM.class);
				List<STP_UPI_NSTL_NETSET_TTUM> savedRecords = serviceUpi.addSTP_UPI_NSTL_NETSET_TTUM(stpUpiList);
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("22")) {
				List<STP_UPI_ADJUSTMENT_REPORT> stpUpiList = parseJsonToList(json, STP_UPI_ADJUSTMENT_REPORT.class);
				List<STP_UPI_ADJUSTMENT_REPORT> savedRecords = serviceUpi.addSTP_UPI_ADJUSTMENT_REPORT(stpUpiList);
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("23")) {
				List<STP_UPI_MULTIREVERSAL> stpUpiList = parseJsonToList(json, STP_UPI_MULTIREVERSAL.class);
				List<STP_UPI_MULTIREVERSAL> savedRecords = serviceUpi.addSTP_UPI_MULTIREVERSAL(stpUpiList);
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("24")) {
				List<STP_UPI_TCC_DATA> stpUpiList = parseJsonToList(json, STP_UPI_TCC_DATA.class);
				List<STP_UPI_TCC_DATA> savedRecords = serviceUpi.addSTP_UPI_TCC_DATA(stpUpiList);
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("25")) {
				List<STP_UPI_RET_DATA> stpUpiList = parseJsonToList(json, STP_UPI_RET_DATA.class);
				List<STP_UPI_RET_DATA> savedRecords = serviceUpi.addSTP_UPI_RET_DATA(stpUpiList);
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("26")) {
				List<STP_UPI_DRC_NPCI> stpUpiList = parseJsonToList(json, STP_UPI_DRC_NPCI.class);
				List<STP_UPI_DRC_NPCI> savedRecords = serviceUpi.addSTP_UPI_DRC_NPCI(stpUpiList);
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("27")) {
				List<STP_UPI_NONCBS_IW> stpUpiList = parseJsonToList(json, STP_UPI_NONCBS_IW.class);
				List<STP_UPI_NONCBS_IW> savedRecords = serviceUpi.addSTP_UPI_NONCBS_IW(stpUpiList);
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("28")) {
				List<STP_UPI_NONCBS_OW> stpUpiList = parseJsonToList(json, STP_UPI_NONCBS_OW.class);
				List<STP_UPI_NONCBS_OW> savedRecords = serviceUpi.addSTP_UPI_NONCBS_OW(stpUpiList);
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			}
		} catch (JsonProcessingException e) {
			// Handle JSON parsing errors
			logger.error("Invalid JSON format for {}: {}", role, e.getMessage());
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseBean("FAILED", "Invalid JSON format: " + e.getMessage(), null)));
		} catch (Exception e) {
			// Handle any other errors during record insertion
			logger.error("Error saving {} records: {}", role, e.getMessage());
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBean("FAILED", "Error saving records: " + e.getMessage(), null)));
		}

		// Default success response if no type-specific logic was executed
		return CompletableFuture.completedFuture(
				ResponseEntity.ok(new ResponseBean("SUCCESS", role + " records inserted successfully.", null)));
	}

	/**
	 * Generic method to parse JSON into a List of objects
	 */
	private <T> List<T> parseJsonToList(String json, Class<T> clazz) throws JsonProcessingException {
		return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
	}

	/**
	 * Unified Fallback Method for RateLimiter, CircuitBreaker, and Timer
	 */
	public CompletableFuture<ResponseBean> handleFallback(Throwable t) {
		logger.error("Service fallback triggered due to: {}", t.getMessage());
		ResponseBean fallbackResponse = new ResponseBean("FAILED", "Service unavailable. Please try again later.",
				t.getMessage());
		return CompletableFuture.completedFuture(fallbackResponse);
	}

	@Async("asyncExecutor")
	@PostMapping("/ttumpost")
	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "handleFallback")
	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "handleFallback")
	@TimeLimiter(name = "QueryTimer", fallbackMethod = "handleFallback")
	public CompletableFuture<ResponseBean> postForTTUM(@RequestBody String json) {
		TTumRequest accObject = null;
		try {
			accObject = objectMapper.readValue(json, TTumRequest.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return fetchEntriesttumPost("PROCESS", accObject);
	}

	private CompletableFuture<ResponseBean> fetchEntriesttumPost(String role, TTumRequest accObject) {
		ResponseBean response = null;
		String type = accObject.getStatus();
		TypeEnum data = TypeEnum.valueOf(type);
		Integer queryid = accObject.getQueryid();
		if (queryid == 21) {
			STP_UPI_NSTL_NETSET_TTUMsheduledTask.PROCESSSTP_UPI_NSTL_NETSET_TTUM();
		} else if (queryid == 22) {
			STP_UPI_ADJUSTMENT_REPORTscheduledTask.PROCESSSTP_UPI_ADJUSTMENT_REPORT();
		} else if (queryid == 23) {
			STP_UPI_MULTIREVERSALscheduledTask.PROCESSSTP_UPI_MULTIREVERSAL();
		} else if (queryid == 24) {
			STP_UPI_TCC_DATAscheduledTask.PROCESSSTP_UPI_TCC_DATA();
		} else if (queryid == 25) {
			STP_UPI_RET_DATAscheduledTask.PROCESSSTP_UPI_RET_DATA();
		} else if (queryid == 26) {
			STP_UPI_DRC_NPCIscheduledTask.PROCESSSTP_UPI_DRC_NPCI();
		} else if (queryid == 27) {
			STP_UPI_NONCBS_IWscheduledTask.PROCESSSTP_UPI_NONCBS_IW();
		} else if (queryid == 28) {
			STP_UPI_NONCBS_OWscheduledTask.PROCESSSTP_UPI_NONCBS_OW();
		}
		response = entryFetchService.fetchEntrieTTUM(Arrays.asList("TTUM PROCESS"), data.getDescription());
		return CompletableFuture.completedFuture(response);
	}

}
