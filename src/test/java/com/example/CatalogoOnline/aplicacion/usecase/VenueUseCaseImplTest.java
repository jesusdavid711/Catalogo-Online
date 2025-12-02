package com.example.CatalogoOnline.aplicacion.usecase;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.CatalogoOnline.dominio.model.Venue;
import com.example.CatalogoOnline.dominio.ports.out.VenueRepositoryPort;

public class VenueUseCaseImplTest {

    @Mock
    private VenueRepositoryPort repository;

    private VenueUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        useCase = new VenueUseCaseImpl(repository);
    }

    @Test
    void createVenue_delegatesToRepository() {
        Venue input = Venue.builder().name("V").build();
        Venue saved = Venue.builder().id(1L).name("V").build();

        when(repository.save(input)).thenReturn(saved);

        Venue result = useCase.createVenue(input);

        verify(repository, times(1)).save(input);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getVenueById_returnsOptional() {
        Venue v = Venue.builder().id(2L).name("V2").build();
        when(repository.findById(2L)).thenReturn(Optional.of(v));

        Optional<Venue> res = useCase.getVenueById(2L);

        assertTrue(res.isPresent());
        assertEquals(2L, res.get().getId());
    }
}
