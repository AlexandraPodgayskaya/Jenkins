package com.epam.esm.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.OrderedGiftCertificateDto;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.util.MessageKey;
import com.epam.esm.util.ValidValue;

/**
 * Class provides methods to validate fields of {@link OrderDto}.
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 */
@Component
public class OrderValidator {

	private static final Logger logger = LogManager.getLogger();

	/**
	 * Validate order id
	 * 
	 * @param id the order id for validation
	 * @throws IncorrectParameterValueException in case incorrect id
	 */
	public void validateId(long id) throws IncorrectParameterValueException {
		if (id < ValidValue.MIN_ID) {
			Map<String, String> incorrectParameter = new HashMap<>();
			incorrectParameter.put(MessageKey.PARAMETER_ID, String.valueOf(id));
			logger.error("id error");
			throw new IncorrectParameterValueException("id validation error", incorrectParameter,
					ErrorCode.ORDER.getCode());
		}
	}

	/**
	 * Validate all fields of order
	 * 
	 * @param orderDto the order for validation
	 * @throws IncorrectParameterValueException in case incorrect parameters
	 */
	public void validate(OrderDto orderDto) {
		Map<String, String> incorrectParameters = new LinkedHashMap<>();
		Long userId = orderDto.getUserId();
		if (userId == null) {
			logger.error("user id - null");
			incorrectParameters.put(MessageKey.PARAMETER_NAME, String.valueOf(userId));
		}
		List<OrderedGiftCertificateDto> giftCertificates = orderDto.getOrderedGiftCertificates();
		if (giftCertificates == null || giftCertificates.isEmpty()) {
			logger.error("list of ordered gift certificates is empty");
			incorrectParameters.put(MessageKey.PARAMETER_ORDERED_GIFT_CERTIFICATES, StringUtils.EMPTY);
		} else {
			checkListOrderedGiftCertificates(giftCertificates, incorrectParameters);
		}
		if (!incorrectParameters.isEmpty()) {
			throw new IncorrectParameterValueException("error in order validation", incorrectParameters,
					ErrorCode.ORDER.getCode());
		}
	}

	private void checkListOrderedGiftCertificates(List<OrderedGiftCertificateDto> giftCertificates,
			Map<String, String> incorrectParameters) {
		List<Long> giftCertificateId = new ArrayList<>();
		giftCertificates.forEach(orderedGifiCertificate -> checkOrderedGiftCertificateAndAddId(orderedGifiCertificate,
				giftCertificateId, incorrectParameters));
		if (Set.copyOf(giftCertificateId).size() < giftCertificateId.size()) {
			logger.error("duplicate gift certificates");
			incorrectParameters.put(MessageKey.PARAMETER_DUPLICATE_GIFT_CERTIFICATES, StringUtils.EMPTY);
		}

	}

	private void checkOrderedGiftCertificateAndAddId(OrderedGiftCertificateDto orderedGiftCertificate,
			List<Long> giftCertificateId, Map<String, String> incorrectParameters) {
		GiftCertificateDto certificate = orderedGiftCertificate.getGiftCertificate();
		if (certificate == null || certificate.getId() == null) {
			logger.error("ordered gift certificate is not specified");
			incorrectParameters.put(MessageKey.PARAMETER_GIFT_CERTIFICATE, StringUtils.EMPTY);
		} else {
			giftCertificateId.add(certificate.getId());
		}
		Integer number = orderedGiftCertificate.getNumber();
		if (number == null) {
			logger.error("number - null");
			incorrectParameters.put(MessageKey.PARAMETER_NUMBER, StringUtils.EMPTY);
		} else if (number < ValidValue.MIN_NUMBER) {
			logger.error("incorrect number");
			incorrectParameters.put(MessageKey.PARAMETER_NUMBER, String.valueOf(number));

		}
	}
}
