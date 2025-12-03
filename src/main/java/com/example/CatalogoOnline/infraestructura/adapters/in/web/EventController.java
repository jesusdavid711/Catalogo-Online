package com.example.CatalogoOnline.infraestructura.adapters.in.web;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.CatalogoOnline.dominio.ports.in.EventUseCase;
import com.example.CatalogoOnline.dominio.ports.in.VenueUseCase;
import com.example.CatalogoOnline.dto.EventDTO;
import com.example.CatalogoOnline.infraestructura.adapters.in.web.mapper.EventDTOMapper;
import com.example.CatalogoOnline.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Events", description = "API for event management")
public class EventController {

    private final EventService eventService; // Keep for pagination backward compatibility
    private final EventUseCase eventUseCase;
    private final VenueUseCase venueUseCase;
    private final EventDTOMapper eventMapper;

    public EventController(EventService eventService, EventUseCase eventUseCase, VenueUseCase venueUseCase,
            EventDTOMapper eventMapper) {
        this.eventService = eventService;
        this.eventUseCase = eventUseCase;
        this.venueUseCase = venueUseCase;
        this.eventMapper = eventMapper;
    }

    @Operation(summary = "Create a new event", description = "Creates a new event in the catalog")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "Duplicate event")
    })
    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@Valid @RequestBody EventDTO eventDTO) {
        // Convert DTO -> domain, delegate to use-case, convert result to DTO
        com.example.CatalogoOnline.dominio.model.Event domain = eventMapper.toDomain(eventDTO);
        com.example.CatalogoOnline.dominio.model.Event created = eventUseCase.createEvent(domain);
        EventDTO response = eventMapper.toDTO(created);
        // completar campos derivados desde venue
        if (created.getVenueId() != null) {
            venueUseCase.getVenueById(created.getVenueId()).ifPresent(v -> {
                response.setVenueName(v.getName());
                response.setCapacity(v.getCapacity());
            });
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get events with pagination and filters", description = "Returns events with support for pagination, sorting and optional filters by city, category and date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated event list retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<Page<EventDTO>> getEventsPaginated(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @PageableDefault(size = 10, sort = "eventDate", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<EventDTO> events;
        // Paginated endpoints keep the existing EventService implementation
        if (city != null || category != null || startDate != null) {
            events = eventService.getEventsWithFilters(city, category, startDate, pageable);
        } else {
            events = eventService.getEventsPaginated(pageable);
        }

        return ResponseEntity.ok(events);
    }

    @Operation(summary = "Obtener todos los eventos sin paginación", description = "Retorna lista completa de eventos (usar solo para datasets pequeños)")
    @ApiResponse(responseCode = "200", description = "Lista completa de eventos")
    @GetMapping("/all")
    public ResponseEntity<List<EventDTO>> getAllEventsNoPagination() {
        // Obtener todos a través del caso de uso y mapear a DTO
        java.util.List<com.example.CatalogoOnline.dominio.model.Event> domains = eventUseCase.getAllEvents();
        java.util.List<EventDTO> dtos = domains.stream().map(d -> {
            EventDTO dto = eventMapper.toDTO(d);
            if (d.getVenueId() != null) {
                venueUseCase.getVenueById(d.getVenueId()).ifPresent(v -> {
                    dto.setVenueName(v.getName());
                    dto.setCapacity(v.getCapacity());
                });
            }
            return dto;
        }).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener un evento por ID", description = "Retorna un evento específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        com.example.CatalogoOnline.dominio.model.Event event = eventUseCase.getEventById(id)
                .orElseThrow(() -> new com.example.CatalogoOnline.exception.NotFoundException(
                        "Evento no encontrado con ID: " + id));
        EventDTO dto = eventMapper.toDTO(event);
        if (event.getVenueId() != null) {
            venueUseCase.getVenueById(event.getVenueId()).ifPresent(v -> {
                dto.setVenueName(v.getName());
                dto.setCapacity(v.getCapacity());
            });
        }
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Actualizar un evento", description = "Actualiza los datos de un evento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Evento duplicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO) {
        com.example.CatalogoOnline.dominio.model.Event domain = eventMapper.toDomain(eventDTO);
        com.example.CatalogoOnline.dominio.model.Event updated = eventUseCase.updateEvent(id, domain);
        EventDTO updatedDto = eventMapper.toDTO(updated);
        if (updated.getVenueId() != null) {
            venueUseCase.getVenueById(updated.getVenueId()).ifPresent(v -> {
                updatedDto.setVenueName(v.getName());
                updatedDto.setCapacity(v.getCapacity());
            });
        }
        return ResponseEntity.ok(updatedDto);
    }

    @Operation(summary = "Eliminar un evento", description = "Elimina un evento del catálogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventUseCase.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Contar eventos", description = "Retorna el número total de eventos")
    @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    @GetMapping("/count")
    public ResponseEntity<Long> countEvents() {
        long count = eventUseCase.countEvents();
        return ResponseEntity.ok(count);
    }
}
