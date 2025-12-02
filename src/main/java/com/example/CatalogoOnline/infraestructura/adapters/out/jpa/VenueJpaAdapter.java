package com.example.CatalogoOnline.infraestructura.adapters.out.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.CatalogoOnline.dominio.model.Venue;
import com.example.CatalogoOnline.dominio.ports.out.VenueRepositoryPort;
import com.example.CatalogoOnline.entity.VenueEntity;
import com.example.CatalogoOnline.repository.VenueRepository;
import com.example.CatalogoOnline.infraestructura.adapters.out.jpa.mapper.VenueEntityMapper;

@Component
public class VenueJpaAdapter implements VenueRepositoryPort {

    private final VenueRepository venueRepository;
    private final VenueEntityMapper mapper;

    public VenueJpaAdapter(VenueRepository venueRepository, VenueEntityMapper mapper) {
        this.venueRepository = venueRepository;
        this.mapper = mapper;
    }

    @Override
    public Venue save(Venue venue) {
        VenueEntity entity = mapper.toEntity(venue);
        VenueEntity saved = venueRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return venueRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Venue> findAll() {
        return venueRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        venueRepository.deleteById(id);
    }

    @Override
    public long count() {
        return venueRepository.count();
    }
}
