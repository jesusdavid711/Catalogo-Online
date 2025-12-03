# Hexagonal Architecture (Ports & Adapters) - Catalogo-Online

## Overview

This project implements a **Hexagonal Architecture** (also known as Ports & Adapters), ensuring clean separation between business logic, infrastructure, and external interfaces. The architecture promotes testability, maintainability, and technology independence.

**Current Version:** HU3 + HU4 Complete  
**Last Updated:** 2025-12-02

---

## Architecture Layers

```
┌─────────────────────────────────────────────────────────────┐
│                     External Systems                        │
│              (REST Clients, Database, etc.)                 │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                   INFRASTRUCTURE LAYER                      │
│  ┌───────────────────┐           ┌──────────────────────┐  │
│  │  REST Adapters    │           │   JPA Adapters       │  │
│  │  (Controllers)    │           │   (Persistence)      │  │
│  │   - EventController          - EventJpaAdapter       │  │
│  │   - VenueController          - VenueJpaAdapter       │  │
│  │   + DTO Mappers              + Entity Mappers        │  │
│  └───────────────────┘           └──────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     APPLICATION LAYER                       │
│  ┌───────────────────────────────────────────────────────┐ │
│  │              Use Case Implementations                 │ │
│  │   - EventUseCaseImpl                                  │ │
│  │   - VenueUseCaseImpl                                  │ │
│  └───────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      DOMAIN LAYER                           │
│  ┌──────────────┐         ┌──────────────────────────────┐ │
│  │    Models    │         │          Ports               │ │
│  │  - Event     │         │  ┌────────────────────────┐  │ │
│  │  - Venue     │         │  │ IN (Use Cases)         │  │ │
│  └──────────────┘         │  │  - EventUseCase        │  │ │
│                           │  │  - VenueUseCase        │  │ │
│                           │  └────────────────────────┘  │ │
│                           │  ┌────────────────────────┐  │ │
│                           │  │ OUT (Repositories)     │  │ │
│                           │  │  - EventRepositoryPort │  │ │
│                           │  │  - VenueRepositoryPort │  │ │
│                           │  └────────────────────────┘  │ │
│                           └──────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

---

## Directory Structure

```
src/main/java/com/example/CatalogoOnline/
│
├── dominio/                              # DOMAIN LAYER (Core)
│   ├── model/                            # Domain models (POJO)
│   │   ├── Event.java                    # Event domain model
│   │   └── Venue.java                    # Venue domain model
│   │
│   └── ports/                            # Port interfaces
│       ├── in/                           # Input ports (use cases)
│       │   ├── EventUseCase.java
│       │   └── VenueUseCase.java
│       │
│       └── out/                          # Output ports (repositories)
│           ├── EventRepositoryPort.java
│           └── VenueRepositoryPort.java
│
├── aplicacion/                           # APPLICATION LAYER
│   └── usecase/                          # Use case implementations
│       ├── EventUseCaseImpl.java
│       └── VenueUseCaseImpl.java
│
├── infraestructura/                      # INFRASTRUCTURE LAYER
│   ├── adapters/
│   │   ├── in/web/                       # REST input adapters
│   │   │   ├── EventController.java
│   │   │   ├── VenueController.java
│   │   │   └── mapper/                   # DTO ↔ Domain mappers
│   │   │       ├── EventDTOMapper.java   # (MapStruct)
│   │   │       └── VenueDTOMapper.java
│   │   │
│   │   └── out/jpa/                      # JPA output adapters
│   │       ├── EventJpaAdapter.java
│   │       ├── VenueJpaAdapter.java
│   │       └── mapper/                   # Entity ↔ Domain mappers
│   │           ├── EventEntityMapper.java # (MapStruct)
│   │           └── VenueEntityMapper.java
│   │
│   ├── config/                           # Spring configuration
│   │   └── SwaggerConfig.java
│   │
│   └── exception/                        # Global exception handling
│       └── GlobalExceptionHandler.java
│
├── entity/                               # JPA entities (persistence layer)
│   ├── EventEntity.java
│   ├── VenueEntity.java
│   └── CategoryEntity.java               # HU4: ManyToMany
│
├── repository/                           # Spring Data JPA repositories
│   ├── EventRepository.java              # HU4: + Specifications
│   ├── VenueRepository.java              # HU4: + JOIN FETCH
│   ├── CategoryRepository.java           # HU4: New
│   └── EventSpecifications.java          # HU4: Dynamic filters
│
├── dto/                                  # Data Transfer Objects
│   ├── EventDTO.java                     # HU4: List<String> categories
│   └── VenueDTO.java
│
└── service/                              # Legacy services (maintained)
    ├── EventService.java                 # HU4: Uses Specifications
    └── VenueService.java

