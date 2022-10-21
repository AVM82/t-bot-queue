BEGIN;
alter table registration_for_the_user
    drop constraint fk_service;
alter table registration_for_the_user
    drop constraint fk_user;
alter table registration_for_the_user
    add
        constraint fk_service
            foreign key (service_id)
                references services (id)
                on delete cascade;
alter table registration_for_the_user
    add
        constraint fk_user
            foreign key (user_id)
                references appuser (id)
                on delete cascade;
COMMIT;
