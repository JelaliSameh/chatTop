package com.chatTop.backend.entities;

import jakarta.persistence.*;



import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
     Long id;

    @ManyToOne
    @JoinColumn(name = "rental_id", referencedColumnName = "id")
    Rental rental;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    @Column(name = "message", nullable = false, length = 2000)
     String message;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
     Date createdAt;


    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP")
     Date updatedAt;

	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}



	public Date getUpdatedAt() {
		// TODO Auto-generated method stub
		return  updatedAt;
	}

	public Date getCreatedAt() {
		// TODO Auto-generated method stub
		return createdAt;
	}

	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}

	public User getUser() {
		// TODO Auto-generated method stub
		return user;
	}

	public void getRental(Rental rental) {
		// TODO Auto-generated method stub
		this.rental=rental;
	}

	public void setCreated_at(Date createdAt) {
		// TODO Auto-generated method stub
		this.createdAt=createdAt;
	}

	public void setId(long id) {
		// TODO Auto-generated method stub
		this.id=id;
	}

	public void setRental(Rental rental) {
		// TODO Auto-generated method stub
		this.rental=rental;
	}

	public void setUpdated_at(Date updatedAt) {
		// TODO Auto-generated method stub
		this.updatedAt = updatedAt;
	}

	public void setMessage(String message) {
		// TODO Auto-generated method stub
		this.message=message;
	}

	public Rental getRental() {
		// TODO Auto-generated method stub
		return rental;
	}

	

	
}