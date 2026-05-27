create table product(
                        product_id BIGINT primary key GENERATED ALWAYS AS IDENTITY,
                        name varchar(50) not null,
                        description varchar(200) not null,
                        price NUMERIC(10,2) not null,
                        stock_quantity int default 0 check(stock_quantity >= 0),
                        sku varchar(20) not null unique,
                        active boolean default true not null,
                        created_at timestamp with time zone not null default current_timestamp,
                        updated_at timestamp with time zone not null default current_timestamp
);