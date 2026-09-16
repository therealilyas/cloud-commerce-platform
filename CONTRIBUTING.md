# Contributing

1. Open an issue for significant behavior changes.
2. Create a focused branch from `main`.
3. Run `./mvnw clean verify` and `helm lint deploy/helm/cloud-commerce`.
4. Update tests, docs, and an ADR when a decision changes architecture.
5. Open a pull request using the template.

Use Conventional Commit prefixes where practical: `feat`, `fix`, `docs`, `test`, `build`, `ci`, `refactor`, or `chore`.

Never commit credentials, `.env`, private keys, production data, or personal information.
