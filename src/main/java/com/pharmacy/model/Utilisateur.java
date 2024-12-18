package com.pharmacy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le nom ne peut pas être nul.")
    @NotBlank(message = "Le nom ne peut pas être vide.")
    @Size(min = 2, max = 100, message = "Le nom doit avoir entre 2 et 100 caractères.")
    private String nom;

    @NotNull(message = "Le prénom ne peut pas être nul.")
    @NotBlank(message = "Le prénom ne peut pas être vide.")
    @Size(min = 2, max = 100, message = "Le prénom doit avoir entre 2 et 100 caractères.")
    private String prenom;

    @NotNull(message = "L'email ne peut pas être nul.")
    @NotBlank(message = "L'email ne peut pas être vide.")
    @Email(message = "L'email doit être valide.")
    @Size(max = 255, message = "L'email doit contenir au maximum 255 caractères.")
    private String email;

    @NotNull(message = "Le mot de passe ne peut pas être nul.")
    @NotBlank(message = "Le mot de passe ne peut pas être vide.")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères.")
    private String motDePasse;

    @NotNull(message = "Le rôle ne peut pas être nul.")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UtilisateurRole role;

    @NotNull(message = "Le statut ne peut pas être nul.")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private UtilisateurStatus status=UtilisateurStatus.Inactive;





}