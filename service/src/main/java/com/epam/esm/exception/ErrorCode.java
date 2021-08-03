package com.epam.esm.exception;

/**
 * Class presents code for building error code in a message to the client
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public enum ErrorCode {

	DEFAULT("00"), GIFT_CERTIFICATE("01"), TAG("02"), USER("03"), ORDER("04");

	private final String code;

	private ErrorCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
