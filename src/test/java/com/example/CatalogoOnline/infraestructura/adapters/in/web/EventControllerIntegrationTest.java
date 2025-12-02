package com.example.CatalogoOnline.infraestructura.adapters.in.web;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.CatalogoOnline.dto.EventDTO;
import com.example.CatalogoOnline.dto.VenueDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateEventWithVenueAndVerifyDTOFields() throws Exception {
        // 1. Create a Venue
        VenueDTO venueDTO = new VenueDTO(
            null, "Teatro Central", "Calle Principal 123", "Bogotá", "Colombia",
            500, "Theater", "3101234567", "info@teatro.com"
        );

        String venueResponse = mockMvc.perform(post("/api/venues")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(venueDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andReturn()
            .getResponse()
            .getContentAsString();

        Long venueId = objectMapper.readTree(venueResponse).get("id").asLong();

        // 2. Create an Event linked to that Venue
        EventDTO eventDTO = new EventDTO(
            null, "Concierto 2025", "Gran concierto de música clásica", 
            LocalDateTime.now().plusDays(10), venueId, null, null, "Música"
        );

        String eventResponse = mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(eventDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.venueName", is("Teatro Central")))
            .andExpect(jsonPath("$.capacity", is(500)))
            .andReturn()
            .getResponse()
            .getContentAsString();

        Long eventId = objectMapper.readTree(eventResponse).get("id").asLong();

        // 3. Get Event and verify venueName and capacity are populated
        mockMvc.perform(get("/api/events/{id}", eventId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(eventId.intValue())))
            .andExpect(jsonPath("$.name", is("Concierto 2025")))
            .andExpect(jsonPath("$.venueName", is("Teatro Central")))
            .andExpect(jsonPath("$.capacity", is(500)))
            .andExpect(jsonPath("$.venueId", is(venueId.intValue())));
    }
}
