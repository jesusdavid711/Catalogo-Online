package com.example.CatalogoOnline.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.CatalogoOnline.entity.EventEntity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

        // Optimized find all with eager loading of relationships to avoid N+1
        @EntityGraph(attributePaths = { "venue", "categories" })
        Page<EventEntity> findAll(Specification<EventEntity> spec, Pageable pageable);

        // Optimized find all (simple pagination)
        @Override
        @EntityGraph(attributePaths = { "venue", "categories" })
        Page<EventEntity> findAll(Pageable pageable);

        // Optimized findById
        @EntityGraph(attributePaths = { "venue", "categories" })
        @Query("SELECT e FROM EventEntity e WHERE e.id = :id")
        Optional<EventEntity> findByIdWithRelationships(@Param("id") Long id);

        // Optimized findAll list
        @EntityGraph(attributePaths = { "venue", "categories" })
        @Query("SELECT DISTINCT e FROM EventEntity e")
        List<EventEntity> findAllWithRelationships();

        // Validation: unique event names (business rule)
        boolean existsByName(String name);

        boolean existsByNameAndIdNot(String name, Long id);

        // Filtros derivados para paginación (TASK 3)
        // Query for events by category name (ManyToMany relationship)
        @Query("SELECT DISTINCT e FROM EventEntity e JOIN e.categories c WHERE c.name = :categoryName")
        Page<EventEntity> findByCategory(@Param("categoryName") String categoryName, Pageable pageable);

        Page<EventEntity> findByVenue_City(String city, Pageable pageable);

        Page<EventEntity> findByEventDateAfter(LocalDateTime startDate, Pageable pageable);

        // Query personalizada para filtros combinados
        @Query("SELECT DISTINCT e FROM EventEntity e LEFT JOIN e.categories c WHERE " +
                        "(:city IS NULL OR e.venue.city = :city) AND " +
                        "(:category IS NULL OR c.name = :category) AND " +
                        "(:startDate IS NULL OR e.eventDate >= :startDate)")
        Page<EventEntity> findByFilters(
                        @Param("city") String city,
                        @Param("category") String category,
                        @Param("startDate") LocalDateTime startDate,
                        Pageable pageable);
}
