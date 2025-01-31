package com.chatTop.backend.dto;


import com.chatTop.backend.entities.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;



public class UserDTO {
     long id;

    @JsonProperty("name")
     String name;

     String email;

    @JsonProperty("password")
     String password;

    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
     Date createdAt;

    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
     Date updatedAt;

    Set<RentalDTO> rentals;
     Set<MessageDTO> messages;

    // Default constructor
    public UserDTO() {}

    // Full constructor
    public UserDTO(long id, String name, String email, String password, Date createdAt, Date updatedAt, Set<RentalDTO> rentals, Set<MessageDTO> messages) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.rentals = rentals;
        this.messages = messages;
    }

    // Method to convert User to UserDTO
    


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return name;
	}

	public void setUsername(String username) {
		this.name = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatedAt() {
		return createdAt;
	}


	public Date getUpdatedAt() {
		return updatedAt;
	}

	

	public Set<RentalDTO> getRentals() {
		return rentals;
	}

	public void setRentals(Set<RentalDTO> rentals) {
		this.rentals = rentals;
	}

	public Set<MessageDTO> getMessages() {
		return messages;
	}

	public void setMessages(Set<MessageDTO> messages) {
		this.messages = messages;
	}



	public static UserDTO fromEntity(User user) {
	    if (user == null) {
	        return null;
	    }

	    UserDTO userDTO = new UserDTO();
	    userDTO.setId(user.getId());
	    userDTO.setUsername(user.getName());  // Correction de l'attribut
	    userDTO.setEmail(user.getEmail());
	    userDTO.setPassword(user.getPassword());
	    userDTO.setCreatedAt(user.getCreatedAt());
	    userDTO.setUpdatedAt(user.getUpdatedAt());

	    // Conversion des entités en DTO
	    userDTO.setRentals(user.getRentals().stream()
	        .map(rental -> RentalDTO.fromEntity(rental))  // Conversion des entités Rental en RentalDTO
	        .collect(Collectors.toSet())  // Collecte dans un Set
	    );

	    userDTO.setMessages(user.getMessages().stream()
	        .map(message -> MessageDTO.fromEntity(message))  // Conversion des entités Message en MessageDTO
	        .collect(Collectors.toSet())  // Collecte dans un Set
	    );

	    return userDTO;
	}


	 void setUpdatedAt(Date updatedAt) {
		// TODO Auto-generated method stub
		this.updatedAt=updatedAt;
	}

	 void setCreatedAt(Date createdAt) {
		// TODO Auto-generated method stub
		this.createdAt=createdAt;
	}




	



}