src/main/resources/
└── db/migration/                         # HU4: Flyway migrations
    └── V1__init.sql                      # Initial schema
```

---

## Key Principles

### 1. Dependency Rule
**Dependencies point inward:**
```
Infrastructure → Application → Domain
Domain has ZERO dependencies on outer layers
```

### 2. Domain Independence
The `dominio/` package contains:
- Pure Java POJOs (no Spring, no JPA annotations)
- Business logic (if any)
- Port interfaces (contracts)

**Example:**
```java
// Event.java - Pure domain model
@Data
@Builder
public class Event {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime eventDate;
    private Long venueId;                  // Reference by ID, not entity
    private List<String> categories;       // List of names, not entities
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### 3. Ports (Interfaces)

**Input Ports (Use Cases):**
```java
public interface EventUseCase {
    Event createEvent(Event event);
    Optional<Event> getEventById(Long id);
    List<Event> getAllEvents();
    Event updateEvent(Long id, Event event);
    void deleteEvent(Long id);
    long countEvents();
}
```

**Output Ports (Repositories):**
```java
public interface EventRepositoryPort {
    Event save(Event event);
    Optional<Event> findById(Long id);
    List<Event> findAll();
    void deleteById(Long id);
    long count();
}
```

### 4. Adapters

**REST Adapter (Input):**
```java
@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventUseCase eventUseCase;
    private final EventDTOMapper eventMapper;
    
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO dto) {
        Event domain = eventMapper.toDomain(dto);
        Event created = eventUseCase.createEvent(domain);
        return ResponseEntity.ok(eventMapper.toDTO(created));
    }
}
```

**JPA Adapter (Output):**
```java
@Component
@Transactional(readOnly = true)
public class EventJpaAdapter implements EventRepositoryPort {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventEntityMapper mapper;
    
    @Override
    @Transactional
    public Event save(Event event) {
        EventEntity entity = mapper.toEntity(event);
        
        // Handle ManyToMany categories (HU4)
        Set<CategoryEntity> categories = event.getCategories().stream()
            .map(name -> categoryRepository.findByName(name)
                .orElseGet(() -> categoryRepository.save(
                    CategoryEntity.builder().name(name).build()
                )))
            .collect(Collectors.toSet());
        entity.setCategories(categories);
        
        return mapper.toDomain(eventRepository.save(entity));
    }
}
```

---

## HU4: Advanced JPA Features

### Relationship Mapping

**One-to-Many (Venue → Events):**
```java
@Entity
public class VenueEntity {
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, 
               orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<EventEntity> events;
}
```

**Many-to-Many (Event ↔ Category):**
```java
@Entity
public class EventEntity {
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "event_categories",
               joinColumns = @JoinColumn(name = "event_id"),
               inverseJoinColumns = @JoinColumn(name = "category_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CategoryEntity> categories;
}
```

### Query Optimization (N+1 Elimination)

**EntityGraph for Single Query:**
```java
@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>,
                                         JpaSpecificationExecutor<EventEntity> {
    
    @Override
    @EntityGraph(attributePaths = {"venue", "categories"})
    Page<EventEntity> findAll(Pageable pageable);
    
    @EntityGraph(attributePaths = {"venue", "categories"})
    @Query("SELECT e FROM EventEntity e WHERE e.id = :id")
    Optional<EventEntity> findByIdWithRelationships(@Param("id") Long id);
}
```

**Generated SQL (Before vs After):**
```sql
-- BEFORE (N+1 problem):
SELECT * FROM events;                    -- 1 query
SELECT * FROM venues WHERE id = 1;       -- N queries
SELECT * FROM event_categories WHERE event_id = 1; -- N queries

-- AFTER (Single query):
SELECT e.*, v.*, c.* 
FROM events e
LEFT JOIN venues v ON e.venue_id = v.id
LEFT JOIN event_categories ec ON e.id = ec.event_id
LEFT JOIN categories c ON ec.category_id = c.id;
```

### Dynamic Filters with Specifications

```java
public class EventSpecifications {
    public static Specification<EventEntity> withDynamicFilters(
        String city, String category, LocalDateTime startDate) {
        
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            if (city != null) {
                predicates.add(cb.equal(root.get("venue").get("city"), city));
            }
            if (category != null) {
                predicates.add(cb.equal(root.join("categories").get("name"), category));
                query.distinct(true);
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), startDate));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
```

**Usage:**
```java
Specification<EventEntity> spec = EventSpecifications.withDynamicFilters(
    "Bogotá", "Music", LocalDateTime.now()
);
Page<EventEntity> results = eventRepository.findAll(spec, pageable);
```

---

## Database Versioning (Flyway)

### Configuration
```properties
# application.properties
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
```

### Migration Script Structure
```
src/main/resources/db/migration/
└── V1__init.sql  # Initial schema with all tables and constraints
```

**Execution Flow:**
1. Flyway runs migrations on startup
2. Hibernate validates schema against entities
3. Application starts if validation passes

---

## Mapping Strategy (MapStruct)

### DTO ↔ Domain
```java
@Mapper(componentModel = "spring")
public interface EventDTOMapper {
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Event toDomain(EventDTO dto);
    
    @Mapping(target = "venueName", ignore = true)
    @Mapping(target = "capacity", ignore = true)
    EventDTO toDTO(Event domain);
}
```

### Entity ↔ Domain
```java
@Mapper(componentModel = "spring")
public interface EventEntityMapper {
    @Mapping(source = "venue.id", target = "venueId")
    @Mapping(source = "categories", target = "categories", 
             qualifiedByName = "categoriesToNames")
    Event toDomain(EventEntity entity);
    
    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EventEntity toEntity(Event domain);
    
    @Named("categoriesToNames")
    default List<String> categoriesToNames(Set<CategoryEntity> categories) {
        return categories.stream()
            .map(CategoryEntity::getName)
            .collect(Collectors.toList());
    }
}
```

---

## Testing Strategy

### 1. Unit Tests (Domain/Use Cases)
```java
@ExtendWith(MockitoExtension.class)
class EventUseCaseImplTest {
    @Mock
    private EventRepositoryPort eventRepository;
    
    @InjectMocks
    private EventUseCaseImpl eventUseCase;
    
    @Test
    void shouldCreateEvent() {
        // Test use case logic without infrastructure
    }
}
```

### 2. Integration Tests (Adapters)
```java
@DataJpaTest
@ActiveProfiles("test")
class EventSpecificationsTest {
    @Autowired
    private EventRepository eventRepository;
    
    @Test
    void shouldFilterByCityAndCategory() {
        // Test JPA queries and Specifications
    }
}
```

### 3. End-to-End Tests (REST)
```java
@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void shouldCreateEventWithVenue() throws Exception {
        // Test full request → response cycle
    }
}
```

---

## Benefits

### Architecture (HU3)
- ✅ **Technology Independence:** Domain can be ported to any framework
- ✅ **Testability:** Use cases testable without Spring/Database
- ✅ **Maintainability:** Changes in one layer don't affect others
- ✅ **Scalability:** Easy to add new adapters (GraphQL, gRPC, etc.)

### Performance (HU4)
- ✅ **Optimized Queries:** N+1 problem eliminated
- ✅ **Flexible Filtering:** Type-safe dynamic queries
- ✅ **Reduced Database Load:** Fewer round-trips

### Quality (HU4)
- ✅ **Schema Validation:** Hibernate validates against Flyway migrations
- ✅ **Version Control:** Database schema tracked in Git
- ✅ **Data Integrity:** Foreign key constraints enforced

---

## API Documentation

**Swagger UI:** `http://localhost:8080/swagger-ui.html`  
**OpenAPI Spec:** `http://localhost:8080/api-docs`  
**H2 Console:** `http://localhost:8080/h2-console`

---

## Next Evolution Steps

1. **Domain-Driven Design:**
   - Add Value Objects
   - Implement Aggregates
   - Domain Events

2. **CQRS Pattern:**
   - Separate read/write models
   - Optimize queries independently

3. **Event Sourcing:**
   - Capture state changes as events
   - Rebuild state from event log

4. **Microservices:**
   - Split into bounded contexts
   - Independent deployment

---

**Maintained By:** Development Team  
**Architecture Pattern:** Hexagonal (Ports & Adapters)  
**Last Review:** 2025-12-02
