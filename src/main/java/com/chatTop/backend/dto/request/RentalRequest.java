package com.chatTop.backend.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class RentalRequest {

	 @NotBlank(message = "Rental name must not be blank")
	    @Size(min = 1, max = 64, message = "The name must be between 1 and 64 characters long")
	    String name;

	    @NotNull(message = "Surface must not be null")
	    @Positive(message = "Surface must be a positive value")
	    Double surface;

	    @NotNull(message = "Price must not be null")
	    @Positive(message = "Price must be a positive value")
	 Double price;

	    @Size(max = 2000, message = "Description can be up to 2000 characters long")
	 String description;

	     MultipartFile picture;

		public String getName() {
			// TODO Auto-generated method stub
			return name;
		}

		public Double getSurface() {
			// TODO Auto-generated method stub
			return surface;
		}

		public Double getPrice() {
			// TODO Auto-generated method stub
			return price;
		}

		public String getDescription() {
			// TODO Auto-generated method stub
			return description;
		}
}