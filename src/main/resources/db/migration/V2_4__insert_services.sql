truncate table services restart identity;
insert into services(description, name, telegram_id, monday_working_hours, tuesday_working_hours,
                     wednesday_working_hours, thursday_working_hours, friday_working_hours,
                     saturday_working_hours, sunday_working_hours, time_between_clients)
values ('Виробництво мягких іграшок', 'Мягкі іграшки', '123', '8:00-18:00', '8:00-18:00', '8:00-18:00', '8:00-18:00', '8:00-18:00', '0:00-0:00',  '0:00-0:00', '1:00'),
       ('Very taste hamburgers', 'Food', '456', '8:00-18:00', '8:00-18:00', '8:00-18:00', '8:00-18:00', '8:00-18:00', '0:00-0:00',  '0:00-0:00', '1:00'),
       ('Ремонт взуття', 'Черевик', '789', '8:00-18:00', '8:00-18:00', '8:00-18:00', '8:00-18:00', '8:00-18:00', '0:00-0:00',  '0:00-0:00', '1:00'),
       ('Стрижка волосся', 'Перукар', '987', '8:00-18:00', '8:00-18:00', '8:00-18:00', '8:00-18:00', '8:00-18:00', '0:00-0:00',  '0:00-0:00', '1:00');

