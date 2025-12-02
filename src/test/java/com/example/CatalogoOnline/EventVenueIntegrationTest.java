package com.example.CatalogoOnline;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.example.CatalogoOnline.dto.EventDTO;
import com.example.CatalogoOnline.dto.VenueDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EventVenueIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createVenueAndEvent_shouldReturnEventWithVenueNameAndCapacity() {
        // Create venue
        VenueDTO venue = new VenueDTO();
        venue.setName("Sala Principal");
        venue.setAddress("Calle Falsa 123");
        venue.setCity("Bogotá");
        venue.setCountry("Colombia");
        venue.setCapacity(500);
        venue.setType("Concierto");

        ResponseEntity<VenueDTO> venueResp = restTemplate.postForEntity("/api/venues", venue, VenueDTO.class);
        assertThat(venueResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        VenueDTO createdVenue = venueResp.getBody();
        assertThat(createdVenue).isNotNull();
        assertThat(createdVenue.getId()).isNotNull();

        // Create event referencing the venue
        EventDTO event = new EventDTO();
        event.setName("Concierto de prueba");
        event.setDescription("Descripcion");
        event.setEventDate(java.time.LocalDateTime.now().plusDays(10));
        event.setVenueId(createdVenue.getId());

        ResponseEntity<EventDTO> eventResp = restTemplate.postForEntity("/api/events", event, EventDTO.class);
        assertThat(eventResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        EventDTO createdEvent = eventResp.getBody();
        assertThat(createdEvent).isNotNull();
        assertThat(createdEvent.getId()).isNotNull();
        // Controller should have filled venueName and capacity
        assertThat(createdEvent.getVenueName()).isEqualTo(createdVenue.getName());
        assertThat(createdEvent.getCapacity()).isEqualTo(createdVenue.getCapacity());

        // Fetch event and assert same
        ResponseEntity<EventDTO> getResp = restTemplate.exchange("/api/events/" + createdEvent.getId(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), EventDTO.class);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        EventDTO fetched = getResp.getBody();
        assertThat(fetched).isNotNull();
        assertThat(fetched.getVenueName()).isEqualTo(createdVenue.getName());
        assertThat(fetched.getCapacity()).isEqualTo(createdVenue.getCapacity());
    }
}
