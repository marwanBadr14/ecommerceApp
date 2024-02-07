CREATE TABLE public.order_items (
	order_item_id serial4 NOT NULL,
	item_price numeric(10, 2) NOT NULL,
	order_id int4 NULL,
	product_id int4 NULL,
	quantity int4 NOT NULL,
	CONSTRAINT order_items_pkey PRIMARY KEY (order_item_id)
);

