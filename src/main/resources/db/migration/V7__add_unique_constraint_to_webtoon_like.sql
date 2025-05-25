ALTER TABLE webtoon_like
    ADD CONSTRAINT uq_user_webtoon_like UNIQUE (user_id, webtoon_id);
