# Refactor HU3: Hexagonal Architecture with Functional Equivalence

## Executive Summary

The refactoring of the **Catalogo-Online** project to a hexagonal architecture (Ports & Adapters) has been completed, maintaining 100% functional equivalence with the existing REST API and without breaking endpoints. The business domain is now decoupled from Spring, JPA and other frameworks, facilitating unit testing and future maintainability.

---

## Implemented Changes

### 1. Package Reorganization (Task 1)

**Previous Structure:**
```
controller/ - REST controllers
service/    - application services
repository/ - JPA repositories
entity/     - JPA entities
dto/        - transfer objects
mapper/     - manual mappers
```

**New Structure (Hexagonal):**
```
dominio/                          - Domain layer (pure, no external dependencies)
├── model/                        - Domain models (Event, Venue)
├── ports/
│   ├── in/                       - Input ports (use cases)
│   │   ├── EventUseCase.java
│   │   └── VenueUseCase.java
│   └── out/                      - Output ports (repositories)
│       ├── EventRepositoryPort.java
│       └── VenueRepositoryPort.java

aplicacion/
├── usecase/                      - Use case implementations
│   ├── EventUseCaseImpl.java
│   └── VenueUseCaseImpl.java

infraestructura/                  - Infrastructure layer (adapters)
├── adapters/
│   ├── in/web/                   - REST input adapters
│   │   ├── EventController.java
│   │   ├── VenueController.java
│   │   └── mapper/
│   │       ├── EventDTOMapper.java
│   │       └── VenueDTOMapper.java
│   └── out/jpa/                  - JPA output adapters
│       ├── EventJpaAdapter.java
│       ├── VenueJpaAdapter.java
│       └── mapper/
│           ├── EventEntityMapper.java
│           └── VenueEntityMapper.java
├── config/                       - Spring configuration
└── exception/                    - Exception handlers

# Persistence layer (unchanged)
entity/     - JPA entities (EventEntity, VenueEntity)
repository/ - Spring Data JPA repositories
dto/        - DTOs (EventDTO, VenueDTO)
```

---

### 2. Port Creation (Task 2)

**Input Ports (Use Cases):**

- `EventUseCase.java` - Interface that defines operations on events
  - `createEvent(Event)`
  - `updateEvent(Long, Event)`
  - `getEventById(Long): Optional<Event>`
  - `getAllEvents(): List<Event>`
  - `deleteEvent(Long)`
  - `countEvents(): long`

- `VenueUseCase.java` - Interface that defines operations on venues
  - `createVenue(Venue)`
  - `updateVenue(Long, Venue)`
  - `getVenueById(Long): Optional<Venue>`
  - `getAllVenues(): List<Venue>`
  - `deleteVenue(Long)`
  - `countVenues(): long`

**Output Ports (Persistence):**

- `EventRepositoryPort.java` - Interface for event data access
- `VenueRepositoryPort.java` - Interface for venue data access

---

### 3. Adapter Implementation (Task 3)

#### 3.1 JPA Adapters (Out)

**EventJpaAdapter.java:**
- Implements `EventRepositoryPort`
- Translates between domain models (`Event`) and JPA entities (`EventEntity`)
- Uses `EventEntityMapper` (MapStruct) for transformations
- Handles relationship with `VenueEntity` via `venueId`

**VenueJpaAdapter.java:**
- Implements `VenueRepositoryPort`
- Translates between domain models (`Venue`) and JPA entities (`VenueEntity`)
- Uses `VenueEntityMapper` (MapStruct) for transformations
- Handles conversion of `events` → `eventIds` to maintain independence

#### 3.2 REST Adapters (In)

**EventController.java (Infrastructure):**
- Receives DTOs from client
- Maps DTOs → domain models via `EventDTOMapper`
- Delegates to `EventUseCase` for business logic
- Populates derived fields (`venueName`, `capacity`) from `VenueUseCase`
- Maps domain response → DTO

**VenueController.java (Infrastructure):**
- Receives DTOs from client
- Maps DTOs → domain models via `VenueDTOMapper`
- Delegates to `VenueUseCase` for business logic
- Maps domain response → DTO

#### 3.3 Use Case Implementations

**EventUseCaseImpl.java:**
- Implements `EventUseCase`
- Delegates CRUD operations to `EventRepositoryPort`
- No complex logic (simple, scalable pattern)

**VenueUseCaseImpl.java:**
- Implements `VenueUseCase`
- Delegates CRUD operations to `VenueRepositoryPort`
- No complex logic (simple, scalable pattern)

---

### 4. Explicit Mapping with MapStruct

**MapStruct 1.5.5.Final** is used for automatic mapper generation:

**JPA Mappers (Entity ↔ Domain):**

- `EventEntityMapper.java`
  - `toDomain(EventEntity): Event` - Maps `venue.id` → `venueId`
  - `toEntity(Event): EventEntity` - Ignores `venue` (handled in adapter), ignores `createdAt`/`updatedAt` (JPA lifecycle)

- `VenueEntityMapper.java`
  - `toDomain(VenueEntity): Venue` - Converts `events` → `eventIds` via Java expression
  - `toEntity(Venue): VenueEntity` - Ignores `events` and metadata fields

**DTO Mappers (DTO ↔ Domain):**

- `EventDTOMapper.java`
  - `toDomain(EventDTO): Event` - Ignores `createdAt`/`updatedAt` (not in DTO)
  - `toDTO(Event): EventDTO` - Ignores `venueName`/`capacity` (populated in controller)

