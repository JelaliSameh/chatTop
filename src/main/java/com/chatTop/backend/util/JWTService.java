package com.chatTop.backend.util;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTService {
	
    @Value("${jwt.secretkey}")
     String secretKey;
    
    @Value("${jwt.expiration}")
   Long expiration;

    // Extrait le username (sujet) du token JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrait la date d'expiration du token JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Méthode générique pour extraire un claim spécifique
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Méthode pour extraire tous les claims
    Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(secretKey.getBytes()) // Correction ici
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    // Vérifie si le token JWT est expiré
    Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Génère un token JWT
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    // Crée un token JWT avec des claims et un sujet
    @SuppressWarnings("deprecation")
	String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(subject)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + expiration))
                   .signWith(SignatureAlgorithm.HS256, secretKey.getBytes()) // Correction ici
                   .compact();
    }
    // Valide le token JWT
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
