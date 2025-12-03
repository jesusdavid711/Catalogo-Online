package com.example.CatalogoOnline.repository;

import com.example.CatalogoOnline.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for CategoryEntity.
 * Provides CRUD operations and custom queries for categories.
 */
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    /**
     * Find a category by its name.
     * 
     * @param name the category name
     * @return Optional containing the category if found
     */
    Optional<CategoryEntity> findByName(String name);

    /**
     * Check if a category exists by name.
     * 
     * @param name the category name
     * @return true if exists, false otherwise
     */
    boolean existsByName(String name);
}
