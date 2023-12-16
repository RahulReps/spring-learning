
    drop table if exists player;

    create table player (
        jersey_no integer not null,
        id varchar(36) not null,
        name varchar(50) not null,
        foot varchar(255) not null,
        play_style varchar(255),
        position varchar(255) not null,
        primary key (id)
    ) engine=InnoDB;
