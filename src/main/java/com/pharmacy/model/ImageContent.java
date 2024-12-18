package com.pharmacy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class ImageContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Lob
    @Column(columnDefinition = "LONGTEXT") // This allows up to 4GB in MySQL
    @Size(max = 16777215, message = "Image content must be less than 16MB")
    private String content;
}
