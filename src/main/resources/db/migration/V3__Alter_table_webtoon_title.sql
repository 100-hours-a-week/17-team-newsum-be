alter table public.webtoon
    alter
        column title type varchar(100) using title::varchar(100);
