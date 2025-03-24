package com.stp.controller;

import static com.stp.autojob.IsoFormatterCards.responsePrint;
import static com.stp.autojob.IsoFormatterCards.responseList1210;
import static com.stp.autojob.IsoFormatterCards.request1200STP_CARDS_NFS_ISS_RECON_TTUM;
import static com.stp.autojob.IsoFormatterCards.request1201STP_CARDS_NFS_ISS_RECON_TTUMRepeat;
import static com.stp.autojob.IsoFormatterCards.request1200STP_CARDS_NFS_ISS_RECON_TTUMEnquiry;
import static com.stp.utility.GenericCLass.HOST;
import static com.stp.utility.GenericCLass.PORT;
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
import com.stp.job.cards.STP_CARDS_NFS_ISS_RECON_TTUMsheduledTask;
import com.stp.model.db1.STP_CARDS_NFS_ISS_RECON_TTUM;
import com.stp.service.ServiceCards;
import com.stp.service.impl.EntryFetchService;
import com.stp.utility.ResponseBean;
import com.stp.utility.TTumRequest;
import com.stp.utility.TypeEnum;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
@RequestMapping(value = "/api_cards")
public class CardsController {

	private static final Logger logger = LoggerFactory.getLogger(CardsController.class);

	private final ServiceCards service;
	private final ObjectMapper objectMapper;
	private final EntryFetchService entryFetchService;
	private final TaskExecutor taskExecutor;

	@Autowired
	public CardsController(ServiceCards service, ObjectMapper objectMapper, EntryFetchService entryFetchService,
			@Qualifier("asyncExecutor") TaskExecutor taskExecutor) {
		this.service = service;
		this.objectMapper = objectMapper;
		this.entryFetchService = entryFetchService;
		this.taskExecutor = taskExecutor;
	}

	private <T> List<T> parseJsonToList(String json, Class<T> clazz) throws JsonProcessingException {
		return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
	}

	public CompletableFuture<ResponseBean> handleFallback(Throwable t) {
		logger.error("Service fallback triggered due to: {}", t.getMessage());
		ResponseBean fallbackResponse = new ResponseBean("FAILED",
				"Service unavailable. Please try again later." + t.getMessage(), null);
		return CompletableFuture.completedFuture(fallbackResponse);
	}

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

	private CompletableFuture<ResponseBean> fetchEntries(String role, TTumRequest accObject) {
		ResponseBean response = null;
		String type = accObject.getStatus();
		TypeEnum data = TypeEnum.valueOf(type);
		Integer queryid = accObject.getQueryid();
		logger.info("queryid" + queryid + "type" + type);
		if (queryid == 101) {
			List<STP_CARDS_NFS_ISS_RECON_TTUM> details = service.viewNFS_ISS_RECON_TTUM(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		}
		return CompletableFuture.completedFuture(response);
	}

	@Async("asyncExecutor")
	@PostMapping("/{role}Insert")
	public CompletableFuture<ResponseEntity<ResponseBean>> saveRecord(@RequestParam(required = false) String json,
			@RequestParam(required = false) String type, @PathVariable String role) {
		logger.error("TYPE ::", type, null); // Logs the type for debugging purposes
		if (json == null || json.trim().isEmpty()) {
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseBean("FAILED", "No JSON data provided.", null)));
		}
		try {
			if (type.equalsIgnoreCase("101")) {
				List<STP_CARDS_NFS_ISS_RECON_TTUM> stpcardsList = parseJsonToList(json,
						STP_CARDS_NFS_ISS_RECON_TTUM.class);
				List<STP_CARDS_NFS_ISS_RECON_TTUM> savedRecords = service.addNFS_ISS_RECON_TTUM(stpcardsList); // records
				stpcardsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + "  Records Process Successfully.", savedRecords)));
			}

		} catch (JsonProcessingException e) {
			logger.error("Invalid JSON format for {}: {}", role, e.getMessage());
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResponseBean("FAILED", "Invalid JSON format: " + e.getMessage(), null)));
		} catch (Exception e) {
			logger.error("Error saving {} records: {}", role, e.getMessage());
			return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ResponseBean("FAILED", "Error saving records: " + e.getMessage(), null)));
		}
		return CompletableFuture.completedFuture(
				ResponseEntity.ok(new ResponseBean("SUCCESS", role + " records inserted successfully.", null)));
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
		if (queryid == 101) {
			STP_CARDS_NFS_ISS_RECON_TTUMsheduledTask.PROCESSSTP_CARDS_NFS_ISS_RECON_TTUM();
		} else if (queryid == 102) {

		} else if (queryid == 103) {

		} else if (queryid == 104) {

		} else if (queryid == 105) {

		} else if (queryid == 106) {

		} else if (queryid == 107) {

		} else if (queryid == 108) {

		} else if (queryid == 109) {

		} else if (queryid == 110) {

		} else if (queryid == 111) {

		} else if (queryid == 112) {

		} else if (queryid == 113) {

		} else if (queryid == 114) {

		} else if (queryid == 115) {

		} else if (queryid == 116) {

		} else if (queryid == 117) {

		}

		response = entryFetchService.fetchEntrieTTUM(Arrays.asList("TTUM PROCESS"), data.getDescription());
		return CompletableFuture.completedFuture(response);
	}

}
