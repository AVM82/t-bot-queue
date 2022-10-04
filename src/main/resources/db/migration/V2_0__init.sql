create table if not exists appuser
(
    id           bigserial
        primary key,
    city         varchar(255),
    created_time timestamp,
    language     varchar(255),
    name         varchar(255),
    phone        varchar(255),
    telegram_id  bigint
);

create table if not exists provider_entity
(
    id            bigserial
        primary key,
    name          varchar(255),
    provider_city varchar(255),
    telegram_id   bigint
);
create table if not exists services
(
    id          bigserial
        primary key,
    avatar      bytea,
    description varchar(255),
    name        varchar(255),
    telegram_id bigint
);

