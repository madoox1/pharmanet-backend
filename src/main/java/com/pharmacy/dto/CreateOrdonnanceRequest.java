package com.pharmacy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrdonnanceRequest {
    @NotNull(message = "Encoded image cannot be null")
    private String encodedImage;
    
    @NotNull(message = "Pharmacy ID cannot be null")
    private Long pharmacieId;
}
