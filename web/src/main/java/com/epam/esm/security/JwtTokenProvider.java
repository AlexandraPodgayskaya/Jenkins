package com.epam.esm.security;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Class contains operations with JSON web tokens
 * 
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Component
public class JwtTokenProvider {

	@Value("${jwt.expiration}")
	private Long expirationInMinutes;
	@Value("${jwt.secret}")
	private String jwtSecret;

	private final UserDetailsService userDetailsService;

	@Autowired
	public JwtTokenProvider(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@PostConstruct
	protected void init() {
		jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
	}

	/**
	 * Create new token
	 * 
	 * @param email the user email
	 * @return created token
	 */
	public String createToken(String email) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime validity = now.plusMinutes(expirationInMinutes);
		return Jwts.builder().setSubject(email).setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
				.setExpiration(Date.from(validity.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
	}

	/**
	 * Validate token
	 * 
	 * @param token the token for validation
	 * @throws JwtException if the token is invalid
	 * 
	 */
	public void validateToken(String token) throws JwtException {
		Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
	}

	/**
	 * Get user name
	 * 
	 * @param token the token for getting user name
	 * @return received user name
	 * @throws JwtException if the token is invalid
	 * 
	 */
	public String getUsername(String token) throws JwtException {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	/**
	 * Get authentication
	 * 
	 * @param token the token for getting authentication
	 * @return authentication
	 */
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, StringUtils.EMPTY, userDetails.getAuthorities());
	}

	/**
	 * Resolve token
	 * 
	 * @param request HttpServletRequest
	 * @return token from Authorization header
	 */
	public String resolveToken(HttpServletRequest request) {
		return request.getHeader(HttpHeaders.AUTHORIZATION);
	}
}
