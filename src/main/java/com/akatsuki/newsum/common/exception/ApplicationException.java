package com.akatsuki.newsum.common.exception;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

	private final ErrorCodeAndMessage errorCodeAndMessage;

	public ApplicationException(ErrorCodeAndMessage errorCodeAndMessage) {
		super(errorCodeAndMessage.getMessage());
		this.errorCodeAndMessage = errorCodeAndMessage;
	}
}
