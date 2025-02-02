package com.chatTop.backend.exception;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.hibernate.TransientPropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            ExpiredJwtException.class,
            UnsupportedJwtException.class,
            MalformedJwtException.class,
            SignatureException.class
    })
    // Capture les exceptions JWT à un niveau global
    public ResponseEntity<ExceptionRepresentation> handleJwtException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .message("Invalid or expired JWT token")
                .build();

        // Retourne une réponse uniforme en cas d'erreur JWT avec un message d'erreur spécifique.
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(representation);
    }

    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @ExceptionHandler({
            IllegalArgumentException.class
    })
    public ResponseEntity<ExceptionRepresentation> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .message(illegalArgumentException.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(representation);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionRepresentation> handleBadCredentialsException(){
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .message("Your email and / or password is incorrect")
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(representation);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionRepresentation> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionRepresentation.builder()
                                .message("Database constraint violation")
                                .source("Database")
                                .validationErrors(Set.of(ex.getCause().getMessage())) // Message technique
                                .build()
                );
    }
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionRepresentation> handleDisabledException(DisabledException disabledException){
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .message(disabledException.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }

    @ExceptionHandler(TransientPropertyValueException.class)
    public ResponseEntity<ExceptionRepresentation> handleTransientPropertyValueException(TransientPropertyValueException transientPropertyValueException){
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .message(transientPropertyValueException.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(representation);
    }

    
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionRepresentation> handleValidationException(MethodArgumentNotValidException ex) {
        Set<String> errors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    return fieldName + ": " + errorMessage;
                })
                .collect(Collectors.toSet());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionRepresentation.builder()
                                .message("Validation error")
                                .source("Request Body")
                                .validationErrors(errors)
                                .build()
                );
    }

}