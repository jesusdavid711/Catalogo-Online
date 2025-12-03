package com.example.CatalogoOnline.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EventDTO {

    private Long id;

    @NotBlank(message = "El nombre del evento no puede estar vacío")
    @Size(min = 3, max = 200, message = "El nombre debe tener entre 3 y 200 caracteres")
    private String name;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;

    @NotNull(message = "La fecha del evento no puede ser nula")
    @Future(message = "La fecha del evento debe ser futura")
    private LocalDateTime eventDate;

    private Long venueId;
    private String venueName;
    private Integer capacity;

    private List<String> categories;

    // Constructors, getters and setters
    public EventDTO() {
    }

    public EventDTO(Long id, String name, String description, LocalDateTime eventDate,
            Long venueId, String venueName, Integer capacity, List<String> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventDate = eventDate;
        this.venueId = venueId;
        this.venueName = venueName;
        this.capacity = capacity;
        this.categories = categories;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}