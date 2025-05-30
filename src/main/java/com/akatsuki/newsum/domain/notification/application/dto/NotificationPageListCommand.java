package com.akatsuki.newsum.domain.notification.application.dto;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;

public record NotificationPageListCommand(
	Cursor cursor,
	int size,
	Long userId
) {
}
