package com.stp.controller;

import static com.stp.utility.GenericCLass.HOST;
import static com.stp.utility.GenericCLass.PORT;

import java.util.Arrays;
import java.util.Iterator;
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
import com.iso.opt.IsoPackagerGlobal;
import com.stp.job.imps.STP_IMPS_CHARGEBACK_REPORTsheduledTask;
import com.stp.job.imps.STP_IMPS_CUSTOMER_COMPsheduledTask;
import com.stp.job.imps.STP_IMPS_IW_NETWORK_DECLINEsheduledTask;
import com.stp.job.imps.STP_IMPS_NONCBS_IWsheduledTask;
import com.stp.job.imps.STP_IMPS_NONCBS_OWsheduledTask;
import com.stp.job.imps.STP_IMPS_NONNPCI_IWsheduledTask;
import com.stp.job.imps.STP_IMPS_NONNPCI_OWscheduledTask;
import com.stp.job.imps.STP_IMPS_NTSL_NETSET_TTUMsheduledTask;
import com.stp.job.imps.STP_IMPS_OW_NETWORK_DECLINEscheduledTask;
import com.stp.job.imps.STP_IMPS_PREARBITRATION_REPORTsheduledTask;
import com.stp.job.imps.STP_IMPS_RCC_REPORT_REPRAISEsheduledTask;
import com.stp.job.imps.STP_IMPS_RCC_REPORTsheduledTask;
import com.stp.job.imps.STP_IMPS_TCC_DATA_IW_RETscheduledTask;
import com.stp.job.imps.STP_IMPS_TCC_DATA_IWscheduledTask;
import com.stp.model.db1.STP_IMPS_CHARGEBACK_REPORT;
import com.stp.model.db1.STP_IMPS_CUSTOMER_COMP;
import com.stp.model.db1.STP_IMPS_IW_NETWORK_DECLINE;
import com.stp.model.db1.STP_IMPS_NONCBS_IW;
import com.stp.model.db1.STP_IMPS_NONCBS_OW;
import com.stp.model.db1.STP_IMPS_NONNPCI_IW;
import com.stp.model.db1.STP_IMPS_NONNPCI_OW;
import com.stp.model.db1.STP_IMPS_NTSL_NETSET_TTUM;
import com.stp.model.db1.STP_IMPS_OW_NETWORK_DECLINE;
import com.stp.model.db1.STP_IMPS_PREARBITRATION_REPORT;
import com.stp.model.db1.STP_IMPS_RCC_REPORT;
import com.stp.model.db1.STP_IMPS_RCC_REPORT_REPRAISE;
import com.stp.model.db1.STP_IMPS_TCC_DATA_IW;
import com.stp.model.db1.STP_IMPS_TCC_DATA_IW_RET;
import com.stp.service.ServiceImps;
import com.stp.service.impl.EntryFetchService;
import com.stp.utility.ResponseBean;
import com.stp.utility.TTumRequest;
import com.stp.utility.TypeEnum;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
@RequestMapping(value = "/api_imps")
public class IMPSControllerSingle {

	private static final Logger logger = LoggerFactory.getLogger(IMPSControllerSingle.class);

	private final ServiceImps serviceImps;
	private final ObjectMapper objectMapper;
	private final EntryFetchService entryFetchService;
	private final TaskExecutor taskExecutor;

	@Autowired
	public IMPSControllerSingle(ServiceImps serviceImps, ObjectMapper objectMapper, EntryFetchService entryFetchService,
			@Qualifier("asyncExecutor") TaskExecutor taskExecutor) {
		this.serviceImps = serviceImps;
		this.objectMapper = objectMapper;
		this.entryFetchService = entryFetchService;
		this.taskExecutor = taskExecutor;
	}

