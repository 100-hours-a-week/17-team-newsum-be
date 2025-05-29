package com.akatsuki.newsum.common.pagination.querybuilder.query;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.pagination.querybuilder.base.CreatedAtIdJpqlPageQueryBuilder;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoon;
import com.querydsl.core.types.dsl.PathBuilder;

@Component
public class WebtoonPageQueryBuilder extends CreatedAtIdJpqlPageQueryBuilder {

	private final Class<?> domainClass = QWebtoon.class;
	private final String domainName = "webtoon";

	@Override
	protected PathBuilder<?> getPathBuilder() {
		return new PathBuilder<>(domainClass, domainName);
	}

	@Override
	protected Class<?> getDomainClass() {
		return domainClass;
	}
}
