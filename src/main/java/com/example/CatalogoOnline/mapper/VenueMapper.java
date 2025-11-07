package com.example.CatalogoOnline.mapper;

import org.springframework.stereotype.Component;

import com.example.CatalogoOnline.dto.VenueDTO;
import com.example.CatalogoOnline.entity.VenueEntity;

@Component
public class VenueMapper {
    
    public VenueDTO toDTO(VenueEntity entity) {
        if (entity == null) return null;
        
        return new VenueDTO(
            entity.getId(),
            entity.getName(),
            entity.getAddress(),
            entity.getCity(),
            entity.getCountry(),
            entity.getCapacity(),
            entity.getType(),
            entity.getPhone(),
            entity.getEmail()
        );
    }
    
    public VenueEntity toEntity(VenueDTO dto) {
        if (dto == null) return null;
        
        return VenueEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .city(dto.getCity())
                .country(dto.getCountry())
                .capacity(dto.getCapacity())
                .type(dto.getType())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .build();
    }
}
