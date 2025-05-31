CREATE TABLE webtoon_favorite
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT    NOT NULL,
    webtoon_id BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_webtoon_favorite_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),

    CONSTRAINT fk_webtoon_favorite_webtoon
        FOREIGN KEY (webtoon_id)
            REFERENCES webtoon (id),

    CONSTRAINT uq_webtoon_favorite_user_webtoon
        UNIQUE (user_id, webtoon_id)
);
