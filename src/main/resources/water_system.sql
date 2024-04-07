create table tbl_bill
(
    id            bigserial primary key,
    submit_time   timestamp,
    description   varchar,
    home_code     varchar,
    price         numeric,
    status        varchar,
    start_number  BIGINT,
    end_number    BIGINT,
    employee_id   BIGINT,
    department_id BIGINT,
    customer_id   BIGINT,
    month INTEGER,
    year INTEGER
);

create table tbl_customer
(
    id       bigserial primary key,
    name     varchar,
    birthday timestamp,
    phone    varchar,
    note     varchar,
    email    varchar
);

create table tbl_employee
(
    id       bigserial primary key,
    username varchar,
    password varchar,
    name     varchar,
    phone    varchar,
    birthday timestamp,
    email    varchar,
    role     varchar
);
create table tbl_department
(
    id          bigserial primary key,
    description varchar,
    customer_id bigserial,
    address_id  bigserial
);

create table tbl_timeline
(
    id           bigserial primary key,
    start_number bigserial,
    end_number   bigserial,
    created_at   timestamp
);

create table tbl_tax
(
    id         bigserial primary key,
    name       varchar,
    percentage decimal(10, 2)
);

create table tbl_address
(
    id          bigserial primary key,
    home_number varchar,
    street_name varchar,
    hamlet      varchar,
    ward        varchar,
    district    varchar,
    city        varchar
)

alter table tbl_department
    add column home_code varchar