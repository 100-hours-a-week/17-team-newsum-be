package com.akatsuki.newsum.common.exception;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;

public class ForbiddenException extends ApplicationException {
	public ForbiddenException(ErrorCodeAndMessage errorCodeAndMessage) {
		super(errorCodeAndMessage);
	}
}
