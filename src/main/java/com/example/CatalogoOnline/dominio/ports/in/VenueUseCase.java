package com.example.CatalogoOnline.dominio.ports.in;

import java.util.List;
import java.util.Optional;
import com.example.CatalogoOnline.dominio.model.Venue;

public interface VenueUseCase {
    Venue createVenue(Venue venue);
    Venue updateVenue(Long id, Venue venue);
    Optional<Venue> getVenueById(Long id);
    List<Venue> getAllVenues();
    void deleteVenue(Long id);
    long countVenues();
}
