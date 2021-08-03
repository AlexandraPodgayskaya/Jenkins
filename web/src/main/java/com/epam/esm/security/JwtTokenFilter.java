package com.epam.esm.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.ExceptionDetails;
import com.epam.esm.util.MessageKey;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;

/**
 * Filter for token validation
 * 
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	private static final Logger logger = LogManager.getLogger();
	private static final String ENCODING = "UTF-8";
	private static final String GIFT_CERTIFICATE = "/gift-certificates";
	private static final String AUTH = "/auth";
	private static final String METHOD_GET = "GET";
	private final JwtTokenProvider jwtTokenProvider;
	private final MessageSource messageSource;

	@Autowired
	public JwtTokenFilter(JwtTokenProvider provider, MessageSource messageSource) {
		this.jwtTokenProvider = provider;
		this.messageSource = messageSource;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (isAvailableWithoutToken(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		try {
			if (StringUtils.isBlank(token)) {
				throw new JwtException("invalid token");
			}
			jwtTokenProvider.validateToken(token);
			Authentication authentication = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(request, response);
		} catch (JwtException | UsernameNotFoundException exception) {
			SecurityContextHolder.clearContext();
			sendError(request, response);
			logger.error(HttpStatus.UNAUTHORIZED, exception);
		}
	}

	private boolean isAvailableWithoutToken(HttpServletRequest request) {
		return request.getRequestURI().contains(GIFT_CERTIFICATE) && request.getMethod().equals(METHOD_GET)
				|| request.getRequestURI().contains(AUTH);
	}

	private void sendError(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String errorMessage = messageSource.getMessage(MessageKey.NO_RIGHTS, new String[] {}, request.getLocale());
		response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(ENCODING);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.getWriter().write(String.valueOf(new ObjectMapper().writeValueAsString(
				new ExceptionDetails(errorMessage, HttpStatus.UNAUTHORIZED.value() + ErrorCode.DEFAULT.getCode()))));
	}

}
