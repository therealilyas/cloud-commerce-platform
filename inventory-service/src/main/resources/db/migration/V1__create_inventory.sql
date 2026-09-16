CREATE TABLE inventory (
  product_id UUID PRIMARY KEY,
  available INTEGER NOT NULL CHECK (available >= 0),
  version BIGINT NOT NULL DEFAULT 0
);
CREATE TABLE stock_reservations (
  id UUID PRIMARY KEY,
  order_id UUID NOT NULL UNIQUE,
  product_id UUID NOT NULL,
  quantity INTEGER NOT NULL CHECK (quantity > 0)
);
