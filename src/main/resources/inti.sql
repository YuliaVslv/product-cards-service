CREATE SCHEMA product_cards;

CREATE TABLE product_cards.brand (
    id INTEGER NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE product_cards.type (
    id INTEGER NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE product_cards.product (
    id INTEGER NOT NULL PRIMARY KEY,
    type_id INTEGER NOT NULL,
    brand_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    discount INTEGER NOT NULL,
    amount INTEGER NOT NULL,
    FOREIGN KEY(type_id) REFERENCES product_cards.type(id),
    FOREIGN KEY(brand_id) REFERENCES product_cards.brand(id),
);