package com.epam.esm.exception;

/**
 * Class presents entity which will be returned from controller in case
 * generating some exception
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 *
 */
public final class ExceptionDetails {

	private final String errorMessage;
	private final String errorCode;

	public ExceptionDetails(String errorMessage, String errorCode) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

}
