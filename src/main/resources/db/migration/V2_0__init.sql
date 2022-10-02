create table if not exists appuser
(
    id_telegram  bigint not null
        primary key,
    city         varchar(255),
    language     varchar(255),
    name         varchar(255),
    phone        varchar(255),
    time_created timestamp
);
create table if not exists provider_entity
(
    id          bigserial
        primary key,
    city        varchar(255),
    id_telegram bigint,
    name        varchar(255)
);
create table if not exists services
(
    id_telegram bigint not null
        primary key,
    avatar      bytea,
    description varchar(255),
    name        varchar(255)
);