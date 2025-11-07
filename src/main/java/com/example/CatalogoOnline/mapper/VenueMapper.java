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
        
        VenueEntity entity = new VenueEntity();
        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setCountry(dto.getCountry());
        entity.setCapacity(dto.getCapacity());
        entity.setType(dto.getType());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        
        return entity;
    }
}
