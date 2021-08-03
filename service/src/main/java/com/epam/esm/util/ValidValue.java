package com.epam.esm.util;

import java.math.BigDecimal;

/**
 * Class presents valid values for data validation
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
public final class ValidValue {

	public static final int MIN_ID = 1;
	public static final int MAX_LENGTH_NAME = 100;
	public static final int MAX_LENGTH_DESCRIPTION = 1000;
	public static final int MAX_SCALE = 2;
	public static final BigDecimal MIN_PRICE = new BigDecimal("0.01");
	public static final BigDecimal MAX_PRICE = new BigDecimal("99999999.99");
	public static final int MIN_DURATION = 1;
	public static final int MAX_DURATION = 1000;
	public static final int MIN_NUMBER = 1;

	private ValidValue() {
	}

}
