package com.chatTop.backend.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;


@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
     Long id;

    @Column(name = "email", nullable = false, length = 45, unique = true)
     String email;

    @Column(name = "name", nullable = false, length = 45)
    String name;

    @Column(name = "password", nullable = false, length = 64)
     String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
   Date createdAt;
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
    
    @Column(name = "updated_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Rental> rentals;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
     List<Message> messages;

	public String getEmail() {
		// TODO Auto-generated method stub
		return email;
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	public void setUpdatedAt(Date updateAt) {
		// TODO Auto-generated method stub
		this.updatedAt = updateAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
		// TODO Auto-generated method stub
		
	}

	public void setPassword(String password) {
		// TODO Auto-generated method stub
		this.password = password;
	}

	
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	public void setName(String name) {
		// TODO Auto-generated method stub
		this.name = name;
	}

	public void setEmail(String email) {
		// TODO Auto-generated method stub
		this.email=email;
	}

	public List<Message> getMessages() {
		// TODO Auto-generated method stub
		return messages;
	}

	public List<Rental> getRentals() {
		// TODO Auto-generated method stub
		return rentals;
	}

	public Date getUpdatedAt() {
		// TODO Auto-generated method stub
		return updatedAt;
	}

	public Date getCreatedAt() {
		// TODO Auto-generated method stub
		return createdAt;
	}



	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public void setMessages( List<Message> messages) {
		// TODO Auto-generated method stub
		this.messages=messages;
	}

	public void setRentals( List<Rental> rentals) {
		// TODO Auto-generated method stub
		this.rentals=rentals;
	}

	public void setId(long id) {
		// TODO Auto-generated method stub
		this.id=id;
	}


}