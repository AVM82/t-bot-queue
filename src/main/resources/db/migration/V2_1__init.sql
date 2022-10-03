alter table appuser
    rename column id_telegram to "telegramId";

create unique index appuser_telegramid_uindex
    on appuser ("telegramId");

alter table appuser
    rename column time_created to "createdTime";

alter table provider_entity
    rename column city to "providerCity";

alter table provider_entity
    rename column id_telegram to "telegramId";

alter table services
    rename column id_telegram to "telegramId";



