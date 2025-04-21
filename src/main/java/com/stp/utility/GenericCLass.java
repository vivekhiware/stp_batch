package com.stp.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericCLass {
	private static final String DATE_FROMAT = "dd-MMM-yyyy";
	public static final String STATUS_SUCCESS = "SUCCESS";
	public static final String STATUS_FAILED = "FAILED";

	private static final Logger logger = LoggerFactory.getLogger(GenericCLass.class);

	public static final String HOST = "10.192.3.82"; // Remote host (e.g., ATM, POS)
	public static final int PORT = 27000; // Port for the communication

	private GenericCLass() {
		throw new UnsupportedOperationException("Utility class — cannot be instantiated");
	}

	public static java.util.Date convertStringToSqlDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FROMAT); // Format like "20-Jun-2023"
		try {
			return sdf.parse(dateString); // Convert to java.sql.Date
		} catch (ParseException e) {
			logger.error("Exception occurred: " + e.getMessage(), e);
		}
		return null; // Return null if parsing fails
	}

	public static String returnString(Object o) {
		if (o == null) {
			return "null";
		} else if (o instanceof String) {
			return (String) o;
		} else if (o instanceof Integer) {
			return String.valueOf(o);
		} else if (o instanceof Double) {
			return String.format("%.2f", o);
		} else {
			return o.toString();
		}
	}

	public static List<String> notFailedTransaction() {
		ArrayList<String> failedtxn = new ArrayList<>();
		failedtxn.add("000");
		failedtxn.add("NULL");
		failedtxn.add("902");
		failedtxn.add("904");
		failedtxn.add("906");
		failedtxn.add("907");
		failedtxn.add("909");
		failedtxn.add("911");
		failedtxn.add("201");
		failedtxn.add("202");
		failedtxn.add("203");
		failedtxn.add("204");
		failedtxn.add("205");
		failedtxn.add("207");
		failedtxn.add("301");
		failedtxn.add("302");
		failedtxn.add("303");
		failedtxn.add("304");
		failedtxn.add("305");
		failedtxn.add("306");
		failedtxn.add("307");
		failedtxn.add("114");

		return failedtxn;
	}

	public static List<String> creditTransaction() {
		ArrayList<String> failedtxn = new ArrayList<>();
		failedtxn.add("201");
		failedtxn.add("202");
		failedtxn.add("203");
		failedtxn.add("204");
		failedtxn.add("205");
		failedtxn.add("206");
		failedtxn.add("207");

		return failedtxn;
	}

	public static List<String> debitTransaction() {
		ArrayList<String> failedtxn = new ArrayList<>();

		failedtxn.add("301");
		failedtxn.add("302");
		failedtxn.add("303");
		failedtxn.add("304");
		failedtxn.add("305");
		failedtxn.add("306");
		failedtxn.add("307");

		return failedtxn;
	}

	public static List<String> repostTransaction() {
		ArrayList<String> failedtxn = new ArrayList<>();

		failedtxn.add("902");
		failedtxn.add("904");
		failedtxn.add("906");
		failedtxn.add("907");
		failedtxn.add("909");
		failedtxn.add("911");

		return failedtxn;
	}

	public static List<String> getStatusList(String status) {
		List<String> statuses = null;
		if (status.equalsIgnoreCase("R")) {
			statuses = Arrays.asList("R");
		} else if (status.equalsIgnoreCase("L0") || status.equalsIgnoreCase("L00")) {
			statuses = Arrays.asList("L0", "R0");
		} else if (status.equalsIgnoreCase("L1")) {
			statuses = Arrays.asList("L1", "R1");
		} else if (status.equalsIgnoreCase("L2")) {
			statuses = Arrays.asList("L2", "R2");
		} else if (status.equalsIgnoreCase("L3")) {
			statuses = Arrays.asList("L3", "R3");
		} else if (status.equalsIgnoreCase("L4")) {
			statuses = Arrays.asList("L4");
		} else if (status.equalsIgnoreCase("L5")) {
			statuses = Arrays.asList("L5");
		} else if (status.equalsIgnoreCase("L6")) {
			statuses = Arrays.asList("L6");
		} else if (status.equalsIgnoreCase("L7")) {
			statuses = Arrays.asList("L7");
		} else if (status.equalsIgnoreCase("L8")) {
			statuses = Arrays.asList("L8");
		} else if (status.equalsIgnoreCase("L9")) {
			statuses = Arrays.asList("L9");
		} else if (status.equalsIgnoreCase("A1")) {
			statuses = Arrays.asList("L10", "L5", "L6");
		}
		logger.info("statuses: {}", statuses);

		return statuses;
	}

	public static ResponseBean createFailedResponse(String message, Exception e) {
		if (e != null) {
			logger.error("Error: {}", message, e);
		} else {
			logger.warn("Warning: {}", message);
		}
		ResponseBean bean = new ResponseBean();
		bean.setStatus(STATUS_FAILED);
		bean.setMessage(e != null ? message + ": " + e.getMessage() : message);
		return bean;
	}

}
