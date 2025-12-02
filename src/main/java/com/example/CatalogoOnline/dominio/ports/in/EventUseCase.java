package com.example.CatalogoOnline.dominio.ports.in;

import java.util.List;
import java.util.Optional;
import com.example.CatalogoOnline.dominio.model.Event;

public interface EventUseCase {
    Event createEvent(Event event);
    Event updateEvent(Long id, Event event);
    Optional<Event> getEventById(Long id);
    List<Event> getAllEvents();
    void deleteEvent(Long id);
    long countEvents();
}
