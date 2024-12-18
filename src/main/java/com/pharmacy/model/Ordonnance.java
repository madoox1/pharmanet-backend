package com.pharmacy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "ordonnance",
        indexes = {
                @Index(name = "idx_patient_id", columnList = "patient_id"),
                @Index(name = "idx_pharmacie_id", columnList = "pharmacie_id"),
                @Index(name = "idx_statut", columnList = "statut"),
                @Index(name = "idx_dateEnvoi", columnList = "dateEnvoi")
        }
)
public class Ordonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "The prescription date (dateEnvoi) cannot be null.")
    @Column(nullable = false)
    private Date dateEnvoi = new Date();

    @Column(nullable = false)
    private String imageUrl;

    @NotNull(message = "The status of the prescription (statut) cannot be null.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @JsonIgnore
    private OrdonnanceStatut statut = OrdonnanceStatut.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "patient-ordonnance")
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "pharmacie-ordonnance")
    @JoinColumn(name = "pharmacie_id", nullable = false)
    private Pharmacie pharmacie;

    @JsonBackReference(value = "commande-ordonnance")
    @OneToOne(mappedBy = "ordonnance", cascade = CascadeType.ALL)
    private Commande commande;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_content_id")
    @JsonIgnore
    private ImageContent imageContent;

    @JsonProperty("imageData")
    public String getImageData() {
        return imageContent != null ? imageContent.getContent() : imageUrl;
    }
}

