CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE brands (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT
);

CREATE TABLE categories (
                          id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT
);

CREATE TABLE products (
                         id UUID PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         description TEXT,
                         price DOUBLE PRECISION NOT NULL,
                         brand_id UUID NOT NULL,
                         category_id UUID NOT NULL,
                         created_at TIMESTAMP NOT NULL DEFAULT now(),
                         updated_at TIMESTAMP NOT NULL DEFAULT now()

--                         CONSTRAINT fk_product_brand FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE CASCADE,
--                         CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE
);
