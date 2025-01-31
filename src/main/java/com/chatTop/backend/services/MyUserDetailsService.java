package com.chatTop.backend.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chatTop.backend.entities.User;
import com.chatTop.backend.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        /**
             Si l'utilisateur est trouvé, un objet UserDetails est créé et retourné
             UserDetails est une interface fournie par Spring Security
             org.springframework.security.core.userdetails.User est une implémentation de cette interface
             Les paramètres sont l'email, le mot de passe et une liste de rôles/permissions (vide dans notre cas),
             car nous n'avons pas implémenter le concept d'Authorization dans ce projet.
         */
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }
}