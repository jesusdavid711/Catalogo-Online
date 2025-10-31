package com.example.CatalogoOnline.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.CatalogoOnline.dto.EventDTO;

@Repository
public class EventRepository {
    
    private final List<EventDTO> events = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Guarda un nuevo evento en memoria
     * @param event El evento a guardar
     * @return El evento guardado con su ID asignado
     */
    public EventDTO save(EventDTO event) {
        if (event.getId() == null) {
            // Nuevo evento
            event.setId(idGenerator.getAndIncrement());
            events.add(event);
        } else {
            // Actualizar evento existente
            Optional<EventDTO> existingEvent = findById(event.getId());
            if (existingEvent.isPresent()) {
                int index = events.indexOf(existingEvent.get());
                events.set(index, event);
            } else {
                events.add(event);
            }
        }
        return event;
    }

    /**
     * Encuentra todos los eventos
     * @return Lista de todos los eventos
     */
    public List<EventDTO> findAll() {
        return new ArrayList<>(events);
    }

    /**
     * Encuentra un evento por su ID
     * @param id El ID del evento
     * @return Optional con el evento si existe
     */
    public Optional<EventDTO> findById(Long id) {
        return events.stream()
                .filter(event -> event.getId().equals(id))
                .findFirst();
    }

    /**
     * Elimina un evento por su ID
     * @param id El ID del evento a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean deleteById(Long id) {
        return events.removeIf(event -> event.getId().equals(id));
    }

    /**
     * Verifica si existe un evento con el ID dado
     * @param id El ID a verificar
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(Long id) {
        return events.stream().anyMatch(event -> event.getId().equals(id));
    }

    /**
     * Cuenta el total de eventos
     * @return Número total de eventos
     */
    public long count() {
        return events.size();
    }

    /**
     * Limpia todos los eventos (útil para testing)
     */
    public void deleteAll() {
        events.clear();
    }
}
