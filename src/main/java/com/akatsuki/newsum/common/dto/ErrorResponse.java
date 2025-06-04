package com.akatsuki.newsum.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 오류 응답을 위한 공통 응답 클래스
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
	private String message;
	private int code;

	private ErrorResponse(String message, int code) {
		this.message = message;
		this.code = code;
	}

	public static ErrorResponse fail(ErrorCodeAndMessage errorCodeAndMessage) {
		return new ErrorResponse(
			errorCodeAndMessage.getMessage(),
			errorCodeAndMessage.getCode()
		);

	}
}
