create table if not exists provider
(
    id                serial primary key,
    name              varchar(200) not null,
    address           varchar(200) not null,
    date_registration timestamp    NOT NULL DEFAULT NOW(),
    description       text,
    start_work_in_sunday varchar(5) default null,
    end_work_in_sunday varchar(5) default null,
    start_work_in_monday varchar(5) default null,
    end_work_in_monday varchar(5) default null,
    start_work_in_tuesday varchar(5) default null,
    end_work_in_tuesday varchar(5) default null,
    start_work_in_wednesday varchar(5) default null,
    end_work_in_wednesday varchar(5) default null,
    start_work_in_thursday varchar(5) default null,
    end_work_in_thursday varchar(5) default null,
    start_work_in_friday varchar(5) default null,
    end_work_in_friday varchar(5) default null,
    start_work_in_saturday varchar(5) default null,
    end_work_in_saturday varchar(5) default null,
    time_between_clients varchar(5) not null
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






