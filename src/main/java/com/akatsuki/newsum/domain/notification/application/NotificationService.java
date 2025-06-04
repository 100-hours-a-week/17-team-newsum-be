package com.akatsuki.newsum.domain.notification.application;

import static com.akatsuki.newsum.common.dto.ErrorCodeAndMessage.*;

import java.util.List;

import org.springframework.stereotype.Service;

import com.akatsuki.newsum.common.exception.NotFoundException;
import com.akatsuki.newsum.common.pagination.CursorPaginationService;
import com.akatsuki.newsum.common.pagination.model.page.CursorPage;
import com.akatsuki.newsum.domain.notification.application.dto.NotificationPageListCommand;
import com.akatsuki.newsum.domain.notification.application.dto.NotificationPageListResult;
import com.akatsuki.newsum.domain.notification.infrastructure.persistence.NotificationRepository;
import com.akatsuki.newsum.domain.notification.infrastructure.persistence.dto.NotificationReadModel;
import com.akatsuki.newsum.domain.notification.presentation.dto.NotificationResult;
import com.akatsuki.newsum.domain.user.entity.User;
import com.akatsuki.newsum.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase {

	private final NotificationRepository notificationRepository;
	private final CursorPaginationService cursorPaginationService;
	private final UserRepository userRepository;

	@Override
	public NotificationPageListResult lookupNotificationsByUserId(
		NotificationPageListCommand notificationPageListCommand) {
		User user = findUserByIdOrThrow(notificationPageListCommand.userId());

		List<NotificationReadModel> notifications = notificationRepository.findByUserWithCursorAndSize(
			user,
			notificationPageListCommand.cursor(),
			notificationPageListCommand.size());

		List<NotificationResult> notificationResults = notifications.stream()
			.map(this::mapNotificationReadModelToResult)
			.toList();

		CursorPage<NotificationResult> cursorPage = cursorPaginationService.create(
			notificationResults,
			notificationPageListCommand.size(),
			notificationPageListCommand.cursor());
		return new NotificationPageListResult(cursorPage);
	}

	private NotificationResult mapNotificationReadModelToResult(NotificationReadModel model) {
		return new NotificationResult(
			model.getUserId(),
			model.getNotificationType().name(),
			model.getContent(),
			model.getCreatedAt(),
			model.getIsRead()
		);
	}

	private User findUserByIdOrThrow(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
	}
}
