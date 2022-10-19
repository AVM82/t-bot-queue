alter table registration_for_the_user
    add sent_reminder_date timestamp default null;

alter table registration_for_the_user
    alter column service_id set not null;

alter table registration_for_the_user
    alter column user_id set not null;

