package com.chatTop.backend.dto.request;



import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;



/**
 * DTO for user registration requests.
 */

public class RegistrationRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email is not compliant")
    String email;

    @NotBlank(message = "Name must not be blank")
    @Pattern(
        regexp = "^[A-Za-zÀ-ÿ\\s-]+$",
        message = "The name must only contain letters, spaces, and hyphens"
    )
    String name;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max = 64, message = "The password must be between 6 and 64 characters long")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,64}$",
        message = "The password must contain at least one letter, one number, and one special character"
    )
 
     String password;

    public String getEmail() {
        return email;  // Retourne l'email
    }

    public String getPassword() {
        return password;  // Retourne le mot de passe
    }

    public String getName() {
        return name;  // Retourne le nom
    }
}