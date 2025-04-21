package com.stp.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {

	@Value("${token.jwt.original}")
	private String signingKey;

	public String getSigningKey() {
		return signingKey;
	}
}
