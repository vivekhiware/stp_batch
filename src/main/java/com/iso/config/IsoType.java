package com.iso.config;

public enum IsoType {
	FIXED, // A fixed-length alphanumeric value.
	AMOUNT, // A fixed-length alphanumeric value.
	LLLVAR, // A variable length alphanumeric value with a 3-digit header length.
	LLVAR, // A variable length alphanumeric value with a 2-digit header length.
	LVAR // A variable length alphanumeric value with a 1-digit header length.

	/*
	 * ALPHA , // A fixed-length alphanumeric value. AMOUNT , // An amount,
	 * expressed in cents with a fixed length of 12. BINARY, // Similar to ALPHA but
	 * holds byte arrays instead of strings. DATE_EXP, // A date in format yyMM
	 * DATE10, // A date in format MMddHHmmss DATE4, // A date in format MMdd LLBIN,
	 * // Similar to LLVAR but holds byte arrays instead of strings. LLLBIN, //
	 * Similar to LLLVAR but holds byte arrays instead of strings. LLLVAR, // A
	 * variable length alphanumeric value with a 3-digit header length. LLVAR, // A
	 * variable length alphanumeric value with a 2-digit header length. NUMERIC12,
	 * // A fixed-length numeric value. TIME // Time of day in format 'HHmmss'
	 */
}
