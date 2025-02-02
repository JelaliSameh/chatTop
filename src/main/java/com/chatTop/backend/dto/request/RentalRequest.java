package com.chatTop.backend.dto.request;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data // Génère automatiquement getters/setters
public class RentalRequest {
    @NotNull(message = "Rental name must not be blank")
    @Size(min = 1, max = 64, message = "The name must be between 1 and 64 characters long")
    private String name;

    @NotNull(message = "Surface must not be null")
    @Positive(message = "Surface must be a positive value")
    private Double surface;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be a positive value")
    private Double price;

    @Size(max = 2000, message = "Description can be up to 2000 characters long")
    private String description;

    private MultipartFile picture; // Lombok génère getPicture() et setPicture()
}