# Food Ordering and Delivery System: Agent Instructions

## Purpose and Scope

This repository contains the backend for a food ordering and delivery system. It is a Java 21 Spring Boot 4.1.1 Maven multi-module application rooted at `com.huy.food`.

Build features for these actors:

- `USER`: browse restaurants and menus, place and track deliveries.
- `RESTAURANT`: manage restaurant details, menu sections/items, and preparation status.
- `SHIPPER`: receive, accept, and complete delivery assignments.
- `ADMIN`: manage accounts and platform-level operations.

Treat the existing `Delivery` aggregate as the order and delivery record unless the product model is deliberately renamed across API, database, and service layers.

## Current Stack

- Java 21 and Maven Wrapper (`mvnw.cmd` on Windows)
- Spring Boot Web, Security, Data JPA, and validation (`jakarta.validation`)
- PostgreSQL on Neon, accessed through the PostgreSQL JDBC driver
- JJWT 0.12.6 with HS256 access and refresh tokens
- MapStruct 1.6.3 and Lombok
- Springdoc OpenAPI for API documentation

Do not introduce a second web framework, ORM, security framework, mapping library, or database without a concrete migration plan and an explicit need.

## Modules

| Module | Responsibility |
| --- | --- |
| `common` | Shared entities, enums, DTOs, exceptions, JWT/security, filters, and configuration. |
| `user-service` | User, shipper, OTP, and authentication features. |
| `restaurant-service` | Restaurant, restaurant section, and menu-item features. |
| `delivery-service` | Delivery, delivery item, and delivery assignment features. |
| `application` | Spring Boot entry point, runtime configuration, integration tests, and executable artifact. |

Modules may depend on `common`; feature modules must not depend on one another. `application` is the composition root and is the only module that depends on every feature module.

## Project Layout and Boundaries

Keep code under `src/main/java/com/huy/food` and follow the existing layers:

| Package | Responsibility |
| --- | --- |
| `controllers` | HTTP route binding, validation, status codes, and response wrapping only. |
| `dtos.requests` / `dtos.responses` | API input and output contracts; never expose JPA entities directly. |
| `services` / `services.impls` | Business rules, authorization decisions, state transitions, and transactions. |
| `repositories` | Spring Data persistence queries; no HTTP concerns. |
| `entities` | JPA mappings and persistence invariants. |
| `mappers` | MapStruct conversion between entities and DTOs. |
| `configs`, `filters`, `securities`, `utils` | Cross-cutting configuration and JWT authentication. |
| `exceptions` | Domain exceptions and centralized error-to-response mapping. |
| `enums` | Persisted role and lifecycle values. |

Use constructor injection with Lombok `@RequiredArgsConstructor`. Keep controllers thin. Give each entity a named service method rather than inheriting generic CRUD behavior; keep feature-specific validation, authorization, and workflow logic in that module service. Put multi-repository writes and order lifecycle changes in a service method annotated with `@Transactional`.

## HTTP API Conventions

- Version public endpoints under `/api/v1`.
- Use request DTOs with bean-validation annotations and `@Valid`; validate IDs, quantities, money, coordinates, and status inputs at the boundary.
- Return `ResponseEntity<ApiResponse<T>>` using the existing `ApiResponse` factories. Use `created(...)` for successful resource creation and `noContent()` only when the response body is intentionally empty.
- Use response DTOs and MapStruct mappers for reads. Configure update mappings to ignore null fields when partial updates are intended.
- Throw `BadRequestException`, `ForbiddenException`, or `NotFoundException` for expected failures so `GlobalExceptionHandler` produces the standard error envelope. Do not catch exceptions in controllers solely to build ad hoc responses.
- Document public endpoints and security requirements in OpenAPI as endpoints are added. Keep Swagger routes publicly accessible.

## Authentication and Authorization

