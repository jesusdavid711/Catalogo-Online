package com.example.CatalogoOnline.dominio.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime eventDate;
    // Reference by id to avoid direct coupling to infrastructure entities
    private Long venueId;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
