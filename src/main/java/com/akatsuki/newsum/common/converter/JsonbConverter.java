package com.akatsuki.newsum.common.converter;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class JsonbConverter implements AttributeConverter<Map<String, Object>, String> {

	private static final ObjectMapper mapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(Map<String, Object> attribute) {
		System.out.println("👉 JsonbConverter: convertToDatabaseColumn 호출됨");
		try {
			return mapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("JSONB 변환 실패", e);
		}
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String dbData) {
		try {
			return mapper.readValue(dbData, HashMap.class);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException("JSONB 역변환 실패", e);
		}
	}
}
