package com.akatsuki.newsum.common.exception;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;

public class NotFoundException extends ApplicationException {
	public NotFoundException(ErrorCodeAndMessage errorCodeAndMessage) {
		super(errorCodeAndMessage);
	}
}
