create table if not exists feedback
(
    id_feedback   bigserial primary key,
    rate          int,
    text_feedback varchar(255),
    telegram_id   bigint not null
);
