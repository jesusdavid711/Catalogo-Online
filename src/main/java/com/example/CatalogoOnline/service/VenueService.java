package com.example.CatalogoOnline.service;

<<<<<<< Updated upstream
public class VenueService {
    
}
=======
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.CatalogoOnline.dto.VenueDTO;
import com.example.CatalogoOnline.entity.VenueEntity;
import com.example.CatalogoOnline.exception.NotFoundException;
import com.example.CatalogoOnline.mapper.VenueMapper;
import com.example.CatalogoOnline.repository.VenueRepository;

@Service
@Transactional
public class VenueService {
    
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public VenueService(VenueRepository venueRepository, VenueMapper venueMapper) {
        this.venueRepository = venueRepository;
        this.venueMapper = venueMapper;
    }

    public VenueDTO createVenue(VenueDTO venueDTO) {
        VenueEntity entity = venueMapper.toEntity(venueDTO);
        VenueEntity savedEntity = venueRepository.save(entity);
        return venueMapper.toDTO(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<VenueDTO> getAllVenues() {
        return venueRepository.findAll().stream()
                .map(venueMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VenueDTO getVenueById(Long id) {
        VenueEntity entity = venueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    "Venue no encontrado con ID: " + id
                ));
        return venueMapper.toDTO(entity);
    }

    public VenueDTO updateVenue(Long id, VenueDTO venueDTO) {
        VenueEntity existingEntity = venueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    "Venue no encontrado con ID: " + id
                ));
        
        existingEntity.setName(venueDTO.getName());
        existingEntity.setAddress(venueDTO.getAddress());
        existingEntity.setCity(venueDTO.getCity());
        existingEntity.setCountry(venueDTO.getCountry());
        existingEntity.setCapacity(venueDTO.getCapacity());
        existingEntity.setType(venueDTO.getType());
        existingEntity.setPhone(venueDTO.getPhone());
        existingEntity.setEmail(venueDTO.getEmail());
        
        VenueEntity updatedEntity = venueRepository.save(existingEntity);
        return venueMapper.toDTO(updatedEntity);
    }

    public void deleteVenue(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new NotFoundException("Venue no encontrado con ID: " + id);
        }
        venueRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long countVenues() {
        return venueRepository.count();
    }
}
>>>>>>> Stashed changes
