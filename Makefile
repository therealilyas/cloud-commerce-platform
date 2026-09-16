.PHONY: build test up down logs smoke

build:
	./mvnw -B clean verify

test:
	./mvnw -B test

up:
	docker compose up --build -d

down:
	docker compose down -v

logs:
	docker compose logs -f --tail=200

smoke:
	./scripts/smoke-test.sh
