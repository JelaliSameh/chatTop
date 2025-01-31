package com.chatTop.backend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionRepresentation {
    String message;
    String source;
   Set<String> validationErrors;

    // Constructeur privé
     ExceptionRepresentation(String message, String source, Set<String> validationErrors) {
        this.message = message;
        this.source = source;
        this.validationErrors = validationErrors;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public String getSource() {
        return source;
    }

    public Set<String> getValidationErrors() {
        return validationErrors;
    }

    // Builder
    public static ExceptionRepresentationBuilder builder() {
        return new ExceptionRepresentationBuilder();
    }

    public static class ExceptionRepresentationBuilder {
        private String message;
        private String source;
        private Set<String> validationErrors;

        public ExceptionRepresentationBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ExceptionRepresentationBuilder source(String source) {
            this.source = source;
            return this;
        }

        public ExceptionRepresentationBuilder validationErrors(Set<String> validationErrors) {
            this.validationErrors = validationErrors;
            return this;
        }

        public ExceptionRepresentation build() {
            return new ExceptionRepresentation(message, source, validationErrors);
        }
    }
}
