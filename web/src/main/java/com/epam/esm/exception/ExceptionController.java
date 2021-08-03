package com.epam.esm.exception;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.epam.esm.util.MessageKey;

/**
 * Class represents controller which handle all generated exceptions
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@RestControllerAdvice
public class ExceptionController {

	private static final Logger logger = LogManager.getLogger();
	private static final String MESSAGE_KEY_SEPARATOR = "(";
	private static final String PART_MESSAGE_KEY = "parameterType.";

	private final MessageSource messageSource;

	@Autowired
	public ExceptionController(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Handle ResourceNotFoundException
	 * 
	 * @param exception the exception
	 * @param locale    the locale of HTTP request
	 * @return the exception details entity
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ExceptionDetails handleResourceNotFoundException(ResourceNotFoundException exception, Locale locale) {
		String errorMessage = messageSource.getMessage(exception.getMessageKey(),
				new String[] { exception.getMessageParameter() }, locale);
		logger.error(HttpStatus.NOT_FOUND, exception);
		return new ExceptionDetails(errorMessage, HttpStatus.NOT_FOUND.value() + exception.getErrorCode());
	}

	/**
	 * Handle IncorrectParameterValueException
	 * 
	 * @param exception the exception
	 * @param locale    the locale of HTTP request
	 * @return the exception details entity
	 */
	@ExceptionHandler(IncorrectParameterValueException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionDetails handleIncorrectParameterValueException(IncorrectParameterValueException exception,
			Locale locale) {
		String message = messageSource.getMessage(MessageKey.INCORRECT_PARAMETER_VALUE, new String[] {}, locale);
		StringBuilder builder = new StringBuilder();
		builder.append(message);
		exception.getParameters().forEach(
				(name, value) -> builder.append(messageSource.getMessage(name, new String[] { value }, locale)));
		logger.error(HttpStatus.BAD_REQUEST + exception.getParameters().toString(), exception);
		return new ExceptionDetails(builder.toString(), HttpStatus.BAD_REQUEST.value() + exception.getErrorCode());
	}

	/**
	 * Handle AccessDeniedException
	 * 
	 * @param exception the exception
	 * @param locale    the locale of HTTP request
	 * @return the exception details entity
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ExceptionDetails handleAccessDeniedException(AccessDeniedException exception, Locale locale) {
		String errorMessage = messageSource.getMessage(MessageKey.ACCESS_DENIED, new String[] {}, locale);
		logger.error(HttpStatus.FORBIDDEN, exception);
		return new ExceptionDetails(errorMessage, HttpStatus.FORBIDDEN.value() + ErrorCode.DEFAULT.getCode());
	}

	/**
	 * Handle AuthenticationException
	 * 
	 * @param exception the exception
	 * @param locale    the locale of HTTP request
	 * @return the exception details entity
	 */
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED) // или HttpStatus.BAD_REQUEST или HttpStatus.NOT_FOUND
	public ExceptionDetails handleAuthenticationException(AuthenticationException exception, Locale locale) {
		String errorMessage = messageSource.getMessage(MessageKey.BAD_CREDENTIALS, new String[] {}, locale);
		logger.error(HttpStatus.UNAUTHORIZED, exception);
		return new ExceptionDetails(errorMessage, HttpStatus.UNAUTHORIZED.value() + ErrorCode.DEFAULT.getCode());
	}

	/**
	 * Handle MethodArgumentTypeMismatchException
	 * 
	 * @param exception the exception
	 * @param locale    the locale of HTTP request
	 * @return the exception details entity
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionDetails handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception,
			Locale locale) {
		String metodArgument = messageSource.getMessage(MessageKey.TYPE_ID, new String[] {}, locale);
		String errorMessage = messageSource.getMessage(MessageKey.INCORRECT_PARAMETER_TYPE,
				new String[] { metodArgument }, locale);
		logger.error(HttpStatus.BAD_REQUEST, exception);
		return new ExceptionDetails(errorMessage, HttpStatus.BAD_REQUEST.value() + ErrorCode.DEFAULT.getCode());
	}

	/**
	 * Handle HttpMessageNotReadableException
	 * 
	 * @param exception the exception
	 * @param locale    the locale of HTTP request
	 * @return the exception details entity
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionDetails handleHttpMessageNotReadableException(HttpMessageNotReadableException exception,
			Locale locale) {
		String parameterKey = exception.getCause() != null && exception.getMessage().contains(PART_MESSAGE_KEY)
				? StringUtils.substringBefore(exception.getCause().getMessage(), MESSAGE_KEY_SEPARATOR)
				: MessageKey.BE_CAREFUL;
		String parameter = messageSource.getMessage(parameterKey.strip(), new String[] {}, locale);
		String errorMessage = messageSource.getMessage(MessageKey.INCORRECT_PARAMETER_TYPE, new String[] { parameter },
				locale);
		logger.error(HttpStatus.BAD_REQUEST, exception);
		return new ExceptionDetails(errorMessage, HttpStatus.BAD_REQUEST.value() + ErrorCode.DEFAULT.getCode());
	}

	/**
	 * Handle all other Exceptions
	 * 
	 * @param exception the exception
	 * @param locale    the locale of HTTP request
	 * @return the exception details entity
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ExceptionDetails handleException(Exception exception, Locale locale) {
		String errorMessage = messageSource.getMessage(MessageKey.INTERNAL_ERROR, new String[] {}, locale);
		logger.error(HttpStatus.INTERNAL_SERVER_ERROR, exception);
		return new ExceptionDetails(errorMessage,
				HttpStatus.INTERNAL_SERVER_ERROR.value() + ErrorCode.DEFAULT.getCode());
	}
}
