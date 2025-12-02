package com.example.CatalogoOnline.infraestructura.adapters.out.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.CatalogoOnline.dominio.model.Event;
import com.example.CatalogoOnline.dominio.ports.out.EventRepositoryPort;
import com.example.CatalogoOnline.entity.EventEntity;
import com.example.CatalogoOnline.repository.EventRepository;
import com.example.CatalogoOnline.repository.VenueRepository;
import com.example.CatalogoOnline.infraestructura.adapters.out.jpa.mapper.EventEntityMapper;

@Component
public class EventJpaAdapter implements EventRepositoryPort {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final EventEntityMapper mapper;

    public EventJpaAdapter(EventRepository eventRepository, VenueRepository venueRepository, EventEntityMapper mapper) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.mapper = mapper;
    }

    @Override
    public Event save(Event event) {
        EventEntity entity = mapper.toEntity(event);
        if (event.getVenueId() != null) {
            venueRepository.findById(event.getVenueId()).ifPresent(entity::setVenue);
        }
        EventEntity saved = eventRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public long count() {
        return eventRepository.count();
    }
}
