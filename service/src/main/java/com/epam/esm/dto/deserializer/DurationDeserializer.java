package com.epam.esm.dto.deserializer;

import java.io.IOException;

import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.util.MessageKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Class converts String to Integer
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 *
 */
public class DurationDeserializer extends JsonDeserializer<Integer> {

	private static final String DURATION_PATTERN = "^\\d{1,9}";
	
	@Override
	public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String duration = p.getValueAsString();
		if (duration == null || !duration.matches(DURATION_PATTERN)) {
			throw new IncorrectParameterValueException(MessageKey.TYPE_DURATION);
		}
		return Integer.valueOf(duration);
	}

}
