package com.akatsuki.newsum.converter;

import java.time.LocalDateTime;

public class DateConverter {

	public static String localDateTimeToDateString(LocalDateTime localDateTime) {
		return localDateTime.toLocalDate().toString();
	}
}
