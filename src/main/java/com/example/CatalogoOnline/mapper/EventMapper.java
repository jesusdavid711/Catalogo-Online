package com.example.CatalogoOnline.mapper;

import org.springframework.stereotype.Component;

import com.example.CatalogoOnline.dto.EventDTO;
import com.example.CatalogoOnline.entity.CategoryEntity;
import com.example.CatalogoOnline.entity.EventEntity;
import com.example.CatalogoOnline.entity.VenueEntity;

import java.util.stream.Collectors;

@Component
public class EventMapper {

    public EventDTO toDTO(EventEntity entity) {
        if (entity == null)
            return null;

        EventDTO dto = new EventDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setEventDate(entity.getEventDate());
        dto.setCategories(
                entity.getCategories() != null ? entity.getCategories().stream()
                        .map(CategoryEntity::getName)
                        .collect(Collectors.toList()) : null);

        if (entity.getVenue() != null) {
            dto.setVenueId(entity.getVenue().getId());
            dto.setVenueName(entity.getVenue().getName());
            dto.setCapacity(entity.getVenue().getCapacity());
        }

        return dto;
    }

    public EventEntity toEntity(EventDTO dto, VenueEntity venue) {
        if (dto == null)
            return null;

        EventEntity entity = EventEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .venue(venue)
                .build();

        // Categories are handled separately in the service layer
        return entity;
    }
}
