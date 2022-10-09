truncate table appuser restart identity;
insert into appuser(city, created_time, language, name, phone, telegram_id)
values ('Starokostiantyniv', '2020-02-23', 'ua', 'Oleksandr', +380673904107, '123'),
       ('Lviv', '2021-07-20', 'en', 'Boris', +380671265893, '456'),
       ('Kiev', '2022-12-31', 'ua', 'Pavlo', +380673904107, '789'),
       ('Lubar', '2020-02-23', 'ua', 'Natalia', +380982563598, '987');
