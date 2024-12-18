package com.pharmacy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrdonnanceRequest {
    @NotNull(message = "Encoded image cannot be null")
    @Size(max = 255, message = "Encoded image must be less than 255 characters")
    private String encodedImage;
    
    @NotNull(message = "Pharmacy ID cannot be null")
    private Long pharmacieId;
}
