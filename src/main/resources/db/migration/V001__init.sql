CREATE TABLE orders
(
    id                  UUID                     NOT NULL,
    customer_id         UUID                     NOT NULL,
    ordered_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    dispatched_at       TIMESTAMP WITH TIME ZONE,
    billing_address_id  UUID                     NOT NULL,
    shipping_address_id UUID                     NOT NULL,
    status              TEXT                     NOT NULL DEFAULT 'OPEN',
    PRIMARY KEY (id)
);

CREATE TABLE addresses
(
    id          UUID NOT NULL,
    customer_id UUID NOT NULL,
    name        TEXT,
    street      TEXT,
    city        TEXT,
    zip_code    TEXT,
    country     CHAR(2),
    PRIMARY KEY (id)
)
