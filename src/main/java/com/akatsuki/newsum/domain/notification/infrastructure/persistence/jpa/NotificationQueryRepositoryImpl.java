package com.akatsuki.newsum.domain.notification.infrastructure.persistence.jpa;

import static com.akatsuki.newsum.domain.notification.domain.QNotification.*;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.query.QueryEngineType;
import com.akatsuki.newsum.common.pagination.model.query.QueryFragment;
import com.akatsuki.newsum.common.pagination.query.CursorPageQueryBuilder;
import com.akatsuki.newsum.common.pagination.query.registry.CursorPageQueryRegistry;
import com.akatsuki.newsum.domain.notification.domain.QNotification;
import com.akatsuki.newsum.domain.notification.infrastructure.persistence.dto.NotificationReadModel;
import com.akatsuki.newsum.domain.notification.infrastructure.persistence.dto.QNotificationReadModel;
import com.akatsuki.newsum.domain.user.entity.User;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotificationQueryRepositoryImpl implements NotificationQueryRepository {

	private final JPAQueryFactory queryFactory;
	private final CursorPageQueryRegistry cursorQueryRegistry;

	@Override
	public List<NotificationReadModel> findByUserWithCursorAndSize(User user, Cursor cursor, int size) {
		QueryFragment queryFragment = buildQueryFragment(cursor);
		return queryFactory
			.select(new QNotificationReadModel(
				notification.id,
				notification.user.id,
				notification.title,
				notification.content,
				notification.referenceId,
				notification.notificationType,
				notification.isRead,
				notification.createdAt
			))
			.from(notification)
			.where(notification.user.id.eq(user.getId())
				.and((Predicate)queryFragment.whereClause()))
			.orderBy((OrderSpecifier<?>[])queryFragment.orderByClause())
			.limit(size + 1)
			.fetch();
	}

	private QueryFragment buildQueryFragment(Cursor cursor) {
		CursorPageQueryBuilder<Cursor> queryBuilder = cursorQueryRegistry.resolve(QNotification.class, cursor,
			QueryEngineType.QUERYDSL);
		return queryBuilder.buildQueryFragment(cursor);
	}
}
