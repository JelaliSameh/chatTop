package com.chatTop.backend.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.chatTop.backend.dto.RentalDTO;
import com.chatTop.backend.dto.request.RentalRequest;
import com.chatTop.backend.dto.response.MessageResponse;
import com.chatTop.backend.dto.response.RentalsListResponse;
import com.chatTop.backend.entities.Rental;
import com.chatTop.backend.entities.User;
import com.chatTop.backend.security.UserSecurity;
import com.chatTop.backend.services.JWTService;
import com.chatTop.backend.services.impl.RentalService;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "Rentals")
public class RentalController {
    @Autowired
     RentalService rentalService;
    @Autowired
      UserSecurity userSecurity;
    @Autowired
     JWTService jwtService;

    @GetMapping
    public ResponseEntity<RentalsListResponse> getRentals() {
        RentalsListResponse rentalsListResponse = new RentalsListResponse();

        rentalsListResponse.setRentals(
                rentalService.getRentals().stream()
                    .map(RentalDTO::fromEntity)
                    .collect(Collectors.toList())
        );

        return ResponseEntity.ok().body(rentalsListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<RentalDTO>> getRentalById(@PathVariable Integer id){
        return ResponseEntity.ok(
                rentalService.getRentalById(id.longValue())
                        .map(RentalDTO::fromEntity)
        );
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createRental(
            @ModelAttribute @Valid RentalRequest rentalRequest,
            @RequestPart("picture") MultipartFile picture,  
            @RequestHeader("Authorization") String token
    ) {
    	
    	   System.out.println("Received token: " + token);

    	    if (token == null || !token.startsWith("Bearer ")) {
    	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Missing or invalid token"));
    	    }

        // Extraire le token de l'en-tête Authorization
        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt);

        Optional<User> user = userSecurity.getUserByEmail(email);
        MessageResponse messageResponse = new MessageResponse();

        if (user.isPresent()) {
            Rental rental = Rental.builder()
                    .name(rentalRequest.getName())
                    .surface(rentalRequest.getSurface())
                    .price(rentalRequest.getPrice())
                    .description(rentalRequest.getDescription())
                    .owner(user.get())
                    .createdAt(new Date())
                    .updatedAt(null)
                    .build();

            try {
                RentalDTO createdRental = RentalDTO.fromEntity(
                        rentalService.createRental(rental, picture)  // Passez le fichier ici
                );

                if (createdRental != null) {
                    messageResponse.setMessage("Rental created!");
                    return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
                } else {
                    messageResponse.setMessage("Failed to create rental");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
                }
            } catch (IOException e) {
                messageResponse.setMessage("Failed to upload picture");
                System.err.println("Failed to upload picture");
                e.printStackTrace(System.err);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
            }
        } else {
            messageResponse.setMessage("Owner not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateRental(
            @PathVariable("id") Integer id,
            @Valid RentalRequest rentalRequest,
            @RequestPart("picture") MultipartFile picture,  // Modification ici
            @RequestHeader("Authorization") String token
    ) {
        // Extraire le token de l'en-tête Authorization
        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt);

        Optional<User> user = userSecurity.getUserByEmail(email);
        Optional<RentalDTO> existingRentalDTO = rentalService.getRentalById(id.longValue()).map(RentalDTO::fromEntity);
        MessageResponse messageResponse = new MessageResponse();

        if (user.isPresent() && existingRentalDTO.isPresent()) {
            if (user.get().getId() == (existingRentalDTO.get().getOwnerId())) {
                Rental rental = Rental.builder()
                        .name(rentalRequest.getName())
                        .surface(rentalRequest.getSurface())
                        .price(rentalRequest.getPrice())
                        .owner(user.get())
                        .description(rentalRequest.getDescription())
                        .updatedAt(new Date())
                        .build();

                try {
                    RentalDTO updatedRental = RentalDTO.fromEntity(
                            rentalService.updateRental(id.longValue(), rental, picture)  // Passez le fichier ici
                    );

                    if (updatedRental != null) {
                        messageResponse.setMessage("Rental updated!");
                        return ResponseEntity.ok().body(messageResponse);
                    } else {
                        messageResponse.setMessage("Failed to update rental");
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
                    }
                } catch (IOException e) {
                    System.err.println("Failed to upload picture");
                    e.printStackTrace(System.err);
                    messageResponse.setMessage("Failed to upload picture");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
                }
            } else {
                messageResponse.setMessage("You are not authorized to update this rental");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageResponse);
            }
        } else {
            messageResponse.setMessage("Resource not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRental(@PathVariable("id") Long id) {
        MessageResponse messageResponse = new MessageResponse();
        try {
            rentalService.deleteRental(id);
            messageResponse.setMessage("Rental deleted!");
            return ResponseEntity.ok(messageResponse);
        } catch (IOException e) {
            System.err.println("Failed to delete rental");
            e.printStackTrace(System.err);
            messageResponse.setMessage("Failed to delete rental");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
        }
    }
}