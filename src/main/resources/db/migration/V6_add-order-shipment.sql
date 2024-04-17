drop table if exists player_order_shipment;

CREATE TABLE player_order_shipment
(
    id                 VARCHAR(36) NOT NULL PRIMARY KEY,
    player_order_id            VARCHAR(36) UNIQUE,
    tracking_number    VARCHAR(50),
    created_date       TIMESTAMP,
    last_modified_date DATETIME(6) DEFAULT NULL,
    version            BIGINT      DEFAULT NULL,
    CONSTRAINT bos_pk FOREIGN KEY (player_order_id) REFERENCES player_order (id)
) ENGINE = InnoDB;

ALTER TABLE player_order
    ADD COLUMN player_order_shipment_id VARCHAR(36);

ALTER TABLE player_order
    ADD CONSTRAINT bos_shipment_fk
        FOREIGN KEY (player_order_shipment_id) REFERENCES player_order_shipment (id);