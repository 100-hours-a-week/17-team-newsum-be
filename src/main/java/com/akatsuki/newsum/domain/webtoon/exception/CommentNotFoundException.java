package com.akatsuki.newsum.domain.webtoon.exception;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
	public CommentNotFoundException(ErrorCodeAndMessage errorCodeAndMessage) {
		super(errorCodeAndMessage);
	}

	public CommentNotFoundException() {
		this(ErrorCodeAndMessage.COMMENT_NOT_FOUND);
	}
}
