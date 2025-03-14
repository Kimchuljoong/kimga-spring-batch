create table products
(
    product_id       varchar(255) primary key,
    seller_id        bigint       not null,
    category         varchar(255) not null,
    product_name     varchar(255) not null,
    sales_start_date date,
    sales_end_date   date,
    product_status   varchar(50),
    brand            varchar(255),
    manufacturer     varchar(255),
    sales_price      integer      not null,
    stock_quantity   integer   default 0,
    created_at       timestamp default current_timestamp,
    updated_at       timestamp default current_timestamp
);

create index idx_products_product_status on products (product_status);
create index idx_products_category on products (category);
create index idx_products_brand on products (brand);
create index idx_products_manufacturer on products (manufacturer);
create index idx_products_seller_id on products (seller_id);


CREATE TABLE transaction_reports
(
    transaction_date     date,
    transaction_type     varchar(5)     not null,
    transaction_count    bigint         not null,
    total_amount         bigint         not null,
    customer_count       bigint         not null,
    order_count          bigint         not null,
    payment_method_count bigint         not null,
    avg_product_count    decimal(15, 0) not null,
    total_item_quantity  bigint         not null,
    primary key (transaction_date, transaction_type)
);