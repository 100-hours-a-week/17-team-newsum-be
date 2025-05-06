package com.akatsuki.newsum.domain.webtoon.repository;

import static com.akatsuki.newsum.domain.user.entity.QUser.*;
import static com.akatsuki.newsum.domain.webtoon.entity.comment.entity.QComment.*;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.querybuilder.CursorQueryBuilder;
import com.akatsuki.newsum.common.pagination.querybuilder.registry.CursorQueryRegistry;
import com.akatsuki.newsum.domain.webtoon.dto.CommentReadDto;
import com.akatsuki.newsum.domain.webtoon.dto.QCommentReadDto;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

	private final JPAQueryFactory queryFactory;
	private final CursorQueryRegistry cursorQueryRegistry;

	@Override
	public List<CommentReadDto> findParentCommentsByCursorAndSize(Long webtoonId, Cursor cursor, int size) {
		CursorQueryBuilder<Cursor> cursorQueryBuilder = cursorQueryRegistry.resolve(cursor);
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
			.where(comment.parentCommentId.isNull()
				.and(cursorQueryBuilder.buildBooleanExpression(cursor, comment)))
			.orderBy(comment.createdAt.asc())
			.orderBy(comment.id.asc())
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
}
