package com.example.CatalogoOnline.infraestructura.adapters.in.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.CatalogoOnline.dominio.model.Venue;
import com.example.CatalogoOnline.dto.VenueDTO;

@Mapper(componentModel = "spring")
public interface VenueDTOMapper {

    // DTO lacks eventIds/createdAt/updatedAt — ignore those targets when mapping from DTO
    @Mapping(target = "eventIds", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Venue toDomain(VenueDTO dto);

    // domain -> DTO: VenueDTO does not contain eventIds/createdAt/updatedAt, regular mapping is fine
    VenueDTO toDTO(Venue domain);
}
