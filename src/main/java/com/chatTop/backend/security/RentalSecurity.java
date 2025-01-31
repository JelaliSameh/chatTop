package com.chatTop.backend.security;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.chatTop.backend.entities.Rental;

public interface RentalSecurity {
    List<Rental> getRentals();
    Optional<Rental> getRentalById(Long rental_id);
    Rental createRental(Rental rental, MultipartFile picture) throws IOException;
    Rental updateRental(Long rental_id, Rental rental, MultipartFile picture) throws IOException;
    void  deleteRental(Long rental_id) throws IOException;
}