- The JWT filter creates a `UserPrincipal` containing `userId`, `phone`, `name`, and `role`; use the authenticated principal as the source of identity. Never accept an actor ID from the client as authorization proof.
- Access tokens are sent as `Authorization: Bearer <token>`. Preserve the current JWT claims and HS256 signing behavior unless all token issuers and consumers are migrated together.
- Keep authentication separate from authorization. Authenticate globally, then restrict actor-specific routes by `AccountRole` (`USER`, `RESTAURANT`, `SHIPPER`, `ADMIN`) and verify resource ownership in the service layer.
- Public routes must be explicitly allow-listed in `SecurityConfig`; all other routes remain authenticated by default.
- Never log JWTs, OTPs, refresh cookies, database URLs, passwords, or signing keys. Treat OTP generation and refresh-token handling as security-sensitive code; add rate limits, expiry, and one-time consumption before production use.
- In production, issue the refresh cookie with `Secure=true`, `HttpOnly=true`, an appropriate `SameSite` setting, and an explicit refresh/rotation or logout strategy.

## PostgreSQL and Neon

- PostgreSQL is the system of record. Use `UUID` for existing UUID-backed aggregate IDs, `BigDecimal` for prices, fees, distance, and revenue, and `Instant` for timestamps.
- Continue the `BaseEntity` audit and soft-delete model. New soft-deletable entities need the same audit fields and an appropriate `@SQLRestriction("deleted_at IS NULL")`; avoid hard deletes for business data.
- Keep enum persistence as `EnumType.STRING`; never reorder or rely on ordinal enum storage.
- Add indexes for proven query paths, especially foreign keys and status/date combinations. Name indexes consistently with the existing `idx_<table>_<columns>` pattern.
- Do not commit Neon connection strings, passwords, JWT secrets, tokens, or production data. Configure credentials through environment variables or a secret manager, for example `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, and `JWT_SECRET`.
- TLS is required for Neon. Retain PostgreSQL SSL settings in deployed connection configuration.
- `spring.jpa.hibernate.ddl-auto=update` is acceptable only for local development. Before a shared, staging, or production deployment, introduce versioned migrations (for example, Flyway), review generated schema changes, and use migrations as the source of truth.

## Ordering and Delivery Rules

- Re-read menu items and prices from the database when an order is created. Validate that every item is available and belongs to an allowed restaurant/order scope.
- Persist item name, charged price, discount, quantity, and calculated totals as order snapshots. Historical deliveries must not change when a menu item is later edited or removed.
- Calculate monetary totals on the server: subtotal, discount amount, shipping fee, total amount, and shipper revenue. Never trust a client-provided total or delivery status.
- Use a transaction for delivery creation, item persistence, price calculation, assignment acceptance, and state changes. Design concurrent acceptance so only one shipper can claim an offered assignment.
- Preserve the existing `DeliveryStatus` lifecycle: `CREATED`, `CONFIRMED`, `PREPARING`, `READY_FOR_PICKUP`, `PICKED_UP`, `DELIVERING`, `DELIVERED`, and `CANCELLED`. Define and enforce valid forward transitions in one service-level policy; reject invalid skips and terminal-state changes.
- Record `deliveredAt` only when a delivery reaches `DELIVERED`. Record cancellation reason/audit data when cancellation is added.
- Keep restaurant, customer, shipper, and administrator permissions distinct. A user may read only their own deliveries; a restaurant may change only its own order preparation state; a shipper may act only on an assigned delivery.

## Implementation Quality

- Follow the repository's package names, Lombok use, and naming conventions. Keep each change focused on its feature; do not perform unrelated refactors.
- Prefer repository methods that express the required constraint or ownership check over loading broad data sets and filtering in memory.
- Avoid N+1 query paths in delivery lists and details. Use projections, fetch joins, or entity graphs where the response requires related data.
- Add tests with every behavior change: service tests for business and authorization rules, controller/security tests for endpoint access and validation, and repository tests for custom queries.
- Test successful and rejected lifecycle transitions, cross-account access attempts, invalid/expired JWT behavior, money calculations, missing records, and soft-delete visibility when relevant.
- Run `./mvnw.cmd test` on Windows before handing off a change. When touching compilation-sensitive mappers or JPA mappings, run `./mvnw.cmd clean verify` when practical.

## Change Checklist

Before completing a feature, verify that request validation, role/ownership authorization, transaction boundaries, DTO mapping, error responses, OpenAPI documentation, and focused tests are all updated. For any schema or security change, also verify its migration path and configuration does not expose a secret.
