package com.example.CatalogoOnline.dominio.ports.out;

import java.util.List;
import java.util.Optional;
import com.example.CatalogoOnline.dominio.model.Event;

public interface EventRepositoryPort {
    Event save(Event event);
    Optional<Event> findById(Long id);
    List<Event> findAll();
    void deleteById(Long id);
    long count();
}
