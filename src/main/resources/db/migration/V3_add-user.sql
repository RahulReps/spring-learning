    drop table if exists user;

    create table account (
       id varchar(36) not null,
        name varchar(255),
        email varchar(255),
        primary key (id)
    ) engine=InnoDB;