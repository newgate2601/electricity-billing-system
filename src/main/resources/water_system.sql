create table tbl_bill
(
    id            bigserial primary key,
    submit_time   timestamp,
    description   varchar,
    home_code     varchar,
    price         decimal(10, 2),
    status        varchar,
    start_number  bigserial,
    end_number    bigserial,
    tax_VAT       decimal(10, 2),
    tax_BVMT      decimal(10, 2),
    employee_id   bigserial,
    department_id bigserial,
    customer_id   bigserial
)
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