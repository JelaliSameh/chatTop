package com.chatTop.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.chatTop.backend.dto.request.RegistrationRequest;
import com.chatTop.backend.entities.User;
import com.chatTop.backend.repository.UserRepository;
import com.chatTop.backend.security.UserSecurity;

import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.Optional;
@Service
@Validated
@RequiredArgsConstructor
public class UserService implements UserSecurity {
   @Autowired
	  UserRepository userRepository;
   @Autowired
     PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUserById(Long user_id) {
        return userRepository.findById(user_id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public User registerUser(RegistrationRequest registrationRequest) throws IllegalArgumentException {
        // Vérification si un utilisateur existe déjà avec cet email
        if (userRepository.findByEmail(registrationRequest.getEmail()) != null) {
            throw new IllegalArgumentException("User already exists with email: " + registrationRequest.getEmail());
        }

        // Créer un nouvel utilisateur à partir des données de la demande
        User newUser = new User();
        newUser.setEmail(registrationRequest.getEmail());
        newUser.setName(registrationRequest.getName());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setCreated_at(new Date());

        // Enregistrer le nouvel utilisateur dans la base de données
        return userRepository.save(newUser);
    }
}