package com.chatTop.backend.dto;

import java.util.Date;
import com.chatTop.backend.entities.Rental;

public class RentalDTO {

     Long id;
     String name;
     Double surface;
     Double price;
     String picture;
     String description;
     Date createdAt;
     Date updatedAt;
     Long ownerId;

    // Constructeur avec tous les paramètres
    public RentalDTO(Long id, String name, Double surface, Double price, String picture, 
                     String description, Date createdAt, Date updatedAt, Long ownerId) {
        this.id = id;
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.picture = picture;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.ownerId = ownerId;
    }

    // Constructeur par défaut
    public RentalDTO() {}

    // Getters et Setters
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

    public Double getSurface() {
        return surface;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    // Méthode pour convertir une entité Rental en DTO
    public static RentalDTO fromEntity(Rental rental) {
        if (rental == null) {
            return null;
        }

        return new RentalDTO(
            rental.getId(),
            rental.getName(),
            rental.getSurface(),
            rental.getPrice(),
            rental.getPicture(),
            rental.getDescription(),
            rental.getCreatedAt(),
            rental.getUpdatedAt(),
            rental.getOwner() != null ? rental.getOwner().getId() : null
        );
    }
}
