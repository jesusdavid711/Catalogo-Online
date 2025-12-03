package com.example.CatalogoOnline.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.CatalogoOnline.entity.EventEntity;

import jakarta.persistence.criteria.Predicate;

public class EventSpecifications {

    public static Specification<EventEntity> withDynamicFilters(String city, String category, LocalDateTime startDate) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by City (via Venue)
            if (city != null && !city.isEmpty()) {
                predicates.add(cb.equal(root.get("venue").get("city"), city));
            }

            // Filter by Category (via ManyToMany relationship)
            if (category != null && !category.isEmpty()) {
                // Use join to filter by category name
                predicates.add(cb.equal(root.join("categories").get("name"), category));
                // Ensure distinct results when joining
                query.distinct(true);
            }

            // Filter by Date
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("eventDate"), startDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
