package com.chatTop.backend.service;

import java.util.Optional;


import org.springframework.validation.annotation.Validated;

import com.chatTop.backend.dto.request.RegistrationRequest;
import com.chatTop.backend.entities.User;


@Validated
/**
 * Interface définissant les opérations liées à la sécurité des utilisateurs.
 */
public interface UserService {
	
	/**
     * Authentifie un utilisateur en fonction de ses identifiants.
     *
     * @param username Le nom d'utilisateur
     * @param password Le mot de passe
     * @return Un token JWT si l'authentification réussit
     */
	  Optional<User> getUserById(Long user_id);
	    Optional<User> getUserByEmail(String email);
	    User registerUser(RegistrationRequest registrationRequest) throws IllegalArgumentException;
	

}
