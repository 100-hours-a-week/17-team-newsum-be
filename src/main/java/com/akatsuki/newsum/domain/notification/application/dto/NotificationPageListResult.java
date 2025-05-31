package com.akatsuki.newsum.domain.notification.application.dto;

import com.akatsuki.newsum.common.pagination.model.page.CursorPage;
import com.akatsuki.newsum.domain.notification.presentation.dto.NotificationResult;

public record NotificationPageListResult(
	CursorPage<NotificationResult> cursorPage
) {
}
