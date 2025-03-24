package com.stp.auth;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stp.exception.DetailNotFoundException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		res.setHeader("Cache-Control", "no-store");
		res.setHeader("X-Frame-Options", "DENY");
		res.setHeader("Pragma", "no-cache");
		res.setHeader("X-XSS-Protection", "0");
		res.setHeader("X-Content-Type-Options", "nosniff");
		String header = req.getHeader(CONSTANT.HEADER_STRING);
		String username = null;
		String authToken = null;
		if (header != null && header.startsWith(CONSTANT.TOKEN_PREFIX)) {
			authToken = header.replace(CONSTANT.TOKEN_PREFIX, "");
			try {
				username = jwtTokenUtil.getUsernameFromToken(authToken);
				System.out.println("username jwt " + username);
			} catch (IllegalArgumentException e) {
				logger.error("an error occured during getting username from token", e);
				throw new DetailNotFoundException("an error occured during getting username from token" + e);
			} catch (ExpiredJwtException e) {
				logger.warn("the token is expired and not valid anymore", e);
				throw new DetailNotFoundException("the token is expired and not valid anymore" + e);
			} catch (SignatureException e) {
				logger.error("Authentication Failed. Username or Password not valid.");
				throw new DetailNotFoundException("Authentication Failed. Username or Password not valid.");
			}
		} else {
			logger.warn("couldn't find bearer string, will ignore the header");
//			throw new DetailNotFoundException("couldn't find bearer string, will ignore the header");
		}
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			if (jwtTokenUtil.validateToken(authToken, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(req, res);
	}
}