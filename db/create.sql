drop table if exists type_service cascade;
drop table if exists rank cascade;
drop table if exists service_provider cascade;
drop table if exists service cascade;
drop table if exists provider cascade;

create table if not exists type_service
(
    id_type     serial primary key,
    name        varchar(200) not null,
    description varchar(2000)
);

create table if not exists rank
(
    id_rank serial primary key,
    name    varchar(2000) not null
);

create table if not exists service
(
    id_service     serial primary key,
    name           varchar(2000) not null,
    type_id        INT,
    cost           decimal,
    CONSTRAINT FK_service_type FOREIGN KEY (type_id)
        REFERENCES type_service (id_type),
    time_available timestamp     not null
);

create table if not exists provider
(
    provider_id       serial primary key,
    name              varchar(200) not null,
    address           varchar(200) not null,
    date_registration timestamp    NOT NULL DEFAULT NOW(),
    service_id        int,
    rank              int,
    CONSTRAINT FK_provider_rank FOREIGN KEY (rank)
        REFERENCES rank (id_rank),
    description       varchar(2000)
);

create table if not exists service_provider
(
    service_id  int references service (id_service) ON UPDATE CASCADE ON DELETE CASCADE,
    provider_id int references provider (provider_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT service_provider_pkey PRIMARY KEY (service_id, provider_id) -- explicit pk
);




