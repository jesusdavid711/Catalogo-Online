# Hexagonal Architecture (Ports & Adapters) - Refactor HU3

Executive Summary
-----------------
Applied refactor: clear separation between Domain (models and ports), Application (use cases) and Infrastructure (web/JPA adapters, configuration). We maintained functional equivalence with existing endpoints.

Created Structure
-----------------
- `dominio/` - models and ports (no Spring/JPA dependency)
  - `dominio/model` - domain models (`Event`, `Venue`)
  - `dominio/ports/in` - input ports (use case interfaces)
  - `dominio/ports/out` - output ports (repositories)

- `aplicacion/usecase` - use case implementations that depend on ports (interfaces)
  - `EventUseCaseImpl`, `VenueUseCaseImpl`

- `infraestructura/adapters/in/web` - REST adapters, DTO<->domain mappers (MapStruct)

- `infraestructura/adapters/out/jpa` - persistence adapters that implement ports and use existing JPA repositories

Key Technical Decisions
-------------------------
- MapStruct for automatic mapping between entities/DTOs and domain models (avoids manual code and errors).
- We kept existing JPA entities and created JPA adapters that convert them to domain. Reduces risk when refactoring.
- Use cases (application) depend only on ports (interfaces). This allows substituting persistence without touching business logic.
- For compatibility, pagination in endpoints continues to use the existing implementation in `EventService` (incremental transition).

Acceptance Criteria (AC)
----------------------------
- [x] The application maintains the same functional behavior as before the refactor.
- [x] The domain is completely decoupled from frameworks or persistence technology.
- [x] Correct use of ports and adapters is evident.
- [x] MapStruct performs conversion between entity and domain.
- [x] The REST API continues to work without breaking endpoints.
- [x] Project documentation reflects the new architecture.

How to Validate Locally
-----------------------
1. Compile and run tests:

```bash
mvn -DskipTests=false clean test
```

2. Run the application:

```bash
mvn spring-boot:run
```

3. Test endpoints (examples):

- GET `/api/events` (paginated)
- POST `/api/venues` (create)

Notes and Next Steps
-----------------------
- Replace use of `EventService` in pagination with a use-case adapter that abstracts `Pageable` from the domain.
- Add integration tests that validate JPA adapters with H2.
- Update README with diagrams and payload examples.

