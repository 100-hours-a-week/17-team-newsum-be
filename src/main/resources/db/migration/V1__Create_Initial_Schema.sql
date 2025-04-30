-- V1__Create_Initial_Schema.sql
-- 사용자 테이블
CREATE TABLE IF NOT EXISTS "user" (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    nickname VARCHAR(20) NOT NULL,
    profile_image_url VARCHAR(1000) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);

-- AI 작가 테이블
CREATE TABLE IF NOT EXISTS ai_author (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(10) NOT NULL,
    style VARCHAR(100) NOT NULL,
    introduction VARCHAR(100) NOT NULL,
    profile_image_url VARCHAR(1000) NOT NULL
);

-- 웹툰 테이블
CREATE TABLE IF NOT EXISTS webtoon (
    id BIGSERIAL PRIMARY KEY,
    ai_author_id BIGINT NOT NULL REFERENCES ai_author(id),
    category VARCHAR(20) NOT NULL,
    title VARCHAR(20) NOT NULL,
    content VARCHAR(255) NOT NULL,
    thumbnail_image_url VARCHAR(1000) NOT NULL,
    view_count BIGINT NOT NULL DEFAULT 0,
    like_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_webtoon_ai_author FOREIGN KEY (ai_author_id) REFERENCES ai_author(id)
);

-- 웹툰 상세 테이블
CREATE TABLE IF NOT EXISTS webtoon_detail (
    id BIGSERIAL PRIMARY KEY,
    webtoon_id BIGINT NOT NULL,
    image_url VARCHAR(1000) NOT NULL,
    content VARCHAR(255) NOT NULL,
    image_seq SMALLINT NOT NULL,
    CONSTRAINT fk_webtoon_detail_webtoon FOREIGN KEY (webtoon_id) REFERENCES webtoon(id)
);

-- 뉴스 소스 테이블
CREATE TABLE IF NOT EXISTS news_source (
    id BIGSERIAL PRIMARY KEY,
    webtoon_id BIGINT NOT NULL,
    headline VARCHAR(255) NOT NULL,
    url VARCHAR(1000) NOT NULL,
    CONSTRAINT fk_news_source_webtoon FOREIGN KEY (webtoon_id) REFERENCES webtoon(id)
);

-- 댓글 테이블
CREATE TABLE IF NOT EXISTS comment (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    webtoon_id BIGINT NOT NULL,
    parent_comment_id BIGINT,
    content VARCHAR(400) NOT NULL,
    like_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_comment_webtoon FOREIGN KEY (webtoon_id) REFERENCES webtoon(id),
    CONSTRAINT fk_comment_parent FOREIGN KEY (parent_comment_id) REFERENCES comment(id)
);

CREATE TABLE IF NOT EXISTS social_login (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    provider_id BIGINT NOT NULL,
    provider VARCHAR(20) NOT NULL,
    CONSTRAINT fk_social_login_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);

-- 최근 조회 테이블 추가
CREATE TABLE IF NOT EXISTS recent_view (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    webtoon_id BIGINT NOT NULL,
    viewed_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_recent_view_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_recent_view_webtoon FOREIGN KEY (webtoon_id) REFERENCES webtoon(id)
);