package com.example.CatalogoOnline.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class VenueDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre del venue no puede estar vacío")
    private String name;
    
    private String address;
    
    private String city;
    
    private String country;
    
    @Positive(message = "La capacidad debe ser un número positivo")
    private Integer capacity;
    
    private String type;
    
    private String phone;
    
    private String email;

    public VenueDTO() {
    }

    public VenueDTO(Long id, String name, String address, String city, 
                    String country, Integer capacity, String type, String phone, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.capacity = capacity;
        this.type = type;
        this.phone = phone;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}