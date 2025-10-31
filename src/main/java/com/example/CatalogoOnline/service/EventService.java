package com.example.CatalogoOnline.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.CatalogoOnline.dto.EventDTO;
import com.example.CatalogoOnline.exception.NotFoundException;
import com.example.CatalogoOnline.repository.EventRepository;

@Service
public class EventService {
    
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Crea un nuevo evento
     * @param eventDTO Los datos del evento a crear
     * @return El evento creado con su ID asignado
     */
    public EventDTO createEvent(EventDTO eventDTO) {
        eventDTO.setId(null); // Aseguramos que sea un nuevo evento
        return eventRepository.save(eventDTO);
    }

    /**
     * Obtiene todos los eventos
     * @return Lista de todos los eventos
     */
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * Obtiene un evento por su ID
     * @param id El ID del evento
     * @return El evento encontrado
     * @throws NotFoundException si el evento no existe
     */
    public EventDTO getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento no encontrado con ID: " + id));
    }

    /**
     * Actualiza un evento existente
     * @param id El ID del evento a actualizar
     * @param eventDTO Los nuevos datos del evento
     * @return El evento actualizado
     * @throws NotFoundException si el evento no existe
     */
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException("Evento no encontrado con ID: " + id);
        }
        eventDTO.setId(id);
        return eventRepository.save(eventDTO);
    }

    /**
     * Elimina un evento por su ID
     * @param id El ID del evento a eliminar
     * @throws NotFoundException si el evento no existe
     */
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException("Evento no encontrado con ID: " + id);
        }
        eventRepository.deleteById(id);
    }

    /**
     * Verifica si existe un evento con el ID dado
     * @param id El ID a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(Long id) {
        return eventRepository.existsById(id);
    }

    /**
     * Cuenta el total de eventos
     * @return Número total de eventos
     */
    public long countEvents() {
        return eventRepository.count();
    }
}