package com.chatTop.backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.Date;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.chatTop.backend.dto.MessageDTO;
import com.chatTop.backend.dto.response.MessageResponse;
import com.chatTop.backend.entities.Message;
import com.chatTop.backend.entities.Rental;
import com.chatTop.backend.entities.User;
import com.chatTop.backend.repository.RentalRepository;
import com.chatTop.backend.repository.UserRepository;
import com.chatTop.backend.service.MessageService;


@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageSecurity;
    private final RentalRepository rentalRepository; // Ajoutez ce repository
    private final UserRepository userRepository;     // Ajoutez ce repository

    @PostMapping("")
    public ResponseEntity<MessageResponse> createMessage(
           @Valid @RequestBody MessageDTO messageDTO
    ) {
        // Récupérer le Rental complet depuis la base
        Rental rental = rentalRepository.findById(messageDTO.getRental_id())
                .orElseThrow(() -> new RuntimeException("Rental non trouvé"));

        // Récupérer l'User complet depuis la base
        User user = userRepository.findById(messageDTO.getUser_id())
                .orElseThrow(() -> new RuntimeException("User non trouvé"));

        // Créer le Message avec les objets complets
        Message message = MessageDTO.toEntity(messageDTO, rental, user);
        message.setCreated_at(new Date());

        Optional<Message> createdMessage = messageSecurity.createMessage(message);

   
    

        MessageResponse messageResponse = new MessageResponse();
        if (createdMessage.isPresent()) {
            messageResponse.setMessage("Message sent successfully");
            return ResponseEntity.ok(messageResponse);
        } else {
            messageResponse.setMessage("Failed to send message");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
        }
    }

}