create table cart(
                     cart_id BIGINT primary key GENERATED ALWAYS AS IDENTITY,
                     user_id BIGINT not null unique,
                     created_at timestamp with time zone not null default current_timestamp,
                     updated_at timestamp with time zone not null default current_timestamp
);

create table cart_items(
                           cart_item_id BIGINT primary key GENERATED ALWAYS AS IDENTITY,
                           cart_id BIGINT not null references cart(cart_id),
                           product_id BIGINT not null,
                           quantity int not null check(quantity >= 1),
                           constraint uk_cart_items_cart_product unique(cart_id, product_id)
);
