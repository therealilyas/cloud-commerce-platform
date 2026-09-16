#!/usr/bin/env sh
set -eu

BASE_URL="${BASE_URL:-http://localhost:8080}"
CUSTOMER_ID="00000000-0000-0000-0000-000000000111"
RUN_ID="$(date +%s)"

product_json=$(curl -fsS -X POST "$BASE_URL/api/products" -H 'Content-Type: application/json' \
  -d "{\"sku\":\"DEVOPS-$RUN_ID\",\"name\":\"DevOps Handbook\",\"description\":\"Portfolio smoke-test product\",\"price\":49.90}")
product_id=$(printf '%s' "$product_json" | sed -n 's/.*"id":"\([^"]*\)".*/\1/p')

curl -fsS -X POST "$BASE_URL/api/inventory/$product_id/stock" -H 'Content-Type: application/json' -d '{"quantity":10}' >/dev/null

order_json=$(curl -fsS -X POST "$BASE_URL/api/orders" \
  -H 'Content-Type: application/json' -H "Idempotency-Key: smoke-$(date +%s)" \
  -d "{\"customerId\":\"$CUSTOMER_ID\",\"productId\":\"$product_id\",\"quantity\":1,\"unitPrice\":49.90}")

printf 'Created product: %s\nCreated order: %s\n' "$product_json" "$order_json"
