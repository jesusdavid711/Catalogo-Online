package com.example.CatalogoOnline.aplicacion.usecase;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.CatalogoOnline.dominio.model.Event;
import com.example.CatalogoOnline.dominio.ports.in.EventUseCase;
import com.example.CatalogoOnline.dominio.ports.out.EventRepositoryPort;

@Service
public class EventUseCaseImpl implements EventUseCase {

    private final EventRepositoryPort eventRepositoryPort;

    public EventUseCaseImpl(EventRepositoryPort eventRepositoryPort) {
        this.eventRepositoryPort = eventRepositoryPort;
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepositoryPort.save(event);
    }

    @Override
    public Event updateEvent(Long id, Event event) {
        event.setId(id);
        return eventRepositoryPort.save(event);
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return eventRepositoryPort.findById(id);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepositoryPort.findAll();
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepositoryPort.deleteById(id);
    }

    @Override
    public long countEvents() {
        return eventRepositoryPort.count();
    }
}
