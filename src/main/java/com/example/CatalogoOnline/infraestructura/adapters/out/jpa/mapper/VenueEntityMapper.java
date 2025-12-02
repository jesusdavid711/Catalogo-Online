package com.example.CatalogoOnline.infraestructura.adapters.out.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.CatalogoOnline.dominio.model.Venue;
import com.example.CatalogoOnline.entity.VenueEntity;

@Mapper(componentModel = "spring")
public interface VenueEntityMapper {

    @Mapping(target = "eventIds", expression = "java(entity.getEvents() == null ? null : entity.getEvents().stream().map(e -> e.getId()).toList())")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    Venue toDomain(VenueEntity entity);

    @Mapping(target = "events", ignore = true)
    // createdAt/updatedAt handled by JPA lifecycle
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VenueEntity toEntity(Venue domain);
}
