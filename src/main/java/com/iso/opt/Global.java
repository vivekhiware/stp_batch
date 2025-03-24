package com.iso.opt;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class Global {

	// Method to parse date from various object types (String, Date, etc.)
	public static Date parseDate1(Object dateObj, String pattern) {
		if (dateObj == null) {
			return null;
		}

		// If it's already a Date object, return it directly
		if (dateObj instanceof Date) {
			return (Date) dateObj;
		}

		// If it's a String, parse it using the provided pattern
		if (dateObj instanceof String) {
			try {
				// Use "yyyy-MM-dd" for parsing the input date format
				SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
				return inputFormat.parse((String) dateObj);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}

		// If it's an unknown type, return null
		return null;
	}

	// Method to generate formatted date string
	public static String generateDateFormat(Object dateObj, String pattern) {
		Date date = parseDate1(dateObj, pattern);
		if (date != null) {
			SimpleDateFormat outputFormat = new SimpleDateFormat(pattern);
			return outputFormat.format(date); // Format and return the date as a string
		}
		return null; // Return null if the date could not be parsed
	}

	public static void main(String[] args) {
		// Test with the input date in "yyyy-MM-dd" format
		String dateString = "2025-03-02"; // The date string
//        String pattern = "yyMMddHHmmss";   // The format pattern
//		String pattern = "MMdd";
		String pattern = "yyyyMMdd";
		// Call the method to generate the formatted date string
		String dt = generateDateFormat(dateString, pattern);

		// Output the formatted date
		System.out.println("Formatted date: " + dt);
	}
}
