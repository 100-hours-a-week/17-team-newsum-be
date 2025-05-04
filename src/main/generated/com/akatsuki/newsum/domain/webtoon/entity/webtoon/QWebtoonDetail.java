package com.akatsuki.newsum.domain.webtoon.entity.webtoon;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWebtoonDetail is a Querydsl query type for WebtoonDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWebtoonDetail extends EntityPathBase<WebtoonDetail> {

    private static final long serialVersionUID = 494595871L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWebtoonDetail webtoonDetail = new QWebtoonDetail("webtoonDetail");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Byte> imageSeq = createNumber("imageSeq", Byte.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final QWebtoon webtoon;

    public QWebtoonDetail(String variable) {
        this(WebtoonDetail.class, forVariable(variable), INITS);
    }

    public QWebtoonDetail(Path<? extends WebtoonDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWebtoonDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWebtoonDetail(PathMetadata metadata, PathInits inits) {
        this(WebtoonDetail.class, metadata, inits);
    }

    public QWebtoonDetail(Class<? extends WebtoonDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.webtoon = inits.isInitialized("webtoon") ? new QWebtoon(forProperty("webtoon"), inits.get("webtoon")) : null;
    }

}

