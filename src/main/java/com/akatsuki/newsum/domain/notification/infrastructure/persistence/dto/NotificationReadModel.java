package com.akatsuki.newsum.domain.notification.infrastructure.persistence.dto;

import java.time.LocalDateTime;

import com.akatsuki.newsum.domain.notification.domain.NotificationType;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

@Getter
public class NotificationReadModel {
	private Long id;
	private Long userId;
	private String title;
	private String content;
	private Long referenceId;
	private NotificationType notificationType;
	private Boolean isRead;
	private LocalDateTime createdAt;

	@QueryProjection
	public NotificationReadModel(Long id, Long userId, String title, String content, Long referenceId,
		NotificationType notificationType, Boolean isRead, LocalDateTime createdAt) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.referenceId = referenceId;
		this.notificationType = notificationType;
		this.isRead = isRead;
		this.createdAt = createdAt;
	}
}
