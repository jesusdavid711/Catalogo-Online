package com.example.CatalogoOnline.infraestructura.adapters.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CatalogoOnline.dominio.ports.in.VenueUseCase;
import com.example.CatalogoOnline.dto.VenueDTO;
import com.example.CatalogoOnline.infraestructura.adapters.in.web.mapper.VenueDTOMapper;
import com.example.CatalogoOnline.service.VenueService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/venues")
@Tag(name = "Venues", description = "API for venue management")
public class VenueController {

    private final VenueService venueService; // Keep for backward compatibility if needed
    private final VenueUseCase venueUseCase;
    private final VenueDTOMapper venueMapper;

    public VenueController(VenueService venueService, VenueUseCase venueUseCase, VenueDTOMapper venueMapper) {
        this.venueService = venueService;
        this.venueUseCase = venueUseCase;
        this.venueMapper = venueMapper;
    }

    @Operation(summary = "Crear un nuevo venue", description = "Crea un nuevo venue en el catálogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venue creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<VenueDTO> createVenue(@Valid @RequestBody VenueDTO venueDTO) {
        com.example.CatalogoOnline.dominio.model.Venue domain = venueMapper.toDomain(venueDTO);
        com.example.CatalogoOnline.dominio.model.Venue created = venueUseCase.createVenue(domain);
        return new ResponseEntity<>(venueMapper.toDTO(created), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todos los venues", description = "Retorna una lista de todos los venues")
    @ApiResponse(responseCode = "200", description = "Lista de venues obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<VenueDTO>> getAllVenues() {
        java.util.List<com.example.CatalogoOnline.dominio.model.Venue> domains = venueUseCase.getAllVenues();
        java.util.List<VenueDTO> dtos = domains.stream().map(venueMapper::toDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener un venue por ID", description = "Retorna un venue específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue encontrado"),
            @ApiResponse(responseCode = "404", description = "Venue no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getVenueById(@PathVariable Long id) {
        com.example.CatalogoOnline.dominio.model.Venue venue = venueUseCase.getVenueById(id)
                .orElseThrow(() -> new com.example.CatalogoOnline.exception.NotFoundException("Venue no encontrado con ID: " + id));
        return ResponseEntity.ok(venueMapper.toDTO(venue));
    }

    @Operation(summary = "Actualizar un venue", description = "Actualiza los datos de un venue existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venue actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Venue no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VenueDTO> updateVenue(@PathVariable Long id, @Valid @RequestBody VenueDTO venueDTO) {
        com.example.CatalogoOnline.dominio.model.Venue domain = venueMapper.toDomain(venueDTO);
        com.example.CatalogoOnline.dominio.model.Venue updated = venueUseCase.updateVenue(id, domain);
        return ResponseEntity.ok(venueMapper.toDTO(updated));
    }

    @Operation(summary = "Eliminar un venue", description = "Elimina un venue del catálogo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venue eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Venue no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueUseCase.deleteVenue(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Contar venues", description = "Retorna el número total de venues")
    @ApiResponse(responseCode = "200", description = "Conteo obtenido exitosamente")
    @GetMapping("/count")
    public ResponseEntity<Long> countVenues() {
        long count = venueUseCase.countVenues();
        return ResponseEntity.ok(count);
    }
}
