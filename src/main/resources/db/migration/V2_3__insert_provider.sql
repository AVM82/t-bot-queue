truncate table provider_entity restart identity;
insert into provider_entity(name, provider_city, telegram_id)
values ('ПП Сміливий', 'Kiev', '123'),
       ('Boris''s cafe', 'Lisbon', '456'),
       ('Kolosok', 'Kiev', '789'),
       ('Перукарня для всіх', 'Київ', '987');
