CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE brand (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT
);

CREATE TABLE category (
                          id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT
);

CREATE TABLE product (
                         id UUID PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         price DOUBLE PRECISION NOT NULL,
                         brand_id UUID NOT NULL,
                         category_id UUID NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT now(),
                         updated_at TIMESTAMP NOT NULL DEFAULT now()

--                         CONSTRAINT fk_product_brand FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE CASCADE,
--                         CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);
