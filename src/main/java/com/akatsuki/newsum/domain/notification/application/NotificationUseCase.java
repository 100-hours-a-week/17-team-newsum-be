package com.akatsuki.newsum.domain.notification.application;

import com.akatsuki.newsum.domain.notification.application.dto.NotificationPageListCommand;
import com.akatsuki.newsum.domain.notification.application.dto.NotificationPageListResult;

public interface NotificationUseCase {
	NotificationPageListResult lookupNotificationsByUserId(NotificationPageListCommand notificationPageListCommand);
}
