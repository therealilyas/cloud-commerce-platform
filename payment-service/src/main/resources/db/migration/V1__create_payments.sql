CREATE TABLE payments (
  id UUID PRIMARY KEY,
  order_id UUID NOT NULL UNIQUE,
  amount NUMERIC(19,2) NOT NULL CHECK (amount > 0),
  status VARCHAR(32) NOT NULL,
  reason VARCHAR(500),
  created_at TIMESTAMPTZ NOT NULL
);
