create table if not exists provider
(
    id                serial primary key,
    name              varchar(200) not null,
    address           varchar(200) not null,
    date_registration timestamp    NOT NULL DEFAULT NOW(),
    description       text
);

create table if not exists service
(
    id          serial primary key,
    name        varchar(80) not null,
    description text,
    provider_id int,
    CONSTRAINT fk_provider
        FOREIGN KEY (provider_id)
            REFERENCES provider (id)
);






