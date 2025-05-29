package com.akatsuki.newsum.common.pagination.query.builder;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.pagination.query.base.CreatedAtIdJpqlPageQueryBuilder;
import com.akatsuki.newsum.domain.webtoon.entity.comment.entity.QComment;
import com.querydsl.core.types.dsl.PathBuilder;

@Component
public class CommentPageQueryBuilder extends CreatedAtIdJpqlPageQueryBuilder {

	private final Class<?> domainClass = QComment.class;
	private final String domainName = "comment";

	@Override
	protected PathBuilder<?> getPathBuilder() {
		return new PathBuilder<>(domainClass, domainName);
	}

	@Override
	protected Class<?> getDomainClass() {
		return domainClass;
	}
}
