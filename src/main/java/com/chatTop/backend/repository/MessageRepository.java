package com.chatTop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.chatTop.backend.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	
	
	
}


