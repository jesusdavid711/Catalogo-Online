package com.example.CatalogoOnline.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.CatalogoOnline.dto.VenueDTO;

@Repository
public class VenueRepository {
    
    private final List<VenueDTO> venues = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Guarda un nuevo venue en memoria
     * @param venue El venue a guardar
     * @return El venue guardado con su ID asignado
     */
    public VenueDTO save(VenueDTO venue) {
        if (venue.getId() == null) {
            // Nuevo venue
            venue.setId(idGenerator.getAndIncrement());
            venues.add(venue);
        } else {
            // Actualizar venue existente
            Optional<VenueDTO> existingVenue = findById(venue.getId());
            if (existingVenue.isPresent()) {
                int index = venues.indexOf(existingVenue.get());
                venues.set(index, venue);
            } else {
                venues.add(venue);
            }
        }
        return venue;
    }

    /**
     * Encuentra todos los venues
     * @return Lista de todos los venues
     */
    public List<VenueDTO> findAll() {
        return new ArrayList<>(venues);
    }

    /**
     * Encuentra un venue por su ID
     * @param id El ID del venue
     * @return Optional con el venue si existe
     */
    public Optional<VenueDTO> findById(Long id) {
        return venues.stream()
                .filter(venue -> venue.getId().equals(id))
                .findFirst();
    }

    /**
     * Elimina un venue por su ID
     * @param id El ID del venue a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean deleteById(Long id) {
        return venues.removeIf(venue -> venue.getId().equals(id));
    }

    /**
     * Verifica si existe un venue con el ID dado
     * @param id El ID a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(Long id) {
        return venues.stream().anyMatch(venue -> venue.getId().equals(id));
    }

    /**
     * Cuenta el total de venues
     * @return Número total de venues
     */
    public long count() {
        return venues.size();
    }
    
    /**
     * Busca venues por ciudad
     * @param city La ciudad a buscar
     * @return Lista de venues en esa ciudad
     */
    public List<VenueDTO> findByCity(String city) {
        return venues.stream()
                .filter(venue -> venue.getCity() != null && venue.getCity().equalsIgnoreCase(city))
                .toList();
    }

    /**
     * Limpia todos los venues (útil para testing)
     */
    public void deleteAll() {
        venues.clear();
    }
}