package com.stp.controller;

import static com.stp.utility.GenericCLass.HOST;
import static com.stp.utility.GenericCLass.PORT;
import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_OW_NETWORK_DECLINEEnquiry;
import static com.stp.autojob.IsoFormatter.reques1200STP_IMPS_NONCBS_IWEnquiry;
import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_IW_NETWORK_DECLINE;
import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_IW_NETWORK_DECLINEEnquiry;
import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_NONCBS_IW;
import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_NONNPCI_IW;
import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_NONNPCI_OW;
import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_TCC_DATA_IW;
import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_TCC_DATA_IWEnquiry;
import static com.stp.autojob.IsoFormatter.request1201STP_IMPS_IW_NETWORK_DECLINERepeat;
import static com.stp.autojob.IsoFormatter.request1201STP_IMPS_NONNPCI_IWRepeat;
import static com.stp.autojob.IsoFormatter.request1201STP_IMPS_NONNPCI_OWRepeat;
import static com.stp.autojob.IsoFormatter.request1201STP_IMPS_OW_NETWORK_DECLINERepeat;
import static com.stp.autojob.IsoFormatter.request1201STP_IMPS_TCC_DATA_IWRepeat;
import static com.stp.autojob.IsoFormatter.request201STP_IMPS_NONCBS_IWRepeat;
import static com.stp.autojob.IsoFormatter.responseList1210;
import static com.stp.autojob.IsoFormatter.responsePrint;
import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_OW_NETWORK_DECLINE;

import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_NTSL_NETSET_TTUM;
import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_NTSL_NETSET_TTUMEnquiry;
import static com.stp.autojob.IsoFormatter.request1201STP_IMPS_NTSL_NETSET_TTUMRepeat;

import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_NONCBS_OW;
import static com.stp.autojob.IsoFormatter.request1201STP_IMPS_NONCBS_OWRepeat;
import static com.stp.autojob.IsoFormatter.request1200STP_IMPS_NONCBS_OWEnquiry;

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
import com.stp.model.db1.STP_IMPS_IW_NETWORK_DECLINE;
import com.stp.model.db1.STP_IMPS_NONCBS_IW;
import com.stp.model.db1.STP_IMPS_NONCBS_OW;
import com.stp.model.db1.STP_IMPS_NONNPCI_IW;
import com.stp.model.db1.STP_IMPS_NONNPCI_OW;
import com.stp.model.db1.STP_IMPS_NTSL_NETSET_TTUM;
import com.stp.model.db1.STP_IMPS_OW_NETWORK_DECLINE;
import com.stp.model.db1.STP_IMPS_TCC_DATA_IW;
import com.stp.service.ServiceImps;
import com.stp.service.impl.EntryFetchService;
import com.stp.utility.ResponseBean;
import com.stp.utility.TTumRequest;
import com.stp.utility.TypeEnum;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
@RequestMapping(value = "/api_impslist")
public class IMPSController {

	private static final Logger logger = LoggerFactory.getLogger(IMPSController.class);

	private final ServiceImps serviceImps;
	private final ObjectMapper objectMapper;
	private final EntryFetchService entryFetchService;
	private final TaskExecutor taskExecutor;

