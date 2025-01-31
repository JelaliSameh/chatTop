package com.chatTop.backend.dto.response;

import java.util.List;

import com.chatTop.backend.dto.RentalDTO;

import lombok.Data;

@Data
public class RentalsListResponse {
     List<RentalDTO> rentals;

     public void setRentals(List<RentalDTO> rentals) {
         this.rentals = rentals;
     }


}