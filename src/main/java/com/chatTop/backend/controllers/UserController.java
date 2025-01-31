package com.chatTop.backend.controllers;



import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chatTop.backend.dto.UserDTO;
import com.chatTop.backend.entities.User;
import com.chatTop.backend.services.impl.UserService;

@RestController

@RequestMapping("/api/user")
@Tag(name = "Gestion des Utilisateurs", description = "APIs REST liées à l'entité Utilisateur")
public class UserController {

    @Autowired
     UserService userService;
    @Operation(summary = "Obtenir un utilisateur par son ID", description = "Récupère les informations d'un utilisateur via son identifiant.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utilisateur récupéré avec succès"),
        @ApiResponse(responseCode = "404", description = "Utilisateur introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserDTO>> getUserById(@PathVariable Integer id){
        Optional<User> user = userService.getUserById(id.longValue());

        if (user.isPresent()) {
            UserDTO userDTO = UserDTO.fromEntity(user.get());
            return ResponseEntity.ok(Optional.of(userDTO));
        } else {
            return ResponseEntity.ok(Optional.empty());
        }
    }

}