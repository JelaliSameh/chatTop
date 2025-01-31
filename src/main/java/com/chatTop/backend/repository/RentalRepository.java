package com.chatTop.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatTop.backend.entities.Rental;
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
}
