package com.akatsuki.newsum.domain.webtoon.exception;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.ApplicationException;

public class WebtoonNotFoundException extends ApplicationException {

	public WebtoonNotFoundException(ErrorCodeAndMessage errorCodeAndMessage) {
		super(errorCodeAndMessage);
	}

	public WebtoonNotFoundException() {
		this(ErrorCodeAndMessage.WEBTOON_NOT_FOUND);
	}
}
