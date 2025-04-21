package com.stp.controller;

import static com.stp.utility.GenericCLass.STATUS_SUCCESS;
import static com.stp.utility.GenericCLass.createFailedResponse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stp.model.db1.StpAccess;
import com.stp.service.AccessServ;
import com.stp.service.impl.EntryFetchService;
import com.stp.utility.ResponseBean;

@RestController
@RequestMapping(value = "/api_access")
public class AccessController {

	private final AccessServ accessService;
	private final ObjectMapper objectMapper;
	private final EntryFetchService entryFetchService;

	@Autowired
	public AccessController(AccessServ accessService, ObjectMapper objectMapper, EntryFetchService entryFetchService) {
		this.accessService = accessService;
		this.objectMapper = objectMapper;
		this.entryFetchService = entryFetchService;
	}

	@GetMapping(value = "fetchAccess")
	public ResponseBean fetchAccess() {
		List<StpAccess> accessList = accessService.fetchAccessList();
		return entryFetchService.fetchEntries(accessList, "Access");
	}

	@GetMapping(value = "fetchAccessAll")
	public ResponseBean fetchAccessAll() {
		List<StpAccess> accessList = accessService.fetchAccessListAll();
		return entryFetchService.fetchEntries(accessList, "All Access");
	}

	@PostMapping(value = "saveAccess")
	public ResponseBean saveAccess(@RequestParam(required = false) String json) {
		if (json == null) {
			return createFailedResponse("No JSON data provided.", null);
		}
		try {
			StpAccess accObject = objectMapper.readValue(json, StpAccess.class);
			StpAccess addedAccessDetail = accessService.addAccessDetail(accObject);

			ResponseBean bean = new ResponseBean();
			bean.setMessage("Access saved successfully.");
			bean.setStatus(STATUS_SUCCESS);
			bean.setData(addedAccessDetail);
			return bean;
		} catch (JsonProcessingException e) {
			return createFailedResponse("Invalid JSON format", e);
		} catch (Exception e) {
			return createFailedResponse("Error saving access", e);
		}
	}

	@PostMapping(value = "removeAccess")
	public ResponseBean removeAccess(@RequestParam String appname) {
		try {
			StpAccess removedAccess = accessService.removeAccessDetail(appname);
			if (removedAccess != null && "N".equalsIgnoreCase(removedAccess.getStatus())) {
				ResponseBean bean = new ResponseBean();
				bean.setData(removedAccess);
				bean.setStatus(STATUS_SUCCESS);
				bean.setMessage("Access entry removed successfully.");
				return bean;
			} else {
				return createFailedResponse("Access entry not found or already removed.", null);
			}
		} catch (Exception e) {
			return createFailedResponse("Error removing access", e);
		}
	}

}
