package com.stp.service.impl;

import java.util.List;
import static com.stp.utility.GenericCLass.STATUS_FAILED;
import static com.stp.utility.GenericCLass.STATUS_SUCCESS;
import org.springframework.stereotype.Service;

import com.stp.utility.ResponseBean;

@Service
public class EntryFetchService {

	// Shared method to handle fetching entries (for both maker and checker)
	public ResponseBean fetchEntries(List<?> entries, String entryType) {
		ResponseBean bean = new ResponseBean();
		try {
			// If the list is not null and not empty, return success message
			if (entries != null && !entries.isEmpty()) {
				bean.setData(entries);
				bean.setStatus(STATUS_SUCCESS);
				bean.setMessage(String.format("%s entries retrieved successfully.", entryType));
			} else {
				// If the list is empty or null, return failure message
				bean.setStatus(STATUS_FAILED);
				bean.setMessage(String.format("No %s entries found.", entryType));
			}
		} catch (Exception e) {
			// Catch and log the exception, returning failure status
			bean.setStatus(STATUS_FAILED);
			bean.setMessage(String.format("Error fetching %s entries: %s", entryType, e.getMessage()));
		}
		return bean;
	}

}
