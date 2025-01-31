package com.chatTop.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
     Long id;

    @Column(name = "name", nullable = false, length = 64)
     String name;

    @Column(name = "surface", nullable = false)
    @NotNull(message = "Surface cannot be null")
    Double surface;


    @Column(name = "price", nullable = false)
     Double price;

    @Column(name = "picture")
     String picture;

    @Column(name = "description", length = 2000)
     String description;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "owner_id", nullable = false)
     User owner;

    @Column(name = "created_at", nullable = false, updatable = false)
     Date createdAt;

    @Column(name = "updated_at")
    Date updatedAt;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL, orphanRemoval = true)
     List<Message> messages;

    // Constructeur par défaut
    public Rental() {}

    // Constructeur avec tous les paramètres
    public Rental(Long id, String name, Double surface, Double price, String picture, 
                  String description, User owner, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.picture = picture;
        this.description = description;
        this.owner = owner;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    // Builder interne
    public static class RentalBuilder {
         Long id;
         String name;
         Double surface;
         Double price;
        String picture;
         String description;
         User owner;
         Date createdAt;
         Date updatedAt;

        public RentalBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RentalBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RentalBuilder surface(Double surface) {
            this.surface = surface;
            return this;
        }

        public RentalBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public RentalBuilder picture(String picture) {
            this.picture = picture;
            return this;
        }

        public RentalBuilder description(String description) {
            this.description = description;
            return this;
        }

        public RentalBuilder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public RentalBuilder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RentalBuilder updatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Rental build() {
            return new Rental(id, name, surface, price, picture, description, owner, createdAt, updatedAt);
        }
    }

    // Méthode pour obtenir un builder
    public static RentalBuilder builder() {
        return new RentalBuilder();
    }
}

