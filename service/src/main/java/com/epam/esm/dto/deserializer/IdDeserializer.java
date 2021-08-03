package com.epam.esm.dto.deserializer;

import java.io.IOException;

import com.epam.esm.exception.IncorrectParameterValueException;
import com.epam.esm.util.MessageKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Class converts String to Long
 *
 * @author Aleksandra Podgayskaya
 * @version 1.0
 *
 */
public class IdDeserializer extends JsonDeserializer<Long> {

	private static final String ID_PATTERN = "^\\d{1,18}";

	@Override
	public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String id = p.getValueAsString();
		if (id == null || !id.matches(ID_PATTERN)) {
			throw new IncorrectParameterValueException(MessageKey.TYPE_ID);
		}
		return Long.valueOf(id);
	}

}
