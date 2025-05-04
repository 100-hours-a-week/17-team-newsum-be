package com.akatsuki.newsum.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSocialLogin is a Querydsl query type for SocialLogin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSocialLogin extends EntityPathBase<SocialLogin> {

    private static final long serialVersionUID = -1171891895L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSocialLogin socialLogin = new QSocialLogin("socialLogin");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<Provider> provider = createEnum("provider", Provider.class);

    public final StringPath providerId = createString("providerId");

    public final QUser user;

    public QSocialLogin(String variable) {
        this(SocialLogin.class, forVariable(variable), INITS);
    }

    public QSocialLogin(Path<? extends SocialLogin> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSocialLogin(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSocialLogin(PathMetadata metadata, PathInits inits) {
        this(SocialLogin.class, metadata, inits);
    }

    public QSocialLogin(Class<? extends SocialLogin> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

