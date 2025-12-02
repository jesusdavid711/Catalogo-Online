package com.example.CatalogoOnline.dominio.ports.out;

import java.util.List;
import java.util.Optional;
import com.example.CatalogoOnline.dominio.model.Venue;

public interface VenueRepositoryPort {
    Venue save(Venue venue);
    Optional<Venue> findById(Long id);
    List<Venue> findAll();
    void deleteById(Long id);
    long count();
}
