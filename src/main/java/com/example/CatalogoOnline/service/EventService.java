package com.example.CatalogoOnline.service;

<<<<<<< Updated upstream
public class EventService {
    
}
=======
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.CatalogoOnline.dto.EventDTO;
import com.example.CatalogoOnline.entity.EventEntity;
import com.example.CatalogoOnline.entity.VenueEntity;
import com.example.CatalogoOnline.exception.DuplicateResourceException;
import com.example.CatalogoOnline.exception.NotFoundException;
import com.example.CatalogoOnline.mapper.EventMapper;
import com.example.CatalogoOnline.repository.EventRepository;
import com.example.CatalogoOnline.repository.VenueRepository;

@Service
@Transactional
public class EventService {
    
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepository, 
                       VenueRepository venueRepository,
                       EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.eventMapper = eventMapper;
    }

    public EventDTO createEvent(EventDTO eventDTO) {
        // Validar nombre duplicado (TASK 2.2)
        if (eventRepository.existsByName(eventDTO.getName())) {
            throw new DuplicateResourceException(
                "Ya existe un evento con el nombre: " + eventDTO.getName()
            );
        }
        
        // Buscar venue si se proporciona
        VenueEntity venue = null;
        if (eventDTO.getVenueId() != null) {
            venue = venueRepository.findById(eventDTO.getVenueId())
                    .orElseThrow(() -> new NotFoundException(
                        "Venue no encontrado con ID: " + eventDTO.getVenueId()
                    ));
        }
        
        EventEntity entity = eventMapper.toEntity(eventDTO, venue);
        EventEntity savedEntity = eventRepository.save(entity);
        return eventMapper.toDTO(savedEntity);
    }

    @Transactional(readOnly = true)
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventDTO getEventById(Long id) {
        EventEntity entity = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    "Evento no encontrado con ID: " + id
                ));
        return eventMapper.toDTO(entity);
    }

    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        EventEntity existingEntity = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                    "Evento no encontrado con ID: " + id
                ));
        
        // Validar nombre duplicado (excluyendo el evento actual)
        if (eventRepository.existsByNameAndIdNot(eventDTO.getName(), id)) {
            throw new DuplicateResourceException(
                "Ya existe otro evento con el nombre: " + eventDTO.getName()
            );
        }
        
        // Buscar venue si cambió
        VenueEntity venue = existingEntity.getVenue();
        if (eventDTO.getVenueId() != null && 
            !eventDTO.getVenueId().equals(existingEntity.getVenue() != null ? 
                existingEntity.getVenue().getId() : null)) {
            venue = venueRepository.findById(eventDTO.getVenueId())
                    .orElseThrow(() -> new NotFoundException(
                        "Venue no encontrado con ID: " + eventDTO.getVenueId()
                    ));
        }
        
        existingEntity.setName(eventDTO.getName());
        existingEntity.setDescription(eventDTO.getDescription());
        existingEntity.setEventDate(eventDTO.getEventDate());
        existingEntity.setCategory(eventDTO.getCategory());
        existingEntity.setVenue(venue);
        
        EventEntity updatedEntity = eventRepository.save(existingEntity);
        return eventMapper.toDTO(updatedEntity);
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException("Evento no encontrado con ID: " + id);
        }
        eventRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long countEvents() {
        return eventRepository.count();
    }
    
    // TASK 3: Métodos con paginación y filtros
    @Transactional(readOnly = true)
    public Page<EventDTO> getEventsPaginated(Pageable pageable) {
        return eventRepository.findAll(pageable)
                .map(eventMapper::toDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<EventDTO> getEventsWithFilters(String city, String category, 
                                               LocalDateTime startDate, Pageable pageable) {
        return eventRepository.findByFilters(city, category, startDate, pageable)
                .map(eventMapper::toDTO);
    }
}
>>>>>>> Stashed changes
