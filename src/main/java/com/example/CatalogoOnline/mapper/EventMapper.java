package com.example.CatalogoOnline.mapper;

import org.springframework.stereotype.Component;

import com.example.CatalogoOnline.dto.EventDTO;
import com.example.CatalogoOnline.entity.EventEntity;
import com.example.CatalogoOnline.entity.VenueEntity;

@Component
public class EventMapper {
    
    public EventDTO toDTO(EventEntity entity) {
        if (entity == null) return null;
        
        EventDTO dto = new EventDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setEventDate(entity.getEventDate());
        dto.setCategory(entity.getCategory());
        
        if (entity.getVenue() != null) {
            dto.setVenueId(entity.getVenue().getId());
            dto.setVenueName(entity.getVenue().getName());
            dto.setCapacity(entity.getVenue().getCapacity());
        }
        
        return dto;
    }
    
    public EventEntity toEntity(EventDTO dto, VenueEntity venue) {
        if (dto == null) return null;
        
        return EventEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .category(dto.getCategory())
                .venue(venue)
                .build();
    }
}
