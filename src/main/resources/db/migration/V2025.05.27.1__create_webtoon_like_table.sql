CREATE TABLE webtoon_like
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    webtoon_id BIGINT NOT NULL,

    CONSTRAINT fk_webtoon_like_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_webtoon_like_webtoon FOREIGN KEY (webtoon_id) REFERENCES webtoon (id)
);

ALTER TABLE webtoon_like
    ADD CONSTRAINT uq_user_webtoon_like UNIQUE (user_id, webtoon_id);
