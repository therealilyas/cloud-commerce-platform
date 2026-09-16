# Senior Java Engineer Readiness

## Qisqa hukm

Bu repository bilan Senior Java Engineer vakansiyasiga apply qilish mumkin. Lekin project’ni hozirgi holatida “production complete” deb ko‘rsatish to‘g‘ri emas.

Hozirgi real baho:

| Yo‘nalish | Baho | Izoh |
|---|---:|---|
| Java/Spring architecture | 7/10 | Multi-module, JPA, validation, events mavjud |
| Distributed systems | 6/10 | Kafka, outbox va idempotency bor; Saga hali to‘liq emas |
| Testing | 3/10 | Build ishlaydi, lekin integration/concurrency/contract testlar yetishmaydi |
| Data consistency | 6/10 | Stock lock va order outbox bor; payment outbox va compensation kerak |
| Security | 4/10 | Container hardening va scan bor; OIDC/RBAC yo‘q |
| Observability | 5/10 | Metrics bor; logs/traces/SLO/alerts kerak |
| DevOps/GitOps | 8/10 | Docker, Helm, Argo CD va CI/CD kuchli |
| Operations | 5/10 | Runbook bor; backup/restore va incident evidence kerak |

Umumiy: taxminan **60–70% Senior portfolio evidence**.

## Hozir nimani isbotlaydi?

- Strong Middle yoki Middle+ Java Backend;
- Java + DevOps/Platform yo‘nalishiga kuchli qiziqish;
- microservice va event-driven architecture asoslarini bilish;
- CI/CD, container va Kubernetes bilan ishlash;
- technical documentation yozish.

## Hali nimani isbotlamaydi?

- katta production tizimiga egalik qilish;
- real incident response tajribasi;
- high-load benchmark natijalari;
- security architecture;
- complete distributed transaction recovery;
- team leadership va architecture governance.

## Apply qilishdan oldin minimum

- [ ] `ZERO_TO_COMPLETE_GUIDE_UZ.md` bo‘yicha project’ni o‘zingiz run qiling;
- [ ] kodning har bir service’ini tushuntira oling;
- [ ] Saga compensation qo‘shing;
- [ ] payment outbox qo‘shing;
- [ ] Testcontainers integration test yozing;
- [ ] concurrent stock reservation test yozing;
- [ ] client price’iga ishonishni olib tashlang;
- [ ] OpenAPI qo‘shing;
- [ ] bitta incident demo va runbook evidence tayyorlang;
- [ ] 5–10 daqiqalik inglizcha project demo tayyorlang.

## CV uchun to‘g‘ri ifoda

> Built a production-style Java 21 microservices platform using Spring Boot, PostgreSQL and Kafka, implementing idempotent order processing, pessimistic stock locking and a transactional outbox; automated delivery with GitHub Actions, Docker, Helm and Argo CD, with Prometheus/Grafana observability and Trivy security scanning.

“Production-grade and complete” deb yozmang, toki P0 correctness va security vazifalari tugamaguncha.

## Senior interview’da tayyor bo‘ladigan savollar

1. Nega microservice? Modular monolith yaxshiroq bo‘lmasmidi?
2. Outbox qaysi dual-write muammosini hal qiladi?
3. Nega exactly-once emas, at-least-once + idempotency?
4. Pessimistic lock qachon bottleneck bo‘ladi?
5. Payment fail bo‘lsa inventory qanday qaytariladi?
6. Schema/event evolution qanday boshqariladi?
7. Retry storm va cascading failure qanday oldi olinadi?
8. SLO va error budget qanday aniqlanadi?
9. Zero-downtime migration qanday qilinadi?
10. RPO/RTO qanday o‘lchanadi?

Har bir savolga repository’dagi kod, test, dashboard yoki ADR bilan javob bera olsangiz, project Senior application uchun ancha kuchli dalil bo‘ladi.

