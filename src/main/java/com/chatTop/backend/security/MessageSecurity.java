package com.chatTop.backend.security;

import java.util.Optional;

import com.chatTop.backend.entities.Message;


public interface MessageSecurity {

    Optional<Message> createMessage(Message message);

}


