package com.chatTop.backend.service;

import java.util.Optional;

import com.chatTop.backend.entities.Message;


public interface MessageService {

    Optional<Message> createMessage(Message message);

}


