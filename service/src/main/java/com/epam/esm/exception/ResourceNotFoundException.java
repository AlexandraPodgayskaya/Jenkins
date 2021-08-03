package com.epam.esm.exception;

/**
 * Exception is generated in case resource tag or gift certificate don't found
 * in database
 * 
 * @author Aleksandra Podgayskaya
 * @version 1.0
 * @see RuntimeException
 */
public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String messageKey;
	private String messageParameter;
	private String errorCode;

	/**
	 * Instantiates a new Resource not found exception
	 * 
	 * @param message          error message
	 * @param messageKey       the message key to get message from properties files
	 * @param messageParameter the message parameter to set into message from
	 *                         properties files
	 * @param errorCode        code for building error code in a message to the
	 *                         client
	 */
	public ResourceNotFoundException(String message, String messageKey, String messageParameter, String errorCode) {
		super(message);
		this.messageKey = messageKey;
		this.messageParameter = messageParameter;
		this.errorCode = errorCode;
	}

	/**
	 * Get message key
	 * 
	 * @return the message key
	 */
	public String getMessageKey() {
		return messageKey;
	}

	/**
	 * Get message parameter
	 * 
	 * @return the message parameter
	 */
	public String getMessageParameter() {
		return messageParameter;
	}

	/**
	 * Get error code
	 * 
	 * @return the error code
	 */
	public String getErrorCode() {
		return errorCode;
	}
}