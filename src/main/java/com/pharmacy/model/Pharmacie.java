package com.pharmacy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "pharmacie",
        indexes = {
                @Index(name = "idx_status", columnList = "_status"),
                @Index(name = "idx_lat_lon", columnList = "latitude, longitude")
        }
)
public class Pharmacie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom ne peut pas être vide")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "L'adresse ne peut pas être vide")
    @Column(nullable = false)
    private String adresse;

    @OneToMany(mappedBy = "pharmacie", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Pharmacien> pharmaciens;

    @NotNull(message = "Le status ne peut pas être nul")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "_status")
    private PharmacieStatus status=PharmacieStatus.Inactive;

    @DecimalMin(value = "-90.0", message = "La latitude doit être entre -90 et 90.")
    @DecimalMax(value = "90.0", message = "La latitude doit être entre -90 et 90.")
    @Column(nullable = false)
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "La longitude doit être entre -180 et 180.")
    @DecimalMax(value = "180.0", message = "La longitude doit être entre -180 et 180.")
    @Column(nullable = false)
    private Double longitude;

}