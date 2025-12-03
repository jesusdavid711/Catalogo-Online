package com.example.CatalogoOnline.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import com.example.CatalogoOnline.entity.CategoryEntity;
import com.example.CatalogoOnline.entity.EventEntity;
import com.example.CatalogoOnline.entity.VenueEntity;

@DataJpaTest
@ActiveProfiles("test")
public class EventSpecificationsTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        private EventRepository eventRepository;

        @Test
        public void shouldFilterByCityCategoryAndDate() {
                // Given
                VenueEntity venue1 = VenueEntity.builder()
                                .name("Venue 1").city("Bogotá").address("Calle 1").country("Colombia").type("Theater")
                                .capacity(100).build();
                entityManager.persist(venue1);

                VenueEntity venue2 = VenueEntity.builder()
                                .name("Venue 2").city("Medellín").address("Calle 2").country("Colombia").type("Hall")
                                .capacity(200).build();
                entityManager.persist(venue2);

                CategoryEntity catMusic = CategoryEntity.builder().name("Music").build();
                entityManager.persist(catMusic);

                CategoryEntity catTech = CategoryEntity.builder().name("Tech").build();
                entityManager.persist(catTech);

                EventEntity event1 = EventEntity.builder()
                                .name("Event 1").eventDate(LocalDateTime.now().plusDays(1))
                                .venue(venue1).build();
                event1.getCategories().add(catMusic);
                entityManager.persist(event1);

                EventEntity event2 = EventEntity.builder()
                                .name("Event 2").eventDate(LocalDateTime.now().plusDays(2))
                                .venue(venue2).build();
                event2.getCategories().add(catTech);
                entityManager.persist(event2);

                entityManager.flush();

                // When: Filter by City "Bogotá"
                Specification<EventEntity> specCity = EventSpecifications.withDynamicFilters("Bogotá", null, null);
                List<EventEntity> resultsCity = eventRepository.findAll(specCity);
                assertThat(resultsCity).hasSize(1);
                assertThat(resultsCity.get(0).getName()).isEqualTo("Event 1");

                // When: Filter by Category "Tech"
                Specification<EventEntity> specCat = EventSpecifications.withDynamicFilters(null, "Tech", null);
                List<EventEntity> resultsCat = eventRepository.findAll(specCat);
                assertThat(resultsCat).hasSize(1);
                assertThat(resultsCat.get(0).getName()).isEqualTo("Event 2");

                // When: Filter by Date (future)
                Specification<EventEntity> specDate = EventSpecifications.withDynamicFilters(null, null,
                                LocalDateTime.now().plusDays(1).minusHours(1));
                List<EventEntity> resultsDate = eventRepository.findAll(specDate);
                assertThat(resultsDate).hasSize(2);
        }
}
