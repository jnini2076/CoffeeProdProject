CREATE TABLE orders (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    customer_name    VARCHAR(255),
    items            TEXT,
    total_amount     DOUBLE,
    card_holder_name VARCHAR(255),
    order_date       VARCHAR(255),
    PRIMARY KEY (id)
);
