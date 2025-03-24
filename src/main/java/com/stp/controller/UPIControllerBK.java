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
@RequestMapping("/api_upibk")
public class UPIControllerBK {

	private static final Logger logger = LoggerFactory.getLogger(UPIControllerBK.class);

	private final ServiceUpi serviceUpi;
	private final EntryFetchService entryFetchService;
	private final TaskExecutor taskExecutor;
	private final ObjectMapper objectMapper;

	@Autowired
	public UPIControllerBK(ServiceUpi serviceUpi, ObjectMapper objectMapper, EntryFetchService entryFetchService,
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
		if (queryid == 1) {
			List<STP_UPI> details = serviceUpi.globalUPIDetail(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
			details = null;
		} else if (queryid == 21) {
			List<STP_UPI_NSTL_NETSET_TTUM> details = serviceUpi.viewSTP_UPI_NSTL_NETSET_TTUM(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 22) {
			List<STP_UPI_ADJUSTMENT_REPORT> details = serviceUpi.viewSTP_UPI_ADJUSTMENT_REPORT(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 23) {
			List<STP_UPI_MULTIREVERSAL> details = serviceUpi.viewSTP_UPI_MULTIREVERSAL(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 24) {
			List<STP_UPI_TCC_DATA> details = serviceUpi.viewSTP_UPI_TCC_DATA(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 25) {
			List<STP_UPI_RET_DATA> details = serviceUpi.viewSTP_UPI_RET_DATA(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 26) {
			List<STP_UPI_DRC_NPCI> details = serviceUpi.viewSTP_UPI_DRC_NPCI(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 27) {
			List<STP_UPI_NONCBS_IW> details = serviceUpi.viewSTP_UPI_NONCBS_IW(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
		} else if (queryid == 28) {
			List<STP_UPI_NONCBS_OW> details = serviceUpi.viewSTP_UPI_NONCBS_OW(accObject);
			response = entryFetchService.fetchEntries(details, data.getDescription());
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
			String checkTccTTum = checkStpUpiNetsetTtum();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 22) {
			String checkTccTTum = checkStpUpiAdjustmentReport();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 23) {
			String checkTccTTum = checkStpUpiMultireversal();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 24) {
			String checkTccTTum = checkStpUpiTccData();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 25) {
			String checkTccTTum = checkStpUpiRetData();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 26) {
			String checkTccTTum = checkStpUpiDrcNpci();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 27) {
			String checkTccTTum = checkStpUpiNonCbsIw();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 28) {
			String checkTccTTum = checkStpUpiNonCbsOw();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		}
		return CompletableFuture.completedFuture(response);
	}

	public String checkStpUpiAdjustmentReport() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			List<STP_UPI_ADJUSTMENT_REPORT> processSTP_UPI_ADJUSTMENT_REPORT = serviceUpi
					.processSTP_UPI_ADJUSTMENT_REPORT("L4");
			List<IsoV93Message> requestIso = request1200STP_UPI_ADJUSTMENT_REPORT(processSTP_UPI_ADJUSTMENT_REPORT);
			processSTP_UPI_ADJUSTMENT_REPORT = null;
			List<IsoV93MessageRes> responseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : requestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					responseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			requestIso = null;

			count = 1;
			ArrayList<IsoV93MessageRes> reslist = responseList1210(responseIso);
			int ReqRespSTP_UPI_ADJUSTMENT_REPORT = serviceUpi.ReqRespSTP_UPI_ADJUSTMENT_REPORT(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("ReqRespSTP_UPI_ADJUSTMENT_REPORT" + ReqRespSTP_UPI_ADJUSTMENT_REPORT);
			reslist = null;

		}
//

		{
			// enquiry request
			List<STP_UPI_ADJUSTMENT_REPORT> enqSTP_UPI_ADJUSTMENT_REPORT = serviceUpi
					.processSTP_UPI_ADJUSTMENT_REPORT("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_UPI_ADJUSTMENT_REPORTEnquiry(
					enqSTP_UPI_ADJUSTMENT_REPORT);
			enqSTP_UPI_ADJUSTMENT_REPORT = null;
			count = 1;
			List<IsoV93MessageRes> enquiryresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : enquiryrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					enquiryresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			enquiryrequestIso = null;
			count = 1;
			ArrayList<IsoV93MessageRes> enquiryreslist = responseList1210(enquiryresponseIso);

			int enqReqRespSTP_UPI_ADJUSTMENT_REPORT = serviceUpi.ReqRespSTP_UPI_ADJUSTMENT_REPORT(enquiryreslist,
					"Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqReqRespSTP_UPI_ADJUSTMENT_REPORT" + enqReqRespSTP_UPI_ADJUSTMENT_REPORT);
		}
		{
			// repeat request
			List<STP_UPI_ADJUSTMENT_REPORT> reqSTP_UPI_ADJUSTMENT_REPORT = serviceUpi
					.processSTP_UPI_ADJUSTMENT_REPORT("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_UPI_ADJUSTMENT_REPORTRepeat(
					reqSTP_UPI_ADJUSTMENT_REPORT);
			reqSTP_UPI_ADJUSTMENT_REPORT = null;
			count = 1;
			List<IsoV93MessageRes> repeatresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : repeatrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					repeatresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			count = 1;
			ArrayList<IsoV93MessageRes> repeatreslist = responseList1210(repeatresponseIso);
			int STP_UPI_ADJUSTMENT_REPORT = serviceUpi.ReqRespSTP_UPI_ADJUSTMENT_REPORT(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("ReqRespSTP_UPI_ADJUSTMENT_REPORT " + STP_UPI_ADJUSTMENT_REPORT);
		}
		return "STP_UPI_ADJUSTMENT_REPORT";
	}

	//
	public String checkStpUpiNetsetTtum() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			List<STP_UPI_NSTL_NETSET_TTUM> processSTP_UPI_NSTL_NETSET_TTUM = serviceUpi
					.processSTP_UPI_NSTL_NETSET_TTUM("L4");
			List<IsoV93Message> requestIso = request1200STP_UPI_NSTL_NETSET_TTUM(processSTP_UPI_NSTL_NETSET_TTUM);
			processSTP_UPI_NSTL_NETSET_TTUM = null;
			List<IsoV93MessageRes> responseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : requestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					responseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			requestIso = null;

			count = 1;
			ArrayList<IsoV93MessageRes> reslist = responseList1210(responseIso);
			int ReqRespSTP_UPI_NSTL_NETSET_TTUM = serviceUpi.ReqRespSTP_UPI_NSTL_NETSET_TTUM(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("ReqRespSTP_UPI_NSTL_NETSET_TTUM" + ReqRespSTP_UPI_NSTL_NETSET_TTUM);
			reslist = null;

		}
//

		{
			// enquiry request
			List<STP_UPI_NSTL_NETSET_TTUM> enqSTP_UPI_NSTL_NETSET_TTUM = serviceUpi
					.processSTP_UPI_NSTL_NETSET_TTUM("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_UPI_NSTL_NETSET_TTUMEnquiry(
					enqSTP_UPI_NSTL_NETSET_TTUM);
			enqSTP_UPI_NSTL_NETSET_TTUM = null;
			count = 1;
			List<IsoV93MessageRes> enquiryresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : enquiryrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					enquiryresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			enquiryrequestIso = null;
			count = 1;
			ArrayList<IsoV93MessageRes> enquiryreslist = responseList1210(enquiryresponseIso);

			int enqReqRespSTP_UPI_NSTL_NETSET_TTUM = serviceUpi.ReqRespSTP_UPI_NSTL_NETSET_TTUM(enquiryreslist,
					"Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqReqRespSTP_UPI_NSTL_NETSET_TTUM" + enqReqRespSTP_UPI_NSTL_NETSET_TTUM);
		}
		{
			// repeat request
			List<STP_UPI_NSTL_NETSET_TTUM> reqSTP_UPI_NSTL_NETSET_TTUM = serviceUpi
					.processSTP_UPI_NSTL_NETSET_TTUM("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_UPI_NSTL_NETSET_TTUMRepeat(
					reqSTP_UPI_NSTL_NETSET_TTUM);
			reqSTP_UPI_NSTL_NETSET_TTUM = null;
			count = 1;
			List<IsoV93MessageRes> repeatresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : repeatrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					repeatresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			count = 1;
			ArrayList<IsoV93MessageRes> repeatreslist = responseList1210(repeatresponseIso);
			int STP_UPI_NSTL_NETSET_TTUM = serviceUpi.ReqRespSTP_UPI_NSTL_NETSET_TTUM(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("ReqRespSTP_UPI_NSTL_NETSET_TTUM " + STP_UPI_NSTL_NETSET_TTUM);
		}
		return "STP_UPI_NSTL_NETSET_TTUM";
	}

/// multi

	public String checkStpUpiMultireversal() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			List<STP_UPI_MULTIREVERSAL> processSTP_UPI_MULTIREVERSAL = serviceUpi.processSTP_UPI_MULTIREVERSAL("L4");
			List<IsoV93Message> requestIso = request1200STP_UPI_MULTIREVERSAL(processSTP_UPI_MULTIREVERSAL);
			processSTP_UPI_MULTIREVERSAL = null;
			List<IsoV93MessageRes> responseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : requestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					responseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			requestIso = null;

			count = 1;
			ArrayList<IsoV93MessageRes> reslist = responseList1210(responseIso);
			int ReqRespSTP_UPI_MULTIREVERSAL = serviceUpi.ReqRespSTP_UPI_MULTIREVERSAL(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("ReqRespSTP_UPI_MULTIREVERSAL" + ReqRespSTP_UPI_MULTIREVERSAL);
			reslist = null;

		}
//

		{
			// enquiry request
			List<STP_UPI_MULTIREVERSAL> enqSTP_UPI_MULTIREVERSAL = serviceUpi.processSTP_UPI_MULTIREVERSAL("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_UPI_MULTIREVERSALEnquiry(enqSTP_UPI_MULTIREVERSAL);
			enqSTP_UPI_MULTIREVERSAL = null;
			count = 1;
			List<IsoV93MessageRes> enquiryresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : enquiryrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					enquiryresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			enquiryrequestIso = null;
			count = 1;
			ArrayList<IsoV93MessageRes> enquiryreslist = responseList1210(enquiryresponseIso);

			int enqReqRespSTP_UPI_MULTIREVERSAL = serviceUpi.ReqRespSTP_UPI_MULTIREVERSAL(enquiryreslist, "Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqReqRespSTP_UPI_MULTIREVERSAL" + enqReqRespSTP_UPI_MULTIREVERSAL);
		}
		{
			// repeat request
			List<STP_UPI_MULTIREVERSAL> reqSTP_UPI_MULTIREVERSAL = serviceUpi.processSTP_UPI_MULTIREVERSAL("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_UPI_MULTIREVERSALRepeat(reqSTP_UPI_MULTIREVERSAL);
			reqSTP_UPI_MULTIREVERSAL = null;
			count = 1;
			List<IsoV93MessageRes> repeatresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : repeatrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					repeatresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			count = 1;
			ArrayList<IsoV93MessageRes> repeatreslist = responseList1210(repeatresponseIso);
			int STP_UPI_MULTIREVERSAL = serviceUpi.ReqRespSTP_UPI_MULTIREVERSAL(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("STP_UPI_MULTIREVERSAL   " + STP_UPI_MULTIREVERSAL);
		}
		return "STP_UPI_MULTIREVERSAL";
	}

	//// tcc data
	public String checkStpUpiTccData() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			List<STP_UPI_TCC_DATA> processSTP_UPI_TCC_DATA = serviceUpi.processSTP_UPI_TCC_DATA("L4");
			List<IsoV93Message> requestIso = request1200STP_UPI_TCC_DATA(processSTP_UPI_TCC_DATA);
			processSTP_UPI_TCC_DATA = null;
			List<IsoV93MessageRes> responseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : requestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					responseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			requestIso = null;

			count = 1;
			ArrayList<IsoV93MessageRes> reslist = responseList1210(responseIso);
			int ReqRespSTP_UPI_TCC_DATA = serviceUpi.ReqRespSTP_UPI_TCC_DATA(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("ReqRespSTP_UPI_TCC_DATA" + ReqRespSTP_UPI_TCC_DATA);
			reslist = null;

		}
//

		{
			// enquiry request
			List<STP_UPI_TCC_DATA> enqSTP_UPI_TCC_DATA = serviceUpi.processSTP_UPI_TCC_DATA("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_UPI_TCC_DATAEnquiry(enqSTP_UPI_TCC_DATA);
			enqSTP_UPI_TCC_DATA = null;
			count = 1;
			List<IsoV93MessageRes> enquiryresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : enquiryrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					enquiryresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			enquiryrequestIso = null;
			count = 1;
			ArrayList<IsoV93MessageRes> enquiryreslist = responseList1210(enquiryresponseIso);

			int enqReqRespSTP_UPI_TCC_DATA = serviceUpi.ReqRespSTP_UPI_TCC_DATA(enquiryreslist, "Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqReqRespSTP_UPI_TCC_DATA" + enqReqRespSTP_UPI_TCC_DATA);
		}
		{
			// repeat request
			List<STP_UPI_TCC_DATA> reqSTP_UPI_TCC_DATA = serviceUpi.processSTP_UPI_TCC_DATA("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_UPI_TCC_DATARepeat(reqSTP_UPI_TCC_DATA);
			reqSTP_UPI_TCC_DATA = null;
			count = 1;
			List<IsoV93MessageRes> repeatresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : repeatrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					repeatresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			count = 1;
			ArrayList<IsoV93MessageRes> repeatreslist = responseList1210(repeatresponseIso);
			int STP_UPI_TCC_DATA = serviceUpi.ReqRespSTP_UPI_TCC_DATA(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("ReqRespSTP_UPI_TCC_DATA   " + STP_UPI_TCC_DATA);
		}
		return "STP_UPI_TCC_DATA";
	}

///
	public String checkStpUpiRetData() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			List<STP_UPI_RET_DATA> processSTP_UPI_RET_DATA = serviceUpi.processSTP_UPI_RET_DATA("L4");
			List<IsoV93Message> requestIso = request1200STP_UPI_RET_DATA(processSTP_UPI_RET_DATA);
			processSTP_UPI_RET_DATA = null;
			List<IsoV93MessageRes> responseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : requestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					responseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			requestIso = null;

			count = 1;
			ArrayList<IsoV93MessageRes> reslist = responseList1210(responseIso);
			int ReqRespSTP_UPI_RET_DATA = serviceUpi.ReqRespSTP_UPI_RET_DATA(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("ReqRespSTP_UPI_RET_DATA" + ReqRespSTP_UPI_RET_DATA);
			reslist = null;

		}
//

		{
			// enquiry request
			List<STP_UPI_RET_DATA> enqSTP_UPI_RET_DATA = serviceUpi.processSTP_UPI_RET_DATA("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_UPI_RET_DATAEnquiry(enqSTP_UPI_RET_DATA);
			enqSTP_UPI_RET_DATA = null;
			count = 1;
			List<IsoV93MessageRes> enquiryresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : enquiryrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					enquiryresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			enquiryrequestIso = null;
			count = 1;
			ArrayList<IsoV93MessageRes> enquiryreslist = responseList1210(enquiryresponseIso);

			int enqReqRespSTP_UPI_RET_DATA = serviceUpi.ReqRespSTP_UPI_RET_DATA(enquiryreslist, "Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqReqRespSTP_UPI_RET_DATA" + enqReqRespSTP_UPI_RET_DATA);
		}
		{
			// repeat request
			List<STP_UPI_RET_DATA> reqSTP_UPI_RET_DATA = serviceUpi.processSTP_UPI_RET_DATA("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_UPI_RET_DATARepeat(reqSTP_UPI_RET_DATA);
			reqSTP_UPI_RET_DATA = null;
			count = 1;
			List<IsoV93MessageRes> repeatresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : repeatrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					repeatresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			count = 1;
			ArrayList<IsoV93MessageRes> repeatreslist = responseList1210(repeatresponseIso);
			int STP_UPI_RET_DATA = serviceUpi.ReqRespSTP_UPI_RET_DATA(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("ReqRespSTP_UPI_RET_DATA  " + STP_UPI_RET_DATA);
		}
		return "STP_UPI_RET_DATA";
	}

	/////
	public String checkStpUpiDrcNpci() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			List<STP_UPI_DRC_NPCI> processSTP_UPI_DRC_NPCI = serviceUpi.processSTP_UPI_DRC_NPCI("L4");
			List<IsoV93Message> requestIso = request1200STP_UPI_DRC_NPCI(processSTP_UPI_DRC_NPCI);
			processSTP_UPI_DRC_NPCI = null;
			List<IsoV93MessageRes> responseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : requestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					responseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			requestIso = null;

			count = 1;
			ArrayList<IsoV93MessageRes> reslist = responseList1210(responseIso);
			int ReqRespSTP_UPI_DRC_NPCI = serviceUpi.ReqRespSTP_UPI_DRC_NPCI(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("ReqRespSTP_UPI_DRC_NPCI" + ReqRespSTP_UPI_DRC_NPCI);
			reslist = null;

		}
//

		{
			// enquiry request
			List<STP_UPI_DRC_NPCI> enqSTP_UPI_DRC_NPCI = serviceUpi.processSTP_UPI_DRC_NPCI("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_UPI_DRC_NPCIEnquiry(enqSTP_UPI_DRC_NPCI);
			enqSTP_UPI_DRC_NPCI = null;
			count = 1;
			List<IsoV93MessageRes> enquiryresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : enquiryrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					enquiryresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			enquiryrequestIso = null;
			count = 1;
			ArrayList<IsoV93MessageRes> enquiryreslist = responseList1210(enquiryresponseIso);

			int enqReqRespSTP_UPI_DRC_NPCI = serviceUpi.ReqRespSTP_UPI_DRC_NPCI(enquiryreslist, "Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqReqRespSTP_UPI_DRC_NPCI" + enqReqRespSTP_UPI_DRC_NPCI);
		}
		{
			// repeat request
			List<STP_UPI_DRC_NPCI> reqSTP_UPI_DRC_NPCI = serviceUpi.processSTP_UPI_DRC_NPCI("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_UPI_DRC_NPCIRepeat(reqSTP_UPI_DRC_NPCI);
			reqSTP_UPI_DRC_NPCI = null;
			count = 1;
			List<IsoV93MessageRes> repeatresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : repeatrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					repeatresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			count = 1;
			ArrayList<IsoV93MessageRes> repeatreslist = responseList1210(repeatresponseIso);
			int STP_UPI_DRC_NPCI = serviceUpi.ReqRespSTP_UPI_DRC_NPCI(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("ReqRespSTP_UPI_DRC_NPCI " + STP_UPI_DRC_NPCI);
		}
		return "STP_UPI_DRC_NPCI";
	}

	/////////
	public String checkStpUpiNonCbsIw() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			List<STP_UPI_NONCBS_IW> processSTP_UPI_NONCBS_IW = serviceUpi.processSTP_UPI_NONCBS_IW("L4");
			List<IsoV93Message> requestIso = request1200STP_UPI_NONCBS_IW(processSTP_UPI_NONCBS_IW);
			processSTP_UPI_NONCBS_IW = null;
			List<IsoV93MessageRes> responseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : requestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					responseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			requestIso = null;

			count = 1;
			ArrayList<IsoV93MessageRes> reslist = responseList1210(responseIso);
			int ReqRespSTP_UPI_NONCBS_IW = serviceUpi.ReqRespSTP_UPI_NONCBS_IW(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("ReqRespSTP_UPI_DRC_NPCI" + ReqRespSTP_UPI_NONCBS_IW);
			reslist = null;

		}
//

		{
			// enquiry request
			List<STP_UPI_NONCBS_IW> enqSTP_UPI_NONCBS_IW = serviceUpi.processSTP_UPI_NONCBS_IW("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_UPI_NONCBS_IWEnquiry(enqSTP_UPI_NONCBS_IW);
			enqSTP_UPI_NONCBS_IW = null;
			count = 1;
			List<IsoV93MessageRes> enquiryresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : enquiryrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					enquiryresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			enquiryrequestIso = null;
			count = 1;
			ArrayList<IsoV93MessageRes> enquiryreslist = responseList1210(enquiryresponseIso);

			int enqReqRespSTP_UPI_NONCBS_IW = serviceUpi.ReqRespSTP_UPI_NONCBS_IW(enquiryreslist, "Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqReqRespSTP_UPI_NONCBS_IW" + enqReqRespSTP_UPI_NONCBS_IW);
		}
		{
			// repeat request
			List<STP_UPI_NONCBS_IW> reqSTP_UPI_NONCBS_IW = serviceUpi.processSTP_UPI_NONCBS_IW("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_UPI_NONCBS_IWRepeat(reqSTP_UPI_NONCBS_IW);
			reqSTP_UPI_NONCBS_IW = null;
			count = 1;
			List<IsoV93MessageRes> repeatresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : repeatrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					repeatresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			count = 1;
			ArrayList<IsoV93MessageRes> repeatreslist = responseList1210(repeatresponseIso);
			int STP_UPI_NONCBS_IW = serviceUpi.ReqRespSTP_UPI_NONCBS_IW(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("ReqRespSTP_UPI_NONCBS_IW " + STP_UPI_NONCBS_IW);
		}
		return "STP_UPI_NONCBS_IW";
	}

	/////////
	public String checkStpUpiNonCbsOw() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			List<STP_UPI_NONCBS_OW> processSTP_UPI_NONCBS_OW = serviceUpi.processSTP_UPI_NONCBS_OW("L4");
			List<IsoV93Message> requestIso = request1200STP_UPI_NONCBS_OW(processSTP_UPI_NONCBS_OW);
			processSTP_UPI_NONCBS_OW = null;
			List<IsoV93MessageRes> responseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : requestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					responseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			requestIso = null;

			count = 1;
			ArrayList<IsoV93MessageRes> reslist = responseList1210(responseIso);
			int ReqRespSTP_UPI_NONCBS_OW = serviceUpi.ReqRespSTP_UPI_NONCBS_OW(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("ReqRespSTP_UPI_NONCBS_OW" + ReqRespSTP_UPI_NONCBS_OW);
			reslist = null;

		}
//

		{
			// enquiry request
			List<STP_UPI_NONCBS_OW> enqSTP_UPI_NONCBS_OW = serviceUpi.processSTP_UPI_NONCBS_OW("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_UPI_NONCBS_OWEnquiry(enqSTP_UPI_NONCBS_OW);
			enqSTP_UPI_NONCBS_OW = null;
			count = 1;
			List<IsoV93MessageRes> enquiryresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : enquiryrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					enquiryresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			enquiryrequestIso = null;
			count = 1;
			ArrayList<IsoV93MessageRes> enquiryreslist = responseList1210(enquiryresponseIso);

			int enqReqRespSTP_UPI_NONCBS_OW = serviceUpi.ReqRespSTP_UPI_NONCBS_OW(enquiryreslist, "Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqReqRespSTP_UPI_NONCBS_OW" + enqReqRespSTP_UPI_NONCBS_OW);
		}
		{
			// repeat request
			List<STP_UPI_NONCBS_OW> reqSTP_UPI_NONCBS_OW = serviceUpi.processSTP_UPI_NONCBS_OW("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_UPI_NONCBS_OWRepeat(reqSTP_UPI_NONCBS_OW);
			reqSTP_UPI_NONCBS_OW = null;
			count = 1;
			List<IsoV93MessageRes> repeatresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : repeatrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					repeatresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			count = 1;
			ArrayList<IsoV93MessageRes> repeatreslist = responseList1210(repeatresponseIso);
			int STP_UPI_NONCBS_OW = serviceUpi.ReqRespSTP_UPI_NONCBS_OW(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("ReqRespSTP_UPI_NONCBS_OW " + STP_UPI_NONCBS_OW);
		}
		return "STP_UPI_NONCBS_OW";
	}
	//////
}
