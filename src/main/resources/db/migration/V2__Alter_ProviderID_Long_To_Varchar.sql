alter table public.social_login
alter column provider_id type varchar(255) using provider_id::varchar(255);

