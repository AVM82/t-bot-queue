create table registration_for_the_user
(
    id                             bigserial
        primary key,
    service_id                     bigint,
    user_id                        bigint,
    service_registration_date_time timestamp,
    CONSTRAINT fk_service
        FOREIGN KEY (service_id)
            REFERENCES services (id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES appuser (id)
);