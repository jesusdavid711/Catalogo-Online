package com.example.CatalogoOnline.infraestructura.adapters.out.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.CatalogoOnline.dominio.model.Event;
import com.example.CatalogoOnline.dominio.ports.out.EventRepositoryPort;
import com.example.CatalogoOnline.entity.CategoryEntity;
import com.example.CatalogoOnline.entity.EventEntity;
import com.example.CatalogoOnline.repository.CategoryRepository;
import com.example.CatalogoOnline.repository.EventRepository;
import com.example.CatalogoOnline.repository.VenueRepository;
import com.example.CatalogoOnline.infraestructura.adapters.out.jpa.mapper.EventEntityMapper;

import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class EventJpaAdapter implements EventRepositoryPort {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final CategoryRepository categoryRepository;
    private final EventEntityMapper mapper;

    public EventJpaAdapter(EventRepository eventRepository,
            VenueRepository venueRepository,
            CategoryRepository categoryRepository,
            EventEntityMapper mapper) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Event save(Event event) {
        EventEntity entity = mapper.toEntity(event);

        // Handle Venue relationship
        if (event.getVenueId() != null) {
            venueRepository.findById(event.getVenueId()).ifPresent(entity::setVenue);
        }

        // Handle Categories relationship (ManyToMany)
        if (event.getCategories() != null && !event.getCategories().isEmpty()) {
            Set<CategoryEntity> categoryEntities = event.getCategories().stream()
                    .map(categoryName -> categoryRepository.findByName(categoryName)
                            .orElseGet(() -> {
                                // Create new category if it doesn't exist
                                CategoryEntity newCategory = CategoryEntity.builder()
                                        .name(categoryName)
                                        .description("Auto-created category")
                                        .build();
                                return categoryRepository.save(newCategory);
                            }))
                    .collect(Collectors.toSet());
            entity.setCategories(categoryEntities);
        } else {
            entity.setCategories(new HashSet<>());
        }

        EventEntity saved = eventRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventRepository.findByIdWithRelationships(id).map(mapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAllWithRelationships().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public long count() {
        return eventRepository.count();
    }
}
