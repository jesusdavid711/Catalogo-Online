package com.example.CatalogoOnline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.CatalogoOnline.entity.VenueEntity;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, Long> {
    
    // Buscar venues por ciudad
    List<VenueEntity> findByCity(String city);
    
    // Buscar venues por país
    List<VenueEntity> findByCountry(String country);
    
    // Buscar venues por tipo
    List<VenueEntity> findByType(String type);
}