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
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_patient_pharmacie",
            columnNames = {"patient_id", "pharmacie_id"}
        )
    },
    indexes = {
        @Index(name = "idx_patient_pharmacie", columnList = "patient_id,pharmacie_id")
    }
)
public class Ordonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date d'envoi ne peut pas être nulle")
    @Column(nullable = false)
    private Date dateEnvoi = new Date();

    @Column(nullable = false)
    private String imageUrl;

    @NotNull(message = "Le statut ne peut pas être nul")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrdonnanceStatut statut = OrdonnanceStatut.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnore
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacie_id", nullable = false)
    @JsonIgnore
    private Pharmacie pharmacie;

    @OneToOne(mappedBy = "ordonnance", cascade = CascadeType.ALL)
    @JsonIgnore
    private Commande commande;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_content_id")
    private ImageContent imageContent;



    // Méthode utilitaire pour récupérer l'image
    @JsonIgnore
    public String getImageContent() {
        return imageContent != null ? imageContent.getContent() : imageUrl;
    }

    // Méthode utilitaire pour définir l'image
    @JsonIgnore
    public void setImageContent(ImageContent imageContent) {
        this.imageContent = imageContent;
    }
}