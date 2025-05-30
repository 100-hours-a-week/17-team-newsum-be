package com.akatsuki.newsum.domain.notification.presentation.dto;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.page.CursorPage;
import com.akatsuki.newsum.common.pagination.model.page.PageInfo;

public record NotificationListPageResponse(
	List<NotificationResult> notifications,
	PageInfo pageInfo
) {
	public static NotificationListPageResponse of(CursorPage<NotificationResult> cursorPage) {
		return new NotificationListPageResponse(cursorPage.getItems(), cursorPage.getPageInfo());
	}
}
