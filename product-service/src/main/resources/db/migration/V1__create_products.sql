CREATE TABLE products (
  id UUID PRIMARY KEY,
  sku VARCHAR(64) NOT NULL,
  name VARCHAR(160) NOT NULL,
  description VARCHAR(1000) NOT NULL,
  price NUMERIC(19,2) NOT NULL CHECK (price > 0),
  active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMPTZ NOT NULL
);
CREATE UNIQUE INDEX uk_product_sku ON products(sku);
