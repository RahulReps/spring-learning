drop table if exists player_order_line;
drop table if exists player_order;

CREATE TABLE `player_order`
(
    id                 varchar(36) NOT NULL,
    created_date       datetime(6)  DEFAULT NULL,
    account_ref       varchar(255) DEFAULT NULL,
    last_modified_date datetime(6)  DEFAULT NULL,
    version            bigint       DEFAULT NULL,
    account_id        varchar(36)  DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FOREIGN KEY (account_id) REFERENCES account (id)
) ENGINE = InnoDB;

CREATE TABLE `player_order_line`
(
    id                 varchar(36) NOT NULL,
    player_id            varchar(36) DEFAULT NULL,
    created_date       datetime(6) DEFAULT NULL,
    last_modified_date datetime(6) DEFAULT NULL,
    order_quantity     int         DEFAULT NULL,
    quantity_allocated int         DEFAULT NULL,
    version            bigint      DEFAULT NULL,
    player_order_id      varchar(36) DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FOREIGN KEY (player_order_id) REFERENCES player_order (id),
    CONSTRAINT FOREIGN KEY (player_id) REFERENCES player (id)
) ENGINE = InnoDB;