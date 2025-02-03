package com.chatTop.backend.controllers;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatTop.backend.dto.UserDTO;
import com.chatTop.backend.dto.request.AuthRequest;
import com.chatTop.backend.dto.request.RegistrationRequest;
import com.chatTop.backend.dto.response.AuthResponse;
import com.chatTop.backend.entities.User;
import com.chatTop.backend.repository.UserRepository;
import com.chatTop.backend.services.impl.UserServiceImpl;
import com.chatTop.backend.util.JWTService;
import com.chatTop.backend.util.MyUserDetailsService;

import org.springframework.security.authentication.AuthenticationManager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/auth")
@Validated
@Tag(name = "Authentication")
public class AuthController {

    @Autowired
    private  UserServiceImpl userService;
    @Autowired
    private  JWTService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private  MyUserDetailsService userDetailsService;
    @Autowired
     PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;

    @Operation(summary = "Enregistrer un nouvel utilisateur", description = "Enregistre un nouvel utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @io.swagger.v3.oas.annotations.media.Content),
            @ApiResponse(responseCode = "409", description = "Le nom d'utilisateur ou l'email est déjà pris.", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(
            @Valid @RequestBody RegistrationRequest registrationRequest, 
            BindingResult result
    ) {
        if (result.hasErrors()) {
            // Retourne un message d'erreur si la validation échoue
            return ResponseEntity.badRequest().body(new AuthResponse());
        }
        userService.registerUser(registrationRequest);

        // Authentifier l'utilisateur
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registrationRequest.getEmail(), registrationRequest.getPassword())
        );

        // Charger les détails de l'utilisateur
        final UserDetails userDetails = userDetailsService.loadUserByUsername(registrationRequest.getEmail());

        // Générer un token
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtService.generateToken(userDetails));

        // Retourner le token dans la réponse
        return ResponseEntity.ok(authResponse);
    }


    @Operation(summary = "Déconnecter l'utilisateur", description = "Déconnecte l'utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createAuthenticationToken(
            @Valid @RequestBody AuthRequest authRequest
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authRequest.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtService.generateToken(userDetails));

        return ResponseEntity.ok(authResponse);
    }


    @Operation(summary = "Obtenir les informations de l'utilisateur authentifié", description = "Renvoie les informations de l'utilisateur actuellement authentifié.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "L'utilisateur n'est pas trouvé.")
    })

   
    @GetMapping("/me")
    public ResponseEntity<Optional<UserDTO>> getMe(
            @RequestHeader("Authorization") String token
    ) {
        // Extraire le token de l'en-tête Authorization
        String jwt = token.substring(7);
        String email = jwtService.extractUsername(jwt);

        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            UserDTO userDTO = UserDTO.fromEntity(user.get());
            return ResponseEntity.ok(Optional.of(userDTO));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}