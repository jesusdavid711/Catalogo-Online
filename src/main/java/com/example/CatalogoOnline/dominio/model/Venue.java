package com.example.CatalogoOnline.dominio.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venue {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String country;
    private Integer capacity;
    private String type;
    private String phone;
    private String email;
    // List of event ids to maintain independence from persistence layer
    private List<Long> eventIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