	@Async("asyncExecutor")
	@PostMapping("/{role}View")
//	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "handleFallback")
//	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "handleFallback")
//	@TimeLimiter(name = "QueryTimer", fallbackMethod = "handleFallback")
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
		if (queryid == 1) {
			List<STP_IMPS_NONCBS_IW> details = serviceImps.viewSTP_IMPS_NONCBS_IW(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 2) {
			List<STP_IMPS_NONNPCI_IW> details = serviceImps.viewSTP_IMPS_NONNPCI_IW(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 3) {
			List<STP_IMPS_NONNPCI_OW> details = serviceImps.viewSTP_IMPS_NONNPCI_OW(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 4) {
			List<STP_IMPS_TCC_DATA_IW> details = serviceImps.viewSTP_IMPS_TCC_DATA_IW(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 5) {
			List<STP_IMPS_IW_NETWORK_DECLINE> details = serviceImps.viewSTP_IMPS_IW_NETWORK_DECLINE(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 6) {
			List<STP_IMPS_OW_NETWORK_DECLINE> details = serviceImps.viewSTP_IMPS_OW_NETWORK_DECLINE(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 7) {
			List<STP_IMPS_NONCBS_OW> details = serviceImps.viewSTP_IMPS_NONCBS_OW(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 8) {
			List<STP_IMPS_NTSL_NETSET_TTUM> details = serviceImps.viewSTP_IMPS_NTSL_NETSET_TTUM(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 9) {
			List<STP_IMPS_CHARGEBACK_REPORT> details = serviceImps.viewSTP_IMPS_CHARGEBACK_REPORT(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 10) {
			List<STP_IMPS_RCC_REPORT> details = serviceImps.viewSTP_IMPS_RCC_REPORT(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 11) {
			List<STP_IMPS_PREARBITRATION_REPORT> details = serviceImps.viewSTP_IMPS_PREARBITRATION_REPORT(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 12) {
			List<STP_IMPS_CUSTOMER_COMP> details = serviceImps.viewSTP_IMPS_CUSTOMER_COMP(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 13) {
			List<STP_IMPS_RCC_REPORT_REPRAISE> details = serviceImps.viewSTP_IMPS_RCC_REPORT_REPRAISE(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 14) {
			List<STP_IMPS_TCC_DATA_IW_RET> details = serviceImps.viewSTP_IMPS_TCC_DATA_IW_RET(accObject);
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
			if (type.equalsIgnoreCase("1")) {
				List<STP_IMPS_NONCBS_IW> stpImpsList = parseJsonToList(json, STP_IMPS_NONCBS_IW.class);
				List<STP_IMPS_NONCBS_IW> savedRecords = serviceImps.addSTP_IMPS_NONCBS_IW(stpImpsList); // records
				stpImpsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + "  Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("2")) {
				List<STP_IMPS_NONNPCI_IW> stpImpsList = parseJsonToList(json, STP_IMPS_NONNPCI_IW.class);
				List<STP_IMPS_NONNPCI_IW> savedRecords = serviceImps.addSTP_IMPS_NONNPCI_IW(stpImpsList);// records
				stpImpsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + "  Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("3")) {
				List<STP_IMPS_NONNPCI_OW> stpImpsList = parseJsonToList(json, STP_IMPS_NONNPCI_OW.class);
				List<STP_IMPS_NONNPCI_OW> savedRecords = serviceImps.addSTP_IMPS_NONNPCI_OW(stpImpsList);// records
				stpImpsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + "  Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("4")) {
				List<STP_IMPS_TCC_DATA_IW> stpImpsList = parseJsonToList(json, STP_IMPS_TCC_DATA_IW.class);
				List<STP_IMPS_TCC_DATA_IW> savedRecords = serviceImps.addSTP_IMPS_TCC_DATA_IW(stpImpsList); // records
				stpImpsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + "  Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("5")) {
				List<STP_IMPS_IW_NETWORK_DECLINE> stpUpiList = parseJsonToList(json, STP_IMPS_IW_NETWORK_DECLINE.class);
				List<STP_IMPS_IW_NETWORK_DECLINE> savedRecords = serviceImps.addSTP_IMPS_IW_NETWORK_DECLINE(stpUpiList); // records
				stpUpiList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + "  Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("6")) {
				List<STP_IMPS_OW_NETWORK_DECLINE> stpImpsList = parseJsonToList(json,
						STP_IMPS_OW_NETWORK_DECLINE.class);
				List<STP_IMPS_OW_NETWORK_DECLINE> savedRecords = serviceImps
						.addSTP_IMPS_OW_NETWORK_DECLINE(stpImpsList); // records
				stpImpsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + "  Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("7")) {
				List<STP_IMPS_NONCBS_OW> stpImpsList = parseJsonToList(json, STP_IMPS_NONCBS_OW.class);
				List<STP_IMPS_NONCBS_OW> savedRecords = serviceImps.addSTP_IMPS_NONCBS_OW(stpImpsList); // records
				stpImpsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("8")) {
				List<STP_IMPS_NTSL_NETSET_TTUM> stpImpsList = parseJsonToList(json, STP_IMPS_NTSL_NETSET_TTUM.class);
				List<STP_IMPS_NTSL_NETSET_TTUM> savedRecords = serviceImps.addSTP_IMPS_NTSL_NETSET_TTUM(stpImpsList); // records
				stpImpsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("9")) {
				List<STP_IMPS_CHARGEBACK_REPORT> stpImpsList = parseJsonToList(json, STP_IMPS_CHARGEBACK_REPORT.class);
				List<STP_IMPS_CHARGEBACK_REPORT> savedRecords = serviceImps.addSTP_IMPS_CHARGEBACK_REPORT(stpImpsList); // records
				stpImpsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("10")) {
				List<STP_IMPS_RCC_REPORT> stpImpsList = parseJsonToList(json, STP_IMPS_RCC_REPORT.class);
				List<STP_IMPS_RCC_REPORT> savedRecords = serviceImps.addSTP_IMPS_RCC_REPORT(stpImpsList); // records
				stpImpsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("11")) {
				List<STP_IMPS_PREARBITRATION_REPORT> stpImpsList = parseJsonToList(json,
						STP_IMPS_PREARBITRATION_REPORT.class);
				List<STP_IMPS_PREARBITRATION_REPORT> savedRecords = serviceImps
						.addSTP_IMPS_PREARBITRATION_REPORT(stpImpsList); // records
				stpImpsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("12")) {
				List<STP_IMPS_CUSTOMER_COMP> stpImpsList = parseJsonToList(json, STP_IMPS_CUSTOMER_COMP.class);
				List<STP_IMPS_CUSTOMER_COMP> savedRecords = serviceImps.addSTP_IMPS_CUSTOMER_COMP(stpImpsList); // records
				stpImpsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("13")) {
				List<STP_IMPS_RCC_REPORT_REPRAISE> stpImpsList = parseJsonToList(json,
						STP_IMPS_RCC_REPORT_REPRAISE.class);
				List<STP_IMPS_RCC_REPORT_REPRAISE> savedRecords = serviceImps
						.addSTP_IMPS_RCC_REPORT_REPRAISE(stpImpsList); // records
				stpImpsList = null;
				return CompletableFuture.completedFuture(ResponseEntity
						.ok(new ResponseBean("SUCCESS", role + " Records Process Successfully.", savedRecords)));
			} else if (type.equalsIgnoreCase("14")) {
				List<STP_IMPS_TCC_DATA_IW_RET> stpImpsList = parseJsonToList(json, STP_IMPS_TCC_DATA_IW_RET.class);
				List<STP_IMPS_TCC_DATA_IW_RET> savedRecords = serviceImps.addSTP_IMPS_TCC_DATA_IW_RET(stpImpsList); // records
				stpImpsList = null;
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
		if (queryid == 1) {
			STP_IMPS_NONCBS_IWsheduledTask.PROCESSSTP_IMPS_NONCBS_IW();
		} else if (queryid == 2) {
			STP_IMPS_NONNPCI_IWsheduledTask.PROCESSSTP_IMPS_NONNPCI_IW();
		} else if (queryid == 3) {
			STP_IMPS_NONNPCI_OWscheduledTask.PROCESSSTP_IMPS_NONNPCI_OW();
		} else if (queryid == 4) {
			STP_IMPS_TCC_DATA_IWscheduledTask.PROCESSSTP_IMPS_TCC_DATA_IW();
		} else if (queryid == 5) {
			STP_IMPS_IW_NETWORK_DECLINEsheduledTask.PROCESSSTP_IMPS_IW_NETWORK_DECLINE();
		} else if (queryid == 6) {
			STP_IMPS_OW_NETWORK_DECLINEscheduledTask.PROCESSSTP_IMPS_OW_NETWORK_DECLINE();
		} else if (queryid == 7) {
			STP_IMPS_NONCBS_OWsheduledTask.PROCESSSTP_IMPS_NONCBS_OW();
		} else if (queryid == 8) {
			STP_IMPS_NTSL_NETSET_TTUMsheduledTask.PROCESSSTP_IMPS_NTSL_NETSET_TTUM();
		} else if (queryid == 9) {
			STP_IMPS_CHARGEBACK_REPORTsheduledTask.PROCESSSTP_IMPS_CHARGEBACK_REPORT();
		} else if (queryid == 10) {
			STP_IMPS_RCC_REPORTsheduledTask.PROCESSSTP_IMPS_RCC_REPORT();
		} else if (queryid == 11) {
			STP_IMPS_PREARBITRATION_REPORTsheduledTask.PROCESSSTP_IMPS_PREARBITRATION_REPORT();
		} else if (queryid == 12) {
			STP_IMPS_CUSTOMER_COMPsheduledTask.PROCESSSTP_IMPS_CUSTOMER_COMP();
		} else if (queryid == 13) {
			STP_IMPS_RCC_REPORT_REPRAISEsheduledTask.PROCESSSTP_IMPS_RCC_REPORT_REPRAISE();
		} else if (queryid == 14) {
			STP_IMPS_TCC_DATA_IW_RETscheduledTask.PROCESSSTP_IMPS_TCC_DATA_IW_RET();
		}
		response = entryFetchService.fetchEntrieTTUM(Arrays.asList("TTUM PROCESS"), data.getDescription());
		return CompletableFuture.completedFuture(response);
	}

	// dynamic implementation BY vivek
	public void PROCESSSTP_IMPS_TCC_DATA_IW_ISO() {
		ISOCommunicationExample serviceNetwork = null;
		List<STP_IMPS_TCC_DATA_IW> newReq = null;
		List<STP_IMPS_TCC_DATA_IW> repeatReq = null;
		List<STP_IMPS_TCC_DATA_IW> enquiryReq = null;
		List<IsoV93Message> request1200 = null;
		IsoV93Message isoV93Message = null;
		IsoV93MessageRes isoV93MessageRes = null;
		IsoV93MessageRes response = null;

		byte[] requestmsgBytes = null;
		byte[] responsemsgBytes = null;
		int dbresponse = 0;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
			System.out.println("NEW REQUEST INITIATED");
			try {
				newReq = serviceImps.processSTP_IMPS_TCC_DATA_IW("L4");
				request1200 = IsoPackagerGlobal.request1200(newReq);
				for (Iterator<IsoV93Message> iterator = request1200.iterator(); iterator.hasNext();) {
					isoV93Message = iterator.next();
					requestmsgBytes = isoV93Message.generateMessage();
					isoV93Message.printMessage("FRESH");
					isoV93Message = null;
					responsemsgBytes = serviceNetwork.networkTransportByte(requestmsgBytes);
					requestmsgBytes = null;
					isoV93MessageRes = new IsoV93MessageRes(responsemsgBytes);
					IsoV93MessageRes responseList1210 = IsoPackagerGlobal.responseList1210(isoV93MessageRes);
					responsemsgBytes = null;
					isoV93MessageRes.printMessage("FRESH");
					response = IsoPackagerGlobal.responseList1210(isoV93MessageRes);
					dbresponse = dbresponse + serviceImps.updateSTP_IMPS_TCC_DATA_IW(response, "FRESH");
					response = null;
					isoV93MessageRes = null;

					iterator.remove();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				newReq = null;
				request1200 = null;
				isoV93Message = null;
				isoV93MessageRes = null;
				response = null;
				System.out.println("UPDATED IN DATABASE FRESH" + dbresponse);
				dbresponse = 0;
			}
			System.out.println("NEW REQUEST END");
			System.out.println("***************");
			System.out.println("ENQUIRED REQUEST START");
			try {
				enquiryReq = serviceImps.processSTP_IMPS_TCC_DATA_IW("L5");
				request1200 = IsoPackagerGlobal.request1200Enquiry(enquiryReq);
				for (Iterator<IsoV93Message> iterator = request1200.iterator(); iterator.hasNext();) {
					isoV93Message = iterator.next();
					requestmsgBytes = isoV93Message.generateMessage();
					isoV93Message.printMessage("ENQUIRY");
					isoV93Message = null;
					responsemsgBytes = serviceNetwork.networkTransportByte(requestmsgBytes);
					requestmsgBytes = null;
					isoV93MessageRes = new IsoV93MessageRes(responsemsgBytes);
					responsemsgBytes = null;
					isoV93MessageRes.printMessage("ENQUIRY");
					response = IsoPackagerGlobal.responseList1210(isoV93MessageRes);
					dbresponse = dbresponse + serviceImps.updateSTP_IMPS_TCC_DATA_IW(response, "ENQUIRY");
					response = null;
					isoV93MessageRes = null;
					iterator.remove();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				enquiryReq = null;
				request1200 = null;
				isoV93Message = null;
				isoV93MessageRes = null;
				response = null;
				System.out.println("UPDATED IN DATABASE ENQUIRE" + dbresponse);
				dbresponse = 0;
			}
			System.out.println("ENQUIRED REQUEST END");
			System.out.println("***************");
			System.out.println("REPEAT REQUEST START");
			try {
				repeatReq = serviceImps.processSTP_IMPS_TCC_DATA_IW("L6");
				request1200 = IsoPackagerGlobal.request1201Repeat(repeatReq);
				for (Iterator<IsoV93Message> iterator = request1200.iterator(); iterator.hasNext();) {
					isoV93Message = iterator.next();
					requestmsgBytes = isoV93Message.generateMessage();
					isoV93Message.printMessage("REPEAT");
					isoV93Message = null;
					responsemsgBytes = serviceNetwork.networkTransportByte(requestmsgBytes);
					requestmsgBytes = null;
					isoV93MessageRes = new IsoV93MessageRes(responsemsgBytes);
					responsemsgBytes = null;
					isoV93MessageRes.printMessage("REPEAT");
					response = IsoPackagerGlobal.responseList1210(isoV93MessageRes);
					dbresponse = dbresponse + serviceImps.updateSTP_IMPS_TCC_DATA_IW(response, "REPEAT");
					response = null;
					isoV93MessageRes = null;
					iterator.remove();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				repeatReq = null;
				request1200 = null;
				isoV93Message = null;
				isoV93MessageRes = null;
				response = null;
				System.out.println("UPDATED IN DATABASE REPEAT" + dbresponse);
				dbresponse = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			newReq = null;
			repeatReq = null;
			enquiryReq = null;
			response = null;
			request1200 = null;
			isoV93Message = null;
			isoV93MessageRes = null;
		}
	}

}