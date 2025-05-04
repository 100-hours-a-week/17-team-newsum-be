package com.akatsuki.newsum.domain.webtoon.entity.webtoon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWebtoon is a Querydsl query type for Webtoon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWebtoon extends EntityPathBase<Webtoon> {

    private static final long serialVersionUID = 585434414L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWebtoon webtoon = new QWebtoon("webtoon");

    public final com.akatsuki.newsum.common.entity.QBaseTimeEntity _super = new com.akatsuki.newsum.common.entity.QBaseTimeEntity(this);

    public final com.akatsuki.newsum.domain.aiAuthor.entity.QAiAuthor aiAuthor;

    public final EnumPath<Category> category = createEnum("category", Category.class);

    public final ListPath<com.akatsuki.newsum.domain.webtoon.entity.comment.entity.Comment, com.akatsuki.newsum.domain.webtoon.entity.comment.entity.QComment> comments = this.<com.akatsuki.newsum.domain.webtoon.entity.comment.entity.Comment, com.akatsuki.newsum.domain.webtoon.entity.comment.entity.QComment>createList("comments", com.akatsuki.newsum.domain.webtoon.entity.comment.entity.Comment.class, com.akatsuki.newsum.domain.webtoon.entity.comment.entity.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final ListPath<WebtoonDetail, QWebtoonDetail> details = this.<WebtoonDetail, QWebtoonDetail>createList("details", WebtoonDetail.class, QWebtoonDetail.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> likeCount = createNumber("likeCount", Long.class);

    public final ListPath<NewsSource, QNewsSource> newsSources = this.<NewsSource, QNewsSource>createList("newsSources", NewsSource.class, QNewsSource.class, PathInits.DIRECT2);

    public final StringPath thumbnailImageUrl = createString("thumbnailImageUrl");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> viewCount = createNumber("viewCount", Long.class);

    public QWebtoon(String variable) {
        this(Webtoon.class, forVariable(variable), INITS);
    }

    public QWebtoon(Path<? extends Webtoon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWebtoon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWebtoon(PathMetadata metadata, PathInits inits) {
        this(Webtoon.class, metadata, inits);
    }

    public QWebtoon(Class<? extends Webtoon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.aiAuthor = inits.isInitialized("aiAuthor") ? new com.akatsuki.newsum.domain.aiAuthor.entity.QAiAuthor(forProperty("aiAuthor")) : null;
    }

}

