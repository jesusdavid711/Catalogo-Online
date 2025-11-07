package com.example.CatalogoOnline.repository;

<<<<<<< Updated upstream
public class EventRepository {
    
=======
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.CatalogoOnline.entity.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    
    // Validación de nombre único
    boolean existsByName(String name);
    
    boolean existsByNameAndIdNot(String name, Long id);
    
    // Filtros derivados para paginación (TASK 3)
    Page<EventEntity> findByCategory(String category, Pageable pageable);
    
    Page<EventEntity> findByVenue_City(String city, Pageable pageable);
    
    Page<EventEntity> findByEventDateAfter(LocalDateTime startDate, Pageable pageable);
    
    // Query personalizada para filtros combinados
    @Query("SELECT e FROM EventEntity e WHERE " +
           "(:city IS NULL OR e.venue.city = :city) AND " +
           "(:category IS NULL OR e.category = :category) AND " +
           "(:startDate IS NULL OR e.eventDate >= :startDate)")
    Page<EventEntity> findByFilters(
        @Param("city") String city,
        @Param("category") String category,
        @Param("startDate") LocalDateTime startDate,
        Pageable pageable
    );
>>>>>>> Stashed changes
}
