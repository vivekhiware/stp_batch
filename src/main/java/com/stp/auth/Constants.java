package com.stp.auth;

import java.util.concurrent.TimeUnit;

public class Constants {

	private Constants() {
		throw new UnsupportedOperationException("Constants class cannot be instantiated");
	}

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = TimeUnit.HOURS.toSeconds(1);
	public static final String HEADER_STRING = "Authorization";

}
