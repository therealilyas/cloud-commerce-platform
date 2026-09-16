# Roadmap

## v0.1 — Portfolio MVP

- [x] Product, inventory, order, payment, and gateway services
- [x] PostgreSQL migrations
- [x] Kafka order/payment flow
- [x] Order idempotency and stock concurrency control
- [x] Transactional outbox
- [x] Docker Compose, Prometheus, and Grafana
- [x] Helm, Argo CD manifest, CI, and GHCR release workflow

## v0.2 — Complete Saga

- [ ] Inventory release compensation
- [ ] Dead-letter topics and retry policy
- [ ] Multi-item orders
- [ ] Integration tests with Testcontainers
- [ ] Contract tests

## v0.3 — Identity and security

- [ ] Keycloak OIDC
- [ ] Customer/admin roles
- [ ] SOPS or External Secrets
- [ ] Trivy, SBOM, image signing, and admission policy

## v0.4 — Full observability

- [ ] OpenTelemetry traces and Tempo
- [ ] Loki structured logs
- [ ] RED/USE dashboards and alert rules
- [ ] SLOs and error-budget reporting

## v1.0 — Homelab production exercise

- [ ] Terraform Proxmox provisioning
- [ ] Ansible bootstrap
- [ ] K3s high-availability cluster
- [ ] Backup, restore, and documented chaos drill
- [ ] Public demo through a protected tunnel