	@Autowired
	public IMPSController(ServiceImps serviceImps, ObjectMapper objectMapper, EntryFetchService entryFetchService,
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
//	@RateLimiter(name = "QueryRateLimiter", fallbackMethod = "handleFallback")
//	@CircuitBreaker(name = "QueryCircuitBreaker", fallbackMethod = "handleFallback")
//	@TimeLimiter(name = "QueryTimer", fallbackMethod = "handleFallback")
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
			String checkTccTTum = CheckNonCBSIWTTum();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 2) {
			String checkTccTTum = CheckNonNpciIwTTum();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 3) {
			String checkTccTTum = checkNonNpciOwTtum();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 4) {
			String checkTccTTum = checkTccTTum();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 5) {
			String checkTccTTum = checkIwNetworkDeclineTtum();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 6) {
			String checkTccTTum = checkOwNetworkDeclineTtum();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 7) {
			String checkTccTTum = checkIwNonCbs();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		} else if (queryid == 8) {
			String checkTccTTum = checkstpimpsNetsetTtum();
			response = entryFetchService.fetchEntrieTTUM(Arrays.asList(checkTccTTum), data.getDescription());
		}
		return CompletableFuture.completedFuture(response);
	}

	public String checkTccTTum() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		{
//		 fresh  request 
			List<STP_IMPS_TCC_DATA_IW> processSTP_IMPS_TCC_DATA_IW = serviceImps.processSTP_IMPS_TCC_DATA_IW("L4");

			List<IsoV93Message> request1200stp_IMPS_TCC_DATA_IW2 = request1200STP_IMPS_TCC_DATA_IW(
					processSTP_IMPS_TCC_DATA_IW);
			processSTP_IMPS_TCC_DATA_IW = null;

			List<IsoV93MessageRes> responseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : request1200stp_IMPS_TCC_DATA_IW2) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("Fresh");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					responseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			request1200stp_IMPS_TCC_DATA_IW2 = null;
			count = 1;
			ArrayList<IsoV93MessageRes> reslist = responseList1210(responseIso);
			int reqRespSTP_IMPS_TCC_DATA_IW = serviceImps.ReqRespSTP_IMPS_TCC_DATA_IW(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("reqRespSTP_IMPS_TCC_DATA_IW:::" + reqRespSTP_IMPS_TCC_DATA_IW);
			reslist = null;

		}
		// enquiry request
		{
			List<STP_IMPS_TCC_DATA_IW> enquiryProcessSTP_IMPS_TCC_DATA_IW2 = serviceImps
					.processSTP_IMPS_TCC_DATA_IW("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_IMPS_TCC_DATA_IWEnquiry(
					enquiryProcessSTP_IMPS_TCC_DATA_IW2);
			enquiryProcessSTP_IMPS_TCC_DATA_IW2 = null;
			count = 1;
			List<IsoV93MessageRes> enquiryresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : enquiryrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("Enquiry");
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
			int reqRespSTP_IMPS_TCC_DATA_IW2 = serviceImps.ReqRespSTP_IMPS_TCC_DATA_IW(enquiryreslist, "Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			System.out.println("reqRespSTP_IMPS_TCC_DATA_IW2:::" + reqRespSTP_IMPS_TCC_DATA_IW2);

			enquiryreslist = null;
			// account enquiry
		}
		//
		{
			// repeat request
			List<STP_IMPS_TCC_DATA_IW> repeatprocessIsoSTP_IMPS_NONCBS_IW = serviceImps
					.processSTP_IMPS_TCC_DATA_IW("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_IMPS_TCC_DATA_IWRepeat(
					repeatprocessIsoSTP_IMPS_NONCBS_IW);
			repeatprocessIsoSTP_IMPS_NONCBS_IW = null;
			count = 1;
			List<IsoV93MessageRes> repeatresponseIso = new ArrayList<IsoV93MessageRes>();
			for (IsoV93Message process : repeatrequestIso) {
				try {
					byte[] msgBytes = process.generateMessage();
					process.printMessage("Repeat");
					byte[] responseInByte = serviceNetwork.networkTransportByte(msgBytes);
					IsoV93MessageRes response = new IsoV93MessageRes(responseInByte);
					repeatresponseIso.add(response);
					process = null;
					count++;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			repeatrequestIso = null;
			count = 1;
			ArrayList<IsoV93MessageRes> repeatreslist = responseList1210(repeatresponseIso);
			int reqRespSTP_IMPS_TCC_DATA_IW2 = serviceImps.ReqRespSTP_IMPS_TCC_DATA_IW(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("reqRespSTP_IMPS_TCC_DATA_IW2   " + reqRespSTP_IMPS_TCC_DATA_IW2);
		}
		//
		return " tcc ttum Process ";

	}

	public String CheckNonCBSIWTTum() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		{
			List<STP_IMPS_NONCBS_IW> processIsoSTP_IMPS_NONCBS_IW = serviceImps.processIsoSTP_IMPS_NONCBS_IW("L4");
			List<IsoV93Message> requestIso = request1200STP_IMPS_NONCBS_IW(processIsoSTP_IMPS_NONCBS_IW);
			processIsoSTP_IMPS_NONCBS_IW = null;
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
			int freshReqRespSTP_IMPS_NONCBS_IW = serviceImps.ReqRespSTP_IMPS_NONCBS_IW(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("freshReqRespSTP_IMPS_NONCBS_IW" + freshReqRespSTP_IMPS_NONCBS_IW);
			reslist = null;
		}

		{
			// enquiry request
			List<STP_IMPS_NONCBS_IW> enquiryprocessIsoSTP_IMPS_NONCBS_IW = serviceImps
					.processIsoSTP_IMPS_NONCBS_IW("L5");
			List<IsoV93Message> enquiryrequestIso = reques1200STP_IMPS_NONCBS_IWEnquiry(
					enquiryprocessIsoSTP_IMPS_NONCBS_IW);
			enquiryprocessIsoSTP_IMPS_NONCBS_IW = null;
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

			int enquiryReqRespSTP_IMPS_NONCBS_IW = serviceImps.ReqRespSTP_IMPS_NONCBS_IW(enquiryreslist, "Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enquiryReqRespSTP_IMPS_NONCBS_IW" + enquiryReqRespSTP_IMPS_NONCBS_IW);
		}
		{
			// repeat request

			List<STP_IMPS_NONCBS_IW> repeatprocessIsoSTP_IMPS_NONCBS_IW = serviceImps
					.processIsoSTP_IMPS_NONCBS_IW("L6");
			List<IsoV93Message> repeatrequestIso = request201STP_IMPS_NONCBS_IWRepeat(
					repeatprocessIsoSTP_IMPS_NONCBS_IW);
			repeatprocessIsoSTP_IMPS_NONCBS_IW = null;
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
			int repeatReqRespSTP_IMPS_NONCBS_IW = serviceImps.ReqRespSTP_IMPS_NONCBS_IW(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("repeatReqRespSTP_IMPS_NONCBS_IW   " + repeatReqRespSTP_IMPS_NONCBS_IW);
		}
		return "IMPS_NONCBS_IW";
	}

	public String CheckNonNpciIwTTum() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		{
			List<STP_IMPS_NONNPCI_IW> processSTP_IMPS_NONNPCI_IW = serviceImps.processSTP_IMPS_NONNPCI_IW("L4");
			List<IsoV93Message> requestIso = request1200STP_IMPS_NONNPCI_IW(processSTP_IMPS_NONNPCI_IW);
			processSTP_IMPS_NONNPCI_IW = null;
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
			int reqRespSTP_IMPS_NONNPCI_IW = serviceImps.ReqRespSTP_IMPS_NONNPCI_IW(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("reqRespSTP_IMPS_NONNPCI_IW" + reqRespSTP_IMPS_NONNPCI_IW);
			reslist = null;
		}

		{
			// enquiry request
			List<STP_IMPS_NONNPCI_IW> enqSTP_IMPS_NONNPCI_IW = serviceImps.processSTP_IMPS_NONNPCI_IW("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_IMPS_NONNPCI_IW(enqSTP_IMPS_NONNPCI_IW);
			enqSTP_IMPS_NONNPCI_IW = null;
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

			int enqRespSTP_IMPS_NONNPCI_IW = serviceImps.ReqRespSTP_IMPS_NONNPCI_IW(enquiryreslist, "Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqRespSTP_IMPS_NONNPCI_IW" + enqRespSTP_IMPS_NONNPCI_IW);
		}
		{
			// repeat request

			List<STP_IMPS_NONNPCI_IW> reqprocessSTP_IMPS_NONNPCI_IW = serviceImps.processSTP_IMPS_NONNPCI_IW("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_IMPS_NONNPCI_IWRepeat(reqprocessSTP_IMPS_NONNPCI_IW);
			reqprocessSTP_IMPS_NONNPCI_IW = null;
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
			int repeatReqRespSTP_IMPS_NONNPCI_IW = serviceImps.ReqRespSTP_IMPS_NONNPCI_IW(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("repeatReqRespSTP_IMPS_NONNPCI_IW   " + repeatReqRespSTP_IMPS_NONNPCI_IW);
		}
		return "IMPS_NONNPCI_IW";
	}

	public String checkNonNpciOwTtum() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		{
			List<STP_IMPS_NONNPCI_OW> processSTP_IMPS_NONNPCI_OW = serviceImps.processSTP_IMPS_NONNPCI_OW("L4");
			List<IsoV93Message> requestIso = request1200STP_IMPS_NONNPCI_OW(processSTP_IMPS_NONNPCI_OW);
			processSTP_IMPS_NONNPCI_OW = null;
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
			int reqRespSTP_IMPS_NONNPCI_OW = serviceImps.ReqRespSTP_IMPS_NONNPCI_OW(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("reqRespSTP_IMPS_NONNPCI_OW" + reqRespSTP_IMPS_NONNPCI_OW);
			reslist = null;
		}

		{
			// enquiry request
			List<STP_IMPS_NONNPCI_OW> enqSTP_IMPS_NONNPCI_OW = serviceImps.processSTP_IMPS_NONNPCI_OW("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_IMPS_NONNPCI_OW(enqSTP_IMPS_NONNPCI_OW);
			enqSTP_IMPS_NONNPCI_OW = null;
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

			int enqRespSTP_IMPS_NONNPCI_OW = serviceImps.ReqRespSTP_IMPS_NONNPCI_OW(enquiryreslist, "Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqRespSTP_IMPS_NONNPCI_OW" + enqRespSTP_IMPS_NONNPCI_OW);
		}
		{
			// repeat request

			List<STP_IMPS_NONNPCI_OW> reqprocessSTP_IMPS_NONNPCI_OW = serviceImps.processSTP_IMPS_NONNPCI_OW("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_IMPS_NONNPCI_OWRepeat(reqprocessSTP_IMPS_NONNPCI_OW);
			reqprocessSTP_IMPS_NONNPCI_OW = null;
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
			int repeatReqRespSTP_IMPS_NONNPCI_OW = serviceImps.ReqRespSTP_IMPS_NONNPCI_OW(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("repeatReqRespSTP_IMPS_NONNPCI_IW   " + repeatReqRespSTP_IMPS_NONNPCI_OW);
		}
		return "NON NPCI OW TTUM";
	}

	public String checkIwNetworkDeclineTtum() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			List<STP_IMPS_IW_NETWORK_DECLINE> processSTP_IMPS_IW_NETWORK_DECLINE = serviceImps
					.processSTP_IMPS_IW_NETWORK_DECLINE("L4");
			List<IsoV93Message> requestIso = request1200STP_IMPS_IW_NETWORK_DECLINE(processSTP_IMPS_IW_NETWORK_DECLINE);
			processSTP_IMPS_IW_NETWORK_DECLINE = null;
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
			int ReqRespSTP_IMPS_IW_NETWORK_DECLINE = serviceImps.ReqRespSTP_IMPS_IW_NETWORK_DECLINE(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("ReqRespSTP_IMPS_IW_NETWORK_DECLINE" + ReqRespSTP_IMPS_IW_NETWORK_DECLINE);
			reslist = null;

		}
//

		{
			// enquiry request
			List<STP_IMPS_IW_NETWORK_DECLINE> enqSTP_IMPS_IW_NETWORK_DECLINE = serviceImps
					.processSTP_IMPS_IW_NETWORK_DECLINE("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_IMPS_IW_NETWORK_DECLINEEnquiry(
					enqSTP_IMPS_IW_NETWORK_DECLINE);
			enqSTP_IMPS_IW_NETWORK_DECLINE = null;
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

			int enqReqRespSTP_IMPS_IW_NETWORK_DECLINE = serviceImps.ReqRespSTP_IMPS_IW_NETWORK_DECLINE(enquiryreslist,
					"Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqReqRespSTP_IMPS_IW_NETWORK_DECLINE" + enqReqRespSTP_IMPS_IW_NETWORK_DECLINE);
		}
		{
			// repeat request

			List<STP_IMPS_IW_NETWORK_DECLINE> reqprocessSTP_IMPS_IW_NETWORK_DECLINE = serviceImps
					.processSTP_IMPS_IW_NETWORK_DECLINE("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_IMPS_IW_NETWORK_DECLINERepeat(
					reqprocessSTP_IMPS_IW_NETWORK_DECLINE);
			reqprocessSTP_IMPS_IW_NETWORK_DECLINE = null;
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
			int repeatReqRespSTP_IMPS_IW_NETWORK_DECLINE = serviceImps.ReqRespSTP_IMPS_IW_NETWORK_DECLINE(repeatreslist,
					"Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out
					.println("repeatReqRespSTP_IMPS_IW_NETWORK_DECLINE   " + repeatReqRespSTP_IMPS_IW_NETWORK_DECLINE);
		}
		//
		return "NETWORK DECLINE INWARD";
	}

	public String checkOwNetworkDeclineTtum() {
		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			List<STP_IMPS_OW_NETWORK_DECLINE> processSTP_IMPS_OW_NETWORK_DECLINE = serviceImps
					.processSTP_IMPS_OW_NETWORK_DECLINE("L4");
			List<IsoV93Message> requestIso = request1200STP_IMPS_OW_NETWORK_DECLINE(processSTP_IMPS_OW_NETWORK_DECLINE);
			processSTP_IMPS_OW_NETWORK_DECLINE = null;
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
			int ReqRespSTP_IMPS_OW_NETWORK_DECLINE = serviceImps.ReqRespSTP_IMPS_OW_NETWORK_DECLINE(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("ReqRespSTP_IMPS_IW_NETWORK_DECLINE" + ReqRespSTP_IMPS_OW_NETWORK_DECLINE);
			reslist = null;

		}
//

		{
			// enquiry request
			List<STP_IMPS_OW_NETWORK_DECLINE> enqSTP_IMPS_OW_NETWORK_DECLINE = serviceImps
					.processSTP_IMPS_OW_NETWORK_DECLINE("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_IMPS_OW_NETWORK_DECLINEEnquiry(
					enqSTP_IMPS_OW_NETWORK_DECLINE);
			enqSTP_IMPS_OW_NETWORK_DECLINE = null;
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

			int enqReqRespSTP_IMPS_OW_NETWORK_DECLINE = serviceImps.ReqRespSTP_IMPS_OW_NETWORK_DECLINE(enquiryreslist,
					"Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqReqRespSTP_IMPS_OW_NETWORK_DECLINE" + enqReqRespSTP_IMPS_OW_NETWORK_DECLINE);
		}
		{
			// repeat request

			List<STP_IMPS_OW_NETWORK_DECLINE> reqprocessSTP_IMPS_IW_NETWORK_DECLINE = serviceImps
					.processSTP_IMPS_OW_NETWORK_DECLINE("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_IMPS_OW_NETWORK_DECLINERepeat(
					reqprocessSTP_IMPS_IW_NETWORK_DECLINE);
			reqprocessSTP_IMPS_IW_NETWORK_DECLINE = null;
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
			int ReqRespSTP_IMPS_OW_NETWORK_DECLINE = serviceImps.ReqRespSTP_IMPS_OW_NETWORK_DECLINE(repeatreslist,
					"Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("ReqRespSTP_IMPS_OW_NETWORK_DECLINE   " + ReqRespSTP_IMPS_OW_NETWORK_DECLINE);
		}
		//
		return "NETWORK DECLINE OUTWARD";
	}

	public String checkstpimpsNetsetTtum() {

		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			List<STP_IMPS_NTSL_NETSET_TTUM> processSTP_IMPS_NTSL_NETSET_TTUM = serviceImps
					.processSTP_IMPS_NTSL_NETSET_TTUM("L4");
			List<IsoV93Message> requestIso = request1200STP_IMPS_NTSL_NETSET_TTUM(processSTP_IMPS_NTSL_NETSET_TTUM);
			processSTP_IMPS_NTSL_NETSET_TTUM = null;
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
			int ReqRespSTP_IMPS_NTSL_NETSET_TTUM = serviceImps.ReqRespSTP_IMPS_NTSL_NETSET_TTUM(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("ReqRespSTP_IMPS_NTSL_NETSET_TTUM" + ReqRespSTP_IMPS_NTSL_NETSET_TTUM);
			reslist = null;

		}
//

		{
			// enquiry request
			List<STP_IMPS_NTSL_NETSET_TTUM> enqSTP_IMPS_NTSL_NETSET_TTUM = serviceImps
					.processSTP_IMPS_NTSL_NETSET_TTUM("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_IMPS_NTSL_NETSET_TTUMEnquiry(
					enqSTP_IMPS_NTSL_NETSET_TTUM);
			enqSTP_IMPS_NTSL_NETSET_TTUM = null;
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

			int enqReqRespSTP_IMPS_NTSL_NETSET_TTUM = serviceImps.ReqRespSTP_IMPS_NTSL_NETSET_TTUM(enquiryreslist,
					"Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqReqRespSTP_IMPS_OW_NETWORK_DECLINE" + enqReqRespSTP_IMPS_NTSL_NETSET_TTUM);
		}
		{
			// repeat request

			List<STP_IMPS_NTSL_NETSET_TTUM> reqSTP_IMPS_NTSL_NETSET_TTUM = serviceImps
					.processSTP_IMPS_NTSL_NETSET_TTUM("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_IMPS_NTSL_NETSET_TTUMRepeat(
					reqSTP_IMPS_NTSL_NETSET_TTUM);
			reqSTP_IMPS_NTSL_NETSET_TTUM = null;
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
			int ReqRespSTP_IMPS_NTSL_NETSET_TTUM = serviceImps.ReqRespSTP_IMPS_NTSL_NETSET_TTUM(repeatreslist,
					"Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("ReqRespSTP_IMPS_NTSL_NETSET_TTUM   " + ReqRespSTP_IMPS_NTSL_NETSET_TTUM);
		}
		//
		return "NETSET TTUM";

	}

	public String checkIwNonCbs() {

		int count = 1;
		ISOCommunicationExample serviceNetwork = null;
		try {
			serviceNetwork = new ISOCommunicationExample(HOST, PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			List<STP_IMPS_NONCBS_OW> processSTP_IMPS_NONCBS_OW = serviceImps.processSTP_IMPS_NONCBS_OW("L4");
			List<IsoV93Message> requestIso = request1200STP_IMPS_NONCBS_OW(processSTP_IMPS_NONCBS_OW);
			processSTP_IMPS_NONCBS_OW = null;
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
			int ReqRespSTP_IMPS_NONCBS_OW = serviceImps.ReqRespSTP_IMPS_NONCBS_OW(reslist, "Fresh");
			responsePrint(reslist, "Fresh");
			System.out.println("ReqRespSTP_IMPS_NONCBS_OW" + ReqRespSTP_IMPS_NONCBS_OW);
			reslist = null;

		}
//

		{
			// enquiry request
			List<STP_IMPS_NONCBS_OW> enqSTP_IMPS_NONCBS_OW = serviceImps.processSTP_IMPS_NONCBS_OW("L5");
			List<IsoV93Message> enquiryrequestIso = request1200STP_IMPS_NONCBS_OWEnquiry(enqSTP_IMPS_NONCBS_OW);
			enqSTP_IMPS_NONCBS_OW = null;
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

			int enqReqRespSTP_IMPS_NONCBS_OW = serviceImps.ReqRespSTP_IMPS_NONCBS_OW(enquiryreslist, "Enquiry");
			responsePrint(enquiryreslist, "Enquiry");
			enquiryreslist = null;
			System.out.println("enqReqRespSTP_IMPS_NONCBS_OW" + enqReqRespSTP_IMPS_NONCBS_OW);
		}
		{
			// repeat request
			List<STP_IMPS_NONCBS_OW> reqSTP_IMPS_NONCBS_OW = serviceImps.processSTP_IMPS_NONCBS_OW("L6");
			List<IsoV93Message> repeatrequestIso = request1201STP_IMPS_NONCBS_OWRepeat(reqSTP_IMPS_NONCBS_OW);
			reqSTP_IMPS_NONCBS_OW = null;
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
			int STP_IMPS_NONCBS_OW = serviceImps.ReqRespSTP_IMPS_NONCBS_OW(repeatreslist, "Repeat");
			responsePrint(repeatreslist, "Repeat");
			repeatreslist = null;
			System.out.println("STP_IMPS_NONCBS_OW   " + STP_IMPS_NONCBS_OW);
		}
		return "STP_IMPS_NTSL_NETSET_TTUM";
	}
}