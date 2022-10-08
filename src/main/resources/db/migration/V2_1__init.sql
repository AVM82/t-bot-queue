create unique index appuser_telegram_id_uindex
    on appuser (telegram_id);
alter table appuser
    alter column name set not null;




