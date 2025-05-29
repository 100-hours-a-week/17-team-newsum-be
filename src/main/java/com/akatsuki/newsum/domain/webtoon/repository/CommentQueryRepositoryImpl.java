package com.akatsuki.newsum.domain.webtoon.repository;

import static com.akatsuki.newsum.domain.user.entity.QUser.*;
import static com.akatsuki.newsum.domain.webtoon.entity.comment.entity.QComment.*;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.query.QueryEngineType;
import com.akatsuki.newsum.common.pagination.model.query.QueryFragment;
import com.akatsuki.newsum.common.pagination.querybuilder.CursorPageQueryBuilder;
import com.akatsuki.newsum.common.pagination.querybuilder.registry.CursorPageQueryRegistry;
import com.akatsuki.newsum.domain.webtoon.dto.CommentReadDto;
import com.akatsuki.newsum.domain.webtoon.dto.QCommentReadDto;
import com.akatsuki.newsum.domain.webtoon.entity.comment.entity.QComment;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

	private final JPAQueryFactory queryFactory;
	private final CursorPageQueryRegistry cursorQueryRegistry;

	@Override
	public List<CommentReadDto> findParentCommentsByCursorAndSize(Long webtoonId, Cursor cursor, int size) {
		QueryFragment queryFragment = buildQueryFragment(cursor);
		return queryFactory
			.select(new QCommentReadDto(
				comment.id,
				user.id,
				user.nickname,
				user.profileImageUrl,
				comment.content,
				comment.parentCommentId,
				comment.likeCount,
				comment.createdAt
			))
			.from(comment)
			.join(user).on(comment.userId.eq(user.id))
			.where(comment.webtoonId.eq(webtoonId)
				.and(comment.parentCommentId.isNull())
				.and((BooleanExpression)queryFragment.whereClause()))
			.orderBy((OrderSpecifier<?>[])queryFragment.orderByClause())
			.limit(size + 1)
			.fetch();
	}

	@Override
	public List<CommentReadDto> findByWebtoonIdAndParentCommentIdIn(Long webtoonId, List<Long> parentCommentIds) {
		return queryFactory
			.select(new QCommentReadDto(
					comment.id,
					user.id,
					user.nickname,
					user.profileImageUrl,
					comment.content,
					comment.parentCommentId,
					comment.likeCount,
					comment.createdAt
				)
			).from(comment)
			.join(user).on(comment.userId.eq(user.id))
			.where(comment.webtoonId.eq(webtoonId)
				.and(comment.parentCommentId.in(parentCommentIds)))
			.fetch();
	}

	private QueryFragment buildQueryFragment(Cursor cursor) {
		CursorPageQueryBuilder<Cursor> queryBuilder = cursorQueryRegistry.resolve(QComment.class, cursor,
			QueryEngineType.QUERYDSL);
		QueryFragment queryFragment = queryBuilder.buildQueryFragment(cursor);
		return queryFragment;
	}
}
