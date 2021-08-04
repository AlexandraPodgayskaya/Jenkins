package com.epam.esm.converter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.epam.esm.dto.GiftCertificateSearchParametersDto;
import com.epam.esm.dto.GiftCertificateSearchParametersDto.OrderType;
import com.epam.esm.dto.GiftCertificateSearchParametersDto.SortType;
import com.epam.esm.dto.PaginationDto;
import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.util.MessageKey;

/**
 * Class converts parameters to Dto
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 *
 */
@Component
public class ParametersToDtoConverter {

	private static final String NUMBER_AND_SIZE_PATTERN = "^[1-9][0-9]{0,8}$";
	private static final String NUMBER = "number";
	private static final String SIZE = "size";
	private static final String TAG_NAMES = "tagNames";
	private static final String PART_NAME_OR_DESCRIPTION = "partNameOrDescription";
	private static final String SORT_TYPE = "sortType";
	private static final String ORDER_TYPE = "orderType";
	private static final String SEPARATOR_TAGS = ",";
	private static final String DEFAULT_NUMBER = "1";
	private static final String DEFAULT_SIZE = "5";

	/**
	 * Get pagination dto
	 * 
	 * @param pageParameters the page parameters
	 * @return pagination dto
	 */
	public PaginationDto getPaginationDto(Map<String, String> pageParameters) {
		String number = pageParameters.get(NUMBER) == null ? DEFAULT_NUMBER : pageParameters.get(NUMBER);
		String size = pageParameters.get(SIZE) == null ? DEFAULT_SIZE : pageParameters.get(SIZE);
		checkParametersValue(number, size);
		int offset = (Integer.valueOf(number) - 1) * Integer.valueOf(size);
		int limit = Integer.valueOf(size);
		return new PaginationDto(offset, limit);

	}

	/**
	 * Get gift certificate search parameters dto
	 * 
	 * @param parameters the search parameters
	 * @return gift certificate search parameters dto
	 */
	public GiftCertificateSearchParametersDto getGiftCertificateSearchParametersDto(Map<String, String> parameters) {
		GiftCertificateSearchParametersDto searchParametersDto = new GiftCertificateSearchParametersDto();
		String tagNames = parameters.get(TAG_NAMES);
		if (StringUtils.isNotBlank(tagNames)) {
			List<String> tags = Arrays.asList(tagNames.split(SEPARATOR_TAGS));
			searchParametersDto.setTagNames(tags.stream().map(tag -> tag.strip()).collect(Collectors.toList()));
		}
		searchParametersDto.setPartNameOrDescription(parameters.get(PART_NAME_OR_DESCRIPTION));
		setSortingParameters(parameters, searchParametersDto);
		return searchParametersDto;
	}

	private void checkParametersValue(String number, String size) {
		Map<String, String> incorrectParameters = new HashMap<>();
		if (!number.matches(NUMBER_AND_SIZE_PATTERN)) {
			incorrectParameters.put(MessageKey.PARAMETER_PAGE_NUMBER, number);
		}
		if (!size.matches(NUMBER_AND_SIZE_PATTERN)) {
			incorrectParameters.put(MessageKey.PARAMETER_PAGE_SIZE, size);
		}

		if (!incorrectParameters.isEmpty()) {
			throw new IncorrectParameterValueException("error checking page parameters", incorrectParameters,
					ErrorCode.DEFAULT.getCode());
		}
	}

	private void setSortingParameters(Map<String, String> parameters,
			GiftCertificateSearchParametersDto searchParametersDto) {
		String sortType = parameters.get(SORT_TYPE);
		String orderType = parameters.get(ORDER_TYPE);
		if (StringUtils.isNotBlank(sortType) && StringUtils.isNotBlank(orderType)) {
			checkSortingParameters(sortType, orderType);
			searchParametersDto.setSortType(SortType.valueOf(sortType.toUpperCase()));
			searchParametersDto.setOrderType(OrderType.valueOf(orderType.toUpperCase()));
		}

	}

	private void checkSortingParameters(String sortType, String orderType) {
		Map<String, String> incorrectParameters = new HashMap<>();
		if (!EnumUtils.isValidEnum(SortType.class, sortType.toUpperCase())) {
			incorrectParameters.put(MessageKey.PARAMETER_SORT_TYPE, sortType);
		}
		if (!EnumUtils.isValidEnum(OrderType.class, orderType.toUpperCase())) {
			incorrectParameters.put(MessageKey.PARAMETER_ORDER_TYPE, orderType);
		}
		if (!incorrectParameters.isEmpty()) {
			throw new IncorrectParameterValueException("error sorting parameters", incorrectParameters,
					ErrorCode.GIFT_CERTIFICATE.getCode());
		}
	}
}
