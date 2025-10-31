package com.example.CatalogoOnline.service;

import com.example.CatalogoOnline.dto.VenueDTO;
import com.example.CatalogoOnline.exception.NotFoundException;
import com.example.CatalogoOnline.repository.VenueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueService {
    
    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    /**
     * Crea un nuevo venue
     * @param venueDTO Los datos del venue a crear
     * @return El venue creado con su ID asignado
     */
    public VenueDTO createVenue(VenueDTO venueDTO) {
        venueDTO.setId(null); // Aseguramos que sea un nuevo venue
        return venueRepository.save(venueDTO);
    }

    /**
     * Obtiene todos los venues
     * @return Lista de todos los venues
     */
    public List<VenueDTO> getAllVenues() {
        return venueRepository.findAll();
    }

    /**
     * Obtiene un venue por su ID
     * @param id El ID del venue
     * @return El venue encontrado
     * @throws NotFoundException si el venue no existe
     */
    public VenueDTO getVenueById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venue no encontrado con ID: " + id));
    }

    /**
     * Actualiza un venue existente
     * @param id El ID del venue a actualizar
     * @param venueDTO Los nuevos datos del venue
     * @return El venue actualizado
     * @throws NotFoundException si el venue no existe
     */
    public VenueDTO updateVenue(Long id, VenueDTO venueDTO) {
        if (!venueRepository.existsById(id)) {
            throw new NotFoundException("Venue no encontrado con ID: " + id);
        }
        venueDTO.setId(id);
        return venueRepository.save(venueDTO);
    }

    /**
     * Elimina un venue por su ID
     * @param id El ID del venue a eliminar
     * @throws NotFoundException si el venue no existe
     */
    public void deleteVenue(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new NotFoundException("Venue no encontrado con ID: " + id);
        }
        venueRepository.deleteById(id);
    }

    /**
     * Verifica si existe un venue con el ID dado
     * @param id El ID a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(Long id) {
        return venueRepository.existsById(id);
    }

    /**
     * Cuenta el total de venues
     * @return Número total de venues
     */
    public long countVenues() {
        return venueRepository.count();
    }
}