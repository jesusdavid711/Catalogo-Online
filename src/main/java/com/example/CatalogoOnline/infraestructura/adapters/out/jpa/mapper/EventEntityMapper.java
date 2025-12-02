package com.example.CatalogoOnline.infraestructura.adapters.out.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.CatalogoOnline.dominio.model.Event;
import com.example.CatalogoOnline.entity.EventEntity;

@Mapper(componentModel = "spring")
public interface EventEntityMapper {

    @Mapping(source = "venue.id", target = "venueId")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    Event toDomain(EventEntity entity);

    @Mapping(target = "venue", ignore = true)
    // createdAt/updatedAt are handled by JPA lifecycle callbacks
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EventEntity toEntity(Event domain);
}
