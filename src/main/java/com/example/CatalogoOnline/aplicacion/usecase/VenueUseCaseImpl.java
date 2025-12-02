package com.example.CatalogoOnline.aplicacion.usecase;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.CatalogoOnline.dominio.model.Venue;
import com.example.CatalogoOnline.dominio.ports.in.VenueUseCase;
import com.example.CatalogoOnline.dominio.ports.out.VenueRepositoryPort;

@Service
public class VenueUseCaseImpl implements VenueUseCase {

    private final VenueRepositoryPort venueRepositoryPort;

    public VenueUseCaseImpl(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    public Venue createVenue(Venue venue) {
        return venueRepositoryPort.save(venue);
    }

    @Override
    public Venue updateVenue(Long id, Venue venue) {
        venue.setId(id);
        return venueRepositoryPort.save(venue);
    }

    @Override
    public Optional<Venue> getVenueById(Long id) {
        return venueRepositoryPort.findById(id);
    }

    @Override
    public List<Venue> getAllVenues() {
        return venueRepositoryPort.findAll();
    }

    @Override
    public void deleteVenue(Long id) {
        venueRepositoryPort.deleteById(id);
    }

    @Override
    public long countVenues() {
        return venueRepositoryPort.count();
    }
}
