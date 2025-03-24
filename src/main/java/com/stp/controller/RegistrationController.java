package com.stp.controller;

import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stp.model.db1.STP_Login;
import com.stp.service.RegistrationServ;
import com.stp.service.impl.EntryFetchService;
import com.stp.utility.ResponseBean;

@RestController
@RequestMapping(value = "/api_reg")
public class RegistrationController {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
	private final RegistrationServ registrationServ;
	private final ObjectMapper objectMapper;
	private final EntryFetchService entryFetchService; // Add EntryFetchService

	@Autowired
	public RegistrationController(RegistrationServ registrationServ, ObjectMapper objectMapper,
			EntryFetchService entryFetchService) {
		this.registrationServ = registrationServ;
		this.objectMapper = objectMapper;
		this.entryFetchService = entryFetchService; // Initialize EntryFetchService
	}

	private ResponseBean createErrorResponse(String message, String status) {
		ResponseBean response = new ResponseBean();
		response.setStatus(status);
		response.setMessage(message);
		return response;
	}

	private ResponseBean createSuccessResponse(String message, Object data) {
		ResponseBean response = new ResponseBean();
		response.setStatus("SUCCESS");
		response.setMessage(message);
		response.setData(data);
		return response;
	}

	@PostMapping(value = "udpateAccess")
	public ResponseBean updateAccess(@RequestBody String json) {
		logger.info("Received  Update JSON: " + json); // Log the received JSON
		if (json == null || json.trim().isEmpty()) {
			return createErrorResponse("No JSON data provided.", "FAILED");
		}
		try {
			// Deserialize the JSON string into an STP_Login object
			STP_Login accObject = objectMapper.readValue(json, STP_Login.class);
			// Process the access detail
			STP_Login addAccessDetail = registrationServ.updateAccess(accObject);
			if (addAccessDetail != null) {
				return createSuccessResponse("Access Update successfully.", addAccessDetail);
			} else {
				return createErrorResponse("Error Update access ", "FAILED");
			}
		} catch (JsonProcessingException e) {
			// Handle invalid JSON format
			return createErrorResponse("Invalid JSON format: " + e.getOriginalMessage(), "FAILED");
		} catch (Exception e) {
			// Log any other unexpected errors and return a generic error response
			logger.error("Error in api_reg Update mapping", e);
			return createErrorResponse("Error saving access: " + e.getMessage(), "FAILED");
		}

	}

	@PostMapping(value = "saveAccess")
	public ResponseBean saveAccess(@RequestBody String json) {
		logger.info("Received JSON: " + json); // Log the received JSON
		if (json == null || json.trim().isEmpty()) {
			return createErrorResponse("No JSON data provided.", "FAILED");
		}
		try {
			// Deserialize the JSON string into an STP_Login object
			STP_Login accObject = objectMapper.readValue(json, STP_Login.class);
			// Process the access detail
			STP_Login fetchAccessList = registrationServ.fetchAccessList(accObject.getEmplycd());
			if (fetchAccessList != null) {
				return createErrorResponse("Already Registered For Access.", "FAILED");
			} else {
				STP_Login addAccessDetail = registrationServ.addAccessDetail(accObject);
				return createSuccessResponse("Access saved successfully.", addAccessDetail);
			}
		} catch (JsonProcessingException e) {
			// Handle invalid JSON format
			return createErrorResponse("Invalid JSON format: " + e.getOriginalMessage(), "FAILED");
		} catch (Exception e) {
			// Log any other unexpected errors and return a generic error response
			logger.error("Error in api_reg saveAccess mapping", e);
			return createErrorResponse("Error saving access: " + e.getMessage(), "FAILED");
		}
	}

	@PostMapping(value = "fetchAccess")
	public ResponseBean fetchAccess(@RequestParam(required = false) String emplycd) {
		// Use EntryFetchService to fetch the entry and handle success/failure
		return entryFetchService.fetchEntries(
				Optional.ofNullable(registrationServ.fetchAccessList(Integer.parseInt(emplycd)))
						.map(Collections::singletonList).orElse(null), // Return null if no entry found
				"Application access entry");
	}

	@PostMapping(value = "fetchAccessAll")
	public ResponseBean fetchAccessAll() {
		// Use EntryFetchService to fetch all entries and handle success/failure
		return entryFetchService.fetchEntries(registrationServ.fetchAccessListAll(), "All application access entries");
	}

}
