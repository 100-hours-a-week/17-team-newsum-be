package com.akatsuki.newsum.domain.notification.infrastructure.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.domain.notification.infrastructure.persistence.dto.NotificationReadModel;
import com.akatsuki.newsum.domain.notification.infrastructure.persistence.jpa.NotificationJpaRepository;
import com.akatsuki.newsum.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

	private final NotificationJpaRepository notificationJpaRepository;

	@Override
	public List<NotificationReadModel> findByUserWithCursorAndSize(User user, Cursor cursor, int size) {
		return notificationJpaRepository.findByUserWithCursorAndSize(user, cursor, size);
	}
}
