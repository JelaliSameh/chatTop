package com.chatTop.backend.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.chatTop.backend.entities.Rental;

@Validated
public interface RentalService {
    List<Rental> getRentals();
    Optional<Rental> getRentalById(Long rental_id);
    Rental createRental(Rental rental, MultipartFile picture) throws IOException;
    Rental updateRental(Long rental_id, Rental rental, MultipartFile picture) throws IOException;
    void  deleteRental(Long rental_id) throws IOException;
}