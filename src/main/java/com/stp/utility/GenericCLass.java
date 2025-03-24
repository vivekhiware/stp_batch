package com.stp.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class GenericCLass {

	public static final String HOST = "10.192.3.82"; // Remote host (e.g., ATM, POS)
	public static final int PORT = 27000; // Port for the communication

	public static void main(String[] args) {
		GenericCLass test = new GenericCLass();
//		  format req YYMMDDHHMMSS INPUT FROM  " 2025-02-12"
//		 format req MMDD INPUT FROM  " 2025-02-12"
//		format req YYMMDD INPUT FROM  " 2025-02-12"

	}

	public static Date convertStringToDate(String dateString) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy"); // Define your date format
		try {
			return format.parse(dateString); // Convert the string to a Date object
		} catch (Exception e) {
			e.printStackTrace();
			return null; // Return null if parsing fails
		}
	}

	public static LocalDate convertStringToLocalDate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy"); // Define your date format
		try {
			return LocalDate.parse(dateString, formatter); // Convert the string to a LocalDate object
		} catch (Exception e) {
			e.printStackTrace();
			return null; // Return null if parsing fails
		}
	}

	public static java.util.Date convertStringToSqlDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); // Format like "20-Jun-2023"
		try {
			java.util.Date parsedDate = sdf.parse(dateString); // Convert to java.util.Date
			return parsedDate; // Convert to java.sql.Date
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null; // Return null if parsing fails
	}

	public static ArrayList<String> notFailedTransaction() {
		ArrayList<String> failedtxn = new ArrayList<String>();
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

	public static ArrayList<String> creditTransaction() {
		ArrayList<String> failedtxn = new ArrayList<String>();
		failedtxn.add("201");
		failedtxn.add("202");
		failedtxn.add("203");
		failedtxn.add("204");
		failedtxn.add("205");
		failedtxn.add("206");
		failedtxn.add("207");

		return failedtxn;
	}

	public static ArrayList<String> debitTransaction() {
		ArrayList<String> failedtxn = new ArrayList<String>();

		failedtxn.add("301");
		failedtxn.add("302");
		failedtxn.add("303");
		failedtxn.add("304");
		failedtxn.add("305");
		failedtxn.add("306");
		failedtxn.add("307");

		return failedtxn;
	}

	public static ArrayList<String> repostTransaction() {
		ArrayList<String> failedtxn = new ArrayList<String>();

		failedtxn.add("902");
		failedtxn.add("904");
		failedtxn.add("906");
		failedtxn.add("907");
		failedtxn.add("909");
		failedtxn.add("911");

		return failedtxn;
	}
}
