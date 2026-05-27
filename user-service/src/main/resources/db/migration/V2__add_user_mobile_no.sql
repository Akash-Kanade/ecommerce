alter table users
    add column mobile_no varchar(20);

update users
set mobile_no = ''
where mobile_no is null;

alter table users
    alter column mobile_no set not null;
