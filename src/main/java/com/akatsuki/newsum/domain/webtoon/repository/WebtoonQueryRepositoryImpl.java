package com.akatsuki.newsum.domain.webtoon.repository;

import static com.akatsuki.newsum.domain.aiAuthor.entity.QAiAuthor.*;
import static com.akatsuki.newsum.domain.webtoon.entity.webtoon.QNewsSource.*;
import static com.akatsuki.newsum.domain.webtoon.entity.webtoon.QRecentView.*;
import static com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoon.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;

import com.akatsuki.newsum.common.pagination.model.cursor.CreatedAtIdCursor;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.querybuilder.CursorQueryBuilder;
import com.akatsuki.newsum.common.pagination.querybuilder.registry.CursorQueryRegistry;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Category;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.RecentView;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebtoonQueryRepositoryImpl implements WebtoonQueryRepository {

	private final JPAQueryFactory queryFactory;
	private final CursorQueryRegistry cursorQueryRegistry;
	private final JdbcTemplate jdbcTemplate;

	@Override
	public List<Webtoon> findWebtoonByCategoryWithCursor(Category category, Cursor cursor, int size) {
		//TODO : CursorQueryBuilder 수정 필요
		CursorQueryBuilder<Cursor> queryBuilder = cursorQueryRegistry.resolve(cursor);

		return queryFactory.selectFrom(webtoon)
			.where(webtoon.category.eq(category)
				.and(buildBooleanExpression(cursor)))
			.orderBy(webtoon.createdAt.asc())
			.orderBy(webtoon.createdAt.desc())
			.limit(size + 1)
			.fetch();
	}

	@Override
	public Optional<Webtoon> findWebtoonAndAiAuthorById(Long webtoonId) {
		return queryFactory
			.selectFrom(webtoon)
			.join(webtoon.aiAuthor, aiAuthor).fetchJoin()
			.where(webtoon.id.eq(webtoonId))
			.fetch()
			.stream().findFirst();
	}

	@Override
	public Optional<Webtoon> findWebtoonAndNewsSourceById(Long webtoonId) {
		return queryFactory
			.selectFrom(webtoon)
			.leftJoin(webtoon.newsSources, newsSource).fetchJoin()
			.where(webtoon.id.eq(webtoonId))
			.fetch()
			.stream().findFirst();
	}

	@Override
	public List<RecentView> findRecentWebtoons(Long id) {
		return queryFactory
			.selectFrom(recentView)
			.join(webtoon).fetchJoin()
			.on(webtoon.id.eq(recentView.webtoon.id))
			.where(recentView.user.id.eq(id))
			.orderBy(recentView.viewedAt.desc())
			.fetch();
	}

	@Override
	public List<Webtoon> searchByTitleContaining(String query, Cursor cursor, int size) {
		CreatedAtIdCursor createdAtIdCursor = (CreatedAtIdCursor)cursor;

		final String sql = """
			   SELECT DISTINCT w.id,
					  w.title,
					  w.content,
					  w.created_at,
					  w.thumbnail_image_url,
					  w.view_count
			   FROM webtoon w
			   LEFT JOIN webtoon_detail d ON w.id = d.webtoon_id
			   WHERE
			       to_tsvector('simple', coalesce(w.title, '') || ' ' || coalesce(w.content, '')) @@ plainto_tsquery('simple', ?)
			   OR
			       to_tsvector('simple', coalesce(d.content, '')) @@ plainto_tsquery('simple', ?)
				 AND (w.created_at < ? OR (w.created_at = ? AND w.id < ?))
			   ORDER BY w.created_at DESC, w.id DESC
			   LIMIT ?
			""";

		return jdbcTemplate.query(sql,
			new Object[] {
				query,
				query,
				createdAtIdCursor.getCreatedAt(),
				createdAtIdCursor.getCreatedAt(),
				createdAtIdCursor.getId(),
				size + 1
			},
			(rs, rowNum) ->
				new Webtoon(
					rs.getTimestamp("created_at").toLocalDateTime(),
					rs.getLong("id"),
					rs.getString("title"),
					rs.getString("thumbnail_image_url"),
					rs.getLong("view_count")
				)
		);
	}

	@Override
	public List<Webtoon> findTop3TodayByViewCount() {
		return queryFactory
			.selectFrom(webtoon)
			.orderBy(webtoon.viewCount.desc())
			.limit(3)
			.fetch();
	}

	@Override
	public List<Webtoon> findTodayNewsTop3() {
		LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();

		return queryFactory
			.selectFrom(webtoon)
			.where(webtoon.createdAt.goe(startOfToday))
			.orderBy(webtoon.createdAt.desc())
			.limit(3)
			.fetch();
	}

	private BooleanExpression buildBooleanExpression(Cursor cursor) {
		CreatedAtIdCursor createdAtIdCursor = (CreatedAtIdCursor)cursor;
		return webtoon.createdAt.gt(createdAtIdCursor.getCreatedAt())
			.or(webtoon.createdAt.eq(createdAtIdCursor.getCreatedAt()).and(webtoon.id.goe(createdAtIdCursor.getId())));
	}
}
