package com.akatsuki.newsum.domain.webtoon.exception;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.ForbiddenException;

public class CommentForbiddenException extends ForbiddenException {
	public CommentForbiddenException(ErrorCodeAndMessage errorCodeAndMessage) {
		super(errorCodeAndMessage);
	}

	public CommentForbiddenException() {
		this(ErrorCodeAndMessage.COMMENT_FORBIDDEN);
	}
}
