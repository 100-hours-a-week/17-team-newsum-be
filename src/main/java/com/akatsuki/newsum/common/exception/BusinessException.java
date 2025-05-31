package com.akatsuki.newsum.common.exception;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;

public class BusinessException extends ApplicationException {
	public BusinessException(ErrorCodeAndMessage errorCodeAndMessage) {
		super(errorCodeAndMessage);
	}
}
