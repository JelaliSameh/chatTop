package com.chatTop.backend.dto;

import java.util.Date;


import com.chatTop.backend.entities.Message;
import com.chatTop.backend.entities.Rental;
import com.chatTop.backend.entities.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MessageDTO {
    private Long id;

    // Permet de renvoyer l'objet rental dans le json
    // @NotNull(message = "Rental must not be null")
    // private RentalDTO rental;

    // Permet de renvoyer uniquement le rental_id dans le json
    @NotNull(message = "Rental id must not be null")
    @Positive(message = "Rental id must be a positive value")
    @JsonProperty("rental_id")
    private Long rental_id;

    // Permet de renvoyer l'objet user dans le json
    // @NotNull(message = "User must not be null")
    // private UserDTO user;

    // Permet de renvoyer uniquement l'user_id dans le json
    @NotNull(message = "User id must not be null")
    @Positive(message = "User id must be a positive value")
    @JsonProperty("user_id")
    private Long user_id;

    @NotBlank(message = "Message content must not be blank")
    @Size(max = 2000, message = "Message content can be up to 2000 characters long")
    private String message;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date created_at;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date updated_at;

    public static MessageDTO fromEntity(Message message){
        return MessageDTO.builder()
                    .id(message.getId())
                    //.rental(RentalDTO.fromEntity(message.getRental()))
                    //.user(UserDTO.fromEntity(message.getUser()))
                    .rental_id(message.getRental().getId())
                    .user_id(message.getUser().getId())
                    .message(message.getMessage())
                    .created_at(message.getCreated_at())
                    .updated_at(message.getUpdated_at())
                .build();
    }

 // Dans MessageDTO
    public static Message toEntity(MessageDTO messageDTO, Rental rental, User user) {
        return Message.builder()
                .id(messageDTO.getId())
                .rental(rental)  // Utilisez l'objet Rental complet
                .user(user)      // Utilisez l'objet User complet
                .message(messageDTO.getMessage())
                .created_at(messageDTO.getCreated_at())
                .updated_at(messageDTO.getUpdated_at())
                .build();
    }
}