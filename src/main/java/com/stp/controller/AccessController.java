package com.stp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stp.model.db1.STP_Access;
import com.stp.service.AccessServ;
import com.stp.service.impl.EntryFetchService;
import com.stp.utility.ResponseBean;

@RestController
@RequestMapping(value = "/api_access")
public class AccessController {
    
    private static final Logger logger = LoggerFactory.getLogger(AccessController.class);

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
        List<STP_Access> accessList = accessService.fetchAccessList();
        return entryFetchService.fetchEntries(accessList, "Access");
    }

    @GetMapping(value = "fetchAccessAll")
    public ResponseBean fetchAccessAll() {
        List<STP_Access> accessList = accessService.fetchAccessListAll();
        return entryFetchService.fetchEntries(accessList, "All Access");
    }

    @PostMapping(value = "saveAccess")
    public ResponseBean saveAccess(@RequestParam(required = false) String json) {
        ResponseBean bean = new ResponseBean();
        if (json != null) {
            try {
                STP_Access accObject = objectMapper.readValue(json, STP_Access.class);
                STP_Access addedAccessDetail = accessService.addAccessDetail(accObject);
                bean.setMessage("Access saved successfully.");
                bean.setStatus("SUCCESS");
                bean.setData(addedAccessDetail);
            } catch (JsonProcessingException e) {
                logger.error("Error in api_access saveAccess mapping ", e);
                bean.setMessage("Invalid JSON format: " + e.getOriginalMessage());
                bean.setStatus("FAILED");
            } catch (Exception e) {
                logger.error("Error in api_access saveAccess mapping ", e);
                bean.setMessage("Error saving access: " + e.getMessage());
                bean.setStatus("FAILED");
            }
        } else {
            bean.setMessage("No JSON data provided.");
            bean.setStatus("FAILED");
        }
        return bean;
    }

    @PostMapping(value = "removeAccess")
    public ResponseBean removeAccess(@RequestParam String appname) {
        ResponseBean bean = new ResponseBean();
        try {
            STP_Access removeAccessDetail = accessService.removeAccessDetail(appname);
            if (removeAccessDetail != null && "N".equalsIgnoreCase(removeAccessDetail.getStatus())) {
                bean.setData(removeAccessDetail);
                bean.setStatus("SUCCESS");
                bean.setMessage("Access entry removed successfully.");
            } else {
                bean.setStatus("FAILED");
                bean.setMessage("Access entry not found or already removed.");
            }
        } catch (Exception e) {
            logger.error("Error in api_access removeAccess mapping ", e);
            bean.setStatus("FAILED");
            bean.setMessage("Error removing access: " + e.getMessage());
        }
        return bean;
    }
}
