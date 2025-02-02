package com.chatTop.backend.services.impl;

import org.springframework.stereotype.Service;
import com.chatTop.backend.entities.Message;
import com.chatTop.backend.repository.MessageRepository;
import com.chatTop.backend.security.MessageSecurity;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
@Service
@RequiredArgsConstructor

public class MessageService implements MessageSecurity {
    private final MessageRepository messageRepository;

    @Override
    public Optional<Message> createMessage(Message message) {
        return Optional.of(messageRepository.save(message));
    }
}