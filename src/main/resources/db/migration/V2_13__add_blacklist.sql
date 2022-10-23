create table if not exists providers_blacklist
(
    provider_id                    bigint,
    user_id                        bigint,

    CONSTRAINT fk_service
        FOREIGN KEY (provider_id)
        REFERENCES provider_entity (id)
);