package com.stp.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ErrorDetail {

	private LocalDateTime timestamp;
	private String message;
	private String details;

	public ErrorDetail(LocalDateTime timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

}
