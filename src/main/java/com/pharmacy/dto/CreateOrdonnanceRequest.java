package com.pharmacy.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOrdonnanceRequest {
    @NotNull(message = "L'image encodée est obligatoire")
    @Size(max = 16777215, message = "L'image doit faire moins de 16MB")
    private String encodedImage;

    @NotNull(message = "L'ID de la pharmacie est obligatoire")
    private Long pharmacieId;
}
