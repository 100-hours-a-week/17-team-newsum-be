CREATE INDEX idx_webtoon_text_fts
    ON webtoon
    USING GIN (
    to_tsvector('simple', coalesce (title, '') || ' ' || coalesce (content, ''))
    );

CREATE INDEX idx_webtoondetail_content_fts
    ON webtoon_detail
    USING GIN (
    to_tsvector('simple', coalesce (content, ''))
    );
