package com.chatTop.backend.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.chatTop.backend.dto.UserDTO;
import com.chatTop.backend.entities.User;
import com.chatTop.backend.security.UserSecurity;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserSecurity userSecurity;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserDTO>> getUserById(@PathVariable Integer id){
        Optional<User> user = userSecurity.getUserById(id.longValue());

        if (user.isPresent()) {
            UserDTO userDTO = UserDTO.fromEntity(user.get());
            return ResponseEntity.ok(Optional.of(userDTO));
        } else {
            return ResponseEntity.ok(Optional.empty());
        }
    }

}