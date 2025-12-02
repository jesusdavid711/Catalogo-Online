# Catalogo-Online

A Spring Boot application for managing an online catalog of events and venues. This project implements a **Hexagonal Architecture** (Ports & Adapters) to achieve clean separation of concerns, technology independence, and enhanced testability.

## Architecture Overview

The application is organized into three main layers:

### 1. **Domain Layer** (`dominio/`)
- **Models**: Pure business entities (`Event`, `Venue`) without any infrastructure dependencies
- **Ports**: Interfaces defining contracts for use-cases (in) and repositories (out)
  - `EventUseCase`, `VenueUseCase`: Define CRUD and business operations
  - `EventRepositoryPort`, `VenueRepositoryPort`: Abstract data access

### 2. **Application Layer** (`aplicacion/`)
- **Use-Cases**: Concrete implementations of business logic
  - `EventUseCaseImpl`, `VenueUseCaseImpl`: Orchestrate between ports and domain models

### 3. **Infrastructure Layer** (`infraestructura/`)
- **Adapters (In)**: Web controllers in `adapters/in/web/`
  - `EventController`, `VenueController`: REST endpoints, handle HTTP and DTO mapping
- **Adapters (Out)**: JPA persistence in `adapters/out/jpa/`
  - `EventJpaAdapter`, `VenueJpaAdapter`: Implement repository ports, bridge to JPA entities
- **Mappers**: MapStruct mappers for transformations
  - DTOs ↔ Domain models ↔ JPA entities

## Key Components

| Component | Purpose |
|-----------|---------|
| `EventDTO`, `VenueDTO` | Data Transfer Objects for API requests/responses |
| `Event`, `Venue` (domain models) | Pure domain objects, framework-agnostic |
| `EventEntity`, `VenueEntity` | JPA entity objects for database persistence |
| `*Mapper` (MapStruct) | Automatic mapping between DTOs, domain, and entities |
| `*UseCase` | Business logic orchestration |
| `*Repository` (JPA) | Spring Data repositories for database access |
| `GlobalExceptionHandler` | Centralized exception handling for REST API |

## API Endpoints

### Events

- `POST /api/events` - Create a new event
- `GET /api/events` - Get paginated events (with filters: city, category, date)
- `GET /api/events/{id}` - Get event by ID
- `PUT /api/events/{id}` - Update an event
- `DELETE /api/events/{id}` - Delete an event
- `GET /api/events/count` - Count total events

### Venues

- `POST /api/venues` - Create a new venue
- `GET /api/venues` - Get all venues
- `GET /api/venues/{id}` - Get venue by ID
- `PUT /api/venues/{id}` - Update a venue
- `DELETE /api/venues/{id}` - Delete a venue
- `GET /api/venues/count` - Count total venues

## Building and Running

### Prerequisites
- Java 17 or later
- Maven 3.8.1 or later

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080` (configurable in `application.properties`).

### Run Tests
```bash
mvn test
```

### Swagger/OpenAPI Documentation
Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

## Technology Stack

- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Build**: Maven
- **Database**: H2 (in-memory for tests), PostgreSQL/MySQL configurable for production
- **Mapping**: MapStruct 1.5.5
- **Testing**: JUnit 5, Mockito
- **Documentation**: Springdoc OpenAPI (Swagger)

## Project Structure

```
src/
├── main/
│   ├── java/com/example/CatalogoOnline/
│   │   ├── dominio/               # Domain layer
│   │   │   ├── model/             # Pure domain objects
│   │   │   └── ports/             # In/Out port interfaces
│   │   ├── aplicacion/            # Application layer
│   │   │   └── usecase/           # Use-case implementations
│   │   ├── infraestructura/       # Infrastructure layer
│   │   │   └── adapters/
│   │   │       ├── in/web/        # Web adapters (controllers)
│   │   │       └── out/jpa/       # JPA adapters (repositories)
│   │   ├── entity/                # JPA entities
│   │   ├── dto/                   # Data Transfer Objects
│   │   ├── repository/            # Spring Data JPA repositories
│   │   ├── service/               # Legacy services (for pagination)
│   │   ├── exception/             # Exception handling
│   │   └── controller/            # Legacy controllers (reference only)
│   └── resources/
│       ├── application.properties
│       ├── application-dev.properties
│       └── application-test.properties
└── test/
    └── java/com/example/CatalogoOnline/
        └── [Integration and unit tests]
```

## Design Patterns

1. **Hexagonal Architecture**: Isolates business logic from external concerns (web, database)
2. **Ports & Adapters**: Defines clear contracts between layers
3. **Dependency Inversion**: High-level modules depend on abstractions, not low-level modules
4. **Mapper Pattern**: Automatic transformation between layer objects (MapStruct)
5. **Repository Pattern**: Abstract data access layer

## Backward Compatibility

The refactor maintains **functional equivalence** with the original CRUD API:
- All existing endpoints remain unchanged
- All business logic is preserved
- Pagination and filtering capabilities are retained
- New architecture is transparent to API clients

## Testing Strategy

- **Unit Tests**: Test use-cases and business logic in isolation
- **Integration Tests**: Test end-to-end flows (controller → use-case → adapter → database)
- **Mapper Tests**: Verify correct transformation between layers

## Configuration

### Profiles

- `dev`: Development environment (H2 in-memory database)
- `test`: Testing environment (H2 with test-specific settings)
- `prod`: Production environment (configured externally)

Set active profile:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

## Contributing

When making changes:
1. Keep business logic in the domain layer
2. Use ports to abstract external dependencies
3. Implement adapters for new input/output channels
4. Write tests for new features
5. Maintain English comments and documentation

## License

This project is part of a Software Engineering course assignment.