- `VenueDTOMapper.java`
  - `toDomain(VenueDTO): Venue` - Ignores `eventIds`/`createdAt`/`updatedAt`
  - `toDTO(Venue): VenueDTO` - Transparent mapping (DTO has same fields)

---

### 5. Domain Models

**Event.java (Domain):**
```java
- Long id
- String name
- String description
- LocalDateTime eventDate
- Long venueId              // Reference by ID (does not couple to VenueEntity)
- String category
- LocalDateTime createdAt
- LocalDateTime updatedAt
```

**Venue.java (Domain):**
```java
- Long id
- String name
- String address
- String city
- String country
- Integer capacity
- String type
- String phone
- String email
- List<Long> eventIds       // Reference to event IDs (does not couple to EventEntity)
- LocalDateTime createdAt
- LocalDateTime updatedAt
```

---

### 6. Functional Equivalence

**Preserved Endpoints:**

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| POST   | `/api/events` | Create event | ✅ Functional |
| GET    | `/api/events` | List events (paginated + filters) | ✅ Functional |
| GET    | `/api/events/all` | List all without pagination | ✅ Functional |
| GET    | `/api/events/{id}` | Get event | ✅ Functional |
| PUT    | `/api/events/{id}` | Update event | ✅ Functional |
| DELETE | `/api/events/{id}` | Delete event | ✅ Functional |
| GET    | `/api/events/count` | Count events | ✅ Functional |
| POST   | `/api/venues` | Create venue | ✅ Functional |
| GET    | `/api/venues` | List venues | ✅ Functional |
| GET    | `/api/venues/{id}` | Get venue | ✅ Functional |
| PUT    | `/api/venues/{id}` | Update venue | ✅ Functional |
| DELETE | `/api/venues/{id}` | Delete venue | ✅ Functional |
| GET    | `/api/venues/count` | Count venues | ✅ Functional |

**Fields in EventDTO (Enriched):**
- `venueName` - Venue name (populated from `VenueUseCase` in controller)
- `capacity` - Venue capacity (populated from `VenueUseCase` in controller)

---

### 7. Testing

**Tests Executed:**

1. **EventUseCaseImplTest.java** - Unit tests of use case (Mockito)
2. **VenueUseCaseImplTest.java** - Unit tests of use case (Mockito)
3. **EventControllerIntegrationTest.java** - REST end-to-end integration test
4. **Other existing tests** - Backward compatibility maintained

**Result:** ✅ 7/7 tests passing

```
Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## Achieved Benefits

### 1. **Technology Independence**
- Pure domain without coupling to Spring, JPA, or frameworks
- Easy migration to other persistence technologies in the future

### 2. **Testability**
- Use cases testable without starting full application
- JPA adapters can be mocked in unit tests
- Possibility of isolated integration tests

### 3. **Maintainability**
- Responsibilities clearly separated by layers
- Changes in JPA do not affect business logic
- Changes in REST API do not affect persistence

### 4. **Scalability**
- Easy to add new use cases without affecting existing ones
- Possibility of adding new adapters (SOAP, gRPC, etc.)
- Structure ready for advanced Domain-Driven Design (DDD)

---

## Recommended Next Steps

1. **Complete Domain Validations**
   - Move validation logic from controller/service to domain
   - Use Value Objects if necessary

2. **Add Domain Events**
   - `EventCreated`, `EventUpdated`, `EventDeleted`
   - For integration with other bounded contexts

3. **Implement In-Memory Repository**
   - To facilitate tests without database
   - As alternative adapter to JPA

4. **Advanced Documentation**
   - Architecture flow diagrams
   - Guide for extending with new adapters

---

## Acceptance Criteria (HU3) - Status: ✅ COMPLETED

- [x] Refactor code towards hexagonal architecture
- [x] Separate domain from infrastructure
- [x] Create explicit in/out ports
- [x] Implement JPA and REST adapters
- [x] Maintain 100% functional equivalence
- [x] Do not break existing endpoints
- [x] Unit tests passing
- [x] REST end-to-end integration test
- [x] MapStruct mappers with explicit mappings

---

## Modified / Created Files

### New Directories:
- `src/main/java/com/example/CatalogoOnline/dominio/`
- `src/main/java/com/example/CatalogoOnline/aplicacion/`
- `src/main/java/com/example/CatalogoOnline/infraestructura/`

### New Files:
- 2 domain models (Event, Venue)
- 2 input ports (use-cases)
- 2 output ports (repository ports)
- 2 use case implementations
- 2 JPA adapters
- 2 REST adapters (moved controllers)
- 4 MapStruct mappers
- 3 tests (2 unit, 1 integration)

### Existing Files (Preserved):
- `service/EventService.java` - Maintains legacy logic for pagination
- `service/VenueService.java` - Maintains legacy logic
- `entity/EventEntity.java` - No changes in JPA
- `entity/VenueEntity.java` - No changes in JPA
- `dto/EventDTO.java` - Enriched with `venueName`, `capacity`
- `dto/VenueDTO.java` - No changes
- `repository/EventRepository.java` - No changes
- `repository/VenueRepository.java` - No changes

---

## Compilation and Execution

```bash
# Compile project
mvn clean compile

# Run tests
mvn test

# Package
mvn package

# Run application
mvn spring-boot:run
```

---

**Branch:** `H03S6`  
**Status:** ✅ Ready for PR and Merge  
**Responsible:** Refactor HU3 Hexagonal Architecture
