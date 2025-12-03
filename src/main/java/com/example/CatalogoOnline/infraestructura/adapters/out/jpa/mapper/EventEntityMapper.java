package com.example.CatalogoOnline.infraestructura.adapters.out.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.CatalogoOnline.dominio.model.Event;
import com.example.CatalogoOnline.entity.CategoryEntity;
import com.example.CatalogoOnline.entity.EventEntity;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EventEntityMapper {

    @Mapping(source = "venue.id", target = "venueId")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "categories", target = "categories", qualifiedByName = "categoriesToNames")
    Event toDomain(EventEntity entity);

    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "categories", ignore = true) // Handled in adapter
    // createdAt/updatedAt are handled by JPA lifecycle callbacks
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EventEntity toEntity(Event domain);

    /**
     * Converts Set of CategoryEntity to List of category names (String).
     * Used when mapping from Entity to Domain.
     */
    @Named("categoriesToNames")
    default List<String> categoriesToNames(Set<CategoryEntity> categories) {
        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }
        return categories.stream()
                .map(CategoryEntity::getName)
                .collect(Collectors.toList());
    }
}
