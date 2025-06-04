package com.akatsuki.newsum.domain.notification.infrastructure.persistence.jpa;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.domain.notification.infrastructure.persistence.dto.NotificationReadModel;
import com.akatsuki.newsum.domain.user.entity.User;

public interface NotificationQueryRepository {
	List<NotificationReadModel> findByUserWithCursorAndSize(User user, Cursor cursor, int size);
}
