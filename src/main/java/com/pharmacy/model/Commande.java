package com.pharmacy.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le montant total ne peut pas être nul.")
    @Column(nullable = false)
    private float montantTotal;

    @NotNull(message = "La date de création ne peut pas être nulle.")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date dateCreation;

    @NotNull(message = "Le statut de la commande ne peut pas être nul.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CommandeStatut statut; // Updated to Enum

    @OneToOne
    @JoinColumn(name = "ordonnance_id", nullable = false)
    @JsonBackReference
    private Ordonnance ordonnance;






}