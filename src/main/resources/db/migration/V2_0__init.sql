create table if not exists appuser
(
    id              bigserial   primary key,
    id_telegram     bigint not null,
    city            varchar(255),
    language        varchar(255),
    name            varchar(255),
    phone           varchar(255),
    time_created    timestamp
);
create table if not exists provider_entity
(
    id          bigserial   primary key,
    city        varchar(255),
    id_telegram bigint,
    name        varchar(255)
);
create table if not exists services
(
    id          bigserial   primary key,
    id_telegram bigint  not null,
    avatar      bytea,
    description varchar(255),
    name        varchar(255)
);