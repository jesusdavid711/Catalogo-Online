package com.example.CatalogoOnline.infraestructura.adapters.in.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.CatalogoOnline.dominio.model.Event;
import com.example.CatalogoOnline.dto.EventDTO;

@Mapper(componentModel = "spring")
public interface EventDTOMapper {

    @Mapping(source = "venueId", target = "venueId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Event toDomain(EventDTO dto);

    @Mapping(source = "venueId", target = "venueId")
    @Mapping(target = "venueName", ignore = true)
    @Mapping(target = "capacity", ignore = true)
    EventDTO toDTO(Event domain);
}
