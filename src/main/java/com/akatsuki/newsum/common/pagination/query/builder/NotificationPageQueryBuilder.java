package com.akatsuki.newsum.common.pagination.query.builder;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.pagination.query.base.CreatedAtIdJpqlPageQueryBuilder;
import com.akatsuki.newsum.domain.notification.domain.QNotification;
import com.querydsl.core.types.dsl.PathBuilder;

@Component
public class NotificationPageQueryBuilder extends CreatedAtIdJpqlPageQueryBuilder {

	private final Class<?> domainClass = QNotification.class;
	private final String domainName = "notification";

	@Override
	protected PathBuilder<?> getPathBuilder() {
		return new PathBuilder<>(domainClass, domainName);
	}

	@Override
	protected Class<?> getDomainClass() {
		return domainClass;
	}
}
