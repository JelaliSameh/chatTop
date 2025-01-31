package com.chatTop.backend.dto;

import java.util.Date;

import com.chatTop.backend.entities.Message;

import jakarta.validation.Valid;







public class MessageDTO {
     long id;
     String message;
     Date createdAt;
     Date updatedAt;
     RentalDTO rental; // Correction ici (non statique)

 

    public MessageDTO(long id, String message, Date createdAt, Date updatedAt, RentalDTO rental) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.rental = rental;
    }

    public MessageDTO() {
		// TODO Auto-generated constructor stub
	}

	public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RentalDTO getRental() {
        return rental;
    }

    public void setRental(RentalDTO rental) {
        this.rental = rental;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static MessageDTO fromEntity(Message message) {
        if (message == null) {
            return null;
        }

        // Conversion de Message à MessageDTO
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setMessage(message.getMessage());
        messageDTO.setCreatedAt(message.getCreatedAt());
        messageDTO.setUpdatedAt(message.getUpdatedAt());

        // Mapper rental si nécessaire
        messageDTO.setRental(message.getRental() != null ? RentalDTO.fromEntity(message.getRental()) : null);

        return messageDTO;
    }


	public String toEntity(@Valid MessageDTO messageDTO) {
		// TODO Auto-generated method stub
		return message;
	}
}