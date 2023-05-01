create table person
(
    person_id uuid primary key,
    name      varchar(64),
    email     varchar(128),
    phone     varchar(12)
);

create table supplier
(
    supplier_id               uuid primary key,
    name                      varchar(64),
    stock_expiry_timeout_days int8,
    contact_person_id         uuid,
    sales_rep_id              uuid
);

create table product_species
(
    product_species_id uuid primary key,
    name               varchar(64)
);

create table drying_method
(
    drying_method_id uuid primary key,
    name             varchar(64)
);

create table treatment
(
    treatment_id uuid primary key,
    name         varchar(64)
);

CREATE TABLE product
(
    product_id           uuid PRIMARY KEY,
    supplier_id          uuid,
    version              int8,
    created_on           timestamp,
    updated_on           timestamp,
    expires_on           timestamp,
    quantity_cbm         int8,
    fixed_price_eur      int8,
    negotiated_price_eur int8,
    thickness            int8,
    width                int8,
    length               int8,
    grade_type           varchar(64),
    grade                varchar(5),
    product_species_id   uuid,
    drying_method_id     uuid,
    treatment_id         uuid
);
CREATE INDEX idx_prod_supplier_id ON product (supplier_id);
CREATE INDEX idx_prod_created_on ON product (created_on);
CREATE INDEX idx_prod_updated_on ON product (updated_on);
CREATE INDEX idx_prod_expires_on ON product (expires_on);

CREATE INDEX idx_prod_thickness ON product (thickness);
CREATE INDEX idx_prod_width ON product (width);
CREATE INDEX idx_prod_length ON product (length);
CREATE INDEX idx_prod_product_species_id ON product (product_species_id);
CREATE INDEX idx_prod_drying_method_id ON product (drying_method_id);
CREATE INDEX idx_prod_treatment_id ON product (treatment_id);

CREATE TABLE product_history
(
    history_id           uuid PRIMARY KEY,
    product_id           uuid,
    supplier_id          uuid,

    version              int8,
    created_on           timestamp,
    updated_on           timestamp,
    expires_on           timestamp,
    quantity_cbm         int8,
    fixed_price_eur      int8,
    negotiated_price_eur int8,
    thickness            int8,
    width                int8,
    length               int8,
    grade_type           varchar(64),
    grade                varchar(5),
    product_species_id   uuid,
    drying_method_id     uuid,
    treatment_id         uuid
);
CREATE INDEX idx_prod_history_product_id ON product_history (product_id);
CREATE INDEX idx_prod_history_supplier_id ON product_history (supplier_id);
