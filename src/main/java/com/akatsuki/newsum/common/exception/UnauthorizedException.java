package com.akatsuki.newsum.common.exception;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;

public class UnauthorizedException extends ApplicationException {

	public UnauthorizedException(ErrorCodeAndMessage errorCodeAndMessage) {
		super(errorCodeAndMessage);
	}
}
