package com.chatTop.backend.services.impl;



import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;


import com.chatTop.backend.entities.Message;
import com.chatTop.backend.repository.MessageRepository;
import com.chatTop.backend.security.MessageSecurity;

import java.util.Optional;

@Service

public class MessageService implements MessageSecurity {
	@Autowired
     MessageRepository messageRepository;

    
    public Optional<Message> createMessage(Message message) {
        return Optional.of(messageRepository.save(message));
    }
}