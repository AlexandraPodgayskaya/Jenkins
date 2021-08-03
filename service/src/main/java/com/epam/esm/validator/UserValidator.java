package com.epam.esm.validator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.util.MessageKey;
import com.epam.esm.util.ValidValue;

/**
 * Class provides methods to validate fields of {@link UserDto}.
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Component
public class UserValidator {

	private static final Logger logger = LogManager.getLogger();
	private static final String NAME_PATTERN = "^[a-zA-Zà-ÿÀ-ß-\\s]{1,45}$";
	private static final String EMAIL_PATTERN = "^([.[^@\\s]]+)@([.[^@\\s]]+)\\.([a-z]+)$";
	private static final String PASSWORD_PATTERN = "^[a-zA-Z\\d]{5,15}$";

	/**
	 * Validate user id
	 * 
	 * @param id the user id for validation
	 * @throws IncorrectParameterValueException in case incorrect id
	 */
	public void validateId(long id) throws IncorrectParameterValueException {
		if (id < ValidValue.MIN_ID) {
			Map<String, String> incorrectParameter = new HashMap<>();
			incorrectParameter.put(MessageKey.PARAMETER_ID, String.valueOf(id));
			logger.error("id error");
			throw new IncorrectParameterValueException("id validation error", incorrectParameter,
					ErrorCode.USER.getCode());
		}
	}

	/**
	 * Validate user data
	 * 
	 * @param user the user for validation
	 * @throws IncorrectParameterValueException in case incorrect name, email, or
	 *                                          password
	 */
	public void validateUser(UserDto user) throws IncorrectParameterValueException {
		Map<String, String> incorrectParameters = new LinkedHashMap<>();

		String name = user.getName();
		if (StringUtils.isEmpty(name) || !name.matches(NAME_PATTERN)) {
			incorrectParameters.put(MessageKey.PARAMETER_NAME, name);
			logger.error("user name error");
		}

		String email = user.getEmail();
		if (StringUtils.isEmpty(email) || email.length() > ValidValue.MAX_LENGTH_NAME
				|| !email.matches(EMAIL_PATTERN)) {
			incorrectParameters.put(MessageKey.PARAMETER_EMAIL, email);
			logger.error("user email error");
		}

		String password = user.getPassword();
		if (StringUtils.isEmpty(password) || !password.matches(PASSWORD_PATTERN)) {
			incorrectParameters.put(MessageKey.PARAMETER_PASSWORD, password);
			logger.error("user password error");
		}

		if (!incorrectParameters.isEmpty()) {
			throw new IncorrectParameterValueException("new user validation error", incorrectParameters,
					ErrorCode.USER.getCode());
		}
	}
}
