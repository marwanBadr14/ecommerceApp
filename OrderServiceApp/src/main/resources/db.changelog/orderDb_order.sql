CREATE TABLE public.orders (
	order_id serial4 NOT NULL,
	customer_id int4 NULL,
	order_date timestamp(6) NULL,
	total_amount numeric(10, 2) NULL,
	CONSTRAINT orders_pkey PRIMARY KEY (order_id)
);



