package com.chatTop.backend.controllers;
import io.swagger.v3.oas.annotations.Operation;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.Date;
import java.util.Optional;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;

import com.chatTop.backend.dto.MessageDTO;
import com.chatTop.backend.dto.response.MessageResponse;
import com.chatTop.backend.entities.Message;
import com.chatTop.backend.services.impl.MessageService;
@RestController
@Tag(name = "Messages")
@RequestMapping("/api/messages")
public class MessageController {
@Autowired
    private  MessageService messageService;

    @Operation(summary = "Obtenir tous les messages", description = "Récupère une liste de tous les messages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste de tous les messages récupérée avec succès",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MessageDTO.class)))}),
            @ApiResponse(responseCode = "403", description = "Accès refusé", content = @Content)
    })
    
    @PostMapping("")
    public ResponseEntity<MessageResponse> createMessage(
           @Valid @RequestBody MessageDTO messageDTO
    ) {
       Message message = new Message();

       // Assigner la date actuelle au champ created_at
       message.setCreated_at(new Date());  // Utilisation de la date actuelle

       Optional<Message> createdMessage = messageService.createMessage(message);

       MessageResponse messageResponse = new MessageResponse(null);
       if (createdMessage.isPresent()) {
           messageResponse.setMessage("Message sent successfully");
           return ResponseEntity.ok(messageResponse);
       } else {
           messageResponse.setMessage("Failed to send message");
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
       }
    }


}