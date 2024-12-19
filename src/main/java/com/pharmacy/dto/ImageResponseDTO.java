package com.pharmacy.dto;
import lombok.Data;
import lombok.AllArgsConstructor;
@Data
@AllArgsConstructor
public class ImageResponseDTO {
    private byte[] imageData;
    private String contentType;
}