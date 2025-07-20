CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabla: warehouse
CREATE TABLE warehouse (
                           id UUID PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           description TEXT,
                           direction TEXT
);

-- Tabla: inventory
CREATE TABLE inventory (
                           id UUID PRIMARY KEY,
                           product_id UUID NOT NULL,
                           quantity INTEGER NOT NULL,
                           creation_date TIMESTAMP NOT NULL DEFAULT now(),
                           update_date TIMESTAMP NOT NULL DEFAULT now()

);

-- Tabla: inventory_warehouse
CREATE TABLE inventory_warehouse (
                                     id UUID PRIMARY KEY,
                                     inventory_id UUID NOT NULL,
                                     warehouse_id UUID NOT NULL,
                                     quantity INTEGER NOT NULL,
                                     creation_date TIMESTAMP NOT NULL DEFAULT now(),
                                     update_date TIMESTAMP NOT NULL DEFAULT now(),

                                     CONSTRAINT fk_inventory_warehouse_inventory FOREIGN KEY (inventory_id) REFERENCES inventory(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_inventory_warehouse_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(id) ON DELETE CASCADE
);