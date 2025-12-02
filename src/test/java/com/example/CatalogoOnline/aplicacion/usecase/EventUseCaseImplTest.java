package com.example.CatalogoOnline.aplicacion.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.CatalogoOnline.dominio.model.Event;
import com.example.CatalogoOnline.dominio.ports.out.EventRepositoryPort;

public class EventUseCaseImplTest {

    @Mock
    private EventRepositoryPort repository;

    private EventUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        useCase = new EventUseCaseImpl(repository);
    }

    @Test
    void createEvent_delegatesToRepository() {
        Event input = Event.builder().name("Test").build();
        Event saved = Event.builder().id(1L).name("Test").build();

        when(repository.save(input)).thenReturn(saved);

        Event result = useCase.createEvent(input);

        verify(repository, times(1)).save(input);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getEventById_returnsOptional() {
        Event e = Event.builder().id(2L).name("E").build();
        when(repository.findById(2L)).thenReturn(Optional.of(e));

        Optional<Event> res = useCase.getEventById(2L);

        assertTrue(res.isPresent());
        assertEquals(2L, res.get().getId());
    }
}
