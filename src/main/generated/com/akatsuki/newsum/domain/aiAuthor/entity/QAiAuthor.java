package com.akatsuki.newsum.domain.aiAuthor.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAiAuthor is a Querydsl query type for AiAuthor
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAiAuthor extends EntityPathBase<AiAuthor> {

    private static final long serialVersionUID = 199599150L;

    public static final QAiAuthor aiAuthor = new QAiAuthor("aiAuthor");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath introduction = createString("introduction");

    public final StringPath name = createString("name");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final StringPath style = createString("style");

    public final ListPath<com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon, com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoon> webtoons = this.<com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon, com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoon>createList("webtoons", com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon.class, com.akatsuki.newsum.domain.webtoon.entity.webtoon.QWebtoon.class, PathInits.DIRECT2);

    public QAiAuthor(String variable) {
        super(AiAuthor.class, forVariable(variable));
    }

    public QAiAuthor(Path<? extends AiAuthor> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAiAuthor(PathMetadata metadata) {
        super(AiAuthor.class, metadata);
    }

}

