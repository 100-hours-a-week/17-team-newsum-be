package com.akatsuki.newsum.domain.webtoon.entity.webtoon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNewsSource is a Querydsl query type for NewsSource
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNewsSource extends EntityPathBase<NewsSource> {

    private static final long serialVersionUID = 1133539982L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNewsSource newsSource = new QNewsSource("newsSource");

    public final StringPath headline = createString("headline");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath url = createString("url");

    public final QWebtoon webtoon;

    public QNewsSource(String variable) {
        this(NewsSource.class, forVariable(variable), INITS);
    }

    public QNewsSource(Path<? extends NewsSource> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNewsSource(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNewsSource(PathMetadata metadata, PathInits inits) {
        this(NewsSource.class, metadata, inits);
    }

    public QNewsSource(Class<? extends NewsSource> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.webtoon = inits.isInitialized("webtoon") ? new QWebtoon(forProperty("webtoon"), inits.get("webtoon")) : null;
    }

}

