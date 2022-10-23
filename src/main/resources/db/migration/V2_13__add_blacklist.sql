create table if not exists providers_blacklist
(
    provider_id                    bigint
        primary key,
    user_id                        bigint
);